package bg.duosoft.ipas.core.model.patent;

import bg.duosoft.ipas.core.model.miscellaneous.CCpcClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CPatentCpcClass extends CCpcClass implements Serializable {
    private String cpcSymbolPosition;
    private String cpcWPublishValidated;
    private Date cpcSymbolCaptureDate;
    private String cpcQualification;
}
