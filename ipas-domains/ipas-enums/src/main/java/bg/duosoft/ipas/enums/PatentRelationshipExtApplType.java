package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum PatentRelationshipExtApplType {
    NATIONAL_PATENT("BG"),
    INTERNATIONAL_PATENT("WO"),
    EUROPEAN_PATENT("EP");

    PatentRelationshipExtApplType(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }

    public static PatentRelationshipExtApplType selectByCode(String code) {
        PatentRelationshipExtApplType type = Arrays.stream(PatentRelationshipExtApplType.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(type))
            throw new RuntimeException("Cannot find PatentRelationshipExtApplType with code: " + code);

        return type;
    }

}
