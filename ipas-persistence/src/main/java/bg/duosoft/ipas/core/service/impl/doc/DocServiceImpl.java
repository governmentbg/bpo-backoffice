package bg.duosoft.ipas.core.service.impl.doc;

import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.document.DocumentMapper;
import bg.duosoft.ipas.core.model.document.CDocument;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocRepository;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class DocServiceImpl implements DocService {

    @Autowired
    private IpDocRepository ipDocRepository;

    @Autowired
    private DocumentIdMapper documentIdMapper;

    @Autowired
    private DocumentMapper documentMapper;

    @Override
    public boolean isDocumentExist(String docOri, String docLog, Integer docSer, Integer docNbr) {
        Integer count = ipDocRepository.countById(docOri, docLog, docSer, docNbr);
        return !(Objects.isNull(count) || 0 == count);
    }

    @Override
    public String selectExternalSystemId(String docOri, String docLog, Integer docSer, Integer docNbr) {
        String externalSystemId = ipDocRepository.selectExternalSystemId(docOri, docLog, docSer, docNbr);
        if (StringUtils.isEmpty(externalSystemId))
            return null;

        return externalSystemId;
    }

    @Override
    public void updateExternalSystemId(String externalSystemId, String docOri, String docLog, Integer docSer, Integer docNbr) {
        ipDocRepository.updateExternalSystemId(externalSystemId, docOri, docLog, docSer, docNbr);
    }

    @Override
    public CDocumentId selectDocumentIdByExternalSystemId(String externalSystemId) {
        List<IpDocPK> result = ipDocRepository.selectDocumentIdByExternalSystemId(externalSystemId);
        if (CollectionUtils.isEmpty(result))
            return null;

        if (result.size() != DefaultValue.ONE_RESULT_SIZE)
            throw new RuntimeException("There is more than 1 result for externalSystemId = " + externalSystemId);

        return documentIdMapper.toCore(result.get(0));
    }

    @Override
    public CDocumentId selectDocumentIdByExternalSystemIdAndTypeAndParentFileId(String externalSystemId, String userdocTyp, CFileId fileId) {
        List<Object[]> result = ipDocRepository.selectDocumentIdByExternalSystemIdAndTypeAndParentFileId(externalSystemId, userdocTyp, fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        if (result.size() != DefaultValue.ONE_RESULT_SIZE) {
            throw new RuntimeException("There is more than 1 result for externalSystemId = " + externalSystemId + " and userdocType = " + userdocTyp + " and parent fileId = " + fileId.createFilingNumber());
        }

        Object[] object = result.get(0);
        return new CDocumentId((String) object[0], (String) object[1], ((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).intValue());
    }

    @Override
    public CDocument selectDocument(CDocumentId documentId) {
        if (Objects.isNull(documentId))
            return null;

        IpDocPK ipDocPK = documentIdMapper.toEntity(documentId);
        IpDoc ipDoc = ipDocRepository.findById(ipDocPK).orElse(null);
        if (Objects.isNull(ipDoc))
            return null;

        return documentMapper.toCore(ipDoc);
    }

    @Override
    public long count() {
        return ipDocRepository.count();
    }
}
