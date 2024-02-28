package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.ipas.core.service.mark.AttachmentViennaClassesService;
import bg.duosoft.ipas.core.service.mark.LogoViennaClassesService;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpLogoViennaClassesRepository;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkAttachmentViennaClassesRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class AttachmentViennaClassesServiceImpl implements AttachmentViennaClassesService {
    @Autowired
    private IpMarkAttachmentViennaClassesRepository ipMarkAttachmentViennaClassesRepository;

    @Override
    public long count() {
        return ipMarkAttachmentViennaClassesRepository.count();
    }
}
