package bg.duosoft.ipas.core.model.mark;

import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CInternationalNiceClass implements Serializable {
    private static final long serialVersionUID = 4740003665918000732L;
    private String tagCode;
    private String tagDescription;
    private Long niceClassCode;
    private String niceClassDescription;
    @EqualsAndHashCode.Exclude
    private List<CTerm> terms;

    public void generateInitialTermsFromDescription(){
        if(this.getNiceClassDescription() == null || this.getNiceClassDescription().trim().isEmpty()){
            setTerms(null);
        } else {
            String[] termsTexts = this.getNiceClassDescription().split(";");
            setTerms(new ArrayList<>());
            for (String termText : termsTexts) {
                if (!termText.trim().isEmpty()) {
                    CTerm cTerm = new CTerm();
                    cTerm.setClassNumber(this.getNiceClassCode().intValue());
                    cTerm.setTermText(termText.trim());
                    getTerms().add(cTerm);
                }
            }
        }
    }

    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public List<CTerm> getTerms() {
        return terms;
    }

}
