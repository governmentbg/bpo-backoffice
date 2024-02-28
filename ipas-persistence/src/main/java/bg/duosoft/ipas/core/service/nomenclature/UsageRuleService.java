package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.mark.CUsageRule;
import java.util.List;

public interface UsageRuleService {
    List<CUsageRule> findAll();
    CUsageRule findById(Integer id);
}
