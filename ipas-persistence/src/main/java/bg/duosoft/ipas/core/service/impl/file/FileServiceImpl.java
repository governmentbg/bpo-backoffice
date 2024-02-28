package bg.duosoft.ipas.core.service.impl.file;

import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.file.FileMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFileRestrictData;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@LogExecutionTime
public class FileServiceImpl implements FileService {

    @Autowired
    private IpFileRepository ipFileRepository;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private DocumentIdMapper documentIdMapper;

    @Override
    public CFile findById(CFileId fileId) {
        return findById(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
    }

    @Override
    public Date findFilingDateById(CFileId fileId) {
        return ipFileRepository.getFilingDate(fileId.getFileSeq(),fileId.getFileType(),fileId.getFileSeries(),fileId.getFileNbr());
    }

    @Override
    public CFile findById(String fileSeq, String fileType, Integer fileSer, Integer fileNbr) {
        IpFilePK ipFilePK = new IpFilePK(fileSeq, fileType, fileSer, fileNbr);
        IpFile ipFile = ipFileRepository.findById(ipFilePK).orElse(null);
        return fileMapper.toCore(ipFile);
    }

    @Override
    public boolean isFileExist(CFileId fileId) {
        return isFileExist(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
    }

    @Override
    public boolean isFileExist(String fileSeq, String fileType, Integer fileSer, Integer fileNbr) {
        Integer count = ipFileRepository.countById(fileSeq, fileType, fileSer, fileNbr);
        return !(Objects.isNull(count) || 0 == count);
    }

    @Override
    public CFileRestrictData selectRestrictData(CFileId fileId) {
        if (Objects.isNull(fileId))
            return null;

        return ipFileRepository.selectRestrictData(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
    }

    @Override
    public CDocumentId selectFileDocumentId(CFileId fileId) {
        if (Objects.isNull(fileId))
            return null;

        IpDocPK ipDocPK = ipFileRepository.selectDocumentId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
        if (Objects.isNull(ipDocPK))
            return null;

        return documentIdMapper.toCore(ipDocPK);
    }

    @Override
    public String selectApplicationTypeByFileId(CFileId fileId) {
        if (Objects.isNull(fileId))
            return null;

        return ipFileRepository.selectApplicationTypeByFileId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
    }

    @Override
    public List<CFile> selectMarkReceptionFiles(List<String> fileTypes, Integer fileNumber) {
        if (CollectionUtils.isEmpty(fileTypes) || Objects.isNull(fileNumber))
            return null;

        List<IpFile> ipFiles = ipFileRepository.selectMarkReceptionFiles(fileTypes, fileNumber);
        if (CollectionUtils.isEmpty(ipFiles))
            return null;

        return fileMapper.toCoreList(ipFiles);
    }

    @Override
    public List<CFile> selectPatentReceptionFiles(List<String> fileTypes, Integer fileNumber) {
        if (CollectionUtils.isEmpty(fileTypes) || Objects.isNull(fileNumber))
            return null;

        List<IpFile> ipFiles = ipFileRepository.selectPatentReceptionFiles(fileTypes, fileNumber);
        if (CollectionUtils.isEmpty(ipFiles))
            return null;

        return fileMapper.toCoreList(ipFiles);
    }

    @Override
    public Integer selectRegistrationNumberById(CFileId fileId) {
        if (Objects.isNull(fileId))
            return null;

        return ipFileRepository.selectRegistrationNumberById(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
    }

    @Override
    public List<CFile> findAllByRegistrationNbrAndDupAndFileType(Integer registrationNbr, String registrationDup, List<String> fileTypes) {
        List<IpFile> allByRegistrationNbrAndDup = ipFileRepository.findAllByRegistrationNbrAndDupAndFileType(registrationNbr, registrationDup, fileTypes);
        if(CollectionUtils.isEmpty(allByRegistrationNbrAndDup)){
            return null;
        }
        return fileMapper.toCoreList(allByRegistrationNbrAndDup);
    }

    @Override
    public List<CFile> findAllByRegistrationNbrAndFileType(Integer registrationNbr, List<String> fileTypes) {
        List<IpFile> allByRegistrationNbr = ipFileRepository.findAllByRegistrationNbrAndFileType(registrationNbr, fileTypes);
        if(CollectionUtils.isEmpty(allByRegistrationNbr)){
            return null;
        }
        return fileMapper.toCoreList(allByRegistrationNbr);
    }

    @Override
    public List<CFile> findAllByFileNbrAndFileType(Integer fileNbr, List<String> fileTypes) {
       List<IpFile> allByFileNbr = ipFileRepository.findAllByFileNbrAndFileType(fileNbr, fileTypes);
       if (CollectionUtils.isEmpty(allByFileNbr)) {
           return null;
       }

       return fileMapper.toCoreList(allByFileNbr);
    }
}
