package bg.duosoft.ipas.integration.dsclass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Raya
 * 17.04.2020
 */
@Getter
@Setter
public class SearchTermsResult implements Serializable {

    @JsonProperty("TotalNumTerms")
    private Integer totalNumTerms;

    @JsonProperty("SearchTermResults")
    private List<TermDetails> searchResultList;

}
