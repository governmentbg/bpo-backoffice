package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.util.filter.OffidocTypeFilter;

import java.util.List;

public interface OffidocTypeService {

    COffidocType selectById(String offidocType);

    List<COffidocType> selectOffidocTypes(OffidocTypeFilter filter);

    Integer selectOffidocTypeCount(OffidocTypeFilter filter);

    COffidocType saveOffidocType(COffidocType cOffidocType);

    COffidocType addOffidocTemplate(String offidocType, String template);

    COffidocType deleteOffidocTemplate(String offidocType, String template);

    COffidocType changeOffidocDefaultTemplate(String offidocType, String template);

}