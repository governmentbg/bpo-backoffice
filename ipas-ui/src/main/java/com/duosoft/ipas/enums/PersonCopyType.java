package com.duosoft.ipas.enums;

import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;

import java.util.Arrays;
import java.util.Objects;

public enum PersonCopyType {
    FROM_MAIN_OBJECT_OWNER(1),
    FROM_MAIN_OBJECT_REPRESENTATIVE(2),
    FROM_CURRENT_OBJECT_APPLICANT(3),
    FROM_CURRENT_OBJECT_REPRESENTATIVE(4);

    PersonCopyType(Integer code) {
        this.code = code;
    }

    private Integer code;

    public Integer code() {
        return code;
    }

    public static PersonCopyType selectByCode(Integer code) {
        PersonCopyType panel = Arrays.stream(PersonCopyType.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(panel))
            throw new RuntimeException("Cannot find person kind with code: " + code);

        return panel;
    }

    public static PersonCopyType selectByUserdocPersonRole(CUserdocPersonRole role) {
        if (Objects.isNull(role))
            throw new RuntimeException("Userdoc person role is empty !");

        Boolean indTakeFromOwner = role.getIndTakeFromOwner();
        Boolean indTakeFromRepresentative = role.getIndTakeFromRepresentative();
        if (Objects.nonNull(indTakeFromOwner) && indTakeFromOwner) {
            return PersonCopyType.FROM_MAIN_OBJECT_OWNER;
        } else if (Objects.nonNull(indTakeFromRepresentative) && indTakeFromRepresentative) {
            return PersonCopyType.FROM_MAIN_OBJECT_REPRESENTATIVE;
        } else {
            return null;
        }
    }

}
