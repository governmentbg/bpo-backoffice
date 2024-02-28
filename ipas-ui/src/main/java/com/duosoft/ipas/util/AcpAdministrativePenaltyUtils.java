package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenalty;
import bg.duosoft.ipas.enums.AdministrativePenaltyPaymentStatus;
import bg.duosoft.ipas.enums.AdministrativePenaltyType;


import java.util.Objects;

public class AcpAdministrativePenaltyUtils {


    public static boolean showAmountSection(CAcpAdministrativePenalty penalty) {

        if (Objects.isNull(penalty) || Objects.isNull(penalty.getPenaltyType())) {
            return false;
        }

        return penalty.getPenaltyType().getId().equals(AdministrativePenaltyType.FINE.code()) || penalty.getPenaltyType().getId().equals(AdministrativePenaltyType.PROPERTY_SANCTION.code());
    }


    public static boolean showOtherTypeDescription(CAcpAdministrativePenalty penalty) {

        if (Objects.isNull(penalty) || Objects.isNull(penalty.getPenaltyType())) {
            return false;
        }

        return penalty.getPenaltyType().getId().equals(AdministrativePenaltyType.OTHERS.code());
    }

    public static boolean showPartiallyPaidAmount(CAcpAdministrativePenalty penalty) {

        if (Objects.isNull(penalty) || Objects.isNull(penalty.getPaymentStatus())) {
            return false;
        }
        return penalty.getPaymentStatus().getId().equals(AdministrativePenaltyPaymentStatus.PARTIALLY_PAID.code());

    }


    public static boolean showNotificationDate(CAcpAdministrativePenalty penalty) {
        if (Objects.isNull(penalty) || Objects.isNull(penalty.getPaymentStatus())) {
            return false;
        }

        if (penalty.getPaymentStatus().getId().equals(AdministrativePenaltyPaymentStatus.UNPAID.code()) || penalty.getPaymentStatus().getId().equals(AdministrativePenaltyPaymentStatus.PARTIALLY_PAID.code())) {
            return true;
        }

        return false;
    }


}
