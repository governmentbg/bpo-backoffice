package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.document.CExtraData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.UserdocExtraDataTypeCode;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserdocServiceScopeValidator implements IpasValidator<CUserdoc> {
    private static final String SERVICE_SCOPE_PANEL = "ServiceScope";

    @Override
    public List<ValidationError> validate(CUserdoc obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        List<CUserdocPanel> panels = obj.getUserdocType().getPanels();
        CUserdocPanel serviceScopePanel = panels.stream()
                .filter(cUserdocPanel -> cUserdocPanel.getPanel().equalsIgnoreCase(SERVICE_SCOPE_PANEL))
                .findFirst()
                .orElse(null);

        if (Objects.nonNull(serviceScopePanel)) {
            Boolean isAllIncluded = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.SERVICE_SCOPE.name(), obj.getUserdocExtraData());
            rejectIfEmpty(errors, isAllIncluded, "userdoc.extraData." + UserdocExtraDataTypeCode.SERVICE_SCOPE.name());
        }

        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
