package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassNice;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassNicePK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Raya
 * 28.03.2019
 */
public interface CfClassNiceRepository extends BaseRepository<CfClassNice, CfClassNicePK> {

    @Query("SELECT c FROM CfClassNice c WHERE c.pk.niceClassCode = :code AND c.pk.niceClassEdition = :edition AND c.pk.niceClassVersion= :version")
    public CfClassNice selectByPK(@Param("code") Long code, @Param("edition") Long edition, @Param("version") String version);
}
