package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CProtectionData;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.FileType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarkNiceClassValidator implements IpasValidator<CMark> {

    @Override
    public List<ValidationError> validate(CMark mark, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (!mark.getFile().getFileId().getFileType().equals(FileType.ACP.code())){
            rejectIfNiceClassesListIsEmpty(errors, mark.getProtectionData());
        }
        return errors;
    }

    private void rejectIfNiceClassesListIsEmpty(List<ValidationError> errors, CProtectionData protectionData) {
        rejectIfEmpty(errors, protectionData, "protectionData.niceClassList");
        if (CollectionUtils.isEmpty(errors)) {
            rejectIfEmptyCollection(errors, protectionData.getNiceClassList(), "protectionData.niceClassList");
        }
    }
}
