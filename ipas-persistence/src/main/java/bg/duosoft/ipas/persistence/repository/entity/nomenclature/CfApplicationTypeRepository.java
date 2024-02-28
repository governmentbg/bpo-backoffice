package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationType;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CfApplicationTypeRepository extends BaseRepository<CfApplicationType, String> {

    List<CfApplicationType> findAllByTableName(String tableName);

    List<CfApplicationType> findAllByTableNameAndFileTypIn(String tableName, Collection<String> fileTyp);

    @Query(value = "SELECT DISTINCT t.TABLE_NAME as value FROM CF_APPLICATION_TYPE t WHERE t.FILE_TYP = ?1", nativeQuery = true)
    String selectTableNameByFileType(String fileTyp);

    CfApplicationType findByFileTyp(String fileTyp);

    List<CfApplicationType> findAllByFileTypInOrderByApplTypeNameAsc(Collection<String> fileTyp);
}
