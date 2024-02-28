package bg.duosoft.ipas.persistence.repository.entity.efiling;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.efiling.IpUserodocEFilingData;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;


public interface UserdocEfilingDataRepository extends BaseRepository<IpUserodocEFilingData, IpDocPK> {
}
