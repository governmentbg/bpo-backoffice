package bg.duosoft.ipas.core.validation.patent;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PatentInsertionValidator implements IpasValidator<CPatent> {
    @Autowired
    private FileService fileService;
    @Override
    public List<ValidationError> validate(CPatent obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        rejectIfTrue(errors, obj.getFile().getFileId() == null, "ipasError", "reception.error");
        if (obj.getFile().getFileId() != null) {
            boolean exists = fileService.isFileExist(obj.getFile().getFileId());
            rejectIfTrue(errors, !exists, "ipasError", "reception.error");
        }
        return errors;
    }
}
