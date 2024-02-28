package bg.duosoft.ipas.core.validation.process;

import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.process.CNextProcessAction;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessInsertActionRequest;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.*;
import bg.duosoft.ipas.core.service.UserdocExtraDataService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.file.FileRecordalService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.userdoc.RecordalChangeNameOrAddressValidator;
import bg.duosoft.ipas.core.validation.userdoc.RecordalRenewalValidator;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.IpObjectSplitCode;
import bg.duosoft.ipas.enums.RecordalType;
import bg.duosoft.ipas.enums.UserdocExtraDataTypeCode;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.process.ProcessActionUtils;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class ProcessInsertActionValidator implements IpasValidator<CProcessInsertActionRequest> {

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private UserdocExtraDataService userdocExtraDataService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private DocService docService;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private FileRecordalService fileRecordalService;

    @Override
    public List<ValidationError> validate(CProcessInsertActionRequest obj, Object... additionalArgs) {
        if (additionalArgs.length != 3)
            throw new RuntimeException("Wrong ProcessActionValidator parameters !");

        CActionType cActionType = (CActionType) additionalArgs[0];
        if (Objects.isNull(cActionType))
            throw new RuntimeException("CActionType is missing in ProcessActionValidator !");

        CNextProcessAction nextProcessAction = (CNextProcessAction) additionalArgs[1];
        if (Objects.isNull(nextProcessAction))
            throw new RuntimeException("CNextProcessAction is missing in ProcessActionValidator !");

        CProcess cProcess = (CProcess) additionalArgs[2];
        if (Objects.isNull(cProcess))
            throw new RuntimeException("CProcess is missing in ProcessActionValidator !");

        List<ValidationError> errors = new ArrayList<>();

        if (nextProcessAction.getContainNotes()) {
            if (Objects.nonNull(cActionType.getNotes1Prompt())) {
                rejectIfEmptyString(errors, obj.getNotes1(), "action.notes1", "required.field", obj.getNotes1());
            }
            if (Objects.nonNull(cActionType.getNotes2Prompt())) {
                rejectIfEmptyString(errors, obj.getNotes2(), "action.notes2", "required.field", obj.getNotes2());
            }
            if (Objects.nonNull(cActionType.getNotes3Prompt())) {
                rejectIfEmptyString(errors, obj.getNotes3(), "action.notes3", "required.field", obj.getNotes3());
            }
            if (Objects.nonNull(cActionType.getNotes4Prompt())) {
                rejectIfEmptyString(errors, obj.getNotes4(), "action.notes4", "required.field", obj.getNotes4());
            }
            if (Objects.nonNull(cActionType.getNotes5Prompt())) {
                rejectIfEmptyString(errors, obj.getNotes5(), "action.notes5", "required.field", obj.getNotes5());
            }
        }
        if (nextProcessAction.getContainManualDueDate()) {
            rejectIfEmpty(errors, obj.getManualDueDate(), "action.manualDueDate", "required.field", obj.getManualDueDate());
        }

        if (nextProcessAction.getCalculateTermFromActionDate()) {
            Date actionDate = obj.getActionDate();
            rejectIfEmpty(errors, actionDate, "action.actionDate", "required.field", actionDate);

            if (Objects.nonNull(actionDate)) {
                LocalDate currentDate = LocalDate.now();
                LocalDate actionDateAsLocalDate = DateUtils.convertToLocalDate(actionDate);
                rejectIfFirstDateIsAfterSecond(errors, actionDateAsLocalDate, currentDate, "action.actionDate", "insertAction.actionDate.future");
            }
        }

        if (!StringUtils.isEmpty(nextProcessAction.getGeneratedOffidoc())) {
            rejectIfEmptyCollection(errors, obj.getOffidocTemplates(), "action.offidocTemplates", "required.field");
        }

        boolean doesActionContainSplitConfiguration = ProcessActionUtils.doesActionContainSplitConfiguration(nextProcessAction);
        if (doesActionContainSplitConfiguration) {
            validateIpObjectSplitting(errors, cProcess, nextProcessAction);
        }

        boolean isStatusTriggerActivityForRecordal = UserdocUtils.isStatusTriggerActivityForRecordalRegistration(nextProcessAction.getStatusCode(), cProcess, statusService, userdocTypesService);
        if (isStatusTriggerActivityForRecordal) {
            validateRecordals(errors, cProcess, obj);
        }

        boolean isStatusTriggerActivityForRecordalInvalidation = UserdocUtils.isStatusTriggerActivityForRecordalInvalidation(nextProcessAction.getStatusCode(), cProcess, statusService, userdocTypesService);
        if (isStatusTriggerActivityForRecordalInvalidation) {
            validateRecordalInvalidation(obj, cProcess, errors);
        }

        return errors;
    }

    private void validateIpObjectSplitting(List<ValidationError> errors, CProcess process, CNextProcessAction nextProcessAction) {
        if (ProcessTypeUtils.isUserdocProcess(process)) {
            if (Objects.nonNull(ProcessActionUtils.selectIpObjectSplitType(nextProcessAction, process, processService))) {
                CUserdoc userdoc = userdocService.findUserdoc(process.getProcessOriginData().getDocumentId());
                if (UserdocUtils.isUserdocRecordalTransfer(userdoc)) {
                    Boolean areAllServicesSelected = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.SERVICE_SCOPE.name(), userdoc.getUserdocExtraData());
                    rejectIfEmpty(errors, areAllServicesSelected, "action.executionConfirmationText", "userdoc.transfer.empty.serviceScope");
                }
            }
        }
    }

    private void validateRecordalInvalidation(CProcessInsertActionRequest obj, CProcess cProcess, List<ValidationError> errors) {
        rejectIfFalse(errors, UserdocUtils.isInvalidationUserdocRelatedToCurrectUserdoc(cProcess, processService, userdocTypesService), "action.invalidationUserdocWrongPosition", "insertAction.invalidationUserdocWrongPosition.msg");
        if (CollectionUtils.isEmpty(errors)) {
            Date invalidationDate = obj.getInvalidationDate();
            rejectIfEmpty(errors, invalidationDate, "action.invalidationDate");
            if (CollectionUtils.isEmpty(errors)) {
                CProcessParentData cProcessParentData = processService.generateProcessParentHierarchy(cProcess.getProcessId());
                CDocumentId recordalUserdocId = UserdocUtils.selectRecordalUserdocIdByInvalidationUserdocParentData(cProcessParentData);
                CUserdocExtraData effectiveDate = userdocExtraDataService.selectById(recordalUserdocId, UserdocExtraDataTypeCode.EFFECTIVE_DATE.name());
                if (Objects.isNull(effectiveDate) || Objects.isNull(effectiveDate.getDateValue())) {
                    rejectIfEmpty(errors, effectiveDate, "action.invalidationDate", "userdoc.recordal.invalidation.missing.recordal.date");
                } else {
                    rejectIfFirstDateIsBeforeSecond(errors, invalidationDate, effectiveDate.getDateValue(), "action.invalidationDate", "userdoc.recordal.invalidation.date.before.recordal");
                }
                if (CollectionUtils.isEmpty(errors)) {
                    rejectIfEmpty(errors, fileRecordalService.selectRecordalByUserdocId(recordalUserdocId), "action.emptyRecordalRecord", "insertAction.emptyRecordalRecord.msg");
                }
            }
        }
    }

    private void validateRecordals(List<ValidationError> errors, CProcess process, CProcessInsertActionRequest obj) {
        CDocumentId documentId = process.getProcessOriginData().getDocumentId();
        rejectIfEmpty(errors, obj.getRecordalDate(), "action.recordalDate");

        CUserdocType cUserdocType = UserdocUtils.selectUserdocTypeFromDatabase(process, userdocTypesService);
        CUserdocPanel recordalPanel = UserdocUtils.selectRecordalPanel(cUserdocType.getPanels());
        if (Objects.nonNull(recordalPanel)) {
            RecordalType recordal = RecordalType.valueOf(recordalPanel.getPanel());
            switch (recordal) {
                case Pledge:
                    validateRecordalPledge(errors, documentId);
                    break;
                case Licenses:
                    validateRecordalLicense(errors, documentId);
                    break;
                case Security_measure:
                    validateRecordalSecurityMeasure(errors, documentId);
                    break;
                case Transfer:
                    validateRecordalTrasfer(errors, documentId);
                    break;
                case Change:
                    validateRecordalChangeNameOrAddress(errors, documentId);
                    break;
                case Change_representative:
                    validateRecordalChangeRepresentative(errors, documentId, obj);
                    break;
                case Change_correspondence_address:
                    validateRecordalChangeCorrespondenceAddress(errors, documentId);
                    break;
                case Renewal:
                    validateRecordalRenewal(errors, documentId);
                    break;
            }
        }
    }

    private void validateRecordalSecurityMeasure(List<ValidationError> errors, CDocumentId documentId) {
    }

    private void validateRecordalLicense(List<ValidationError> errors, CDocumentId documentId) {
        CUserdoc userdoc = UserdocUtils.selectUserdocFromDatabase(documentId, userdocService);
        List<CUserdocPerson> grantees = UserdocPersonUtils.selectGrantees(userdoc.getUserdocPersonData());
        rejectIfEmptyCollection(errors, grantees, "action.recordalDate", "userdoc.license.empty.grantees");
    }

    private void validateRecordalPledge(List<ValidationError> errors, CDocumentId documentId) {
        CUserdoc userdoc = UserdocUtils.selectUserdocFromDatabase(documentId, userdocService);
        List<CUserdocPerson> payees = UserdocPersonUtils.selectPayees(userdoc.getUserdocPersonData());
        rejectIfEmptyCollection(errors, payees, "action.recordalDate", "userdoc.pledge.empty.payees");
    }

    private void validateRecordalTrasfer(List<ValidationError> errors, CDocumentId documentId) {
        CUserdoc userdoc = UserdocUtils.selectUserdocFromDatabase(documentId, userdocService);
        CUserdocPersonData userdocPersonData = userdoc.getUserdocPersonData();
        List<CUserdocPerson> newOwners = UserdocPersonUtils.selectNewOwners(userdocPersonData);
        rejectIfEmptyCollection(errors, newOwners, "action.recordalDate", "userdoc.transfer.empty.new.owners");

        CUserdocMainObjectData userdocMainObjectData = userdoc.getUserdocMainObjectData();
        CFileId fileId = userdocMainObjectData.getFileId();
        FileType fileType = FileType.selectByCode(fileId.getFileType());
        switch (fileType) {
            case MARK:
            case DESIGN:
                Boolean areAllServicesSelected = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.SERVICE_SCOPE.name(), userdoc.getUserdocExtraData());
                rejectIfEmpty(errors, areAllServicesSelected, "action.recordalDate", "userdoc.transfer.empty.serviceScope");
                break;
        }
    }

    private void validateRecordalChangeRepresentative(List<ValidationError> errors, CDocumentId documentId, CProcessInsertActionRequest obj) {
        rejectIfEmpty(errors, obj.getTransferCorrespondenceAddress(), "action.transferCorrespondenceAddress", "required.field", obj.getTransferCorrespondenceAddress());
    }

    private void validateRecordalChangeCorrespondenceAddress(List<ValidationError> errors, CDocumentId documentId) {
        CUserdoc userdoc = UserdocUtils.selectUserdocFromDatabase(documentId, userdocService);
        CUserdocPersonData userdocPersonData = userdoc.getUserdocPersonData();
        List<CUserdocPerson> newCorrespondenceAddress = UserdocPersonUtils.selectNewCorrespondenceAddress(userdocPersonData);
        if (CollectionUtils.isEmpty(newCorrespondenceAddress)) {
            errors.add(ValidationError.builder().pointer("action.recordalDate").messageCode("userdoc.change.ca.empty.new.ca").build());
        } else if (newCorrespondenceAddress.size() > 1) {
            errors.add(ValidationError.builder().pointer("action.recordalDate").messageCode("userdoc.change.ca.more.than.one.new.ca").build());
        }
    }

    private void validateRecordalRenewal(List<ValidationError> errors, CDocumentId documentId) {
        CUserdoc userdoc = UserdocUtils.selectUserdocFromDatabase(documentId, userdocService);
        IpasValidator<CUserdoc> validator = validatorCreator.create(false, RecordalRenewalValidator.class);
        List<ValidationError> externalValidatorErrors = validator.validate(userdoc, "action.recordalDate");
        if (!CollectionUtils.isEmpty(externalValidatorErrors)) {
            errors.addAll(externalValidatorErrors);
        }
    }

    private void validateRecordalChangeNameOrAddress(List<ValidationError> errors, CDocumentId documentId) {
        CUserdoc userdoc = UserdocUtils.selectUserdocFromDatabase(documentId, userdocService);
        CUserdocPersonData userdocPersonData = userdoc.getUserdocPersonData();
        List<CUserdocPerson> newOwners = UserdocPersonUtils.selectNewOwners(userdocPersonData);
        rejectIfEmptyCollection(errors, newOwners, "action.recordalDate", "userdoc.change.name.or.address.empty.new.owners");

        IpasValidator<CUserdoc> validator = validatorCreator.create(false, RecordalChangeNameOrAddressValidator.class);
        List<ValidationError> externalValidatorErrors = validator.validate(userdoc, "action.recordalDate");
        if (!CollectionUtils.isEmpty(externalValidatorErrors)) {
            errors.addAll(externalValidatorErrors);
        }
    }

}
