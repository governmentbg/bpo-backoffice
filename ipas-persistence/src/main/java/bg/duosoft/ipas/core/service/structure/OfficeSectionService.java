package bg.duosoft.ipas.core.service.structure;

import bg.duosoft.ipas.core.model.structure.OfficeSection;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;

import java.util.List;

public interface OfficeSectionService {

    default OfficeSection getSection(OfficeStructureId officeStructureId) {
        return officeStructureId == null ? null : getSection(officeStructureId.getOfficeDivisionCode(), officeStructureId.getOfficeDepartmentCode(), officeStructureId.getOfficeSectionCode());
    }
    OfficeSection getSection(String divisionCode, String departmentCode, String sectionCode);

    List<OfficeSection> getSectionsOfDepartment(String divisionCode, String departmentCode, boolean onlyActive);

    List<OfficeSection> getSectionsByPartOfName(String partOfName, boolean onlyActive);

    List<OfficeSection> transferSections(List<OfficeSection> section, String divisionCode, String departmentCode);

    void archiveSection(OfficeSection section);

    void updateSection(OfficeSection section);

    OfficeStructureId insertSection(OfficeSection section);

}
