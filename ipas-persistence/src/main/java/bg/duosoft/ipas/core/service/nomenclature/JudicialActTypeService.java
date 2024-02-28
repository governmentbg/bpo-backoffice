package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.userdoc.court_appeal.CJudicialActType;

import java.util.List;

public interface JudicialActTypeService {
    List<CJudicialActType> findAll();
    CJudicialActType findById(Integer id);
}
