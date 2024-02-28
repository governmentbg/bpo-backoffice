package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.Data;

import java.io.Serializable;

@Data
public class COfficeSectionId implements Serializable {
    private static final long serialVersionUID = 3658016673769231402L;
    private String officeDivisionCode;
    private String officeDepartmentCode;
    private String officeSectionCode;

}
