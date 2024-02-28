package bg.duosoft.ipas.persistence.repository.entity.offidoc;

import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidoc;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface IpOffidocRepository extends BaseRepository<IpOffidoc, IpOffidocPK> {

    @Query(value = "SELECT f.OFFIDOC_TYP FROM IPASPROD.IP_OFFIDOC f where f.OFFIDOC_ORI = ?1 AND f.OFFIDOC_SER = ?2 AND f.OFFIDOC_NBR = ?3", nativeQuery = true)
    String selectOffidocType(String offidocOrigin, Integer offidocSeries, Integer offidocNbr);

}
