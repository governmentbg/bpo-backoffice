package bg.duosoft.ipas.core.service.file;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.file.CRelationshipExtended;

import java.util.Date;
import java.util.List;

public interface FileRelationshipsService {
    Integer findSingleDesignMaxNumberById1(String fileSeq, String fileType, Integer fileSer, Integer fileNbr,String relationshipType);

    long count();

     Date getPatentLikeObjectEntitlementDateFromRelationships(List<CRelationship> relationshipList, CRelationshipExtended relationshipExtended,Date defaultEntitlementDate);

    List<CRelationship> findRelationships1ForFile(CFileId fileId, String relationshipType);

    List<CRelationship> findRelationships2ForFile(CFileId fileId, String relationshipType);

}
