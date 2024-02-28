package bg.duosoft.ipas.integration.dsclass.mapper;

import bg.duosoft.ipas.core.model.design.CProductTerm;
import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import bg.duosoft.ipas.integration.dsclass.model.SearchTermsResult;
import bg.duosoft.ipas.integration.dsclass.model.TermDetails;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static bg.duosoft.ipas.integration.dsclass.mapper.ProductClassMapperUtil.dsClassToIpasProductClass;

/**
 * Created by Raya
 * 21.04.2020
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class SearchTermsResultProductTermsMapper {

    public List<CProductTerm> searchTermsResultToProductTerms(SearchTermsResult searchTermsResult){
        List<CProductTerm> productTerms = new ArrayList<>();

        if(searchTermsResult != null && searchTermsResult.getSearchResultList() != null &&
            searchTermsResult.getSearchResultList().size()>0){
            productTerms.addAll(searchTermsResult.getSearchResultList().stream().
                map(t -> termDetailsToProductTerm(t)).collect(Collectors.toList()));
        }

        return productTerms;
    }

    public CProductTerm termDetailsToProductTerm(TermDetails termDetails){
        CProductTerm productTerm = new CProductTerm();
        productTerm.setProductClasses(new ArrayList<>());
        productTerm.getProductClasses().add(dsClassToIpasProductClass(termDetails.getClassification()));
        productTerm.setTermText(termDetails.getTermText());
        productTerm.setTermStatus(CommonTerm.VALID);

        return productTerm;
    }
}
