package bg.duosoft.ipas.core.validation.patent;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.ipobject.ExhibitionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: Georgi
 * Date: 5.12.2019 г.
 * Time: 15:05
 */
@Component
public class PatentExhibitionValidator implements IpasValidator<CPatent> {
    @Autowired
    private ExhibitionValidator exhibitionValidator;
    @Override
    public List<ValidationError> validate(CPatent obj, Object... additionalArgs) {
        return exhibitionValidator.validate(obj.getFile());
    }
}
