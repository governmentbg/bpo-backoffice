package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentPK;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSection;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSectionExtended;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.OfficeDepartmentRepositoryCustom;
import bg.duosoft.ipas.persistence.repository.entity.structure.OfficeSectionExtendedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 * User: ggeorgiev
 * Date: 15.7.2019 г.
 * Time: 15:34
 */
@Repository
@Transactional
public class OfficeDepartmentRepositoryCustomImpl extends BaseRepositoryCustomImpl implements OfficeDepartmentRepositoryCustom {
    @Autowired
    private OfficeSectionExtendedRepository officeSectionExtendedRepository;

    public CfOfficeDepartmentPK transferDepartment(CfOfficeDepartmentPK departmentPK, String newOfficeDivisionPk) {
//        throw new RuntimeException("Not implemented...");
        String newDepartmentCode = getNextDepartmentCode(newOfficeDivisionPk);
        em.createNativeQuery("ALTER TABLE ipasprod.CF_OFFICE_DEPARTMENT NOCHECK CONSTRAINT ALL; " +
                "ALTER TABLE EXT_USER.CF_OFFICE_DEPARTMENT NOCHECK CONSTRAINT ALL; " +
                "ALTER TABLE ipasprod.CF_OFFICE_SECTION NOCHECK CONSTRAINT ALL; " +
                "ALTER TABLE EXT_USER.CF_OFFICE_SECTION NOCHECK CONSTRAINT ALL; " +
                "ALTER TABLE IPASPROD.IP_USER NOCHECK CONSTRAINT ALL;").executeUpdate();
        List<CfOfficeSectionExtended> sectionsByDepartment = officeSectionExtendedRepository.getByOfficeDivisionCodeAndOfficeDepartmentCode(departmentPK.getOfficeDivisionCode(), departmentPK.getOfficeDepartmentCode(), false);

        Query q = em.createNativeQuery(
                "UPDATE ipasprod.CF_OFFICE_DEPARTMENT SET OFFICE_DIVISION_CODE = :newDivisionCode, OFFICE_DEPARTMENT_CODE = :newDepartmentCode WHERE OFFICE_DIVISION_CODE = :oldDivisionCode and OFFICE_DEPARTMENT_CODE = :oldDepartmentCode ;\n" +
                "UPDATE EXT_USER.CF_OFFICE_DEPARTMENT SET OFFICE_DIVISION_CODE = :newDivisionCode, OFFICE_DEPARTMENT_CODE = :newDepartmentCode WHERE OFFICE_DIVISION_CODE = :oldDivisionCode and OFFICE_DEPARTMENT_CODE = :oldDepartmentCode ;\n" +
                "UPDATE IPASPROD.IP_USER SET OFFICE_DIVISION_CODE = :newDivisionCode, OFFICE_DEPARTMENT_CODE = :newDepartmentCode WHERE OFFICE_DIVISION_CODE = :oldDivisionCode and OFFICE_DEPARTMENT_CODE = :oldDepartmentCode \n ;");
        q.setParameter("newDivisionCode", newOfficeDivisionPk);
        q.setParameter("newDepartmentCode", newDepartmentCode);

        q.setParameter("oldDivisionCode", departmentPK.getOfficeDivisionCode());
        q.setParameter("oldDepartmentCode", departmentPK.getOfficeDepartmentCode());

        q.executeUpdate();

        for (CfOfficeSection cfOfficeSection : sectionsByDepartment) {
            officeSectionExtendedRepository.transferSection(cfOfficeSection.getCfOfficeSectionPK(), new CfOfficeDepartmentPK(newOfficeDivisionPk, newDepartmentCode), false);
        }

        em.createNativeQuery("ALTER TABLE ipasprod.CF_OFFICE_DEPARTMENT CHECK CONSTRAINT ALL; " +
                "ALTER TABLE EXT_USER.CF_OFFICE_DEPARTMENT CHECK CONSTRAINT ALL; " +
                "ALTER TABLE ipasprod.CF_OFFICE_SECTION CHECK CONSTRAINT ALL; " +
                "ALTER TABLE EXT_USER.CF_OFFICE_SECTION CHECK CONSTRAINT ALL; " +
                "ALTER TABLE IPASPROD.IP_USER CHECK CONSTRAINT ALL; ");
        em.flush();
        em.clear();
        return new CfOfficeDepartmentPK(newOfficeDivisionPk, newDepartmentCode);
    }

    public String getNextDepartmentCode(String officeDivision) {
        Integer res = getMaxDepartmentId(officeDivision).orElse(0);
        return String.format("%03d", res + 1);
    }


    private Optional<Integer> getMaxDepartmentId(String officeDivisionCode) {
        Query q = em.createNativeQuery("select max(cast (OFFICE_DEPARTMENT_CODE as int)) from ipasprod.CF_OFFICE_DEPARTMENT where OFFICE_DIVISION_CODE = ?1");
        q.setParameter(1, officeDivisionCode);
        Integer res = (Integer) q.getSingleResult();
        return Optional.ofNullable(res);
    }
}
