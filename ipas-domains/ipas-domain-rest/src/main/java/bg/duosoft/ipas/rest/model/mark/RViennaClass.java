package bg.duosoft.ipas.rest.model.mark;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RViennaClass implements Serializable {
	private String viennaVersion;
	private Long viennaCategory;
	private Long viennaDivision;
	private Long viennaSection;
	private String viennaDescription;
	private String vclWpublishValidated;
	private String viennaVersionCalculated;
}

