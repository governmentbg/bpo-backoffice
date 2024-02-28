package bg.duosoft.ipas.core.validation.offidoc;

import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.core.model.offidoc.COffidocTypeTemplate;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ChangeOffidocDefaultTemplateValidator implements IpasValidator<String> {

    @Autowired
    private OffidocTypeService offidocTypeService;

    @Override
    public List<ValidationError> validate(String offidocType, Object... additionalArgs) {
        String template = (String) additionalArgs[0];
        List<ValidationError> result = new ArrayList<>();

        rejectIfEmptyString(result, offidocType, "change.offidoc.default.template", "missing.offidoc.type");
        rejectIfEmptyString(result, template, "change.offidoc.default.template", "missing.offidoc.template");

        COffidocType cOffidocType = offidocTypeService.selectById(offidocType);
        if (cOffidocType == null) {
            result.add(ValidationError.builder().pointer("change.offidoc.default.template").messageCode("missing.offidoc.type").build());
        } else {
            COffidocTypeTemplate changeTemplate = cOffidocType.getTemplates().stream().filter(t -> t.getNameWFile().equals(template)).findFirst().orElse(null);
            if (Objects.isNull(changeTemplate)) {
                result.add(ValidationError.builder().pointer("change.offidoc.default.template").messageCode("missing.offidoc.template").build());
            } else {
                if (cOffidocType.getDefaultTemplate().equals(template)) {
                    result.add(ValidationError.builder().pointer("change.offidoc.default.template").messageCode("change.offidoc.default.template.error").build());
                }
            }
        }

        return result;
    }
}
