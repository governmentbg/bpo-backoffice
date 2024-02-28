package bg.duosoft.ipas.core.service.patent;


import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.model.util.CEuPatentsReceptionIds;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;

import java.util.List;

public interface PatentService {

    CPatent findPatent(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, boolean loadFileContent);

    CPatent findPatent(CFileId id, boolean loadFileContent);

    void updatePatent(CPatent patent);

    void saveEuPatentFromRelationshipIfMissing(CPatent patent);

    void updatePatentOnUserdocAuthorization(CPatent patent);

    void insertPatent(CPatent patent) throws IpasValidationException;

    boolean isMainEpoPatentRequestForValidation(CDocumentId documentId);

    CPatent insertEuPatent(CPatent patent) throws IpasValidationException;

    CDrawing selectDrawing(CFileId cFileId, Integer drawingNumber);

    boolean isPatentExists(CFileId id);

    void updateRowVersion(CFileId id);

    void deletePatent(CFileId fileId) ;
    void deletePatent(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    long count();
}