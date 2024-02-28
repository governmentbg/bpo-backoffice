package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.service.MyGroupedUserdocsService;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.persistence.repository.nonentity.MyGroupedUserdocsRepository;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MyGroupedUserdocsServiceImpl implements MyGroupedUserdocsService {

    @Autowired
    private MyGroupedUserdocsRepository myGroupedUserdocsRepository;

    @Override
    public List<UserdocSimpleResult> getMyUserdocsList(MyUserdocsFilter filter) {
        return myGroupedUserdocsRepository.getMyUserdocsList(filter);
    }

    @Override
    public Integer getMyUserdocsCount(MyUserdocsFilter filter) {
        return myGroupedUserdocsRepository.getMyUserdocsCount(filter);
    }

    @Override
    public Integer getNewlyAllocatedUserdocsCount(MyUserdocsFilter filter) {
        return myGroupedUserdocsRepository.getNewlyAllocatedUserdocsCount(filter);
    }

    @Override
    public Map<String, String> getGroupedUserdocStatuses(MyUserdocsFilter filter) {
        return myGroupedUserdocsRepository.getGroupedUserdocStatuses(filter);
    }

    @Override
    public Map<String, String> getGroupedUserdocObjectType(MyUserdocsFilter filter) {
        return myGroupedUserdocsRepository.getGroupedUserdocObjectType(filter);
    }
}
