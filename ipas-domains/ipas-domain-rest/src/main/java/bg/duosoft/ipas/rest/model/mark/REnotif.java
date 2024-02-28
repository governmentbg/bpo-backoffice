package bg.duosoft.ipas.rest.model.mark;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class REnotif implements Serializable {
	private String gazno;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date notDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date pubDate;
	private String weekno;
	private String notLang;
	private Integer paidCount;
	private Integer licenceNewNameCount;
	private Integer licenceBirthCount;
	private Integer createdCount;
	private Integer processedCount;
	private Integer correctionCount;
	private Integer prolongCount;
	private Integer newBaseCount;
	private Integer restrictCount;
	private Integer newNameCount;
	private Integer deathCount;
	private Integer birthCount;
}

