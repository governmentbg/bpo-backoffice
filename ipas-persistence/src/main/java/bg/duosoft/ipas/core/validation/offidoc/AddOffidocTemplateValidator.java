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
import java.util.stream.Collectors;

@Component
public class AddOffidocTemplateValidator implements IpasValidator<String> {

    @Autowired
    private OffidocTypeService offidocTypeService;

    @Override
    public List<ValidationError> validate(String offidocType, Object... additionalArgs) {
        String template = (String) additionalArgs[0];
        List<ValidationError> result = new ArrayList<>();

        rejectIfEmptyString(result,offidocType,"add.offidoc.template", "missing.offidoc.type");
        rejectIfEmptyString(result,template,"add.offidoc.template", "missing.offidoc.template");

        COffidocType cOffidocType = offidocTypeService.selectById(offidocType);
        if (cOffidocType == null) {
            result.add(ValidationError.builder().pointer("add.offidoc.template").messageCode("missing.offidoc.type").build());
        } else {
            if (containsCaseInsensitive(template.trim(), cOffidocType.getTemplates().stream().map(COffidocTypeTemplate::getNameWFile).collect(Collectors.toList()))) {
                result.add(ValidationError.builder().pointer("add.offidoc.template").messageCode("offidoc.template.already.exist").build());
            }
        }

        return result;
    }

    private boolean containsCaseInsensitive(String s, List<String> list){
        for (String string : list){
            if (string.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }
}
