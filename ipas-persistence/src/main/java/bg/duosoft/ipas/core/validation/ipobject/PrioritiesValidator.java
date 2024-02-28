package bg.duosoft.ipas.core.validation.ipobject;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Georgi
 * Date: 6.2.2020 г.
 * Time: 18:03
 */
@Component
public class PrioritiesValidator implements IpasValidator<CFile> {
    @Autowired
    private PriorityValidator priorityValidator;

    @Override
    public List<ValidationError> validate(CFile file, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (file.getPriorityData() != null) {
            if (file.getPriorityData().isHasParisPriorityData()) {
                if (CollectionUtils.isEmpty(file.getPriorityData().getParisPriorityList())) {
                    errors.add(ValidationError.builder().pointer("priority.hasPriority").messageCode("missing.parisPriority").build());
                } else {
                    file.getPriorityData().getParisPriorityList().stream().map(r -> priorityValidator.validate(r, file)).filter(CollectionUtils::isNotEmpty).forEach(errors::addAll);
                }

            } else {
                if (!CollectionUtils.isEmpty(file.getPriorityData().getParisPriorityList())) {
                    errors.add(ValidationError.builder().pointer("priority.hasPriority").messageCode("should.be.empty").build());
                }
            }
        }
        return errors;
    }
}
