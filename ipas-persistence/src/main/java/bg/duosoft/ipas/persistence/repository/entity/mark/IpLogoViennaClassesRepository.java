package bg.duosoft.ipas.persistence.repository.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.mark.IpLogoViennaClasses;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogoViennaClassesPK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClassesPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpLogoViennaClassesRepository extends BaseRepository<IpLogoViennaClasses, IpLogoViennaClassesPK> {
    @Query(value = "SELECT lvc.pk FROM IpLogoViennaClasses lvc")
    List<IpLogoViennaClassesPK> listLogoViennaClassesPK();
}
