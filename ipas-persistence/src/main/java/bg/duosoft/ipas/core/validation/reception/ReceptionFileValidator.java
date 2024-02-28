package bg.duosoft.ipas.core.validation.reception;

import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.ApplTyp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * User: Georgi
 * Date: 2.6.2020 г.
 * Time: 0:00
 */
@Component
class ReceptionFileValidator extends ReceptionBaseValidator {

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Override
    public List<ValidationError> validate(CReception obj, Object... additionalArgs) {
        List<ValidationError> errors = super.validate(obj, additionalArgs);
        if (errors == null) {
            errors = new ArrayList<>();
        }
        validateIpObjects(obj, errors);

        return errors;
    }

    private void validateIpObjects(CReception receptionForm, List<ValidationError> errors) {

        if (receptionForm.getFile() == null) {
            errors.add(ValidationError.builder().messageCode("reception.wrong.reception.type").pointer("receptionType").build());
            return;
        }

        if (Objects.nonNull(receptionForm.getFile().getApplicationType()) && !receptionForm.getFile().getApplicationType().equals(ApplTyp.ACP_SIGNAL)) {
            if (receptionForm.getFile().isEmptyTitle()) {
                rejectIfTrue(errors, !StringUtils.isEmpty(receptionForm.getFile().getTitle()), "receptionName", "should.be.empty");
            } else {
                rejectIfEmptyString(errors, receptionForm.getFile().getTitle(), "receptionName");
            }
        }


        if (receptionForm.isRegisterInDocflowSystem()) {
            if (containsOwners(receptionForm)) {
                validateOwnersData(errors, receptionForm.getOwnershipData().getOwnerList());
            } else {
                errors.add(ValidationError.builder().pointer("receptionOwners").messageCode("reception.owners.empty").build());
            }
        } else {
            rejectIfTrue(errors, receptionForm.getOwnershipData() != null && !CollectionUtils.isEmpty(receptionForm.getOwnershipData().getOwnerList()), "receptionOwners", "reception.owners.should.be.empty");
        }

        rejectIfTrue(errors, receptionForm.getFile().getApplicationType() == null, "receptionType", "reception.wrong.apptype");
        if (!StringUtils.isEmpty(receptionForm.getFile().getApplicationSubType())) {
            Optional<String> subtypeExists = StringUtils.isEmpty(receptionForm.getFile().getApplicationType()) ? null : applicationTypeService.findAllApplicationSubTypesByApplTyp(receptionForm.getFile().getApplicationType()).stream().map(r -> r.getApplicationSubType()).filter(r -> Objects.equals(r, receptionForm.getFile().getApplicationSubType())).findAny();
            rejectIfTrue(errors, !subtypeExists.isPresent(), "applicationSubType", "reception.wrong.application.subtype");
        }
        if (!StringUtils.isEmpty(receptionForm.getFile().getApplicationType()) && receptionForm.getFile().getFileId() != null && !StringUtils.isEmpty(receptionForm.getFile().getFileId().getFileType())) {
            String ft = receptionForm.getFile().getFileId().getFileType();
            String dbFt = applicationTypeService.getFileTypeByApplicationType(receptionForm.getFile().getApplicationType());
            rejectIfTrue(errors, !ft.equals(dbFt), "file.fileId.fileType", "incorrect.file.type");
        }
    }

}
