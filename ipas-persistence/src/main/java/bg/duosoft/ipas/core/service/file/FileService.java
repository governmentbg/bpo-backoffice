package bg.duosoft.ipas.core.service.file;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFileRestrictData;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;

import java.util.Date;
import java.util.List;

public interface FileService {

    CFile findById(CFileId fileId);

    Date findFilingDateById(CFileId fileId);

    CFile findById(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    boolean isFileExist(CFileId fileId);

    boolean isFileExist(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    CFileRestrictData selectRestrictData(CFileId fileId);

    CDocumentId selectFileDocumentId(CFileId fileId);

    String selectApplicationTypeByFileId(CFileId fileId);

    List<CFile> selectMarkReceptionFiles(List<String> fileTypes, Integer fileNumber);

    List<CFile> selectPatentReceptionFiles(List<String> fileTypes, Integer fileNumber);

    Integer selectRegistrationNumberById(CFileId fileId);

    List<CFile> findAllByRegistrationNbrAndDupAndFileType(Integer registrationNbr, String registrationDup, List<String> fileTypes);

    List<CFile> findAllByRegistrationNbrAndFileType(Integer registrationNbr, List<String> fileTypes);

    List<CFile> findAllByFileNbrAndFileType(Integer fileNbr, List<String> fileTypes);
}
