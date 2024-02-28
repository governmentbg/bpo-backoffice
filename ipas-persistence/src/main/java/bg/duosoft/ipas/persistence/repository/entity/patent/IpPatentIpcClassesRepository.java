package bg.duosoft.ipas.persistence.repository.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentIpcClasses;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentIpcClassesPK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentSummaryPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpPatentIpcClassesRepository extends BaseRepository<IpPatentIpcClasses, IpPatentIpcClassesPK> {

    @Query(value = "SELECT ic.pk FROM IpPatentIpcClasses ic")
    List<IpPatentIpcClassesPK> listPatentIpcClassesPK();

    @Query(value = "SELECT * FROM IPASPROD.IP_PATENT_IPC_CLASSES e WHERE e.FILE_SEQ = ?1 AND e.FILE_TYP = ?2 AND e.FILE_SER = ?3 AND e.FILE_NBR = ?4", nativeQuery = true)
    List<IpPatentIpcClasses> findByObjectId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);
}
