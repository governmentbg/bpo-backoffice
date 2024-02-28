package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.util.filter.ReceptionUserdocListFilter;

import java.util.List;
import java.util.Map;

public interface ReceptionUserdocRequestRepositoryCustom {

    List<UserdocSimpleResult> selectUserdocReceptions(ReceptionUserdocListFilter filter);

    int selectUserdocReceptionsCount(ReceptionUserdocListFilter filter);

    Map<String, String> getUserdocStatuses(ReceptionUserdocListFilter filter);

    Map<String, String> getUserdocTypes(ReceptionUserdocListFilter filter);

    Map<String, String> getUserdocObjectTypes(ReceptionUserdocListFilter filter);

}
