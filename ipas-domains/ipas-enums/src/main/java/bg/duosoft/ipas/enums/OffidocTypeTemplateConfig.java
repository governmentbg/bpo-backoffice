package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum OffidocTypeTemplateConfig {

    TEMPLATE("TEMPLATE"),
    REGISTRATION_NBR("REGISTRATION_NBR");

    OffidocTypeTemplateConfig(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }

    public static OffidocTypeTemplateConfig selectByCode(String code) {
        OffidocTypeTemplateConfig templateConfig = Arrays.stream(OffidocTypeTemplateConfig.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(templateConfig)) {
            throw new RuntimeException("Cannot find offidoc type template config with code: " + code);
        }

        return templateConfig;
    }
}
