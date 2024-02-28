package bg.duosoft.ipas.rest.model.patent;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPatentAttachment implements Serializable {
	private Integer id;
	private RAttachmentType attachmentType;
	private String attachmentName;
	private boolean contentLoaded;
	private byte[] attachmentContent;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date dateCreated;
}

