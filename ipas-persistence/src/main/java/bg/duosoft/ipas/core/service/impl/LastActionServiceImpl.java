package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.service.LastActionService;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.LastActionsResult;
import bg.duosoft.ipas.persistence.repository.nonentity.LastActionRepository;
import bg.duosoft.ipas.util.filter.LastActionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LastActionServiceImpl implements LastActionService {

    @Autowired
    private LastActionRepository lastActionRepository;

    @Override
    public List<LastActionsResult> getLastActionList(LastActionFilter filter) {
        return lastActionRepository.getLastActionList(filter);
    }

    @Override
    public int getLastActionCount(LastActionFilter filter) {
        return lastActionRepository.getLastActionCount(filter);
    }

    @Override
    public Map<String, String> getLastActionFileTypes(LastActionFilter filter) {
        return lastActionRepository.getLastActionFileTypes(filter);
    }

    @Override
    public Map<String, String> getLastActionUserdocTypes(LastActionFilter filter) {
        return lastActionRepository.getLastActionUserdocTypes(filter);
    }
}
