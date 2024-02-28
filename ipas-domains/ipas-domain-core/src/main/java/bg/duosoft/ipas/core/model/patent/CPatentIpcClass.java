package bg.duosoft.ipas.core.model.patent;

import bg.duosoft.ipas.core.model.miscellaneous.CIpcClass;
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
public class CPatentIpcClass extends CIpcClass implements Serializable {

    private String ipcSymbolPosition;
    private String ipcWpublishValidated;
    private Date ipcSymbolCaptureDate;
    private String ipcQualification;

}
