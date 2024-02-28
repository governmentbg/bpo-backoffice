package bg.duosoft.ipas.rest.model.userdoc.reviewers;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.rest.model.person.RUser;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocReviewer implements Serializable {
	private RUser user;
	private boolean main;
}

