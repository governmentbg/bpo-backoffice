package bg.duosoft.ipas.rest.model.file;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.document.RDocument;
import bg.duosoft.ipas.rest.model.userdoc.RUserdocType;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RFilingData implements Serializable {
	private String applicationType;
	private String applicationSubtype;
	private Integer lawCode;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date filingDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date receptionDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date captureDate;
	private Long captureUserId;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date validationDate;
	private Long validationUserId;
	private String externalOfficeCode;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date externalOfficeFilingDate;
	private String externalSystemId;
	private Boolean indManualInterpretationRequired;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date novelty1Date;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date novelty2Date;
	private Long receptionUserId;
	private String corrFileType;
	private Long corrFileSeries;
	private String corrFileSeq;
	private Long corrFileNbr;
	private String indIncorrRecpDeleted;
	private RDocument receptionDocument;
	private List<RUserdocType> userdocTypeList;
	public List<RUserdocType> getUserdocTypeList() {
		if (userdocTypeList == null) {
			userdocTypeList = new ArrayList<>();
		}
		return userdocTypeList;
	}
}

