package bg.duosoft.ipas.integration.dsclass.mapper;

import bg.duosoft.ipas.core.model.design.CProductTerm;
import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import bg.duosoft.ipas.integration.dsclass.model.MatchingResult;
import bg.duosoft.ipas.integration.dsclass.model.VerificationResponse;
import bg.duosoft.ipas.integration.dsclass.model.VerificationResult;
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
public abstract class VerificationResponseProductTermMapper {

    public CProductTerm verificationResponseToProductTerm(VerificationResponse[] response,
                                                          String originalTermText,
                                                          List<String> originalProductClasses){
        CProductTerm term = new CProductTerm();
        term.setTermText(originalTermText);
        term.setProductClasses(originalProductClasses);

        if(response != null && response.length > 0) {
            term.setTermStatus(verificationResultToProductTermStatus(response[0].getVerificationResult()));

            if(!response[0].getVerificationResult().equals(VerificationResult.OK)){
                term.setMatchedTerms(new ArrayList<>());
                for(VerificationResponse verification: response) {
                    if(verification.getMatchingResults() != null && verification.getMatchingResults().size()>0) {
                        term.getMatchedTerms().addAll(verification.getMatchingResults().stream().map(m -> (matchingResultToProductTerm(m))).collect(Collectors.toList()));
                    }
                }
            }
        }

        return term;
    }

    public String verificationResultToProductTermStatus(VerificationResult responseStatus){
        switch (responseStatus){
            case OK: return CommonTerm.VALID;
            case HINT: return CommonTerm.EDITABLE;
            case NOT_OK: return CommonTerm.INVALID;
            case NOT_FOUND:return CommonTerm.NOTFOUND;
            default: return CommonTerm.UNKNOWN;
        }
    }

    public CProductTerm matchingResultToProductTerm(MatchingResult matchingResult){
        CProductTerm term = new CProductTerm();
        term.setTermText(matchingResult.getTermText());
        term.setProductClasses(new ArrayList<>());
        term.getProductClasses().add(dsClassToIpasProductClass(matchingResult.getTermClass()));
        term.setTermStatus(CommonTerm.VALID);
        return term;
    }
}
