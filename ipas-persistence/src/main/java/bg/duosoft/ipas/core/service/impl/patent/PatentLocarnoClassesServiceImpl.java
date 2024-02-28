package bg.duosoft.ipas.core.service.impl.patent;

import bg.duosoft.ipas.core.service.patent.PatentLocarnoClassesService;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentLocarnoClassesRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class PatentLocarnoClassesServiceImpl implements PatentLocarnoClassesService {
    @Autowired
    private IpPatentLocarnoClassesRepository patentLocarnoClassesRepository;

    @Override
    public long count() {
        return patentLocarnoClassesRepository.count();
    }
}
