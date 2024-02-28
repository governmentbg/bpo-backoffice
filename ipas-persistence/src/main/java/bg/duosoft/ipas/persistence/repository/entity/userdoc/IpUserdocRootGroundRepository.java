package bg.duosoft.ipas.persistence.repository.entity.userdoc;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocRootGroundsPK;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpUserdocRootGrounds;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpUserdocRootGroundRepository extends BaseRepository<IpUserdocRootGrounds, IpUserdocRootGroundsPK> {
    @Query(value = "select ipmark.FILE_NBR,ipmark.FILING_DATE,ipfile.REGISTRATION_NBR,ipfile.REGISTRATION_DATE,ipmark.SIGN_WCODE,ipfile.TITLE from IP_MARK ipmark\n" +
            "inner join IP_FILE ipfile on ipmark.FILE_NBR = ipfile.FILE_NBR and ipmark.FILE_SEQ = ipfile.FILE_SEQ and ipmark.FILE_TYP = ipfile.FILE_TYP and ipmark.FILE_SER = ipfile.FILE_SER\n" +
            "where ipmark.FILE_SEQ = ? and ipmark.FILE_TYP = ? and ipmark.FILE_SER = ? and ipmark.FILE_NBR = ?", nativeQuery = true)
     List<Object[]> findGroundMarkRelatedData(String fileSeq,String fileTyp,Integer fileSer,Integer fileNbr);
}
