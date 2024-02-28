package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class CfUserdocTypeDepartmentPK  implements Serializable {

    @Column(name = "USERDOC_TYP")
    private String userdocTyp;

    @Column(name = "OFFICE_DIVISION_CODE")
    private String officeDivisionCode;

    @Column(name = "OFFICE_DEPARTMENT_CODE")
    private String officeDepartmentCode;


}
