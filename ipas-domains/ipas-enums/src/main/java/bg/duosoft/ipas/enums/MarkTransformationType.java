package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum MarkTransformationType {
    INTERNATIONAL_MARK("WO"),
    EUROPEAN_MARK("EM");

    MarkTransformationType(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }

    public static MarkTransformationType selectByCode(String code) {
        MarkTransformationType type = Arrays.stream(MarkTransformationType.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(type))
            throw new RuntimeException("Cannot find TransformationType with code: " + code);

        return type;
    }

}
