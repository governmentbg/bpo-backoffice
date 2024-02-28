package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CIpcClass implements Serializable {
    private static final long serialVersionUID = -3504947797746459548L;
    private String ipcEdition;
    private String ipcSection;
    private String ipcClass;
    private String ipcSubclass;
    private String ipcGroup;
    private String ipcSubgroup;
    private String ipcVersionCalculated;
    private String ipcEditionOriginal;
    private String ipcSymbolDescription;
}
