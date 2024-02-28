package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.process.ProcessTypeMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CCourt;
import bg.duosoft.ipas.core.model.reception.CProcessType;
import bg.duosoft.ipas.core.service.nomenclature.ProcessTypeService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfProcessType;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfProcessTypeRepository;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProcessTypeServiceImpl implements ProcessTypeService {

    @Autowired
    private CfProcessTypeRepository cfProcessTypeRepository;

    @Autowired
    private ProcessTypeMapper processTypeMapper;

    @Override
    @Cacheable("processTypeMap")
    public Map<String, String> getProcessTypeMap() {
        List<CfProcessType> processTypes = cfProcessTypeRepository.findAll();
        if (CollectionUtils.isEmpty(processTypes))
            return null;


        return processTypes.stream()
                .sorted(Comparator.comparing(CfProcessType::getProcTypeName))
                .collect(LinkedHashMap::new, (map, processType) -> map.put(processType.getProcTyp(), processType.getProcTypeName()), Map::putAll);
    }

    @Override
    public boolean isProcessTypeForManualSubProcess(String processType) {
        CfProcessType cfProcessType = cfProcessTypeRepository.findById(processType).orElse(null);
        if (Objects.isNull(cfProcessType))
            return false;

        Integer relatedToWcode = cfProcessType.getRelatedToWcode();
        return Objects.nonNull(relatedToWcode) && relatedToWcode.equals(ProcessTypeUtils.MANUAL_SUB_PROCESS_TYPE_WCODE);
    }

    @Override
    public List<CProcessType> selectByRelatedToWcode(Integer relatedToWcode) {
        if (Objects.isNull(relatedToWcode))
            return null;

        List<CfProcessType> allByRelatedToWcode = cfProcessTypeRepository.findAllByRelatedToWcode(relatedToWcode);
        if (CollectionUtils.isEmpty(allByRelatedToWcode))
            return null;

        List<CProcessType> cProcessTypes = processTypeMapper.toCoreList(allByRelatedToWcode);
        cProcessTypes.sort(Comparator.comparing(CProcessType::getProcTypeName));
        return cProcessTypes;
    }

}