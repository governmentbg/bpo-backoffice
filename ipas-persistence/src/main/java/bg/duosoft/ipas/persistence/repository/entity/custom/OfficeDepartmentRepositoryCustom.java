package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentPK;

/**
 * User: ggeorgiev
 * Date: 8.7.2019 г.
 * Time: 18:38
 */
public interface OfficeDepartmentRepositoryCustom {
    CfOfficeDepartmentPK transferDepartment(CfOfficeDepartmentPK departmentPK, String newOfficeDivisionPk);
    String getNextDepartmentCode(String officeDivision);
}
