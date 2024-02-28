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
public class RRenewalData implements Serializable {
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date lastRenewalDate;
}

