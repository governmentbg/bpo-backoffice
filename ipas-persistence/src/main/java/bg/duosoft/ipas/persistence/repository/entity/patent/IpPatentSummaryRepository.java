package bg.duosoft.ipas.persistence.repository.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentSummary;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentSummaryPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpPatentSummaryRepository extends BaseRepository<IpPatentSummary, IpPatentSummaryPK> {


    @Query(value = "SELECT ps.pk FROM IpPatentSummary ps")
    List<IpPatentSummaryPK> listPatentSummaryPK();

}
