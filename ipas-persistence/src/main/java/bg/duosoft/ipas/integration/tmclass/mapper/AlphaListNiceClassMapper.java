package bg.duosoft.ipas.integration.tmclass.mapper;

import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.mark.CTerm;
import bg.duosoft.ipas.integration.tmclass.alpha.model.ClassDescriptionType;
import bg.duosoft.ipas.integration.tmclass.alpha.model.GoodsServicesType;
import bg.duosoft.ipas.integration.tmclass.alpha.model.TransactionType;
import org.mapstruct.Mapper;

import java.util.ArrayList;

/**
 * Created by Raya
 * 13.04.2020
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class AlphaListNiceClassMapper {

    public CNiceClass transactionToCNiceClass(TransactionType transaction){
        CNiceClass cNiceClass = new CNiceClass();
        if(transaction != null && transaction.getTradeMarkTransactionBody() != null && transaction.getTradeMarkTransactionBody().size() >0  &&
            transaction.getTradeMarkTransactionBody().get(0) != null &&
            transaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails() != null &&
            transaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData() != null &&
            transaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData().getGoodsServicesDetails() != null &&
            transaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData().getGoodsServicesDetails().getGoodServices() != null &&
            transaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData().getGoodsServicesDetails().getGoodServices().size() > 0){

            GoodsServicesType gsType =transaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData()
                .getGoodsServicesDetails().getGoodServices().get(0);

            if(gsType.getClassDescriptionDetails() != null && gsType.getClassDescriptionDetails().getClassDescription() != null &&
                gsType.getClassDescriptionDetails().getClassDescription().size() >0){

                ClassDescriptionType classDescriptionType = gsType.getClassDescriptionDetails().getClassDescription().get(0);

                if(classDescriptionType.getGoodsServicesDescription() != null && classDescriptionType.getGoodsServicesDescription().size()>0){
                    cNiceClass.setNiceClassNbr(classDescriptionType.getClassNumber());
                    cNiceClass.setTerms(new ArrayList<>());
                    cNiceClass.setNiceClassDescription(classDescriptionType.getGoodsServicesDescription().get(0).getValue());
                    cNiceClass.generateInitialTermsFromDescription(CTerm.VALID);
                }
            }
        }

        return cNiceClass;
    }
}
