package bg.duosoft.ipas.rest.model.userdoc;

import bg.duosoft.ipas.rest.model.file.RProcessId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 28.04.2022
 * Time: 16:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RGetFileUserdocHierarchyFilteredResponse implements Serializable {

    private RUserdocRelationRestriction linkedUserdocRestriction;
    private List<RUserdocHierarchyNode> nodeList;
    private RProcessId mainProcessId;
}
