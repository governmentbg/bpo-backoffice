package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraData;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.UserdocExtraDataTypeCode;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserdocClaimValidator implements IpasValidator<CUserdoc> {

    @Autowired
    private MessageSource messageSource;

    private final Integer motivesLength = 2000;
    private final String claimPanelName = "Claim";

    @Override
    public List<ValidationError> validate(CUserdoc obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        List<CUserdocPanel> panels = obj.getUserdocType().getPanels();
        if (!CollectionUtils.isEmpty(panels)) {
            CUserdocPanel claimPanel = panels.stream()
                    .filter(r -> r.getPanel().equals(claimPanelName))
                    .findFirst()
                    .orElse(null);

            List<CUserdocExtraData> userdocExtraData = obj.getUserdocExtraData();
            if (Objects.nonNull(claimPanel) && Objects.nonNull(userdocExtraData)) {
                String claimMotive = UserdocExtraDataUtils.selectText(UserdocExtraDataTypeCode.CLAIM_MOTIVE.name(), userdocExtraData);
                if (StringUtils.hasText(claimMotive) && claimMotive.length() > motivesLength) {
                    String message = messageSource.getMessage("invalid.extra.data.text1.symbols.length", new String[]{motivesLength.toString()}, LocaleContextHolder.getLocale());
                    errors.add(ValidationError.builder().pointer("userdoc.extraData." + UserdocExtraDataTypeCode.CLAIM_MOTIVE.name()).message(message).build());
                }
            }
        }
        return errors;
    }
}
