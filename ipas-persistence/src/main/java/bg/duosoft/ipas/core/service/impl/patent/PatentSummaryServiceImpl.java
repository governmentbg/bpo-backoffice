package bg.duosoft.ipas.core.service.impl.patent;

import bg.duosoft.ipas.core.service.patent.PatentSummaryService;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentSummaryRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class PatentSummaryServiceImpl implements PatentSummaryService {
    @Autowired
    private IpPatentSummaryRepository patentSummaryRepository;

    @Override
    public long count() {
        return patentSummaryRepository.count();
    }
}
