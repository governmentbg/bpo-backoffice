package bg.duosoft.ipas.rest.model.miscellaneous;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RIpcClass implements Serializable {
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

