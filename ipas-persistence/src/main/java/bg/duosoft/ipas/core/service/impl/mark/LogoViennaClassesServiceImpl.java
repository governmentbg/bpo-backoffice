package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.ipas.core.service.mark.LogoViennaClassesService;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpLogoViennaClassesRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class LogoViennaClassesServiceImpl implements LogoViennaClassesService {
    @Autowired
    private IpLogoViennaClassesRepository logoViennaClassesRepository;

    @Override
    public long count() {
        return logoViennaClassesRepository.count();
    }
}
