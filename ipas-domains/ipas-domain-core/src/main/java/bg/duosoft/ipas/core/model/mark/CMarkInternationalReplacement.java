package bg.duosoft.ipas.core.model.mark;

import bg.duosoft.ipas.core.model.file.CFileId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMarkInternationalReplacement implements Serializable {
    private static final long serialVersionUID = 1919436437680667116L;
    private CFileId fileId;
    private Integer registrationNumber;
    private String registrationDup;
    private String replacementFilingNumber;
    private Boolean isAllServices;
    private List<CNiceClass> replacementNiceClasses;
}
