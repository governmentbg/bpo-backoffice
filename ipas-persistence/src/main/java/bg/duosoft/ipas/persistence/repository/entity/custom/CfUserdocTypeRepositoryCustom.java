package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import bg.duosoft.ipas.util.filter.UserdocTypesFilter;

import java.util.List;

public interface CfUserdocTypeRepositoryCustom {

    List<CfUserdocType> getUserdocTypes(UserdocTypesFilter filter);

    Integer getUserdocTypesCount(UserdocTypesFilter filter);
}
