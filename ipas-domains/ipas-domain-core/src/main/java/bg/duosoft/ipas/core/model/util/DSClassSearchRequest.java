package bg.duosoft.ipas.core.model.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Raya
 * 17.04.2020
 */
@Getter
@Setter
@EqualsAndHashCode
public class DSClassSearchRequest implements Serializable {

    private LocalizationProperties localizationProperties;
    private String searchText;
    private String searchClass;
    private String searchSubclass;
    private int numPage;
    private int numTerms;

    public DSClassSearchRequest(LocalizationProperties localizationProperties, int numPage, int numTerms){
        this.localizationProperties = localizationProperties;
        this.numPage = numPage;
        this.numTerms = numTerms;
    }
}
