package bg.duosoft.ipas.persistence.repository.entity.userdoc;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpUserdocRepositoryCustom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IpUserdocRepository extends BaseRepository<IpUserdoc, IpDocPK>, IpUserdocRepositoryCustom {

    @Query(value = "SELECT p.USERDOC_TYP FROM IP_USERDOC_TYPES p where p.DOC_ORI = ?1 AND p.DOC_LOG = ?2 and p.DOC_SER = ?3 and p.DOC_NBR = ?4", nativeQuery = true)
    String selectUserdocTypeByDocId(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Modifying
    @Query(value = "EXECUTE changeUserDocumentType @docOri = ?1, @docLog = ?2, @docSer = ?3, @docNbr = ?4, @newUserdocType = ?5", nativeQuery = true)
    void changeUserdocType(String docOri, String docLog, Integer docSer, Integer docNbr, String newUserdocType);

    @Modifying
    @Query(value = "UPDATE IpDoc d SET d.rowVersion = (d.rowVersion + 1) WHERE d.pk.docOri = ?1 AND d.pk.docLog = ?2 AND d.pk.docSer = ?3 AND d.pk.docNbr = ?4")
    void updateRowVersion(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Modifying
    @Query(value = "exec deleteUserdocDetails @docOri = ?1, @docLog = ?2, @docSer = ?3, @docNbr = ?4", nativeQuery = true)
    void deleteUserdoc(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Query(value = "SELECT ud.pk FROM IpUserdoc ud")
    List<IpDocPK> listUserdocPk();

    @Query(value = "SELECT ud FROM IpUserdoc ud where ud.ipDoc.externalSystemId = ?1")
    IpUserdoc selectByExternalSystemId(String externalSystemId);

    @Query(value = "SELECT ud FROM IpUserdoc ud WHERE ud.ipDoc.dataText1 = :dataText and ud.ipDoc.proc.userdocTyp = :userdocTyp")
    IpUserdoc selectByDataTextAndType(@Param("dataText") String dataText, @Param("userdocTyp") String userdocTyp);
}
