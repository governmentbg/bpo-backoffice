package bg.duosoft.ipas.persistence.repository.entity.ext;


import bg.duosoft.ipas.persistence.model.entity.ext.ExtLiabilityDetails;
import bg.duosoft.ipas.persistence.model.entity.ext.ExtLiabilityDetailsPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExtLiabilityDetailsRepository extends BaseRepository<ExtLiabilityDetails, ExtLiabilityDetailsPK> {

    @Query(value = "SELECT l from ExtLiabilityDetails l where l.pk.id =  ?1")
    ExtLiabilityDetails selectByLiabilityId(Integer id);

    @Modifying
    @Query(value = "UPDATE ExtLiabilityDetails l SET l.processed = true where l.pk.fileSeq = ?1 and l.pk.fileTyp = ?2 and l.pk.fileSer = ?3 and l.pk.fileNbr = ?4 and l.pk.id = ?5 ")
    void setLiabilityDetailAsProcessed(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, Integer id);

}
