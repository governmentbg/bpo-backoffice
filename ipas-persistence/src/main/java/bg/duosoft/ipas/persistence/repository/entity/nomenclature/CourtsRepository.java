package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.ext.legal.Courts;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourtsRepository extends BaseRepository<Courts, Integer> {

    @Query(value = "SELECT c FROM Courts c where (c.name like ?1 )")
    List<Courts> selectByNameLike(String name);

    List<Courts> findByName(String name);
}
