package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.userdoc.UserdocPanelService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeletePanelForUserdocTypeValidator implements IpasValidator<String> {

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private UserdocPanelService userdocPanelService;

    @Override
    public List<ValidationError> validate(String userdocType, Object... additionalArgs) {
        String panel = (String) additionalArgs[0];
        List<ValidationError> result = new ArrayList<>();

        if (userdocType == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.panel.delete").messageCode("missing.userdoc.type").build());
        }
        if (panel == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.panel.delete").messageCode("missing.panel").build());
        }

        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(userdocType);
        if (cUserdocType == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.panel.delete").messageCode("missing.userdoc.type").build());
        } else {
            CUserdocPanel cUserdocPanel = userdocPanelService.findUserdocPanelByPanelAndUserdocType(panel, userdocType);
            if (cUserdocPanel == null) {
                result.add(ValidationError.builder().pointer("userdoc.type.panel.delete").messageCode("missing.panel").build());
            } else {
                if (!cUserdocType.getPanels().contains(cUserdocPanel)) {
                    result.add(ValidationError.builder().pointer("userdoc.type.panel.delete").messageCode("missing.panel").build());
                }
            }
        }

        return result;
    }
}
