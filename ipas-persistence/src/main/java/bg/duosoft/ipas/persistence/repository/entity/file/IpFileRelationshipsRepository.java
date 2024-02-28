package bg.duosoft.ipas.persistence.repository.entity.file;

import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpFileRelationshipsRepository extends BaseRepository<IpFileRelationship, IpFileRelationshipPK> {

    @Query(value = "SELECT MAX(FILE_NBR2) FROM IP_FILE_RELATIONSHIP e WHERE e.FILE_SEQ1 = ?1 AND e.FILE_TYP1 = ?2 AND e.FILE_SER1 = ?3 AND e.FILE_NBR1 = ?4 " +
            "AND RELATIONSHIP_TYP = ?5", nativeQuery = true)
    Integer findSingleDesignMaxNumberById1(String fileSeq, String fileType, Integer fileSer, Integer fileNbr,String relationshipType);

    @Query(value = "SELECT fr.pk FROM IpFileRelationship fr")
    List<IpFileRelationshipPK> listFileRelationshipPK();

    @Query("SELECT rel FROM IpFileRelationship rel WHERE rel.pk.fileSeq2 = ?1 AND rel.pk.fileTyp2 =?2 AND rel.pk.fileSer2= ?3 AND rel.pk.fileNbr2 = ?4 AND rel.relationshipType.relationshipTyp =?5")
    List<IpFileRelationship> findRelationships1ForFile(String fileSeq, String fileType, Integer fileSer, Integer fileNbr,String relationshipType);

    @Query("SELECT rel FROM IpFileRelationship rel WHERE rel.pk.fileSeq1 = ?1 AND rel.pk.fileTyp1 =?2 AND rel.pk.fileSer1= ?3 AND rel.pk.fileNbr1 = ?4 AND rel.relationshipType.relationshipTyp =?5")
    List<IpFileRelationship> findRelationships2ForFile(String fileSeq, String fileType, Integer fileSer, Integer fileNbr,String relationshipType);

}
