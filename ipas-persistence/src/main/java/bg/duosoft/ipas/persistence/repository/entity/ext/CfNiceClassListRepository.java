package bg.duosoft.ipas.persistence.repository.entity.ext;

import bg.duosoft.ipas.persistence.model.entity.ext.core.CfNiceClassList;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Raya
 * 23.09.2020
 */
public interface CfNiceClassListRepository extends BaseRepository<CfNiceClassList, Integer> {

    @Query(value = "SELECT n.niceClassCode from CfNiceClassList n")
    List<Integer> getNiceClassesCodes();
}
