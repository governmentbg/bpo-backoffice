package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.acp.CAcpCheckResult;

import java.util.List;

public interface AcpCheckResultService {
    CAcpCheckResult findById(Integer id);
    List<CAcpCheckResult> findAll();
}
