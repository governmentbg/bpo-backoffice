package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.mapper.reception.SubmissionTypeMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.reception.CSubmissionType;
import bg.duosoft.ipas.core.service.reception.SubmissionTypeService;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.SubmissionType;
import bg.duosoft.ipas.persistence.repository.entity.reception.SubmissionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class SubmissionTypeServiceImpl implements SubmissionTypeService {

    @Autowired
    private SubmissionTypeRepository submissionTypeRepository;

    @Autowired
    private SubmissionTypeMapper submissionTypeMapper;

    @Override
    @Cacheable(value = "submissionTypes", key = "{'all'}")
    public List<CSubmissionType> selectAll() {
        List<SubmissionType> all = submissionTypeRepository.findAll();
        if (CollectionUtils.isEmpty(all))
            return null;

        List<CSubmissionType> cSubmissionTypes = submissionTypeMapper.toCoreList(all);
        cSubmissionTypes.sort(Comparator.comparing(CSubmissionType::getName));
        return cSubmissionTypes;
    }

    @Override
    @Cacheable(value = "submissionTypes", key = "{#id}")
    public CSubmissionType selectById(Integer id) {
        if (Objects.isNull(id))
            return null;

        SubmissionType submissionType = submissionTypeRepository.findById(id).orElse(null);
        if (Objects.isNull(submissionType))
            return null;

        return submissionTypeMapper.toCore(submissionType);
    }
}
