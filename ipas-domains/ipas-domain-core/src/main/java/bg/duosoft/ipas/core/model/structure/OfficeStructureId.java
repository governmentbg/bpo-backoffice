package bg.duosoft.ipas.core.model.structure;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * User: ggeorgiev
 * Date: 4.7.2019 г.
 * Time: 10:56
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class OfficeStructureId implements Serializable {
    public static final int TYPE_DIVISION = 1;
    public static final int TYPE_DEPARTMENT = 2;
    public static final int TYPE_SECTION = 3;

    private String officeDivisionCode;

    private String officeDepartmentCode;

    private String officeSectionCode;

    public OfficeStructureId(String officeDivisionCode, String officeDepartmentCode, String officeSectionCode) {
        setOfficeDivisionCode(officeDivisionCode);
        setOfficeDepartmentCode(officeDepartmentCode);
        setOfficeSectionCode(officeSectionCode);
    }

    public void setOfficeDivisionCode(String officeDivisionCode) {
        this.officeDivisionCode = (officeDivisionCode == null || "".equals(officeDivisionCode)) ? null : officeDivisionCode;
    }

    public void setOfficeDepartmentCode(String officeDepartmentCode) {
        this.officeDepartmentCode = (officeDepartmentCode == null || "".equals(officeDepartmentCode)) ? null : officeDepartmentCode;
    }

    public void setOfficeSectionCode(String officeSectionCode) {
        this.officeSectionCode = (officeSectionCode == null || "".equals(officeSectionCode)) ? null : officeSectionCode;
    }

    public String getCode() {
        if (isDivision()) {
            return getFullDivisionCode();
        } else if (isDepartment()) {
            return getFullDepartmentCode();
        } else if (isSection()) {
            return getFullSectionCode();
        } else {
            throw new RuntimeException("Unknown type...");
        }
    }

    public int getType() {
        if (isDivision()) {
            return TYPE_DIVISION;
        } else if (isDepartment()) {
            return TYPE_DEPARTMENT;
        } else if (isSection()) {
            return TYPE_SECTION;
        } else {
            throw new RuntimeException("Unknown type");
        }
    }

    public boolean isDivision() {
        return this.officeDivisionCode != null && this.officeDepartmentCode == null && this.officeSectionCode == null;
    }

    public boolean isDepartment() {
        return this.officeDivisionCode != null && this.officeDepartmentCode != null && this.officeSectionCode == null;
    }

    public boolean isSection() {
        return this.officeDivisionCode != null && this.officeDepartmentCode != null && this.officeSectionCode != null;
    }

    public String getFullSectionCode() {
        return isSection() ? officeDivisionCode + "-" + officeDepartmentCode + "-" + officeSectionCode : null;
    }
    public String getFullDivisionCode() {
        return officeDivisionCode;
    }
    public String getFullDepartmentCode() {
        return isSection() || isDepartment() ? officeDivisionCode + "-" + officeDepartmentCode : null;
    }

}
