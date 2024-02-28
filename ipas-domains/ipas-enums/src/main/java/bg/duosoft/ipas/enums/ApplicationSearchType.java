package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum ApplicationSearchType {
    REGISTRATION_NUMBER("regNumber"),
    FILE_NUMBER("fileNumber");

    ApplicationSearchType(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }

    public static ApplicationSearchType selectByCode(String code) {
        ApplicationSearchType applicationSearchType = Arrays.stream(ApplicationSearchType.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(applicationSearchType))
            throw new RuntimeException("Cannot find FileType with code: " + code);

        return applicationSearchType;
    }

}