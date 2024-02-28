package bg.duosoft.ipas.core.service.structure;

import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;

import java.util.List;

public interface OfficeDepartmentService {
    default OfficeDepartment getDepartment(OfficeStructureId officeStructureId) {
        return officeStructureId == null ? null : getDepartment(officeStructureId.getOfficeDivisionCode(), officeStructureId.getOfficeDepartmentCode());
    }
    OfficeDepartment getDepartment(String divisionCode, String departmentCode);

    List<OfficeDepartment> getDepartmentsOfDivision(String divisionCode, boolean onlyActive);

    List<OfficeDepartment> getDepartmentsByPartOfName(String partOfName, boolean onlyActive);

    void archiveDepartment(OfficeDepartment department);

    void updateDepartment(OfficeDepartment department);

    OfficeStructureId insertDepartment(OfficeDepartment department);

    List<OfficeDepartment> transferDepartments(List<OfficeDepartment> departments, String divisionCode);

    List<OfficeDepartment> findAll();
}
