package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum ReceptionType {
    MARK(1),
    PATENT(2),
    SPC(3),
    UTILITY_MODEL(4),
    GEOGRAPHICAL_INDICATION(5),
    DESIGN(6),
    SORT_AND_BREEDS(7),
    EU_PATENT(8),
    ACP(11),
    USERDOC(9);

    ReceptionType(int code) {
        this.code = code;
    }

    private int code;

    /**
     * Method that returns the code
     *
     * @return the code
     */
    public int code() {
        return code;
    }


    public static ReceptionType selectByCode(Integer code) {
        ReceptionType receptionType = Arrays.stream(ReceptionType.values())
                .filter(c -> c.code == code)
                .findFirst()
                .orElse(null);

        if (Objects.isNull(receptionType))
            throw new RuntimeException("Canno find FileType with code: " + code);

        return receptionType;
    }

}
