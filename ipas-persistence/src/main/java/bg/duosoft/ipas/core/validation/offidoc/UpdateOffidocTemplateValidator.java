package bg.duosoft.ipas.core.validation.offidoc;

import bg.duosoft.ipas.core.model.offidoc.COffidocTypeTemplate;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeTemplateService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateOffidocTemplateValidator implements IpasValidator<String> {

    @Autowired
    private OffidocTypeTemplateService offidocTypeTemplateService;

    @Override
    public List<ValidationError> validate(String offidocType, Object... additionalArgs) {
        String template = (String) additionalArgs[0];
        String nameConfig = (String) additionalArgs[1];
        List<ValidationError> result = new ArrayList<>();

        if (offidocType == null) {
            result.add(ValidationError.builder().pointer("offidoc.type.template.update").messageCode("missing.offidoc.type").build());
        }
        if (template == null) {
            result.add(ValidationError.builder().pointer("offidoc.type.template.update").messageCode("missing.offidoc.template").build());
        }
        if (nameConfig == null) {
            result.add(ValidationError.builder().pointer("offidoc.type.template.update").messageCode("missing.offidoc.template.nameConfig").build());
        }

        COffidocTypeTemplate cOffidocTypeTemplate = offidocTypeTemplateService.selectById(offidocType,template);
        if (cOffidocTypeTemplate == null) {
            result.add(ValidationError.builder().pointer("offidoc.type.template.update").messageCode("missing.offidoc.template").build());
        }

        return result;
    }
}
