package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.structure.Group;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisGroup;
import bg.duosoft.ipas.persistence.repository.entity.user.CfThisGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User: ggeorgiev
 * Date: 5.7.2019 г.
 * Time: 11:27
 */
@Component
public class SaveGroupValidator implements IpasValidator<Group> {
    @Autowired
    private CfThisGroupRepository cfThisGroupRepository;


    @Override
    public List<ValidationError> validate(Group group, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();
        CfThisGroup sameName = cfThisGroupRepository.getByGroupname(group.getGroupName());
        if (sameName != null && !Objects.equals(sameName.getGroupId(), group.getGroupId())) {
            result.add(ValidationError.builder().pointer("groupName").messageCode("object.already.exist").build());
        }
        return result;
    }
}
