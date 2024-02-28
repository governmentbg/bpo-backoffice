package bg.duosoft.ipas.persistence.repository.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClasses;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClassesPK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentLocarnoClassesPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpMarkNiceClassesRepository extends BaseRepository<IpMarkNiceClasses, IpMarkNiceClassesPK> {
    @Query(value = "SELECT nc.pk FROM IpMarkNiceClasses nc")
    List<IpMarkNiceClassesPK> listNiceClassesPK();
}
