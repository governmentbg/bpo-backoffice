package bg.duosoft.ipas.core.service.impl.action;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.*;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.process.CProcessAuthorizationData;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.*;
import bg.duosoft.ipas.core.service.action.RecordalAuthorizationService;
import bg.duosoft.ipas.core.service.file.FileRecordalService;
import bg.duosoft.ipas.core.service.logging.LogChangesService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.userdoc.RecordalChangeNameOrAddressValidator;
import bg.duosoft.ipas.core.validation.userdoc.UserdocValidator;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.RecordalType;
import bg.duosoft.ipas.enums.UserdocExtraDataTypeCode;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.file.FileTypeUtils;
import bg.duosoft.ipas.util.person.OwnerUtils;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@LogExecutionTime
public class RecordalAuthorizationServiceImpl implements RecordalAuthorizationService {

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private FileRecordalService fileRecordalService;

    @Autowired
    private PatentService patentService;

    @Autowired
    private MarkService markService;

    @Autowired
    private UserdocValidator userdocValidator;

    @Autowired
    private RecordalChangeNameOrAddressValidator recordalChangeNameOrAddressValidator;

    @Autowired
    private LogChangesService logChangesService;

    @Override
    public void authorize(CDocumentId documentId, CActionId actionId, CProcessAuthorizationData data) {
        CUserdoc userdoc = userdocService.findUserdoc(documentId,true);
        if (Objects.isNull(userdoc)) {
            throw new RuntimeException("Cannot find userdoc " + documentId);
        }

        List<ValidationError> errors = userdocValidator.validate(userdoc, true);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new IpasValidationException(errors);
        }

        List<CUserdocPanel> userdocPanels = userdoc.getUserdocType().getPanels();
        boolean isRecordal = UserdocUtils.isRecordal(userdocPanels);

        List<String> invalidatedUserdocTypes = userdoc.getUserdocType().getInvalidatedUserdocTypes();
        boolean isRecordalInvalidation = UserdocUtils.isRecordalInvalidation(invalidatedUserdocTypes);

        if (isRecordal && isRecordalInvalidation) {
            throw new RuntimeException("Userdoc cannot be for recordal and invalidation at the same time !");
        }

