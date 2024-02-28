package bg.duosoft.ipas.core.validation.reception;

import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionEuPatent;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.EuPatentReceptionType;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.util.eupatent.EuPatentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User: Georgi
 * Date: 2.6.2020 г.
 * Time: 0:32
 */
@Component
class ReceptionEuPatentValidator extends ReceptionBaseValidator {
    
    @Autowired
    private EbdPatentService ebdPatentService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ConfigParamService configParamService;
    @Autowired
    private FileService fileService;
    @Override
    public List<ValidationError> validate(CReception obj, Object... additionalArgs) {
        List<ValidationError> errors = super.validate(obj, additionalArgs);
        if (errors == null) {
            errors = new ArrayList<>();
        }
        validateEuPatent(obj, errors);
        return errors;
    }


    private void validateEuPatent(CReception receptionForm, List<ValidationError> errors) {
        rejectIfEmptyString(errors, receptionForm.getFile().getTitle(), "receptionName");
//        rejectIfFalse(errors, isOwnersExists(receptionForm), "receptionOwners", "reception.owners.empty");

        CReceptionEuPatent euPatent = receptionForm.getEuPatent();
        Integer objectNumber = euPatent.getObjectNumber();
        if (Objects.isNull(objectNumber)) {
            rejectIfEmpty(errors, objectNumber, "receptionEuPatentNumber");
        } else {
            CEbdPatent ebdPatent = ebdPatentService.selectByFileNumber(objectNumber);
            if (Objects.isNull(ebdPatent)) {
                rejectIfEmpty(errors, ebdPatent, "receptionEuPatentNumber", "object.not.exist");
            } else {
                String euPatentReceptionType = euPatent.getUserdocType();//euPatentReceptionType might be null if the patent does not exist in ipas, and there should not be inserted any userdoctype at the end of insertion!
                CFileId euPatentCFileId = EuPatentUtils.generateEUPatentCFileId(ebdPatent);
                CFile ipasEuPatent = fileService.findById(euPatentCFileId.getFileSeq(), euPatentCFileId.getFileType(), euPatentCFileId.getFileSeries(), euPatentCFileId.getFileNbr());
                if (Objects.isNull(ipasEuPatent)) {
                    if (euPatentReceptionType != null && euPatentReceptionType.equals(EuPatentReceptionType.VALIDATION_AFTER_MODIFICATION.code()))
                        errors.add(ValidationError.builder().pointer("euPatentType").messageCode("object.not.exist." + EuPatentReceptionType.VALIDATION_AFTER_MODIFICATION).build());
                } else {
                    rejectIfEmpty(errors, euPatentReceptionType, "euPatentType");
                    CProcess cProcess = processService.selectProcess(ipasEuPatent.getProcessId(), true);
                    if (EuPatentUtils.isEuPatentValidated(cProcess, configParamService) && !euPatentReceptionType.equals(EuPatentReceptionType.VALIDATION_AFTER_MODIFICATION.code()))
                        errors.add(ValidationError.builder().pointer("euPatentType").messageCode("eupatent.ipas.validated").build());
                }

                if (CollectionUtils.isEmpty(errors)) {
                    String registrationNumber = ebdPatent.getRegistrationNumber();
                    Integer statusCode = ebdPatent.getStatusCode();
                    boolean isEpoPatentNotIssued = (statusCode == EuPatentUtils.EPO_PATENT_VALIDATION_STATUS_600 || statusCode == EuPatentUtils.EPO_PATENT_VALIDATION_STATUS_601);
                    if (StringUtils.isEmpty(registrationNumber) || isEpoPatentNotIssued) {
                        if (!StringUtils.isEmpty(euPatentReceptionType) && (euPatentReceptionType.equals(EuPatentReceptionType.VALIDATION.code()) || euPatentReceptionType.equals(EuPatentReceptionType.VALIDATION_AFTER_MODIFICATION.code()))) {
                            errors.add(ValidationError.builder().pointer("euPatentType").messageCode("eupatent.ipas.not.issued").build());
                        }
                    }
                }

            }
        }
    }
}
