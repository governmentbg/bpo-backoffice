package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.service.nomenclature.LawService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLaw;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLawApplicationSubtype;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfLawApplicationSubTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfLawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class LawServiceImpl implements LawService {

    @Autowired
    private CfLawRepository cfLawRepository;
    @Autowired
    private CfLawApplicationSubTypeRepository cfLawApplicationSubTypeRepository;

    @Cacheable("lawMap")
    @Override
    public Map<Integer, String> getLawMap() {
        List<CfLaw> cfLaws = cfLawRepository.findAll();
        if (CollectionUtils.isEmpty(cfLaws))
            return null;

        return cfLaws.stream().collect(Collectors.toMap(CfLaw::getLawCode,CfLaw::getLawName));
    }

    @Override
    public CfLaw findById(Integer id) {
        return cfLawRepository.findById(id).orElse(null);
    }

    @Override
    public Integer getLawIdByApplicationTypeAndSubtype(String applicationType, String applicationSubType) {
        CfLawApplicationSubtype res = cfLawApplicationSubTypeRepository.findByApplicationTypeAndSubtype(applicationType, applicationSubType);
        return res == null ? null : res.getPk().getLawCode();
    }
}