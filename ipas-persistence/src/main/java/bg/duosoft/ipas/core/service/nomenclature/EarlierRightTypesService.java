package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.userdoc.grounds.CEarlierRightTypes;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfEarlierRightTypes;


import java.util.List;

public interface EarlierRightTypesService {
    List<CEarlierRightTypes> findAllEarlierRightTypesForSpecificPanel(String panel,String version);
    CEarlierRightTypes findById(Integer id);
    CEarlierRightTypes findTypeByVersionAndCode(String version, String code);
}
