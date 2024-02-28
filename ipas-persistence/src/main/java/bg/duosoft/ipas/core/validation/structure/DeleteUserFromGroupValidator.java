package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 29.7.2019 г.
 * Time: 11:57
 */
@Component
public class DeleteUserFromGroupValidator implements IpasValidator<Integer> {
    @Autowired
    private UserService userService;
    @Override
    public List<ValidationError> validate(Integer groupId, Object... additionalArgs) {
        Integer userId = (Integer) additionalArgs[0];
        List<ValidationError> result = new ArrayList<>();
        if (groupId == null) {
            result.add(ValidationError.builder().pointer("group.user.delete").messageCode("missing.group").build());
        }
        if (userId == null) {
            result.add(ValidationError.builder().pointer("group.user.delete").messageCode("missing.user").build());
        }
        User user = userService.getUser(userId);
        if (user == null) {
            result.add(ValidationError.builder().pointer("group.user.delete").messageCode("missing.user").build());
        }
        if (!user.getGroupIds().contains(groupId)) {
            result.add(ValidationError.builder().pointer("group.user.delete").messageCode("missing.user.group").build());
        }
        return result;
    }
}
