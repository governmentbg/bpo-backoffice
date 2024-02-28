package bg.duosoft.ipas.core.service.impl.offidoc;

import bg.duosoft.ipas.core.mapper.offidoc.OffidocAbdocsDocumentMapper;
import bg.duosoft.ipas.core.mapper.offidoc.OffidocIdMapper;
import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.service.ext.OffidocAbdocsDocumentService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpOffidocAbdocsDocument;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;
import bg.duosoft.ipas.persistence.repository.entity.ext.IpOffidocAbdocsDocumentRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

@Service
@Transactional
@LogExecutionTime
public class OffidocAbdocsDocumentServiceImpl implements OffidocAbdocsDocumentService {

    @Autowired
    private IpOffidocAbdocsDocumentRepository ipOffidocAbdocsDocumentRepository;

    @Autowired
    private OffidocIdMapper offidoctIdMapper;

    @Autowired
    private OffidocAbdocsDocumentMapper offidocAbdocsDocumentMapper;

    @Override
    public COffidocAbdocsDocument selectById(COffidocId id) {
        if (Objects.isNull(id))
            return null;

        IpOffidocPK ipOffidocPK = offidoctIdMapper.toEntity(id);
        IpOffidocAbdocsDocument ipOffidocAbdocsDocument = ipOffidocAbdocsDocumentRepository.findById(ipOffidocPK).orElse(null);
        if (Objects.isNull(ipOffidocAbdocsDocument))
            return null;

        return offidocAbdocsDocumentMapper.toCore(ipOffidocAbdocsDocument);
    }

    @Override
    public COffidocAbdocsDocument save(COffidocAbdocsDocument offidocAbdocsDocument) {
        if (Objects.isNull(offidocAbdocsDocument))
            return null;

        IpOffidocAbdocsDocument ipOffidocAbdocsDocument = offidocAbdocsDocumentMapper.toEntity(offidocAbdocsDocument);
        if (Objects.isNull(ipOffidocAbdocsDocument))
            return null;

        IpOffidocAbdocsDocument result = ipOffidocAbdocsDocumentRepository.save(ipOffidocAbdocsDocument);
        return offidocAbdocsDocumentMapper.toCore(result);
    }

    @Override
    public COffidocAbdocsDocument selectByAbdocsId(Integer id) {
        if (Objects.isNull(id))
            return null;

        IpOffidocAbdocsDocument result = ipOffidocAbdocsDocumentRepository.findByAbdocsDocumentId(id);
        if (Objects.isNull(result))
            return null;

        return offidocAbdocsDocumentMapper.toCore(result);
    }

    @Override
    public COffidocAbdocsDocument selectByRegistrationNumber(String registrationNumber) {
        if (Objects.isNull(registrationNumber))
            return null;

        IpOffidocAbdocsDocument result = ipOffidocAbdocsDocumentRepository.findByRegistrationNumber(registrationNumber);
        if (Objects.isNull(result))
            return null;

        return offidocAbdocsDocumentMapper.toCore(result);
    }

    @Override
    public void updateRegistrationNumber(String registrationNumber, Integer documentId) {
        ipOffidocAbdocsDocumentRepository.updateRegistrationNumber(registrationNumber, documentId);
    }

    @Override
    public void updateNotificationFinishedDate(Date notificationFinistedDate, Integer documentId) {
        ipOffidocAbdocsDocumentRepository.updateNotificationFinishedDate(notificationFinistedDate, documentId);
    }

    @Override
    public void updateEmailNotificationReadDate(Date date, Integer documentId) {
        ipOffidocAbdocsDocumentRepository.updateEmailNotificationReadDate(date, documentId);
    }

    @Override
    public void updatePortalNotificationReadDate(Date date, Integer documentId) {
        ipOffidocAbdocsDocumentRepository.updatePortalNotificationReadDate(date, documentId);
    }

}
