package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentPK;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSectionPK;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.OfficeSectionRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Optional;

/**
 * User: ggeorgiev
 * Date: 28.3.2019 г.
 * Time: 14:47
 */
@Repository
@Transactional
public class OfficeSectionRepositoryCustomImpl extends BaseRepositoryCustomImpl implements OfficeSectionRepositoryCustom {

    @Override
    public CfOfficeSectionPK transferSection(CfOfficeSectionPK sectionPk, CfOfficeDepartmentPK newPk, boolean disableConstraints) {
        String newSectionCode = getNextSectionCode(newPk.getOfficeDivisionCode(), newPk.getOfficeDepartmentCode());
        if (disableConstraints) {
            em.createNativeQuery(
                    "ALTER TABLE ipasprod.CF_OFFICE_SECTION NOCHECK CONSTRAINT ALL;" +
                    " ALTER TABLE EXT_USER.CF_OFFICE_SECTION NOCHECK CONSTRAINT ALL; " +
                    "ALTER TABLE IPASPROD.IP_USER NOCHECK CONSTRAINT ALL;").executeUpdate();
        }


        Query q = em.createNativeQuery("UPDATE ipasprod.CF_OFFICE_SECTION SET OFFICE_DIVISION_CODE = :newDivisionCode, OFFICE_DEPARTMENT_CODE = :newDepartmentCode, OFFICE_SECTION_CODE = :newSectionCode WHERE OFFICE_DIVISION_CODE = :oldDivisionCode and OFFICE_DEPARTMENT_CODE = :oldDepartmentCode and OFFICE_SECTION_CODE = :oldSectionCode ;\n" +
                "UPDATE EXT_USER.CF_OFFICE_SECTION SET OFFICE_DIVISION_CODE = :newDivisionCode, OFFICE_DEPARTMENT_CODE = :newDepartmentCode, OFFICE_SECTION_CODE = :newSectionCode WHERE OFFICE_DIVISION_CODE = :oldDivisionCode and OFFICE_DEPARTMENT_CODE = :oldDepartmentCode and OFFICE_SECTION_CODE = :oldSectionCode ;\n" +
                "UPDATE IPASPROD.IP_USER SET OFFICE_DIVISION_CODE = :newDivisionCode, OFFICE_DEPARTMENT_CODE = :newDepartmentCode, OFFICE_SECTION_CODE = :newSectionCode WHERE OFFICE_DIVISION_CODE = :oldDivisionCode and OFFICE_DEPARTMENT_CODE = :oldDepartmentCode and OFFICE_SECTION_CODE = :oldSectionCode \n;");
        q.setParameter("newDivisionCode", newPk.getOfficeDivisionCode());
        q.setParameter("newDepartmentCode", newPk.getOfficeDepartmentCode());
        q.setParameter("newSectionCode", newSectionCode);
        q.setParameter("oldDivisionCode", sectionPk.getOfficeDivisionCode());
        q.setParameter("oldDepartmentCode", sectionPk.getOfficeDepartmentCode());
        q.setParameter("oldSectionCode", sectionPk.getOfficeSectionCode());
        q.executeUpdate();
        if (disableConstraints) {
            em.createNativeQuery("ALTER TABLE ipasprod.CF_OFFICE_SECTION CHECK CONSTRAINT ALL; " +
                    "ALTER TABLE EXT_USER.CF_OFFICE_SECTION CHECK CONSTRAINT ALL; " +
                    "ALTER TABLE IPASPROD.IP_USER CHECK CONSTRAINT ALL; ");
        }
        em.flush();
        em.clear();
        return new CfOfficeSectionPK(newPk.getOfficeDivisionCode(), newPk.getOfficeDepartmentCode(), newSectionCode);
    }

    public String getNextSectionCode(String officeDivision, String officeDepartment) {
        Integer res = getMaxSectionId(officeDivision, officeDepartment).orElse(0);
        return String.format("%03d", res + 1);
    }


    private Optional<Integer> getMaxSectionId(String officeDivisionCode, String officeDepartmentCode) {
        Query q = em.createNativeQuery("select max(cast (office_section_code as int)) from ipasprod.CF_OFFICE_SECTION where OFFICE_DIVISION_CODE = ?1 and OFFICE_DEPARTMENT_CODE = ?2");
        q.setParameter(1, officeDivisionCode);
        q.setParameter(2, officeDepartmentCode);
        Integer res = (Integer) q.getSingleResult();
        return Optional.ofNullable(res);
    }

}
