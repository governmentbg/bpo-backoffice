package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.service.ExpiredTermService;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.repository.nonentity.ExpiredTermRepository;
import bg.duosoft.ipas.util.filter.ExpiredTermFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ExpiredTermServiceImpl implements ExpiredTermService {

    @Autowired
    private ExpiredTermRepository expiredTermRepository;

    @Override
    public List<IPObjectSimpleResult> getExpiredTermsList(ExpiredTermFilter filter) {
        return expiredTermRepository.getExpiredTermsList(filter);
    }

    @Override
    public Integer getExpiredTersmCount(ExpiredTermFilter filter) {
        return expiredTermRepository.getExpiredTersmCount(filter);
    }

    @Override
    public List<CActionType> getExpiredActionTypes(ExpiredTermFilter filter) {
        return expiredTermRepository.getExpiredActionTypes(filter);
    }

    @Override
    public Map<String, String> getStatuses(ExpiredTermFilter filter) {
        return expiredTermRepository.getStatuses(filter);
    }
}
