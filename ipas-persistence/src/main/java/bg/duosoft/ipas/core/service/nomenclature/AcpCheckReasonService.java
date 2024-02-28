package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.acp.CAcpCheckReasonNomenclature;

import java.util.List;

public interface AcpCheckReasonService {

    CAcpCheckReasonNomenclature findById(Integer id);

    List<CAcpCheckReasonNomenclature> findAll();

    List<CAcpCheckReasonNomenclature> findAllByApplicationType(String applicationType);

}
