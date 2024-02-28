package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.service.WaitingTermService;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.repository.nonentity.WaitingTermRepository;
import bg.duosoft.ipas.util.filter.WaitingTermFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WaitingTermServiceImpl implements WaitingTermService {

    @Autowired
    private WaitingTermRepository waitingTermRepository;

    @Override
    public List<IPObjectSimpleResult> getWaitingTermList(WaitingTermFilter filter) {
        return waitingTermRepository.getWaitingTermList(filter);
    }

    @Override
    public Integer getWaitingTermCount(WaitingTermFilter filter) {
        return waitingTermRepository.getWaitingTermCount(filter);
    }

    @Override
    public Map<String, String> getStatuses(WaitingTermFilter filter) {
        return waitingTermRepository.getStatuses(filter);
    }
}
