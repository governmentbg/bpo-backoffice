package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenaltyType;

import java.util.List;

public interface AcpAdministrativePenaltyTypeService {

    CAcpAdministrativePenaltyType findById(Integer id);

    List<CAcpAdministrativePenaltyType> findAll();

}
