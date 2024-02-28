package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.userdoc.grounds.CMarkGroundType;

import java.util.List;

public interface MarkGroundTypeService {
     List<CMarkGroundType> findAll();
     CMarkGroundType findById(Integer markGroupType);
}
