package bg.duosoft.ipas.rest.model.patent;

import bg.duosoft.ipas.rest.model.miscellaneous.RCpcClass;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPatentCpcClass extends RCpcClass implements Serializable {
    private String cpcSymbolPosition;
    private String cpcWPublishValidated;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date cpcSymbolCaptureDate;
    private String cpcQualification;
}
