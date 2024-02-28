package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.ipobject.RelationshipsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: Georgi
 * Date: 13.12.2019 г.
 * Time: 15:00
 */
@Component
public class MarkRelationshipsValidator implements IpasValidator<CMark> {
    @Autowired
    private RelationshipsValidator relationshipsValidator;

    @Override
    public List<ValidationError> validate(CMark obj, Object... additionalArgs) {
        return relationshipsValidator.validate(obj.getFile(), additionalArgs);
    }
}
