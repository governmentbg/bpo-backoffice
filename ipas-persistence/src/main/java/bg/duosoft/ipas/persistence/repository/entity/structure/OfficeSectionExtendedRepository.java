package bg.duosoft.ipas.persistence.repository.entity.structure;

import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSectionExtended;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSectionPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.OfficeSectionRepositoryCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficeSectionExtendedRepository extends BaseRepository<CfOfficeSectionExtended, CfOfficeSectionPK>, OfficeSectionRepositoryCustom {

    @Query(value = "select os from CfOfficeSectionExtended os where os.cfOfficeSectionPK.officeDivisionCode = :officeDivisionCode and os.cfOfficeSectionPK.officeDepartmentCode = :officeDepartmentCode and (:onlyActive = true and os.indInactive = 'N' or :onlyActive = false)")
    List<CfOfficeSectionExtended> getByOfficeDivisionCodeAndOfficeDepartmentCode(@Param("officeDivisionCode") String officeDivisionCode, @Param("officeDepartmentCode") String officeDepartmentCode, @Param("onlyActive") boolean onlyActive);

    @Query(value = "select os from CfOfficeSectionExtended os where 1 = 1 AND (:sectionName is null and 1 = 1 or lower(os.name) like lower(concat('%', coalesce(:sectionName,''),'%'))) AND (:onlyActive = true and os.indInactive = 'N' or :onlyActive = false)")
    List<CfOfficeSectionExtended> findByOfficeSectionNameContainingIgnoreCase(@Param("sectionName")String sectionName, @Param("onlyActive") boolean onlyActive);

}
