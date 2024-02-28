package bg.duosoft.ipas.core.service.logging;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.logging.CFileLogChanges;
import bg.duosoft.ipas.core.model.logging.CUserdocLogChanges;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 30.1.2019 г.
 * Time: 12:05
 */
public interface LogChangesService {
    public void insertMarkLogChanges(CMark oldMark, CMark newMark);

    public void insertPatentLogChanges(CPatent oldPatent, CPatent newPatent);

    //promenite po edinichnite dizajni se otrazqvat vyrhu masterDesign-a, tyj kato operaciqta moje da e za triene na edinichen dizajn, togava shte se iztrie i zapisa v IP_PATENT_LOG_CHANGES, tyj kato toi ima referenciq kym IP_PATENT!!!
    public void insertDesignLogChanges(CPatent oldMasterDesign, CPatent newMasterDesign, List<CPatent> oldSingleDesigns, List<CPatent> newSingleDesigns);

    //if a userdocs initiates changes to an object (mark/patent), a record is getting added to the IP_MARK_LOG_CHANGES/IP_PATENT_LOG_CHANGES/ table
    public void insertObjectUserdocLogChanges(CFileId fileId, CDocumentId documentId, String userdocType);

    public void insertUserdocLogChanges(CUserdoc oldUserdoc, CUserdoc newUserdoc);


    public List<CFileLogChanges> getFileLogChanges(CFileId fileId, boolean addData);

    public List<CUserdocLogChanges> getUserdocLogChanges(CDocumentId documentId, boolean addData);

    public CFileLogChanges getFileLogChange(CFileId fileId, int logChangeNumber);

    public CUserdocLogChanges getUserdocLogChange(CDocumentId documentId, int logChangeNumber);


}
