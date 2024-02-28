package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfOffidocType;
import bg.duosoft.ipas.util.filter.OffidocTypeFilter;

import java.util.List;

public interface CfOffidocTypeRepositoryCustom {

    List<CfOffidocType> selectOffidocTypes(OffidocTypeFilter filter);

    Integer selectOffidocTypesCount(OffidocTypeFilter filter);
}
