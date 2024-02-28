package bg.duosoft.ipas.core.validation.process;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.process.ProcessEventUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ProcessResponsibleUserValidator implements IpasValidator<CProcess> {

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private ProcessService processService;

    @Override
    public List<ValidationError> validate(CProcess obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        CUser responsibleUser = (CUser) additionalArgs[0];
        Integer oldResponsibleUser = processService.selectResponsibleUser(obj.getProcessId());
        rejectIfTrue(errors, (Objects.isNull(oldResponsibleUser) && Objects.isNull(responsibleUser)), "process.responsibleUser", "process.responsibleUser.select");
        if (Objects.nonNull(oldResponsibleUser) && Objects.nonNull(responsibleUser)) {
            rejectIfTrue(errors, oldResponsibleUser.equals(responsibleUser.getUserId()), "process.responsibleUser", "process.responsibleUser.same");
        }
        if (Objects.isNull(responsibleUser)) {
            rejectIfTrue(errors, ProcessEventUtils.isProcessEventActionExists(obj.getProcessEventList()), "process.responsibleUser", "process.responsibleUser.action.exists");
        }
        rejectIfUserDoesNotExistInDatabase(responsibleUser, errors);
        return errors;
    }

    private void rejectIfUserIsEmpty(CUser responsibleUser, List<ValidationError> errors) {
        if (Objects.isNull(responsibleUser) || Objects.isNull(responsibleUser.getUserId()))
            errors.add(ValidationError.builder().pointer("process.responsibleUser").messageCode("process.responsibleUser.select").build());
    }

    private void rejectIfUserDoesNotExistInDatabase(CUser responsibleUser, List<ValidationError> errors) {
        if (CollectionUtils.isEmpty(errors) && Objects.nonNull(responsibleUser)) {
            CUser cUser = simpleUserService.findSimpleUserById(responsibleUser.getUserId());
            rejectIfEmpty(errors, cUser, "process.responsibleUser", "process.responsibleUser.not.exist");
        }
    }
}
