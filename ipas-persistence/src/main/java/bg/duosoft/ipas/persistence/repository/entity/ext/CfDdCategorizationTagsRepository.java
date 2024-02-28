package bg.duosoft.ipas.persistence.repository.entity.ext;

import bg.duosoft.ipas.persistence.model.entity.ext.core.CfDdCategorizationTags;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 01.06.2021
 * Time: 16:13
 */
public interface CfDdCategorizationTagsRepository extends BaseRepository<CfDdCategorizationTags, Integer> {


    @Query(value = "SELECT dd from CfDdCategorizationTags dd WHERE dd.fileType = :fileType AND dd.userdocType IS NULL")
    Optional<CfDdCategorizationTags> findByFileTypeAndEmptyUserdocType(@Param("fileType") String fileType);

    Optional<CfDdCategorizationTags> findByFileTypeAndUserdocType(String fileType, String userdocType);

}
