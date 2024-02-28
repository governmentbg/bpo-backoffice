package bg.duosoft.ipas.rest.model.patent;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import bg.duosoft.ipas.rest.model.miscellaneous.RIpcClass;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPatentIpcClass extends RIpcClass implements Serializable {
	private String ipcSymbolPosition;
	private String ipcWpublishValidated;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date ipcSymbolCaptureDate;
	private String ipcQualification;
}

