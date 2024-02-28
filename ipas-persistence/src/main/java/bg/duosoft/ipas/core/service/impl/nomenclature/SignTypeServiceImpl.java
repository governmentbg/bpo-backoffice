package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.service.nomenclature.SignTypeService;
import bg.duosoft.ipas.persistence.model.entity.ext.ExtCfSignType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfGeoCountry;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisGroup;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfSignTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SignTypeServiceImpl implements SignTypeService {

    @Autowired
    private CfSignTypeRepository cfSignTypeRepository;

    @Override
    @Cacheable("signTypesMap")
    public Map<String, String> getSignTypesMap() {
        List<ExtCfSignType> signTypes = cfSignTypeRepository.findAll();
        if (!CollectionUtils.isEmpty(signTypes))
            return signTypes.stream()
                    .sorted(Comparator.comparing(ExtCfSignType::getSignTypeName))
                    .collect(LinkedHashMap::new,
                            (map, signType) -> map.put(signType.getSignType(), signType.getSignTypeName()),
                            Map::putAll);
        return null;
    }

}