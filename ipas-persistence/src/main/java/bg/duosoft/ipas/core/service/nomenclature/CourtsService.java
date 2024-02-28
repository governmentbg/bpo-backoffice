package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.miscellaneous.CCourt;
import bg.duosoft.ipas.core.model.userdoc.court_appeal.CJudicialActType;

import java.util.List;

public interface CourtsService {

    List<CCourt> selectByNameLike(String name);

    List<CCourt> selectByName(String name);

    List<CCourt> findAll();
    CCourt findById(Integer id);
}