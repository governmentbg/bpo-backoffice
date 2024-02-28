package bg.duosoft.ipas.integration.dsclass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Raya
 * 17.04.2020
 */
@Getter
@Setter
public class TermDetails {

    @JsonProperty("Identifier")
    private String identifier;

    @JsonProperty("Classification")
    private String classification;

    @JsonProperty("TermAccepted")
    private boolean termAccepted;

    @JsonProperty("TermText")
    private String termText;

    @JsonProperty("TermDetails")
    private List<TermSource> termSources;

}
