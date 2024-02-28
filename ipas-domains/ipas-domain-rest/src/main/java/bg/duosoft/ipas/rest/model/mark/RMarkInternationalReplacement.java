package bg.duosoft.ipas.rest.model.mark;

import bg.duosoft.ipas.rest.model.file.RFileId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RMarkInternationalReplacement implements Serializable {
    private static final long serialVersionUID = 1919436437680667116L;
    private RFileId fileId;
    private Integer registrationNumber;
    private String registrationDup;
    private String replacementFilingNumber;
    private Boolean isAllServices;
    private List<RNiceClass> replacementNiceClasses;
}
