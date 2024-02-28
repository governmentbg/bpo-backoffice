package bg.duosoft.ipas.rest.model.offidoc;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ROffidocAbdocsDocument implements Serializable {
	private ROffidocId offidocId;
	private String acstreDocumentId;
	private Integer abdocsDocumentId;
	private String registrationNumber;
	private String closestMainParentObjectId;
	private String closestMainParentObjectType;
	private String directParentObjectId;
	private String directParentObjectType;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date notificationFinishedDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date emailNotificationReadDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date portalNotificationReadDate;
}

