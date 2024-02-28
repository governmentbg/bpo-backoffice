package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum PartnershipType {

    PARTNERSHIP("PARTNERSHIP"),
    COMPANY("COMPANY");

    PartnershipType(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }

    public static PartnershipType selectByCode(String code) {
        PartnershipType type = Arrays.stream(PartnershipType.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(type))
            throw new RuntimeException("Cannot find PartnershipType with code: " + code);

        return type;
    }
}
