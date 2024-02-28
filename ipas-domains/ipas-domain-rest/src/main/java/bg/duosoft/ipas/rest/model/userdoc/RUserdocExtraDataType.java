package bg.duosoft.ipas.rest.model.userdoc;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocExtraDataType implements Serializable {
	private String code;
	private String title;
	private String titleEn;
	private Boolean isText;
	private Boolean isNumber;
	private Boolean isDate;
	private Boolean isBoolean;
	private String booleanTextTrue;
	private String booleanTextFalse;
	private String booleanTextTrueEn;
	private String booleanTextFalseEn;
}

