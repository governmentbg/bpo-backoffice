package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.nonentity.UserdocMainTypeSimpleResult;

import java.util.List;
import java.util.Map;

public interface UserdocReceptionRelationRepositoryCustom {

    Map<String, String> selectMainTypesMap();

    List<UserdocMainTypeSimpleResult> selectMainTypesByUserdocType(String userdocType);

}
