package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.nomenclature.ProcessTypeService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.RegisterToProcessType;
import bg.duosoft.ipas.enums.UserdocGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class UpdateUserdocTypeValidator implements IpasValidator<CUserdocType> {

    @Autowired
    private ProcessTypeService processTypeService;

    @Override
    public List<ValidationError> validate(CUserdocType cUserdocType, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();

        if (cUserdocType == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.update").messageCode("missing.userdoc.type").build());
        } else {
           rejectIfEmptyString(result, cUserdocType.getUserdocName(), "userdoc.name.label");

           if (Objects.nonNull(cUserdocType.getUserdocTypeConfig()) && cUserdocType.getUserdocTypeConfig().getRegisterToProcess().equals(RegisterToProcessType.NONE.code())){
               result.add(ValidationError.builder().pointer("userdoc.type.update").messageCode("missing.userdoc.type.configuration.register.to.process.type").build());
           }

           if (cUserdocType.getIndInactive() == null) {
               result.add(ValidationError.builder().pointer("userdoc.type.update").messageCode("missing.indInactive").build());
           }

           if (!checkIfUserdocGroupExist(cUserdocType.getUserdocGroup())) {
               result.add(ValidationError.builder().pointer("userdoc.type.update").messageCode("missing.userdoc.group").build());
           }

           if (StringUtils.isEmpty(cUserdocType.getGenerateProcTyp())) {
               result.add(ValidationError.builder().pointer("userdoc.type.update").messageCode("missing.proc.type").build());
           } else {
               Map<String, String> processTypesMap = processTypeService.getProcessTypeMap();
               if (!processTypesMap.containsKey(cUserdocType.getGenerateProcTyp())) {
                   result.add(ValidationError.builder().pointer("userdoc.type.update").messageCode("missing.proc.type").build());
               }
           }
        }
        return result;
    }

    private boolean checkIfUserdocGroupExist (UserdocGroup value) {
        if (value == null) {
            return false;
        }

        for (UserdocGroup group : UserdocGroup.values()) {
            if (group == value) {
                return true;
            }
        }
        return false;
    }
}
