package bg.duosoft.ipas.core.service.impl.efiling;

import bg.duosoft.ipas.core.mapper.efiling.ObjectEFilingDataMapper;
import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.efiling.IpObjectEfilingDataService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.efiling.IpObjectEFilingData;
import bg.duosoft.ipas.persistence.model.entity.efiling.IpUserodocEFilingData;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.efiling.IpObjectEfilingDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;

@Service
@Transactional
public class IpObjectEfilingDataServiceImpl implements IpObjectEfilingDataService {

    @Autowired
    private IpObjectEfilingDataRepository ipObjectEfilingDataRepository;

    @Autowired
    private FileIdMapper fileIdMapper;

    @Autowired
    private ObjectEFilingDataMapper objectEFilingDataMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void updateLogUserName(CFileId cFileId, String username) {
        if (Objects.isNull(cFileId))
            throw new RuntimeException("Empty file id!");

        IpFilePK pk= fileIdMapper.toEntity(cFileId);
        IpObjectEFilingData ipObjectEFilingData = ipObjectEfilingDataRepository.findById(pk).orElse(null);
        if (Objects.nonNull(ipObjectEFilingData)){
            ipObjectEFilingData.setLogUserName(username);
            entityManager.merge(ipObjectEFilingData);
        }else{
            throw new RuntimeException("Object does not exist : " + cFileId.createFilingNumber());
        }
    }

    @Override
    public boolean doesRecordExist(CFileId fileId) {
        if (Objects.nonNull(fileId)) {
            IpFilePK pk = new IpFilePK(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
            IpObjectEFilingData result = ipObjectEfilingDataRepository.findById(pk).orElse(null);
            return Objects.nonNull(result);
        }
        return false;
    }

    @Override
    public CEFilingData selectById(CFileId fileId) {
        if (Objects.nonNull(fileId)) {
            IpFilePK pk = new IpFilePK(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
            IpObjectEFilingData result = ipObjectEfilingDataRepository.findById(pk).orElse(null);
            if (Objects.nonNull(result)) {
                return objectEFilingDataMapper.toCore(result);
            }
        }
        return null;
    }
}
