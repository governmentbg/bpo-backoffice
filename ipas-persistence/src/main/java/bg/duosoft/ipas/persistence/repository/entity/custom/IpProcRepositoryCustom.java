package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.nonentity.ProcessEventResult;
import bg.duosoft.ipas.persistence.model.nonentity.TopProcessFileData;

import java.util.List;

public interface IpProcRepositoryCustom {

    IpFilePK selectTopProcessFileId(String processType, Integer processNbr);

    IpProcPK selectFileProcessId(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr);

    IpProcPK selectUserdocProcessId(String docOri, String docLog, Integer docSer, Integer docNbr);

    Integer selectResponsibleUserOfUserdocParentProcess(String docOri, String docLog, Integer docSer, Integer docNbr);

    Integer selectIpObjectResponsibleUser(CFileId fileId);

    Integer selectUserdocResponsibleUser(CDocumentId documentId);

    Integer selectOffidocResponsibleUser(COffidocId offidocId);

    List<ProcessEventResult> selectByUpperProcess(String procTyp, Integer procNbr);

    TopProcessFileData selectTopProcessFileData(CProcessId topProcessId);

}
