package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.acp.CAcpTakenItemStorage;

import java.util.List;

public interface AcpTakenItemStorageService {
    CAcpTakenItemStorage findById(Integer id);
    List<CAcpTakenItemStorage> findAll();
}
