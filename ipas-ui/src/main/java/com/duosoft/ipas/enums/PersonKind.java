package com.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum PersonKind {
    Applicant(1),
    Representative(2),
    Partnership(3),
    CorrespondenceAddress(4),
    Inventor(5),
    Grantor(6),
    Grantee(7),
    Payer(8),
    Payee(9),
    Pledger(10),
    NewOwner(11),
    OldOwner(12),
    OldRepresentative(13),
    NewRepresentative(14),
    OldCorrespondenceAddress(15),
    NewCorrespondenceAddress(16),
    Creditor(17),
    AcpCorrespondenceAddress(18),
    AcpInfringer(19),
    AcpRepresentative(20),
    RepresentativeOfTheOwner(21),
    AffectedInventor(22);

    PersonKind(Integer code) {
        this.code = code;
    }

    private Integer code;

    /**
     * Method that returns the code
     *
     * @return the code
     */
    public Integer code() {
        return code;
    }

    public static PersonKind selectByCode(Integer code) {
        PersonKind panel = Arrays.stream(PersonKind.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(panel))
            throw new RuntimeException("Cannot find person kind with code: " + code);

        return panel;
    }
}
