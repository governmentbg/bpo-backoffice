package bg.duosoft.ipas.persistence.repository.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentCpcClasses;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentCpcClassesPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpPatentCpcClassesRepository extends BaseRepository<IpPatentCpcClasses, IpPatentCpcClassesPK> {

    @Query(value = "SELECT cpc.pk FROM IpPatentCpcClasses cpc")
    List<IpPatentCpcClassesPK> listPatentCpcClassesPK();

    @Query(value = "SELECT * FROM IPASPROD.IP_PATENT_CPC_CLASSES e WHERE e.FILE_SEQ = ?1 AND e.FILE_TYP = ?2 AND e.FILE_SER = ?3 AND e.FILE_NBR = ?4", nativeQuery = true)
    List<IpPatentCpcClasses> findByObjectId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);
}
