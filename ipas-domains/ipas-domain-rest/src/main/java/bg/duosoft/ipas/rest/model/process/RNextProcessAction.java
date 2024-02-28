package bg.duosoft.ipas.rest.model.process;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.enums.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RNextProcessAction implements Serializable {
	private String actionType;
	private String actionTypeName;
	private Integer automaticActionWcode;
	private String actionTypeGroup;
	private String actionTypeListCode;
	private Integer restrictLawCode;
	private String restrictFileTyp;
	private String restrictApplicationType;
	private String restrictApplicationSubType;
	private String userdocListCodeInclude;
	private String userdocListCodeExclude;
	private String statusCode;
	private String statusName;
	private NextProcessActionType processActionType;
	private Boolean containNotes;
	private Boolean containManualDueDate;
	private Boolean calculateTermFromActionDate;
	private String generatedOffidoc;
}

