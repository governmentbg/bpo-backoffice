package bg.duosoft.ipas.rest.model.process;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RProcessInsertActionRequest implements Serializable {
	private RProcessId processId;
	private String actionType;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date actionDate;
	private String notes1;
	private String notes2;
	private String notes3;
	private String notes4;
	private String notes5;
	private String notes;
	private Integer responsibleUser;
	private String specialFinalStatus;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date manualDueDate;
	private Integer captureUser;
	private Integer certificateReference;
	private List<String> offidocTemplates;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date recordalDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date invalidationDate;
	public List<String> getOffidocTemplates() {
		if (offidocTemplates == null) {
			offidocTemplates = new ArrayList<>();
		}
		return offidocTemplates;
	}
}

