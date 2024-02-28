package bg.duosoft.ipas.core.service.structure;

import bg.duosoft.ipas.core.model.structure.OfficeDivision;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;

import java.util.List;

public interface OfficeDivisionService {
    default OfficeDivision getDivision(OfficeStructureId officeStructureId) {
        return officeStructureId == null ? null : getDivision(officeStructureId.getOfficeDivisionCode());
    }
    OfficeDivision getDivision(String divisionCode);

    List<OfficeDivision> getDivisionsByPartOfName(String partOfName, boolean onlyActive);

    void archiveDivision(OfficeDivision division);

    void updateDivision(OfficeDivision division);

    OfficeStructureId insertDivision(OfficeDivision division);

}
