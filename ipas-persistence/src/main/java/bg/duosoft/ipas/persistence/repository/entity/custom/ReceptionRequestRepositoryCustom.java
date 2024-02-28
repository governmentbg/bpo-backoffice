package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionRequest;
import bg.duosoft.ipas.persistence.model.nonentity.ReceptionRequestSimpleResult;
import bg.duosoft.ipas.util.filter.ReceptionListFilter;

import java.util.List;

public interface ReceptionRequestRepositoryCustom {

    List<ReceptionRequestSimpleResult> getReceptionsWithoutStatus(ReceptionListFilter filter);

    List<ReceptionRequestSimpleResult> getFirstReceptionsWithoutStatus();

    Integer getReceptionsWithoutStatusCount();

}
