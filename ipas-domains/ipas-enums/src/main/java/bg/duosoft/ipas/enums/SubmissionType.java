package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum SubmissionType {
    COUNTER(1),
    COURIER(2),
    MAIL(3),
    FAX(4),
    EMAIL(5),
    INTERDEPARTMENTAL_EXCHANGE(6),
    ELECTRONIC(7),
    COMMUNICATOR(8),
    BACKOFFICE_SYSTEM(9),
    SECURE_ELECTRONIC_SERVE(10),
    IMPORT(11),
    DIVIDED(12);


    SubmissionType(int code) {
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

    public static SubmissionType selectByCode(Integer code) {
        SubmissionType submissionType = Arrays.stream(SubmissionType.values())
                .filter(c -> c.code() == code)
                .findFirst()
                .orElse(null);

        if (Objects.isNull(submissionType))
            throw new RuntimeException("Cannot find SubmissionType with code: " + code);

        return submissionType;
    }
}
