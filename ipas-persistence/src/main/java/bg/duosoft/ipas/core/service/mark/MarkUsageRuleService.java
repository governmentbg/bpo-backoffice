package bg.duosoft.ipas.core.service.mark;

import bg.duosoft.ipas.core.model.mark.CMarkUsageRule;

public interface MarkUsageRuleService {
    CMarkUsageRule findMarkUsageRule(Integer id, Integer type, String fileSeq, String fileType, Integer fileSeries, Integer fileNbr, boolean loadContent);
}
