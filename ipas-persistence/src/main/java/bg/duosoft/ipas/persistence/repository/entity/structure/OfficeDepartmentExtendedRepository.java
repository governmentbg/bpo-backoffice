package bg.duosoft.ipas.persistence.repository.entity.structure;


import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentExtended;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.OfficeDepartmentRepositoryCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficeDepartmentExtendedRepository extends BaseRepository<CfOfficeDepartmentExtended, CfOfficeDepartmentPK>, OfficeDepartmentRepositoryCustom {

    @Query("select od from CfOfficeDepartmentExtended od where 1 = 1 and od.cfOfficeDepartmentPK.officeDivisionCode = :officeDivisionCode AND (:onlyActive = true and od.indInactive = 'N' or :onlyActive = false) ")
    List<CfOfficeDepartmentExtended> getByOfficeDivisionCodeAndOnlyActive(@Param("officeDivisionCode") String officeDivisionCode, @Param("onlyActive") boolean onlyActive);

    @Query(value = "select od from CfOfficeDepartmentExtended od where 1 = 1 AND (:departmentName is null and 1 = 1 or lower(od.name) like lower(concat('%', coalesce(:departmentName,''),'%'))) AND (:onlyActive = true and od.indInactive = 'N' or :onlyActive = false)")
//    @Query(value = "SELECT * from CF_OFFICE_DEPARTMENT where 1 = 1 and (:departmentName is null and 1 = 1 or lower(OFFICE_DEPARTMENT_NAME) like lower ('%' + coalesce(:departmentName,'') + '%')) and (:onlyActive = 1 and :onlyActive = is_active or :onlyActive = 0)", nativeQuery = true)
    List<CfOfficeDepartmentExtended> findByOfficeDepartmentNameContainingIgnoreCase(@Param("departmentName") String departmentName, @Param("onlyActive") boolean onlyActive);
}
