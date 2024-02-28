package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.mark.UsageRuleMapper;
import bg.duosoft.ipas.core.model.mark.CUsageRule;
import bg.duosoft.ipas.core.service.nomenclature.UsageRuleService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfUsageRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsageRuleServiceImpl implements UsageRuleService {

    @Autowired
    private CfUsageRuleRepository cfUsageRuleRepository;

    @Autowired
    private UsageRuleMapper mapper;

    @Override
    public List<CUsageRule> findAll() {
        List<CUsageRule> usageRules = cfUsageRuleRepository.findAll().stream().map(r -> mapper.toCore(r)).collect(Collectors.toList());
        return usageRules;
    }

    @Override
    public CUsageRule findById(Integer id) {
        return mapper.toCore(cfUsageRuleRepository.findById(id).orElse(null));
    }
}