        if (isRecordal) {
            authorizeRecordal(userdoc, actionId, data);
        } else if (isRecordalInvalidation) {
            authorizeInvalidation(userdoc, actionId, data);
        }
        log.warn("Userdoc " + documentId + " reached authorized status !");
    }

    private void authorizeRecordal(CUserdoc userdoc, CActionId actionId, CProcessAuthorizationData data) {
        CDocumentId documentId = userdoc.getDocumentId();

        Date effectiveDate = data.getEffectiveDate();
        if (Objects.isNull(effectiveDate)) {
            throw new RuntimeException("Effective date for recordal userdoc is empty !" + documentId);
        }

        fileRecordalService.insertNewRecordal(documentId, actionId, effectiveDate);
        UserdocExtraDataUtils.setUserdocExtraDataProperty(userdoc, UserdocExtraDataTypeCode.EFFECTIVE_DATE.name(), CUserdocExtraDataValue.builder().dateValue(effectiveDate).build());

        CUserdocPanel recordalPanel = UserdocUtils.selectRecordalPanel(userdoc.getUserdocType().getPanels());
        RecordalType recordalType = RecordalType.valueOf(recordalPanel.getPanel());
        switch (recordalType) {
            case Licenses:
                processLicense(userdoc);
                break;
            case Transfer:
                processTransfer(userdoc);
                break;
            case Bankruptcy:
                //TODO
                break;
            case Change:
                processChangeNameOrAddress(userdoc);
                break;
            case Pledge:
                processPledge(userdoc);
                break;
            case Renewal:
                processRenewal(userdoc);
                break;
            case Withdrawal:
                //TODO
                break;
            case Security_measure:
                processSecurityMeasure(userdoc);
                break;
            case Change_representative:
                processChangeRepresentative(userdoc, data.getTransferCorrespondenceAddress());
                break;
            case Change_correspondence_address:
                processChangeCorrespondenceAddress(userdoc);
                break;
        }
    }

    private void authorizeInvalidation(CUserdoc userdoc, CActionId actionId, CProcessAuthorizationData data) {
        CDocumentId documentId = userdoc.getDocumentId();

        Date invalidationDate = data.getInvalidationDate();
        if (Objects.isNull(invalidationDate)) {
            throw new RuntimeException("Invalidation date for invalidation userdoc is empty !" + documentId);
        }

        fileRecordalService.insertInvalidationOfRecordal(documentId, actionId, invalidationDate);
    }

    private void processSecurityMeasure(CUserdoc userdoc) {

    }

    private void processLicense(CUserdoc userdoc) {
        CUserdocPersonData userdocPersonData = userdoc.getUserdocPersonData();

        List<CUserdocPerson> grantees = UserdocPersonUtils.selectGrantees(userdocPersonData);
        if (CollectionUtils.isEmpty(grantees)) {
            throw new RuntimeException("There aren't grantees in license userdoc ! " + userdoc.getDocumentId());
        }
    }

    private void processPledge(CUserdoc userdoc) {
        CUserdocPersonData userdocPersonData = userdoc.getUserdocPersonData();

        List<CUserdocPerson> payees = UserdocPersonUtils.selectPayees(userdocPersonData);
        if (CollectionUtils.isEmpty(payees)) {
            throw new RuntimeException("There aren't payees in pledge userdoc ! " + userdoc.getDocumentId());
        }
    }

    private void processTransfer(CUserdoc userdoc) {
        FileType fileType = FileType.selectByCode(userdoc.getUserdocMainObjectData().getFileId().getFileType());
        switch (fileType) {
            case MARK:
            case DIVISIONAL_MARK:
            case DESIGN: {
                Boolean areAllServicesSelected = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.SERVICE_SCOPE.name(), userdoc.getUserdocExtraData());
                if (Objects.isNull(areAllServicesSelected) || !areAllServicesSelected)
                    return;// Replace main owner only if all services are selected !
            }
        }
        replaceMainObjectOwners(userdoc);
    }

    private void processRenewal(CUserdoc userdoc) {
        Date newExpirationDate = UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.RENEWAL_NEW_EXPIRATION_DATE.name(), userdoc.getUserdocExtraData());
        if (Objects.nonNull(newExpirationDate)) {
            CProcessParentData userdocParentData = userdoc.getUserdocParentData();
            CFileId fileId = UserdocUtils.selectUserdocMainObject(userdocParentData);
            switch (FileTypeUtils.selectIpObjectTypeByFileType(fileId.getFileType())) {
                case DefaultValue.MARK_OBJECT_INDICATION: {
                    CMark mark = selectMark(fileId);
                    CRegistrationData registrationData = mark.getFile().getRegistrationData();
                    if (Objects.isNull(registrationData)) {
                        mark.getFile().setRegistrationData(new CRegistrationData());
                    }
                    mark.getFile().getRegistrationData().setExpirationDate(newExpirationDate);
                    updateMarkAndLogChange(mark, userdoc.getDocumentId(), userdoc.getUserdocType().getUserdocType());
                    break;
                }
                case DefaultValue.PATENT_OBJECT_INDICATION: {
                    CPatent patent = selectPatent(fileId);

                    CRegistrationData registrationData = patent.getFile().getRegistrationData();
                    if (Objects.isNull(registrationData)) {
                        patent.getFile().setRegistrationData(new CRegistrationData());
                    }
                    patent.getFile().getRegistrationData().setExpirationDate(newExpirationDate);
                    updatePatentAndLogChange(patent, userdoc.getDocumentId(), userdoc.getUserdocType().getUserdocType());
                    break;
                }
            }
        }
    }

    private void fillNewOwnersData(List<CPerson> newOwnersPersons, CFile file) {
        COwnershipData newOwnershipData = OwnerUtils.createOwnershipData(newOwnersPersons);
        boolean isCAOwner = OwnerUtils.isCorrespondenceAddressOwner(file.getServicePerson(), file.getOwnershipData());
        if (isCAOwner) {
            file.setServicePerson(OwnerUtils.selectFirstCPersonOwner(newOwnershipData));
        }
        file.setOwnershipData(newOwnershipData);
    }

    private void processChangeRepresentative(CUserdoc userdoc, Boolean transferCorrespondenceAddress) {
        CUserdocPersonData userdocPersonData = userdoc.getUserdocPersonData();

        List<CRepresentative> newRepresentativesPersons = null;
        List<CUserdocPerson> newRepresentatives = UserdocPersonUtils.selectNewRepresentatives(userdocPersonData);
        if (!CollectionUtils.isEmpty(newRepresentatives)) {
            newRepresentativesPersons = newRepresentatives.stream()
                    .map(p -> new CRepresentative(p.getRepresentativeType(), p.getPerson(),p.getAttorneyPowerTerm(),p.getReauthorizationRight(),p.getPriorReprsRevocation(),p.getAuthorizationCondition()))
                    .collect(Collectors.toList());
        }

        CProcessParentData userdocParentData = userdoc.getUserdocParentData();
        CFileId fileId = UserdocUtils.selectUserdocMainObject(userdocParentData);
        switch (FileTypeUtils.selectIpObjectTypeByFileType(fileId.getFileType())) {
            case DefaultValue.MARK_OBJECT_INDICATION: {
                CMark mark = selectMark(fileId);
                replaceRepresentativesAndUpdateUserdoc(userdoc, newRepresentativesPersons, mark.getFile());
                transferNewRepresentativesCA(transferCorrespondenceAddress, userdoc.getServicePerson(), newRepresentatives, mark.getFile());
                updateMarkAndLogChange(mark, userdoc.getDocumentId(), userdoc.getUserdocType().getUserdocType());
                break;
            }
            case DefaultValue.PATENT_OBJECT_INDICATION: {
                CPatent patent = selectPatent(fileId);
                replaceRepresentativesAndUpdateUserdoc(userdoc, newRepresentativesPersons, patent.getFile());
                transferNewRepresentativesCA(transferCorrespondenceAddress, userdoc.getServicePerson(), newRepresentatives, patent.getFile());
                updatePatentAndLogChange(patent, userdoc.getDocumentId(), userdoc.getUserdocType().getUserdocType());
                break;
            }
        }

    }

    private void transferNewRepresentativesCA(Boolean transferCorrespondenceAddress, CPerson userdocServicePerson, List<CUserdocPerson> newRepresentatives, CFile file) {
        if (Objects.nonNull(transferCorrespondenceAddress) && transferCorrespondenceAddress) {
            boolean isCARepresentative = UserdocPersonUtils.isCAExistsInUserdocPersons(userdocServicePerson, newRepresentatives);
            if (isCARepresentative) {
                file.setServicePerson(userdocServicePerson);
            }
        }
    }

    private void replaceRepresentativesAndUpdateUserdoc(CUserdoc userdoc, List<CRepresentative> newRepresentativesPersons, CFile file) {
        List<CUserdocPerson> oldRepresentatives = selectOldRepresentatives(file);

        UserdocPersonUtils.removeUserdocPersonsByRole(userdoc.getUserdocPersonData(), UserdocPersonRole.OLD_REPRESENTATIVE);
        if (!CollectionUtils.isEmpty(oldRepresentatives)) {
            userdoc.getUserdocPersonData().getPersonList().addAll(oldRepresentatives);
        }
        userdocService.updateUserdoc(userdoc, true);

        fillNewRepresentativesData(newRepresentativesPersons, file);
    }

    private void fillNewRepresentativesData(List<CRepresentative> newRepresentatives, CFile file) {
        if (!CollectionUtils.isEmpty(newRepresentatives)) {
            CRepresentationData newRepresentationData = new CRepresentationData();
            newRepresentationData.setRepresentativeList(newRepresentatives);
            boolean isCARepresentative = RepresentativeUtils.isCorrespondenceAddressRepresentative(file.getServicePerson(), file.getRepresentationData());
            if (isCARepresentative) {
                file.setServicePerson(RepresentativeUtils.selectFirstCPersonRepresentative(newRepresentationData));
            }
            file.setRepresentationData(newRepresentationData);
        } else {
            file.setRepresentationData(null);
        }
    }

    private void processChangeCorrespondenceAddress(CUserdoc userdoc) {
        CUserdocPersonData userdocPersonData = userdoc.getUserdocPersonData();

        List<CUserdocPerson> newCorrespondenceAddresses = UserdocPersonUtils.selectNewCorrespondenceAddress(userdocPersonData);
        if (CollectionUtils.isEmpty(newCorrespondenceAddresses)) {
            throw new RuntimeException("There isn't new correspondence address in userdoc for change correspondence address ! " + userdoc.getDocumentId());
        } else if (newCorrespondenceAddresses.size() > DefaultValue.MIN_NOT_EMPTY_COLLECTION_SIZE) {
            throw new RuntimeException("New correspondence address must be only one !");
        }
        CUserdocPerson newCorrespondenceAddress = newCorrespondenceAddresses.get(DefaultValue.FIRST_RESULT);

        CProcessParentData userdocParentData = userdoc.getUserdocParentData();
        CFileId fileId = UserdocUtils.selectUserdocMainObject(userdocParentData);
        switch (FileTypeUtils.selectIpObjectTypeByFileType(fileId.getFileType())) {
            case DefaultValue.MARK_OBJECT_INDICATION: {
                CMark mark = selectMark(fileId);
                CPerson markServicePerson = mark.getFile().getServicePerson();
                UserdocPersonUtils.removeUserdocPersonsByRole(userdoc.getUserdocPersonData(), UserdocPersonRole.OLD_CORRESPONDENCE_ADDRESS);
                if (Objects.nonNull(markServicePerson)) {
                    CUserdocPerson oldServicePerson = UserdocPersonUtils.convertToUserdocPerson(markServicePerson, UserdocPersonRole.OLD_CORRESPONDENCE_ADDRESS, false, null, null);
                    userdoc.getUserdocPersonData().getPersonList().add(oldServicePerson);
                }
                userdocService.updateUserdoc(userdoc, true);

                mark.getFile().setServicePerson(newCorrespondenceAddress.getPerson());
                updateMarkAndLogChange(mark, userdoc.getDocumentId(), userdoc.getUserdocType().getUserdocType());
                break;
            }
            case DefaultValue.PATENT_OBJECT_INDICATION: {
                CPatent patent = selectPatent(fileId);
                CPerson patentServicePerson = patent.getFile().getServicePerson();
                UserdocPersonUtils.removeUserdocPersonsByRole(userdoc.getUserdocPersonData(), UserdocPersonRole.OLD_CORRESPONDENCE_ADDRESS);
                if (Objects.nonNull(patentServicePerson)) {
                    CUserdocPerson oldServicePerson = UserdocPersonUtils.convertToUserdocPerson(patentServicePerson, UserdocPersonRole.OLD_CORRESPONDENCE_ADDRESS, false, null, null);
                    userdoc.getUserdocPersonData().getPersonList().add(oldServicePerson);
                }
                userdocService.updateUserdoc(userdoc, true);

                patent.getFile().setServicePerson(newCorrespondenceAddress.getPerson());
                updatePatentAndLogChange(patent, userdoc.getDocumentId(), userdoc.getUserdocType().getUserdocType());
                break;
            }
        }
    }

    private void processChangeNameOrAddress(CUserdoc userdoc) {
        List<ValidationError> externalValidatorErrors = recordalChangeNameOrAddressValidator.validate(userdoc, "userdocPersonData.newOwnersList");
        if (!CollectionUtils.isEmpty(externalValidatorErrors)) {
            throw new RuntimeException("There are validation errors for change name/address recordal !");
        }

        replaceMainObjectOwners(userdoc);
    }

    private List<CUserdocPerson> selectOldRepresentatives(CFile file) {
        CRepresentationData representationData = file.getRepresentationData();
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> oldRepresentatives = representationData.getRepresentativeList();
            List<CUserdocPerson> oldUserdocRepresentatives = UserdocPersonUtils.convertToUserdocPerson(oldRepresentatives, UserdocPersonRole.OLD_REPRESENTATIVE);
            if (!CollectionUtils.isEmpty(oldUserdocRepresentatives)) {
                return oldUserdocRepresentatives;
            }
        }
        return null;
    }

    private List<CUserdocPerson> selectOldOwners(CUserdoc userdoc, CFile file) {
        List<CPerson> oldOwners = OwnerUtils.convertToCPersonList(file.getOwnershipData());
        List<CUserdocPerson> oldUserdocOwners = UserdocPersonUtils.convertToUserdocPerson(oldOwners, UserdocPersonRole.OLD_OWNER, null);
        if (CollectionUtils.isEmpty(oldUserdocOwners)) {
            throw new RuntimeException("There aren't old owners in userdoc for transfer ! " + userdoc.getDocumentId());
        }
        return oldUserdocOwners;
    }

    private CMark selectMark(CFileId fileId) {
        CMark mark = markService.findMark(fileId, true);
        if (Objects.isNull(mark)) {
            throw new RuntimeException("Cannot find mark " + fileId);
        }
        return mark;
    }

    private CPatent selectPatent(CFileId fileId) {
        CPatent patent = patentService.findPatent(fileId, true);
        if (Objects.isNull(patent)) {
            throw new RuntimeException("Cannot find patent " + fileId);
        }
        return patent;
    }

    private void replaceMainObjectOwners(CUserdoc userdoc) {
        CUserdocPersonData userdocPersonData = userdoc.getUserdocPersonData();

        List<CUserdocPerson> newOwners = UserdocPersonUtils.selectNewOwners(userdocPersonData);
        if (CollectionUtils.isEmpty(newOwners)) {
            throw new RuntimeException("There aren't new owners in userdoc ! " + userdoc.getDocumentId());
        }
        List<CPerson> newOwnersPersons = newOwners.stream().map(CUserdocPerson::getPerson).collect(Collectors.toList());

        CProcessParentData userdocParentData = userdoc.getUserdocParentData();
        CFileId fileId = UserdocUtils.selectUserdocMainObject(userdocParentData);
        switch (FileTypeUtils.selectIpObjectTypeByFileType(fileId.getFileType())) {
            case DefaultValue.MARK_OBJECT_INDICATION: {
                CMark mark = selectMark(fileId);
                List<CUserdocPerson> oldOwners = selectOldOwners(userdoc, mark.getFile());

                UserdocPersonUtils.removeUserdocPersonsByRole(userdoc.getUserdocPersonData(), UserdocPersonRole.OLD_OWNER);
                userdoc.getUserdocPersonData().getPersonList().addAll(oldOwners);
                userdocService.updateUserdoc(userdoc, true);

                fillNewOwnersData(newOwnersPersons, mark.getFile());
                updateMarkAndLogChange(mark, userdoc.getDocumentId(), userdoc.getUserdocType().getUserdocType());
                break;
            }
            case DefaultValue.PATENT_OBJECT_INDICATION: {
                CPatent patent = selectPatent(fileId);
                List<CUserdocPerson> oldOwners = selectOldOwners(userdoc, patent.getFile());

                UserdocPersonUtils.removeUserdocPersonsByRole(userdoc.getUserdocPersonData(), UserdocPersonRole.OLD_OWNER);
                userdoc.getUserdocPersonData().getPersonList().addAll(oldOwners);
                userdocService.updateUserdoc(userdoc, true);

                fillNewOwnersData(newOwnersPersons, patent.getFile());
                updatePatentAndLogChange(patent, userdoc.getDocumentId(), userdoc.getUserdocType().getUserdocType());
                break;
            }
        }
    }

    private void updatePatentAndLogChange(CPatent patent, CDocumentId documentId, String userdocType) {
        patentService.updatePatentOnUserdocAuthorization(patent);
        logChangesService.insertObjectUserdocLogChanges(patent.getFile().getFileId(), documentId, userdocType);
    }

    private void updateMarkAndLogChange(CMark mark, CDocumentId documentId, String userdocType) {
        markService.updateMarkOnUserdocAuthorization(mark);
        logChangesService.insertObjectUserdocLogChanges(mark.getFile().getFileId(), documentId, userdocType);
    }

}