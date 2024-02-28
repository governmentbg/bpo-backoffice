package bg.duosoft.ipas.integration.tmclass.mapper;

import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import bg.duosoft.ipas.integration.tmclass.heading.model.ClassDescriptionType;
import bg.duosoft.ipas.integration.tmclass.heading.model.TransactionDataType;
import bg.duosoft.ipas.integration.tmclass.heading.model.TransactionType;
import org.mapstruct.Mapper;

import java.util.ArrayList;


/**
 * Created by Raya
 * 15.02.2019
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class HeadingNiceClassMapper {

    public CNiceClass transactionToCNiceClass(TransactionType transaction){
        CNiceClass cNiceClass = new CNiceClass();
        if (transaction != null && transaction.getTradeMarkTransactionBody() != null && transaction.getTradeMarkTransactionBody().size() > 0 &&
                transaction.getTradeMarkTransactionBody().get(0) != null &&
                transaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails() != null &&
                transaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData() != null &&
                transaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData().getGoodsServicesDetails() != null){
            TransactionDataType.GoodsServicesDetails gsDetails = transaction.getTradeMarkTransactionBody().get(0).
                getTransactionContentDetails().getTransactionData().getGoodsServicesDetails();

            if( gsDetails.getGoodServices().size()>0 &&
                gsDetails.getGoodServices().get(0) != null &&
                gsDetails.getGoodServices().get(0).getClassDescriptionDetails() != null &&
                gsDetails.getGoodServices().get(0).getClassDescriptionDetails().getClassDescription() != null &&
                gsDetails.getGoodServices().get(0).getClassDescriptionDetails().getClassDescription().size() >0
                   ){
                ClassDescriptionType classDescrType = gsDetails.getGoodServices().get(0).getClassDescriptionDetails().getClassDescription().get(0);

                if(classDescrType != null){
                    if(classDescrType.getGoodsServicesDescription() != null && classDescrType.getGoodsServicesDescription().size() >0) {
                        cNiceClass.setNiceClassNbr(classDescrType.getClassNumber());
                        cNiceClass.setTerms(new ArrayList<>());
                        cNiceClass.setNiceClassDescription(classDescrType.getGoodsServicesDescription().get(0).getValue());

                        cNiceClass.generateInitialTermsFromDescription(CommonTerm.VALID);
                    }
                }
            }
        }

        return cNiceClass;
    }
}
