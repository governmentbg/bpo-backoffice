package bg.duosoft.ipas.core.validation.reception;

import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Georgi
 * Date: 2.6.2020 г.
 * Time: 12:32
 */
@Component
public class ReceptionValidator implements IpasValidator<CReception> {
    @Autowired
    private ReceptionEuPatentValidator receptionEuPatentValidator;
    @Autowired
    private ReceptionUserdocValidator receptionUserdocValidator;
    @Autowired
    private ReceptionFileValidator receptionFileValidator;
    @Autowired
    private ReceptionAttachmentValidator receptionAttachmentValidator;

    @Override
    public List<ValidationError> validate(CReception obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (!obj.isUserdocRequest() && !obj.isFileRequest() && !obj.isEuPatentRequest()) {
            errors.add(ValidationError.builder().pointer("receptionType").messageCode("reception.wrong.reception.type").build());
            return errors;
        }
        if (obj.isUserdocRequest()) {
            _validateUserdoc(obj, errors,null);
        }
        if (obj.isEuPatentRequest()) {
            rejectIfTrue(errors, obj.isUserdocRequest(), "receptionType", "reception.wrong.reception.type");
//            rejectIfTrue(errors, obj.isFileRequest(), "receptionType", "reception.wrong.reception.type");//eu patents might have fileData too
            List<ValidationError> err = receptionEuPatentValidator.validate(obj, additionalArgs);
            if (!CollectionUtils.isEmpty(err)) {
                errors.addAll(err);
            }
        }
        if (obj.isFileRequest() && !obj.isEuPatentRequest()) {//eu patents might have fileData too
            rejectIfTrue(errors, obj.isUserdocRequest(), "receptionType", "reception.wrong.reception.type");
//            rejectIfTrue(errors, obj.isEuPatentRequest(), "receptionType", "reception.wrong.reception.type");
            errors.addAll(receptionFileValidator.validate(obj, additionalArgs));
        }
        errors.addAll(receptionAttachmentValidator.validate(obj));
        if (!CollectionUtils.isEmpty(obj.getUserdocReceptions())) {
            if (!obj.isFileRequest() && !obj.isEuPatentRequest()) {
                rejectIfTrue(errors, true, "receptionType", "userdoc.receptions.only.master.file.reception");
            } else {
                for (CReception r : obj.getUserdocReceptions()) {
                    //skip affected document validation, because it's empty at this point
                    _validateUserdoc(r, errors, obj);
                }
            }

        }
        return errors;
    }
    private void _validateUserdoc(CReception obj, List<ValidationError> errors, CReception masterReceptionRequest) {
        boolean wrongReceptionType = !obj.isUserdocRequest() || obj.isEuPatentRequest() || obj.isFileRequest();
        rejectIfTrue(errors, wrongReceptionType, "receptionType", "reception.wrong.reception.type");
        if (!wrongReceptionType) {
            List<ValidationError> err = receptionUserdocValidator.validate(obj, masterReceptionRequest);
            if (!CollectionUtils.isEmpty(err)) {
                errors.addAll(err);
            }
        }
    }
}
