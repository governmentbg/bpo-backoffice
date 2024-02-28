package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.nonentity.UserdocHierarchyChildNode;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocIpDocSimpleResult;

import java.util.List;

public interface IpUserdocRepositoryCustom {

    void changeUserdocPosition(String userdocProcTyp, Integer userdocProcNbr, String newUpperProcTyp, Integer newUpperProcNbr, Integer userChanged);

    List<UserdocIpDocSimpleResult> selectUserdocsFromAcstre();

    public List<UserdocHierarchyChildNode> getFileUserdocHierarchy(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr);

    public List<UserdocHierarchyChildNode> getUserdocUserdocHierarchy(String docOri, String docLog, Integer docSer, Integer docNbr);

    public boolean isMainEpoPatentRequestForValidation(String docOri, String docLog, Integer docSer, Integer docNbr);
}
