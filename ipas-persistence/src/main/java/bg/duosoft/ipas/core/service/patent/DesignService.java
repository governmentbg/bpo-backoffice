package bg.duosoft.ipas.core.service.patent;


import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;

import java.util.List;

public interface DesignService {
    void updateDesigns(CPatent design, List<CPatent> singleDesigns);

    CPatent findSingleDesign(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, boolean loadFileContent);

    List<CPatent> getAllSingleDesignsForIndustrialDesign(CPatent patent, boolean loadFileContent);

    void insertDesigns(CPatent design, List<CPatent> singleDesigns);

}
