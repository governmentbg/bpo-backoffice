package bg.duosoft.ipas.rest.model.action;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RActionType implements Serializable {
	private String actionType;
	private String actionName;
	private String listCode;
	private String listName;
	private String notes1Prompt;
	private String notes2Prompt;
	private String notes3Prompt;
	private String notes4Prompt;
	private String notes5Prompt;
	private Boolean indManualDueDateRequired;
	private String restrictApplicationType;
	private String restrictApplicationSubtype;
	private Integer restrictLawCode;
	private String restrictFileType;
	private Boolean indAcceptReferencedPriority;
	private Boolean indRejectReferencedPriority;
	private String chk1Prompt;
	private String chk2Prompt;
	private String chk3Prompt;
	private String chk4Prompt;
	private String chk5Prompt;
	private String listCode2;
	private String listCode3;
	private String listName2;
	private String listName3;
	private Long journalPublicationWcode;
	private Long automaticActionWcode;
}

