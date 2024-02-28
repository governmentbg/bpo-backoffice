package bg.duosoft.ipas.core.model.decisiondesktop;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 01.06.2021
 * Time: 16:19
 */
@Data
public class CDdCategorizationTags implements Serializable {

    private String fileType;
    private String userdocType;
    private String dossierType;
    private String categories;
    private String tags;
    private boolean fetchFromParent;

    public Set<String> getTagsSet(){
        if(tags != null && !tags.isEmpty()){
            String[] tagsArray = tags.split(",");
            return Arrays.stream(tagsArray).collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }

    public Set<String> getCategoriesSet(){
        if(categories != null && !categories.isEmpty()){
            String[] tagsArray = categories.split(",");
            return Arrays.stream(tagsArray).collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }



}
