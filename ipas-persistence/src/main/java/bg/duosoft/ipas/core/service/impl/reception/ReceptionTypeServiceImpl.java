package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.mapper.reception.ReceptionTypeMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.reception.CReceptionType;
import bg.duosoft.ipas.core.service.reception.ReceptionTypeService;
import bg.duosoft.ipas.enums.ReceptionTypeConfig;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.CfReceptionType;
import bg.duosoft.ipas.persistence.repository.entity.reception.ReceptionTypeRepository;
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
public class ReceptionTypeServiceImpl implements ReceptionTypeService {

    @Autowired
    private ReceptionTypeRepository receptionTypeRepository;

    @Autowired
    private ReceptionTypeMapper receptionTypeMapper;

    @Override
    @Cacheable(value = "receptionTypes", key = "{'all'}")
    public List<CReceptionType> selectAll() {
        List<CfReceptionType> all = receptionTypeRepository.findAll();
        if (CollectionUtils.isEmpty(all))
            return null;

        List<CReceptionType> cReceptionTypes = receptionTypeMapper.toCoreList(all);
        cReceptionTypes.sort(Comparator.comparing(CReceptionType::getTitle));
        return cReceptionTypes;
    }

    @Override
    public List<CReceptionType> selectAllDependingOnConfiguration(List<ReceptionTypeConfig> configList) {
        if (CollectionUtils.isEmpty(configList)) {
            throw new RuntimeException("configList is empty!");
        }

        List<CReceptionType> all = selectAll();

        if (CollectionUtils.isEmpty(all))
            return null;

        for (ReceptionTypeConfig config : configList) {
            if (config.equals(ReceptionTypeConfig.RECEPTION_ON_COUNTER)) {
                all.removeIf(r -> Objects.isNull(r.getReceptionOnCounter()) || !r.getReceptionOnCounter());
            }
            if (config.equals(ReceptionTypeConfig.RECEPTION_FROM_EXISTING_DOCUMENT)) {
                all.removeIf(r -> Objects.isNull(r.getReceptionFromExistingDocument()) || !r.getReceptionFromExistingDocument());
            }
            //TODO  for more configs if it is needed
        }

        return all;
    }

    @Override
    @Cacheable(value = "receptionTypes", key = "{#id}")
    public CReceptionType selectById(Integer id) {
        if (Objects.isNull(id))
            return null;

        CfReceptionType cfReceptionType = receptionTypeRepository.findById(id).orElse(null);
        if (Objects.isNull(cfReceptionType))
            return null;

        return receptionTypeMapper.toCore(cfReceptionType);
    }

    @Override
    @Cacheable(value = "receptionTypes", key = "{#fileType}")
    public CReceptionType selectByFileType(String fileType) {
        if (Objects.isNull(fileType))
            return null;

        CfReceptionType cfReceptionType = receptionTypeRepository.findByFileType(fileType);
        if (Objects.isNull(cfReceptionType))
            return null;

        return receptionTypeMapper.toCore(cfReceptionType);
    }
}
