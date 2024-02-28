package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenaltyPaymentStatus;
import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenaltyType;

import java.util.List;

public interface AcpAdministrativePenaltyPaymentStatusService {

    CAcpAdministrativePenaltyPaymentStatus findById(Integer id);

    List<CAcpAdministrativePenaltyPaymentStatus> findAll();

}
