package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.acp.CAcpTakenItemType;

import java.util.List;

public interface AcpTakenItemTypeService {
    CAcpTakenItemType findById(Integer id);
    List<CAcpTakenItemType> findAll();
}
