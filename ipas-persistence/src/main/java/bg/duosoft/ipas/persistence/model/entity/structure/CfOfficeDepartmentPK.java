package bg.duosoft.ipas.persistence.model.entity.structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CfOfficeDepartmentPK implements Serializable {
    @Column(name = "OFFICE_DIVISION_CODE")
    private String officeDivisionCode;
    @Column(name = "OFFICE_DEPARTMENT_CODE")
    private String officeDepartmentCode;
}
