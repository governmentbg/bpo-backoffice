package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.service.MyObjectsService;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectHomePanelResult;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.repository.nonentity.MyObjectsHomePanelRepository;
import bg.duosoft.ipas.persistence.repository.nonentity.MyObjectsRepository;
import bg.duosoft.ipas.util.filter.MyObjectsFilter;
import bg.duosoft.ipas.util.filter.MyObjectsHomePanelFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MyObjectsServiceImpl implements MyObjectsService {
    @Autowired
    private MyObjectsRepository myObjectsRepository;
    @Autowired
    private MyObjectsHomePanelRepository myObjectsHomePanelRepository;


    public List<IPObjectHomePanelResult> getMyObjectsForHomePanelList(MyObjectsHomePanelFilter filter) {
        return myObjectsHomePanelRepository.getMyObjectsList(filter);
    }
    public Integer getMyObjectsHomePanelCount(MyObjectsHomePanelFilter filter) {
        return myObjectsHomePanelRepository.getMyObjectsCount(filter);
    }
    @Override
    public Integer getNewlyAllocatedObjectsCount(MyObjectsHomePanelFilter filter) {
        return myObjectsHomePanelRepository.getNewlyAllocatedObjectsCount(filter);
    }


    @Override
    public List<IPObjectSimpleResult> getMyObjectsList(MyObjectsFilter filter) {
        return myObjectsRepository.getMyObjectsList(filter);
    }

    @Override
    public Integer getMyObjectsCount(MyObjectsFilter filter) {
        return myObjectsRepository.getMyObjectsCount(filter);
    }


}
