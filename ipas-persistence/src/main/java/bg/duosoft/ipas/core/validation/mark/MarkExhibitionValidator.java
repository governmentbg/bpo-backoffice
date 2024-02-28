package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.ipobject.ExhibitionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarkExhibitionValidator implements IpasValidator<CMark> {
    @Autowired
    private ExhibitionValidator exhibitionValidator;

    @Override
    public List<ValidationError> validate(CMark mark, Object... additionalArgs) {
        return exhibitionValidator.validate(mark.getFile());
    }
}
