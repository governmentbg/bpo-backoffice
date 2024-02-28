package bg.duosoft.ipas.core.model.mark;

import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by Raya
 * 14.02.2019
 */
@Getter
@Setter
public class CTerm implements CommonTerm, Serializable {

    private Integer classNumber;
    private String termText;
    private String termStatus;
    private String verificationResult;

    private List<CTerm> matchedTerms;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CTerm cTerm = (CTerm) o;
        if((termText == null && cTerm.getTermText() != null) ||
            (termText != null && cTerm.getTermText() == null)){
            return false;
        }
        return Objects.equals(classNumber, cTerm.classNumber) && ((termText == null && cTerm.getTermText() == null) ||
            termText.trim().equalsIgnoreCase(cTerm.getTermText().trim()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(classNumber, termText != null? termText.toLowerCase().trim(): termText);
    }

    public String getMatchedTermsString(){
        if(matchedTerms == null || matchedTerms.size()==0){
            return "";
        }

        StringBuilder termStrBuilder = new StringBuilder();
        matchedTerms.forEach(t -> {
            termStrBuilder.append(t.getTermText()).append(" (").append(t.getClassNumber()).append("); ");
        });
        return termStrBuilder.toString();
    }
}
