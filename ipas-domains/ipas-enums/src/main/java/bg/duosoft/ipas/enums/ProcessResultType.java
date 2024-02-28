package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum ProcessResultType {
    DUMMY_STATUS("D"),
    LAPSED_INVALIDATED_TERMINATED("L"),
    PENDING("P"),
    REGISTERED("R"),
    SECRET_PATENTS_UM("S"),
    WITHDROWN_REFUSED("W");

    ProcessResultType(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }

    public static ProcessResultType selectByCode(String code) {
        ProcessResultType processResultType = Arrays.stream(ProcessResultType.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(processResultType))
            throw new RuntimeException("Cannot find ProcessResultType with code: " + code);

        return processResultType;
    }

}
