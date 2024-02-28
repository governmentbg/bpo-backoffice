package bg.duosoft.ipas.core.service.reception;

import bg.duosoft.ipas.core.model.reception.CUserdocReceptionRelation;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocMainTypeSimpleResult;

import java.util.List;
import java.util.Map;

public interface UserdocReceptionRelationService {

    List<CUserdocReceptionRelation> selectUserdocsByMainType(String fileType, boolean onlyVisible);

    Map<String, String> selectMainTypesMap();

    List<UserdocMainTypeSimpleResult> selectAllMainTypesForUserdocType(String userdocType);

    void addReceptionRelationForUserdocType(String userdocType, String mainType, Boolean isVisible);

    void deleteReceptionRelationForUserdocType(String userdocType, String mainType);

    Boolean existsById(String userdocType, String mainType);

    CUserdocReceptionRelation selectById(String userdocType, String mainType);

    void updateReceptionRelation(String userdocType, String mainType, Boolean isVisible);

}
