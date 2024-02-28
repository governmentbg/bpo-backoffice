package bg.duosoft.ipas.rest.model.document;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RExtraData implements Serializable {
	private Boolean dataFlag1;
	private Boolean dataFlag2;
	private Boolean dataFlag3;
	private Boolean dataFlag4;
	private Boolean dataFlag5;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date dataDate1;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date dataDate2;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date dataDate3;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date dataDate4;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date dataDate5;
	private Integer dataNbr1;
	private Integer dataNbr2;
	private Integer dataNbr3;
	private Integer dataNbr4;
	private Integer dataNbr5;
	private String dataText1;
	private String dataText2;
	private String dataText3;
	private String dataText4;
	private String dataText5;
	private String dataCodeTyp1;
	private String dataCodeTyp2;
	private String dataCodeTyp3;
	private String dataCodeTyp4;
	private String dataCodeTyp5;
	private String dataCodeId1;
	private String dataCodeId2;
	private String dataCodeId3;
	private String dataCodeId4;
	private String dataCodeId5;
}

