package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.ipas.core.mapper.mark.MarkUsageRuleMapper;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMarkUsageRule;
import bg.duosoft.ipas.core.service.mark.MarkUsageRuleService;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkUsageRule;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkUsageRulePK;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkUsageRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarkUsageRuleServiceImpl implements MarkUsageRuleService {
    @Autowired
    private MarkUsageRuleMapper markUsageRuleMapper;

    @Autowired
    private IpMarkUsageRuleRepository ipMarkUsageRuleRepository;

    @Override
    public CMarkUsageRule findMarkUsageRule(Integer id, Integer type, String fileSeq, String fileType, Integer fileSeries, Integer fileNbr, boolean loadContent) {
        IpMarkUsageRulePK pk = new IpMarkUsageRulePK();
        pk.setFileNbr(fileNbr);
        pk.setFileSeq(fileSeq);
        pk.setFileSer(fileSeries);
        pk.setFileTyp(fileType);
        pk.setId(id);
        pk.setType(type);
        IpMarkUsageRule ipMarkUsageRule = ipMarkUsageRuleRepository.findById(pk).orElse(null);
        return markUsageRuleMapper.toCore(ipMarkUsageRule, loadContent);
    }
}
