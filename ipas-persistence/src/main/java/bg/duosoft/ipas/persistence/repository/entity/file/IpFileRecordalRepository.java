package bg.duosoft.ipas.persistence.repository.entity.file;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpFileRecordal;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpFileRecordalPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpFileRecordalRepository extends BaseRepository<IpFileRecordal, IpFileRecordalPK> {

    @Query(value = "SELECT * FROM EXT_CORE.IP_FILE_RECORDAL f where f.DOC_ORI = ?1 AND f.DOC_LOG = ?2 AND f.DOC_SER = ?3 AND f.DOC_NBR = ?4", nativeQuery = true)
    IpFileRecordal selectByRecordalUserdoc(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Query(value = "SELECT * FROM EXT_CORE.IP_FILE_RECORDAL f where f.INVALIDATION_DOC_ORI = ?1 AND f.INVALIDATION_DOC_LOG = ?2 AND f.INVALIDATION_DOC_SER = ?3 AND f.INVALIDATION_DOC_NBR = ?4", nativeQuery = true)
    IpFileRecordal selectByInvalidationUserdoc(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Query(value = "SELECT * FROM EXT_CORE.IP_FILE_RECORDAL f where f.PROC_TYP = ?1 AND f.PROC_NBR = ?2 AND f.ACTION_NBR = ?3", nativeQuery = true)
    List<IpFileRecordal> selectByRecordalActionId(String procTyp, Integer procNbr, Integer actionNbr);

}
