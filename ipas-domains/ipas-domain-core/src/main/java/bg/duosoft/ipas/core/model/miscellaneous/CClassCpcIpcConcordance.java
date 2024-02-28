package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CClassCpcIpcConcordance implements Serializable {
    private String cpcSectionCode;
    private String cpcClassCode;
    private String cpcSubclassCode;
    private String cpcGroupCode;
    private String cpcSubgroupCode;
    private String cpcSymbol;
    private String ipcSectionCode;
    private String ipcClassCode;
    private String ipcSubclassCode;
    private String ipcGroupCode;
    private String ipcSubgroupCode;
    private String ipcSymbol;
    private String latestVersion;
}
