package bg.duosoft.ipas.integration.tmview.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TMViewAutocompleteResult {
    @JsonProperty("ST13")
    private String euLevelId;
    @JsonProperty("TradeMarkName")
    private String trademarkName;
    @JsonProperty("RegistrationNumber")
    private String registrationNumber;
}
