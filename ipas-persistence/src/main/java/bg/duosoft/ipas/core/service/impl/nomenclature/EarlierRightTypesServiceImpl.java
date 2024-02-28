package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.userdoc.grounds.EarlierRightTypesMapper;
import bg.duosoft.ipas.core.model.userdoc.grounds.CApplicantAuthority;
import bg.duosoft.ipas.core.model.userdoc.grounds.CEarlierRightTypes;
import bg.duosoft.ipas.core.service.nomenclature.EarlierRightTypesService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfEarlierRightTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EarlierRightTypesServiceImpl implements EarlierRightTypesService {

    @Autowired
    private CfEarlierRightTypesRepository cfEarlierRightTypesRepository;

    @Autowired
    private EarlierRightTypesMapper earlierRightTypesMapper;

    @Override
    public List<CEarlierRightTypes> findAllEarlierRightTypesForSpecificPanel(String panel,String version) {
        List<CEarlierRightTypes> cEarlierRightTypes =  cfEarlierRightTypesRepository.findAllEarlierRightTypesForSpecificPanel(panel,version)
                .stream().map(r->earlierRightTypesMapper.toCore(r)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(cEarlierRightTypes)){
            cEarlierRightTypes.sort(Comparator.comparing(CEarlierRightTypes::getName));
        }
        return cEarlierRightTypes;
    }

    @Override
    public CEarlierRightTypes findById(Integer id) {
        return earlierRightTypesMapper.toCore(cfEarlierRightTypesRepository.findById(id).orElse(null));
    }

    @Override
    public CEarlierRightTypes findTypeByVersionAndCode(String version, String code) {
        return earlierRightTypesMapper.toCore(cfEarlierRightTypesRepository.findTypeByVersionAndCode(version,code));
    }
}
