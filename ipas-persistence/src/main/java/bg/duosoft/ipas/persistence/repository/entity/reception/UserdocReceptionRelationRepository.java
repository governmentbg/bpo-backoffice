package bg.duosoft.ipas.persistence.repository.entity.reception;

import bg.duosoft.ipas.persistence.model.entity.ext.reception.UserdocReceptionRelation;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.UserdocReceptionRelationPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.UserdocReceptionRelationRepositoryCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserdocReceptionRelationRepository extends BaseRepository<UserdocReceptionRelation, UserdocReceptionRelationPK>, UserdocReceptionRelationRepositoryCustom {

    List<UserdocReceptionRelation> findAllByPk_MainTypeOrderByUserdocType_UserdocName(String fileType);

    @Query(value = "SELECT urr.*\n" +
            "FROM EXT_RECEPTION.USERDOC_RECEPTION_RELATION urr\n" +
            "         JOIN EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE adt on urr.LINKED_USERDOC_TYPE = adt.TYPE\n" +
            "         JOIN IPASPROD.CF_USERDOC_TYPE ut on adt.TYPE = ut.USERDOC_TYP\n" +
            "where urr.MAIN_TYPE = ?1 and (?2 IS NULL OR IS_VISIBLE = ?2) and (?3 IS NULL OR ut.IND_INACTIVE = ?3) ORDER BY ut.USERDOC_NAME", nativeQuery = true)
    List<UserdocReceptionRelation> selectUserdocReceptionRelationByMainType(String mainType, String isVisible,String isActive);

}
