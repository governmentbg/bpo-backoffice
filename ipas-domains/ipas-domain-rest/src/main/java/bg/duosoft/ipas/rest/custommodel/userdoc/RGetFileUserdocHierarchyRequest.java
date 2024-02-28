package bg.duosoft.ipas.rest.custommodel.userdoc;


import bg.duosoft.ipas.rest.model.file.RFileId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User: ggeorgiev
 * Date: 28.01.2021
 * Time: 15:34
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RGetFileUserdocHierarchyRequest {
    private RFileId fileId;
    private Boolean flatHierarchy;
}
