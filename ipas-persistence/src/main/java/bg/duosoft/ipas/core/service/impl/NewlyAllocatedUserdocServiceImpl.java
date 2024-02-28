package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.service.NewlyAllocatedUserdocService;
import bg.duosoft.ipas.persistence.model.nonentity.NewlyAllocatedUserdocSimpleResult;
import bg.duosoft.ipas.persistence.repository.nonentity.NewlyAllocatedUserdocRepository;
import bg.duosoft.ipas.util.filter.NewlyAllocatedUserdocFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class NewlyAllocatedUserdocServiceImpl implements NewlyAllocatedUserdocService {

    @Autowired
    private NewlyAllocatedUserdocRepository newlyAllocatedUserdocRepository;

    @Override
    public List<NewlyAllocatedUserdocSimpleResult> selectNewlyAllocatedUserdocs(NewlyAllocatedUserdocFilter filter) {
        return newlyAllocatedUserdocRepository.selectNewlyAllocatedUserdocs(filter);
    }

    @Override
    public int selectNewlyAllocatedUserdocsCount(NewlyAllocatedUserdocFilter filter) {
        return newlyAllocatedUserdocRepository.selectNewlyAllocatedUserdocsCount(filter);
    }

    @Override
    public Map<String, String> getUserdocTypes(NewlyAllocatedUserdocFilter filter) {
        return newlyAllocatedUserdocRepository.getUserdocTypes(filter);
    }

    @Override
    public Map<String, String> getUserdocObjectTypes(NewlyAllocatedUserdocFilter filter) {
        return newlyAllocatedUserdocRepository.getUserdocObjectTypes(filter);
    }
}
