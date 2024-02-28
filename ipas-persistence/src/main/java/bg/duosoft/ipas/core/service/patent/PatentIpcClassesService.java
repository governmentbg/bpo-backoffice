package bg.duosoft.ipas.core.service.patent;

import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;

import java.util.List;

public interface PatentIpcClassesService {
    long count();
    List<CPatentIpcClass> findByObjectId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);
}
