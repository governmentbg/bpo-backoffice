package bg.duosoft.ipas.core.model.design;

import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by Raya
 * 17.04.2020
 */
@Getter
@Setter
public class CProductTerm implements CommonTerm, Serializable {

    private List<String> productClasses;
    private String termText;
    private String termStatus;

    List<CProductTerm> matchedTerms;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CProductTerm)) return false;
        CProductTerm that = (CProductTerm) o;
        return productClasses.equals(that.productClasses) &&
            termText.equals(that.termText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productClasses, termText);
    }

    public String getMatchedTermsString(){
        if(matchedTerms == null || matchedTerms.size()==0){
            return "";
        }

        StringBuilder termStrBuilder = new StringBuilder();
        matchedTerms.forEach(t -> {
            termStrBuilder.append(t.getTermText()).
                append(" (").append(t.getProductClasses().stream().reduce((left, right) -> left+", "+right).orElse("-")).append("); ");
        });
        return termStrBuilder.toString();
    }

    public String getProductClassesText(){
        StringBuilder classBuilder = new StringBuilder();
        if(productClasses != null){
            productClasses.stream().forEach(t -> classBuilder.append(t).append(", "));
        }
        if(classBuilder.length() > 2){
            classBuilder.delete(classBuilder.length()-2, classBuilder.length());
        }
        return classBuilder.toString();
    }
}
