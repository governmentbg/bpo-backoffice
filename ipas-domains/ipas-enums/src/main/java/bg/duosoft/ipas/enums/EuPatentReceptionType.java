package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum EuPatentReceptionType {
    VALIDATION("ЕПИВ"),
    TEMPORARY_PROTECTION("ЕПИВЗ"),
    VALIDATION_AFTER_MODIFICATION("ЕПТ4");

    EuPatentReceptionType(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }

    public static EuPatentReceptionType selectByCode(String code) {
        EuPatentReceptionType type = Arrays.stream(EuPatentReceptionType.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(type))
            throw new RuntimeException("Cannot find eu patent reception type : " + code);

        return type;
    }

}
