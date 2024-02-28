package bg.duosoft.ipas.persistence.repository.entity.structure;


import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDivisionExtended;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.OfficeDivisionRepositoryCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficeDivisionExtendedRepository extends BaseRepository<CfOfficeDivisionExtended,String>, OfficeDivisionRepositoryCustom {
    @Query(value = "select od from CfOfficeDivision od where 1 = 1 AND (:divisionName is null and 1 = 1 or lower(od.name) like lower(concat('%', coalesce(:divisionName,''),'%'))) AND (:onlyActive = true and od.indInactive = 'N' or :onlyActive = false)")
    List<CfOfficeDivisionExtended> findByOfficeDivisionNameContainingIgnoreCase(@Param("divisionName")String divisionName, @Param("onlyActive")boolean onlyActive);

}
