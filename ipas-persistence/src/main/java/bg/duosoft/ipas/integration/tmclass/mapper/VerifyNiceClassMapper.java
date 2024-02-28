package bg.duosoft.ipas.integration.tmclass.mapper;

import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.mark.CTerm;
import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import bg.duosoft.ipas.integration.tmclass.verify.model.*;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raya
 * 15.02.2019
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class VerifyNiceClassMapper {

    public CNiceClass transactionTypeToCNiceClass(TransactionType transaction){
        CNiceClass cNiceClass = new CNiceClass();

        if(transaction != null && transaction.getTransactionBody() != null && transaction.getTransactionBody().getTransactionContentDetails() != null &&
            transaction.getTransactionBody().getTransactionContentDetails().getTransactionData() != null &&
            transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getVerifyList() != null &&
            transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getVerifyList().getVerifyListResponse() != null &&
            transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getVerifyList().getVerifyListResponse().getGoodsServicesDetails() != null){

            GoodsServicesDetailsType gsDetails =  transaction.getTransactionBody().getTransactionContentDetails().getTransactionData().getVerifyList().
                getVerifyListResponse().getGoodsServicesDetails();

            if(gsDetails != null && gsDetails.getClassDescriptionDetails() != null){
                ClassDescriptionDetailsType classDetailsType = gsDetails.getClassDescriptionDetails();

                cNiceClass.setNiceClassNbr(classDetailsType.getClassNumber());
                cNiceClass.setNiceClassDescription(classDetailsType.getInputTermList());

                if(classDetailsType.getVerifiedTermsDetails() != null && classDetailsType.getVerifiedTermsDetails().getVerifiedTerm()!= null &&
                    classDetailsType.getVerifiedTermsDetails().getVerifiedTerm().size()>0){

                    cNiceClass.setTerms(new ArrayList<>());

                    List<VerifiedTermType> verifiedTerms = classDetailsType.getVerifiedTermsDetails().getVerifiedTerm();
                    for(VerifiedTermType verifiedTerm: verifiedTerms){
                        if(verifiedTerm.getText() == null || verifiedTerm.getText().trim().isBlank()){
                            continue;
                        }
                        CTerm cTerm = new CTerm();
                        cTerm.setTermText(verifiedTerm.getText());
                        cTerm.setClassNumber(cNiceClass.getNiceClassNbr());

                        if(cNiceClass.getTerms().contains(cTerm)){
                            cTerm.setTermStatus(CommonTerm.DUPLICATE);
                        } else if(verifiedTerm.getVerificationResult().equals(VerifiedTermResultType.OK)) {
                            cTerm.setTermStatus(CommonTerm.VALID);
                        } else {
                            cTerm.setTermStatus(verifiedTermAssessmentTypeToString(verifiedTerm.getVerificationAssessment()));
                            cTerm.setVerificationResult(verifiedTerm.getVerificationAssessment().value());

                            if(verifiedTerm.getMatchedTerms() != null && verifiedTerm.getMatchedTerms().getMatchedTerm() != null &&
                                verifiedTerm.getMatchedTerms().getMatchedTerm().size()>0) {
                                cTerm.setMatchedTerms(new ArrayList<>());

                                for(MatchedTermType matchedTermType: verifiedTerm.getMatchedTerms().getMatchedTerm()) {
                                    CTerm cMatchedTerm = new CTerm();
                                    cMatchedTerm.setTermText(matchedTermType.getMatchedTermText());
                                    cMatchedTerm.setClassNumber(matchedTermType.getMatchedClassNumber());
                                    cMatchedTerm.setTermStatus(CommonTerm.VALID);
                                    cTerm.getMatchedTerms().add(cMatchedTerm);
                                }
                            }
                        }

                        cNiceClass.getTerms().add(cTerm);
                    }
                }
            }
        }

        return cNiceClass;
    }

    public String verifiedTermAssessmentTypeToString(VerifiedTermAssessmentType verifiedTermAssessmentType){
        switch(verifiedTermAssessmentType){
            case NOT_FOUND:
                return CommonTerm.NOTFOUND;
            case REJECTION_TERM:
                return CommonTerm.INVALID;
            case CONTROLLED_TERM_FOUND:
                return CommonTerm.VALID;
            default: return CommonTerm.EDITABLE;
        }
    }
}
