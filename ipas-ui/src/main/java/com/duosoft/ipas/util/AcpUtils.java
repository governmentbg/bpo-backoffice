package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.acp.CAcpAffectedObject;
import bg.duosoft.ipas.core.model.acp.CAcpExternalAffectedObject;

import java.util.Objects;

public class AcpUtils {

    public static String generateAffectedObjectTitle(CAcpAffectedObject affectedObject) {
        if (Objects.isNull(affectedObject)) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        CAcpExternalAffectedObject externalAffectedObject = affectedObject.getExternalAffectedObject();
        if (Objects.nonNull(externalAffectedObject)) {
            builder.append(externalAffectedObject.getRegistrationNbr()).append(", ").append(externalAffectedObject.getName());
            return builder.toString();
        }


        builder.append(affectedObject.getFileId().createFilingNumber());
        if (Objects.nonNull(affectedObject.getRegistrationNbr())) {
            builder.append(", " + affectedObject.getRegistrationNbr());
        }
        if (Objects.nonNull(affectedObject.getTitle())) {
            builder.append(", " + affectedObject.getTitle());
        }

        return builder.toString();
    }

}
