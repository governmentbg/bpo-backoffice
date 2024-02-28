package bg.duosoft.ipas.core.model.search;

import bg.duosoft.ipas.enums.SearchResultColumn;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.core.model.search.SearchPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class CCriteriaPerson extends SearchPage implements Serializable {
    private static final long serialVersionUID = -2865006527930116282L;

    public CCriteriaPerson() {
        super(SearchResultColumn.PERSON_NAME_SORT, Sortable.ASC_ORDER.toString());
    }

    private String personNbr;
    private String personName;
    private String personNameContainsWords;
    private String personNameWholeWords;
    private String personNameExactly;

    private String nationalityCountryCode;

    private String residenceCountryCode;

    private String email;
    private String emailContainsWords;

    private String telephoneContainsWords;

    private String city;
    private String cityContainsWords;

    private String street;
    private String streetContainsWords;

    private String zipCode;

    private String agentCode;
    private String agentCodeOrNameContainsWords;

    private boolean onlyAgent;
    private boolean onlyActiveAgent;
    private boolean onlyForeignCitizens;

    private boolean excludeOldVersions;
    private Boolean indCompany;

    private String individualIdText;
    private String individualIdType;
}
