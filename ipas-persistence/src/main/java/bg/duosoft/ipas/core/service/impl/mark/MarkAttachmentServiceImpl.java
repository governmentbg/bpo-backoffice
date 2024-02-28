package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.ipas.core.mapper.mark.MarkAttachmentMapper;
import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.service.mark.MarkAttachmentService;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkAttachment;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkAttachmentsRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class MarkAttachmentServiceImpl implements MarkAttachmentService {

    @Autowired
    private IpMarkAttachmentsRepository ipMarkAttachmentsRepository;

    @Autowired
    private MarkAttachmentMapper markAttachmentMapper;

    @Override
    public CMarkAttachment selectAttachmentById(Integer id, boolean addAttachmentData) {
        if (Objects.isNull(id))
            return null;

        IpMarkAttachment ipMarkAttachment = ipMarkAttachmentsRepository.findById(id).orElse(null);
        if (Objects.isNull(ipMarkAttachment))
            return null;

        return markAttachmentMapper.toCore(ipMarkAttachment, addAttachmentData);
    }

}