package bg.duosoft.ipas.rest.model.document;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.userdoc.RUserdocType;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RDocument implements Serializable {
	private Integer rowVersion;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date filingDate;
	private String receptionWcode;
	private RDocumentId documentId;
	private Long externalOfficeCode;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date externalOfficeFilingDate;
	private String externalSystemId;
	private Boolean indNotAllFilesCapturedYet;
	private String notes;
	private String applSubtyp;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date receptionDate;
	private RDocumentSeqId documentSeqId;
	private Integer receptionUserId;
	private Boolean indFaxReception;
	private RExtraData extraData;
	private RFileId fileId;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date dailyLogDate;
	private String appType;
	private boolean isUserdoc;
	private RUserdocType cUserdocType;
}

