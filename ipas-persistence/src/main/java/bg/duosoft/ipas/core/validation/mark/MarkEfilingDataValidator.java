package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.ipobject.IpObjectEFilingDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarkEfilingDataValidator implements IpasValidator<CMark> {

    @Autowired
    private IpObjectEFilingDataValidator patentIpObjectEFilingDataValidator;

    @Override
    public List<ValidationError> validate(CMark obj, Object... additionalArgs) {
        return patentIpObjectEFilingDataValidator.validate(obj.getMarkEFilingData());
    }
}
