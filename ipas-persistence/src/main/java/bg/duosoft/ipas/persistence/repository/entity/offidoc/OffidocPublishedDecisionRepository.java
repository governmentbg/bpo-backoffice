package bg.duosoft.ipas.persistence.repository.entity.offidoc;

import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPublishedDecision;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface OffidocPublishedDecisionRepository extends BaseRepository<IpOffidocPublishedDecision, IpOffidocPK> {


    @Modifying
    @Query(value = "insert into ext_Core.IP_OFFIDOC_PUBLISHED_DECISION(OFFIDOC_ORI, OFFIDOC_SER, OFFIDOC_NBR, ATTACHMENT_CONTENT, ATTACHMENT_NAME,DECISION_DATE)\n" +
            "values(?3,?4,?5,?2,?1,?6)",nativeQuery = true)
    void insert(String fileName, byte[] content, String offidocOrigin, Integer offidocSeries, Integer offidocNbr, Date decisionDate);

    @Modifying
    @Query(value = "delete from ext_Core.IP_OFFIDOC_PUBLISHED_DECISION  where " +
            "OFFIDOC_ORI=?1 AND OFFIDOC_SER=?2 AND OFFIDOC_NBR=?3",nativeQuery = true)
    void delete(String offidocOrigin, Integer offidocSeries,Integer offidocNbr);
}
