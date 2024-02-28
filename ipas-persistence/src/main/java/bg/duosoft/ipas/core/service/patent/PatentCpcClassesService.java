package bg.duosoft.ipas.core.service.patent;

import bg.duosoft.ipas.core.model.patent.CPatentCpcClass;

import java.util.List;

public interface PatentCpcClassesService {
    long count();
    List<CPatentCpcClass> findByObjectId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);
}
