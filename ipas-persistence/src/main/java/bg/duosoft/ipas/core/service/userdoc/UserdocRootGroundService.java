package bg.duosoft.ipas.core.service.userdoc;

import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;

import java.util.List;


public interface UserdocRootGroundService {
    CUserdocRootGrounds findById(Integer rootGroundId, String docOri, String docLog, Integer docSeR, Integer docNbr);
    List<Object[]> findGroundMarkRelatedData(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr);
}
