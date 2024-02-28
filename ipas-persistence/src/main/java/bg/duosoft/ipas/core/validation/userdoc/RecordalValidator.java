package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.miscellaneous.CCourt;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.service.nomenclature.CourtsService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.RecordalType;
import bg.duosoft.ipas.enums.UserdocExtraDataTypeCode;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RecordalValidator implements IpasValidator<CUserdoc> {

    private static final String POINTER_PREFIX = "userdoc.extraData.";

    @Autowired
    private CourtsService courtsService;

    @Override
    public List<ValidationError> validate(CUserdoc userdoc, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        List<CUserdocPanel> panels = userdoc.getUserdocType().getPanels();
        List<CUserdocPanel> recordalPanels = panels.stream().filter(CUserdocPanel::getIndRecordal).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(recordalPanels)) {
            validateRecordalPanelsCount(errors, recordalPanels);
            if (CollectionUtils.isEmpty(errors)) {
                String panel = recordalPanels.get(0).getPanel();
                RecordalType recordalType = RecordalType.valueOf(panel);
                switch (recordalType) {
                    case Bankruptcy:
                        validateRecordalBankruptcy(userdoc, errors);
                        break;
                    case Pledge:
                        validateRecordalPledge(userdoc, errors);
                        break;
                    case Security_measure:
                        validateSecurityMeasure(userdoc, errors);
                        break;
                    case Licenses:
                        validateRecordalLicense(userdoc, errors);
                        break;
                    case Transfer:
                        validateRecordalTransfer(userdoc, errors);
                        break;
                    case Renewal:
                        validateRecordalRenewal(userdoc, errors);
                        break;
                    case Withdrawal:
                        validateRecordalWithdrawal(userdoc, errors);
                        break;
                    case Change:
                        validateRecordalChangeNameOrAddress(userdoc, errors);
                        break;
                    case Change_correspondence_address:
                        validateRecordalChangeCorrespondenceAddress(userdoc, errors);
                        break;
                    case Change_representative:
                        validateRecordalChangeRepresentative(userdoc, errors);
                        break;
                }
            }
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

    private void validateRecordalWithdrawal(CUserdoc userdoc, List<ValidationError> errors) {
        rejectIfEffectiveDateIsEmptyForRegisteredRecordal(errors, userdoc);

        if (UserdocUtils.isUserdocMainObjectMarkOrDesign(userdoc.getUserdocParentData())) {
            rejectIfServiceScopeRadioIsNotSelected(errors, userdoc);
        }
    }

    private void validateRecordalChangeRepresentative(CUserdoc userdoc, List<ValidationError> errors) {
        rejectIfEffectiveDateIsEmptyForRegisteredRecordal(errors, userdoc);
    }

    private void validateRecordalChangeCorrespondenceAddress(CUserdoc userdoc, List<ValidationError> errors) {
        rejectIfEffectiveDateIsEmptyForRegisteredRecordal(errors, userdoc);
    }

    private void validateRecordalChangeNameOrAddress(CUserdoc userdoc, List<ValidationError> errors) {
        rejectIfEffectiveDateIsEmptyForRegisteredRecordal(errors, userdoc);
    }

    private void validateRecordalRenewal(CUserdoc userdoc, List<ValidationError> errors) {
        rejectIfEffectiveDateIsEmptyForRegisteredRecordal(errors, userdoc);

        Date expirationDate = UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.RENEWAL_NEW_EXPIRATION_DATE.name(), userdoc.getUserdocExtraData());
        rejectIfEmpty(errors, expirationDate, POINTER_PREFIX + UserdocExtraDataTypeCode.RENEWAL_NEW_EXPIRATION_DATE.name());
        if (UserdocUtils.isUserdocMainObjectMarkOrDesign(userdoc.getUserdocParentData())) {
            rejectIfServiceScopeRadioIsNotSelected(errors, userdoc);
        }
    }

    private void validateRecordalBankruptcy(CUserdoc userdoc, List<ValidationError> errors) {
        rejectIfInvalidationDateIsBeforeEffectiveDate(errors, userdoc);
        rejectIfInvalidationDateIsEmptyForInvalidatedRecordal(errors, userdoc);
        rejectIfEffectiveDateIsEmptyForRegisteredRecordal(errors, userdoc);

        String caseNumber = UserdocExtraDataUtils.selectText(UserdocExtraDataTypeCode.BANKRUPTCY_CASE_NUMBER.name(), userdoc.getUserdocExtraData());
        rejectIfEmptyString(errors, caseNumber, POINTER_PREFIX + UserdocExtraDataTypeCode.BANKRUPTCY_CASE_NUMBER.name());

        String court = UserdocExtraDataUtils.selectText(UserdocExtraDataTypeCode.BANKRUPTCY_COURT_NAME.name(), userdoc.getUserdocExtraData());
        rejectIfEmptyString(errors, court, POINTER_PREFIX + UserdocExtraDataTypeCode.BANKRUPTCY_COURT_NAME.name());
        if (CollectionUtils.isEmpty(errors)) {
            List<CCourt> databaseCourts = courtsService.selectByName(court);
            rejectIfEmptyCollection(errors, databaseCourts, POINTER_PREFIX + UserdocExtraDataTypeCode.BANKRUPTCY_COURT_NAME.name(), "userdoc.bankruptcy.wrong.court");
        }
    }

    private void validateRecordalTransfer(CUserdoc userdoc, List<ValidationError> errors) {
        rejectIfEffectiveDateIsEmptyForRegisteredRecordal(errors, userdoc);

        if (UserdocUtils.isUserdocMainObjectMarkOrDesign(userdoc.getUserdocParentData())) {
            rejectIfServiceScopeRadioIsNotSelected(errors, userdoc);
        }
    }

    private void validateRecordalPledge(CUserdoc userdoc, List<ValidationError> errors) {
        rejectIfInvalidationDateIsBeforeEffectiveDate(errors, userdoc);
        rejectIfInvalidationDateIsEmptyForInvalidatedRecordal(errors, userdoc);
        rejectIfEffectiveDateIsEmptyForRegisteredRecordal(errors, userdoc);

        String price = UserdocExtraDataUtils.selectText(UserdocExtraDataTypeCode.PLEDGE_AMOUNT.name(), userdoc.getUserdocExtraData());
        String priceRegex = "^([0-9]+.[0-9][0-9])";
        rejectIfEmptyString(errors, price, POINTER_PREFIX + UserdocExtraDataTypeCode.PLEDGE_AMOUNT.name());
        rejectIfNotMatchRegex(errors, price, priceRegex, POINTER_PREFIX + UserdocExtraDataTypeCode.PLEDGE_AMOUNT.name(), "userdoc.recordal.pledge.price.format");

        Integer sequenceNumber = UserdocExtraDataUtils.selectNumber(UserdocExtraDataTypeCode.PLEDGE_SEQUENCE_NUMBER.name(), userdoc.getUserdocExtraData());
        rejectIfEmpty(errors, sequenceNumber, POINTER_PREFIX + UserdocExtraDataTypeCode.PLEDGE_SEQUENCE_NUMBER.name());
        rejectIfNotPositiveNumber(errors, sequenceNumber, POINTER_PREFIX + UserdocExtraDataTypeCode.PLEDGE_SEQUENCE_NUMBER.name(), "userdoc.recordal.pledge.sequence.positive");

        String expirationDate = UserdocExtraDataUtils.selectText(UserdocExtraDataTypeCode.PLEDGE_EXPIRATION_DATE.name(), userdoc.getUserdocExtraData());
        rejectIfEmptyString(errors, expirationDate, POINTER_PREFIX + UserdocExtraDataTypeCode.PLEDGE_EXPIRATION_DATE.name());
    }

    private void validateSecurityMeasure(CUserdoc userdoc, List<ValidationError> errors) {
        rejectIfInvalidationDateIsBeforeEffectiveDate(errors, userdoc);
        rejectIfInvalidationDateIsEmptyForInvalidatedRecordal(errors, userdoc);
        rejectIfEffectiveDateIsEmptyForRegisteredRecordal(errors, userdoc);

        Boolean rightsUse = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.SECURITY_MEASURE_PROHIBITION_RIGHTS_USE.name(), userdoc.getUserdocExtraData());
        Boolean rightsManage = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.SECURITY_MEASURE_PROHIBITION_RIGHTS_MANAGE.name(), userdoc.getUserdocExtraData());
        rejectIfBothAreNotTrue(errors, rightsUse, rightsManage, POINTER_PREFIX + UserdocExtraDataTypeCode.SECURITY_MEASURE_PROHIBITION_RIGHTS_MANAGE.name(), "userdoc.securityMeasure.checkbox");
    }

    private void validateRecordalLicense(CUserdoc userdoc, List<ValidationError> errors) {
        rejectIfInvalidationDateIsBeforeEffectiveDate(errors, userdoc);
        rejectIfInvalidationDateIsEmptyForInvalidatedRecordal(errors, userdoc);
        rejectIfEffectiveDateIsEmptyForRegisteredRecordal(errors, userdoc);

        List<CUserdocPerson> grantees = UserdocPersonUtils.selectGrantees(userdoc.getUserdocPersonData());
        rejectIfEmptyCollection(errors, grantees, "userdocPersonData.granteeList");

        List<CUserdocPerson> grantors = UserdocPersonUtils.selectGrantors(userdoc.getUserdocPersonData());
        rejectIfEmptyCollection(errors, grantors, "userdocPersonData.grantorList");

        Boolean isFullTerritorialCoverage = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.LICENSE_TERRITORIAL_SCOPE.name(), userdoc.getUserdocExtraData());
        if (Objects.nonNull(isFullTerritorialCoverage) && !isFullTerritorialCoverage) {
            String territorialRestriction = UserdocExtraDataUtils.selectText(UserdocExtraDataTypeCode.LICENSE_TERRITORIAL_RESTRICTION.name(), userdoc.getUserdocExtraData());
            rejectIfEmptyString(errors, territorialRestriction, POINTER_PREFIX + UserdocExtraDataTypeCode.LICENSE_TERRITORIAL_RESTRICTION.name());
        }

        Boolean isSublicense = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.LICENSE_SUBLICENSE.name(), userdoc.getUserdocExtraData());
        if (Objects.nonNull(isSublicense) && isSublicense) {
            String sublicenseNumber = UserdocExtraDataUtils.selectText(UserdocExtraDataTypeCode.LICENSE_SUBLICENSE_IDENTIFIER.name(), userdoc.getUserdocExtraData());
            rejectIfEmptyString(errors, sublicenseNumber, POINTER_PREFIX + UserdocExtraDataTypeCode.LICENSE_SUBLICENSE_IDENTIFIER.name());
        }

        Boolean isToSpecifiedDate = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.LICENSE_EXPIRATION_DATE_TYPE.name(), userdoc.getUserdocExtraData());
        if (Objects.nonNull(isToSpecifiedDate) && isToSpecifiedDate) {
            Date date = UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.LICENSE_EXPIRATION_DATE.name(), userdoc.getUserdocExtraData());
            rejectIfEmpty(errors, date, POINTER_PREFIX + UserdocExtraDataTypeCode.LICENSE_EXPIRATION_DATE.name());
        }

        if (UserdocUtils.isUserdocMainObjectMarkOrDesign(userdoc.getUserdocParentData())) {
            rejectIfServiceScopeRadioIsNotSelected(errors, userdoc);
        }
    }

    private void rejectIfServiceScopeRadioIsNotSelected(List<ValidationError> errors, CUserdoc userdoc) {
        Boolean isAllIncluded = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.SERVICE_SCOPE.name(), userdoc.getUserdocExtraData());
        rejectIfEmpty(errors, isAllIncluded, POINTER_PREFIX + UserdocExtraDataTypeCode.SERVICE_SCOPE.name());
    }

    private void validateRecordalPanelsCount(List<ValidationError> errors, List<CUserdocPanel> recordalPanels) {
        if (recordalPanels.size() > 1) {
            errors.add(ValidationError.builder().pointer("userdoc.recordal.panels").messageCode("userdoc.recordal.more.than.one").build());
        }
    }

    private void rejectIfInvalidationDateIsBeforeEffectiveDate(List<ValidationError> errors, CUserdoc userdoc) {
        Date effectiveDate = UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.EFFECTIVE_DATE.name(), userdoc.getUserdocExtraData());
        Date invalidationDate = UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.INVALIDATION_DATE.name(), userdoc.getUserdocExtraData());
        if (Objects.nonNull(effectiveDate) && Objects.nonNull(invalidationDate)) {
            if (invalidationDate.before(effectiveDate)) {
                errors.add(ValidationError.builder().pointer(POINTER_PREFIX + UserdocExtraDataTypeCode.INVALIDATION_DATE.name()).messageCode("userdoc.recordal.invalidation.date.before.recordal").build());
            }
        }
    }

    private void rejectIfInvalidationDateIsEmptyForInvalidatedRecordal(List<ValidationError> errors, CUserdoc userdoc) {
        CFileRecordal fileRecordal = userdoc.getFileRecordal();
        if (Objects.nonNull(fileRecordal)) {
            CDocumentId invalidationDocumentId = fileRecordal.getInvalidationDocumentId();
            if (Objects.nonNull(invalidationDocumentId) && Objects.nonNull(invalidationDocumentId.getDocNbr())) {
                Date invalidationDate = UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.INVALIDATION_DATE.name(), userdoc.getUserdocExtraData());
                if (Objects.isNull(invalidationDate)) {
                    errors.add(ValidationError.builder().pointer(POINTER_PREFIX + UserdocExtraDataTypeCode.INVALIDATION_DATE.name()).messageCode("userdoc.recordal.invalidation.date.non.null").build());
                }
            }
        }
    }

    private void rejectIfEffectiveDateIsEmptyForRegisteredRecordal(List<ValidationError> errors, CUserdoc userdoc) {
        CFileRecordal fileRecordal = userdoc.getFileRecordal();
        if (Objects.nonNull(fileRecordal)) {
            Date effectiveDate = UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.EFFECTIVE_DATE.name(), userdoc.getUserdocExtraData());
            if (Objects.isNull(effectiveDate)) {
                errors.add(ValidationError.builder().pointer(POINTER_PREFIX + UserdocExtraDataTypeCode.EFFECTIVE_DATE.name()).messageCode("userdoc.recordal.effective.date.non.null").build());
            }
        }
    }
}
