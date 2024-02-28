package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum ListCode {
    DISPDES("DISPDES"),
    LATCLS("LATCLS"),
    SPLIT("SPLIT");

    ListCode(String code) {
        this.code = code;
    }

    private final String code;

    public String code() {
        return code;
    }

    public static ListCode selectByCode(String code) {
        ListCode listCode = Arrays.stream(ListCode.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(listCode))
            throw new RuntimeException("Cannot find ListCode with code: " + code);

        return listCode;
    }

}
