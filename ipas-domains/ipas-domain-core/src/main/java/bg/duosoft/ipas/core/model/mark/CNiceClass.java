package bg.duosoft.ipas.core.model.mark;

import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class CNiceClass implements Serializable {
    private static final long serialVersionUID = -1135679795847076680L;
    private Integer niceClassEdition;
    private Integer niceClassNbr;
    private String niceClassGlobalStatus;
    private String niceClassDetailedStatus;
    private String niceClassDescription;
    private String niceClassDescriptionInOtherLang;
    private String niceClassVersion;
    private String niceEditionCalculated;
    private String niceVersionCalculated;
    private String niceClassInvalid;

    @EqualsAndHashCode.Exclude
    private List<CTerm> terms;

    private Boolean allTermsDeclaration;

    public void addTerms(Integer index, Collection<CTerm> termsToAdd){
        if (termsToAdd == null) {
            return;
        }
        if(this.getTerms() == null){
            this.setTerms(new ArrayList<>());
        }

        List termsFinalList = termsToAdd.stream().filter(t -> (t != null && t.getTermText() != null && !t.getTermText().trim().isEmpty())).collect(Collectors.toList());
        if(index == null || getTerms() == null || getTerms().size() == 0) {
            this.getTerms().addAll(termsFinalList);
        } else {
            this.getTerms().addAll(index.intValue(), termsFinalList);
        }

        generateDescriptionFromTerms();
    }

    public void removeTerm(Integer index) {
        if(index == null){
            return;
        }
        this.getTerms().remove(index.intValue());

        generateDescriptionFromTerms();
    }

    public void generateDescriptionFromTerms(){
        if(this.getTerms() == null || this.getTerms().size() == 0){
            setNiceClassDescription(null);
        } else {
            String description = this.getTerms().stream()
                    .map(CTerm::getTermText)
                    .collect(Collectors.joining(";"));

            setNiceClassDescription(description);
        }
    }

    public void generateInitialTermsFromDescription(String initialTermStatus){
        if(this.getNiceClassDescription() == null || this.getNiceClassDescription().trim().isEmpty()){
            setTerms(null);
        } else {
            String[] termsTexts = this.getNiceClassDescription().split(";");
            setTerms(new ArrayList<>());
            for (String termText : termsTexts) {
                if (!termText.trim().isEmpty()) {
                    CTerm cTerm = new CTerm();
                    cTerm.setClassNumber(this.getNiceClassNbr());
                    cTerm.setTermText(termText.trim());

                    if(getTerms().contains(cTerm)){
                        cTerm.setTermStatus(CommonTerm.DUPLICATE);
                    } else {
                        cTerm.setTermStatus(initialTermStatus);
                    }
                    getTerms().add(cTerm);
                }
            }
        }
    }

    public void duplicatedTermsIdentify(){
        if(getTerms() != null && getTerms().size() >0) {
            for(int i =0 ; i< getTerms().size(); i++){
                CTerm current = getTerms().get(i);
                if(current.getTermStatus().equals(CommonTerm.DUPLICATE)){
                    current.setTermStatus(CommonTerm.UNKNOWN);
                }
                if(i > 0) {
                    List<CTerm> subList = getTerms().subList(0, i);
                    if (subList.contains(getTerms().get(i))){
                        current.setTermStatus(CommonTerm.DUPLICATE);
                    }
                }
            }
        }
    }

    public void allTermsToLowercase(){
        if(terms != null){
            terms.stream().forEach(term -> term.setTermText(term.getTermText().toLowerCase()));
            generateDescriptionFromTerms();
        }
    }

    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public List<CTerm> getTerms() {
        return terms;
    }

}


