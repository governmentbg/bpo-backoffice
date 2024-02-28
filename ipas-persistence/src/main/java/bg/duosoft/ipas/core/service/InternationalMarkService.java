package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.persistence.model.nonentity.IPMarkSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.InternationalMarkSimpleResult;
import bg.duosoft.ipas.util.filter.InternationalMarkFilter;
import java.util.List;

public interface InternationalMarkService {
    List<IPMarkSimpleResult> getInternationalMarksList(InternationalMarkFilter filter);

    Integer getInternationalMarksCount(InternationalMarkFilter filter);

    List<InternationalMarkSimpleResult> selectInternationalRegistrations(String registrationNumberWithDup);

    InternationalMarkSimpleResult selectInternationalRegistration(String filingNumber);

}
