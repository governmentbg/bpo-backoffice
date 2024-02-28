package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtypePK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationType;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CfApplicationSubTypeRepository extends BaseRepository<CfApplicationSubtype, CfApplicationSubtypePK> {

    List<CfApplicationSubtype> findAllByPk_ApplTyp(String applTyp);

    @Query(value = "SELECT ast " +
            "FROM CfApplicationSubtype ast " +
            "JOIN CfApplicationType cat ON ast.cfApplicationType.applTyp = cat.applTyp " +
            "WHERE cat.fileTyp in (?1)" +
            "ORDER BY ast.applSubtypeName ASC")
    List<CfApplicationSubtype> findAllByFileTypesOrderByApplTypeNameAsc(Collection<String> fileTyp);

    @Query(value = "SELECT ast " +
            "FROM CfApplicationSubtype ast " +
            "JOIN CfApplicationType cat ON ast.cfApplicationType.applTyp = cat.applTyp " +
            "WHERE cat.fileTyp in (?1) AND cat.applTyp = ?2 " +
            "ORDER BY ast.applSubtypeName ASC")
    List<CfApplicationSubtype> findAllByFileTypesAndApplTypOrderByApplTypeNameAsc(Collection<String> fileTyp, String applTyp);
}
