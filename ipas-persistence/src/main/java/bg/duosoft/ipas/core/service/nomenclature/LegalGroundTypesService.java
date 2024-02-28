package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.userdoc.grounds.CLegalGroundTypes;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLegalGroundTypes;

import java.util.List;

public interface LegalGroundTypesService {
   List<CLegalGroundTypes> findAllLegalGroundTypesForSpecificPanel(String version,String panel);
   List<CLegalGroundTypes> findAllLegalGroundsForSpecificPanelAndEarlierRight(String version, String panel, Integer earlierRightTypeId);
   CLegalGroundTypes findById(Integer id);
   List<CLegalGroundTypes> findAllLegalGroundsByVersionAndCode(String version, String code);
}
