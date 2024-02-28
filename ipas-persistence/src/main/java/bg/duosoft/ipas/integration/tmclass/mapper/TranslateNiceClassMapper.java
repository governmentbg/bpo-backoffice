package bg.duosoft.ipas.integration.tmclass.mapper;

import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.mark.CTerm;
import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import bg.duosoft.ipas.integration.tmclass.translate.model.*;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 04.03.2022
 * Time: 10:54
 */
@Mapper(componentModel = "spring", uses = {})
public class TranslateNiceClassMapper {

    public CNiceClass transactionTypeToCNiceClass(TransactionType transaction){
        CNiceClass niceClass = new CNiceClass();
        niceClass.setTerms(new ArrayList<>());
        if(transaction != null &&
                transaction.getTransactionBody() != null && transaction.getTransactionBody().getTransactionContentDetails() != null &&
                transaction.getTransactionBody().getTransactionContentDetails().getTransactionData() != null &&
                transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getTermTranslation() != null) {
            if(transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getTermTranslation().getTermTranslationRequest() != null &&
                    transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getTermTranslation().getTermTranslationRequest().getClassNumber() != null){
                niceClass.setNiceClassNbr(transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getTermTranslation().getTermTranslationRequest().getClassNumber());
            }

            if(transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getTermTranslation().getTermTranslationResponseDetails() != null &&
                    transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getTermTranslation().getTermTranslationResponseDetails().getTranslatedTerm() != null){
                List<TranslatedTermType> translatedTermList = transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getTermTranslation().getTermTranslationResponseDetails().getTranslatedTerm();
                translatedTermList.stream().forEach(translation -> {
                    if (translation.getTranslationResult().equals(TranslationResultType.OK) && translation.getMatchedTerms() != null &&
                            translation.getMatchedTerms().getMatchedTerm() != null && translation.getMatchedTerms().getMatchedTerm().size() > 0) {
                        MatchedTermType matched = translation.getMatchedTerms().getMatchedTerm().get(0);
                        CTerm term = new CTerm();
                        term.setTermText(matched.getTargetTerm());
                        niceClass.getTerms().add(term);
                    } else {
                        CTerm term = new CTerm();
                        term.setTermStatus(CommonTerm.NOTFOUND);
                        term.setTermText(translation.getMatchingTerm().getValue());
                        niceClass.getTerms().add(term);
                    }
                });
            }
        }
        return niceClass;
    }
}
