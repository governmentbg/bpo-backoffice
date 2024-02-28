package bg.duosoft.ipas.core.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CExtraData implements Serializable {
    private Boolean dataFlag1;
    private Boolean dataFlag2;
    private Boolean dataFlag3;
    private Boolean dataFlag4;
    private Boolean dataFlag5;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date dataDate1;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date dataDate2;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date dataDate3;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date dataDate4;
    @JsonFormat(pattern = "dd.MM.yyyy")
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


