package bg.duosoft.ipas.core.service.impl.efiling;

import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.efiling.UserdocEFilingDataMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.efiling.UserdocEfilingDataService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.efiling.IpObjectEFilingData;
import bg.duosoft.ipas.persistence.model.entity.efiling.IpUserodocEFilingData;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocRepository;
import bg.duosoft.ipas.persistence.repository.entity.efiling.UserdocEfilingDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserdocEfilingDataServiceImpl implements UserdocEfilingDataService {

    @Autowired
    private UserdocEfilingDataRepository userdocEfilingDataRepository;

    @Autowired
    private IpDocRepository ipDocRepository;

    @Autowired
    private UserdocEFilingDataMapper userdocEFilingDataMapper;

    @PersistenceContext
    private EntityManager entityManager;


    private void updateLogUserName(String username,List<IpDocPK> ipDocPks){
        if (CollectionUtils.isEmpty(ipDocPks)){
            throw new RuntimeException("Userdoc not found on update log user name operation !");
        }
        IpDocPK ipDocPK = ipDocPks.get(0);
        IpUserodocEFilingData ipUserodocEFilingData = userdocEfilingDataRepository.findById(ipDocPK).orElse(null);
        ipUserodocEFilingData.setLogUserName(username);
        entityManager.merge(ipUserodocEFilingData);
    }

    @Override
    public void updateLogUserName(String externalSystemId, String username) {
        if (Objects.isNull(externalSystemId)){
            throw new RuntimeException("Empty externalSystemId!");
        }
        List<IpDocPK> ipDocPksByExternalSystemId = ipDocRepository.selectPkByExtarnalId(externalSystemId);
        if (!CollectionUtils.isEmpty(ipDocPksByExternalSystemId)){
            updateLogUserName(username,ipDocPksByExternalSystemId);
        }else{
            String[] splitExternalSystemId = externalSystemId.split("/");
            String docOri = splitExternalSystemId[0];
            String docLog = splitExternalSystemId[1];
            Integer docSeqSeries = Integer.valueOf(splitExternalSystemId[2]);
            Integer docSeqNbr = Integer.valueOf(splitExternalSystemId[3]);
            List<IpDocPK> ipDocPksByDorOriLogSeqSeriesSeqNbr = ipDocRepository.selectPkByDocOriDocLogDocSeqSeriesDocSeqNbr(docOri,docLog,docSeqSeries,docSeqNbr);
            if (!CollectionUtils.isEmpty(ipDocPksByDorOriLogSeqSeriesSeqNbr)){
                updateLogUserName(username,ipDocPksByDorOriLogSeqSeriesSeqNbr);
            }else{
                throw new RuntimeException("Userdoc  does not exist : externalSystemId - " + externalSystemId);
            }
        }
    }

    @Override
    public boolean doesRecordExist(CDocumentId documentId) {
        if (Objects.nonNull(documentId)) {
            IpDocPK pk = new IpDocPK(documentId.getDocOrigin(),documentId.getDocLog(),documentId.getDocSeries(),documentId.getDocNbr());
            IpUserodocEFilingData result = userdocEfilingDataRepository.findById(pk).orElse(null);
            return Objects.nonNull(result);
        }
        return false;
    }

    @Override
    public CEFilingData selectById(CDocumentId documentId) {
        if (Objects.nonNull(documentId)) {
            IpDocPK pk = new IpDocPK(documentId.getDocOrigin(),documentId.getDocLog(),documentId.getDocSeries(),documentId.getDocNbr());
            IpUserodocEFilingData result = userdocEfilingDataRepository.findById(pk).orElse(null);
            if (Objects.nonNull(result)) {
                return userdocEFilingDataMapper.toCore(result);
            }
        }
        return null;
    }
}
