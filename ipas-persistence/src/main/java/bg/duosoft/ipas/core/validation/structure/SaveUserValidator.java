package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.user.IpUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User: ggeorgiev
 * Date: 24.7.2019 г.
 * Time: 18:45
 */
@Component
public class SaveUserValidator implements IpasValidator<User> {
    @Autowired
    private IpUserRepository ipUserRepository;
    @Override
    public List<ValidationError> validate(User obj, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();
        IpUser userByLogin = ipUserRepository.findByLogin(obj.getLogin());
        if (userByLogin != null && !Objects.equals(userByLogin.getUserId(), obj.getUserId())) {
            result.add(ValidationError.builder().pointer("userName").messageCode("unique.login.name").build());
        }
        return result;
    }
}
