package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.file.CRelationshipType;

import java.util.Map;

public interface RelationshipTypeService {

    Map<String, CRelationshipType> selectAll();

}