package bg.duosoft.ipas.persistence.repository.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.patent.*;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpPatentLocarnoClassesRepository extends BaseRepository<IpPatentLocarnoClasses, IpPatentLocarnoClassesPK> {

    @Query(value = "SELECT plc.pk FROM IpPatentLocarnoClasses plc")
    List<IpPatentLocarnoClassesPK> listLocarnoClassesPK();
}
