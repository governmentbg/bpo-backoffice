package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.ipas.core.service.mark.MarkNiceClassesService;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkNiceClassesRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class MarkNiceClassesServiceImpl implements MarkNiceClassesService {

    @Autowired
    private IpMarkNiceClassesRepository markNiceClassesRepository;

    @Override
    public long count() {
        return markNiceClassesRepository.count();
    }
}
