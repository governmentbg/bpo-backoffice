package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum RepresentativeType {
    NATURAL_PERSON("AG"),
    CORRESPONDENCE_ADDRESS("AS"),
    PARTNERSHIP("PA"),
    LAWYER("LA"),
    LAWYER_COMPANY("LC"),
    LAWYER_PARTNERSHIP("LP"),
    TEMP_SERVICE_PERSON("TS"),
    INTERNATIONAL_AGENT("IR"),
    PATENT_SPECIALIST("RE");
    private final String value;

    RepresentativeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RepresentativeType selectByCode(String value) {
        RepresentativeType result = Arrays.stream(RepresentativeType.values())
                .filter(c -> c.getValue().equals(value))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(result))
            throw new RuntimeException("Cannot find RepresentativeType with value: " + value);

        return result;
    }
}
