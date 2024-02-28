package bg.duosoft.ipas.integration.dsclass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Raya
 * 21.04.2020
 */
@Getter
@Setter
public class TermSource {

    @JsonProperty("SourceId")
    private String sourceId;

    @JsonProperty("SourceName")
    private String sourceName;
}
