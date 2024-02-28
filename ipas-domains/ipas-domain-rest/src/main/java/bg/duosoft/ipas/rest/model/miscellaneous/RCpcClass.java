package bg.duosoft.ipas.rest.model.miscellaneous;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RCpcClass implements Serializable {
    private static final long serialVersionUID = -3504947797746459548L;
    private String cpcEdition;
    private String cpcSection;
    private String cpcClass;
    private String cpcSubclass;
    private String cpcGroup;
    private String cpcSubgroup;
    private String cpcVersionCalculated;
    private String cpcName;
    private String symbol;
}
