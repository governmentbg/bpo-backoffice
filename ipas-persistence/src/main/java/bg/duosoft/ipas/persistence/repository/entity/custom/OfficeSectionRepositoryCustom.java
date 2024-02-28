package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentPK;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSectionPK;

/**
 * User: ggeorgiev
 * Date: 8.7.2019 г.
 * Time: 18:38
 */
public interface OfficeSectionRepositoryCustom {
    CfOfficeSectionPK transferSection(CfOfficeSectionPK oldPk, CfOfficeDepartmentPK newDepartmentPk, boolean disableEnableConstraints);
    String getNextSectionCode(String officeDivision, String officeDepartment);
}
