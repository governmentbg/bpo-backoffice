package bg.duosoft.ipas.rest.custommodel.userdoc;


import bg.duosoft.ipas.rest.model.file.RFileId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RGetFileUserdocHierarchyFilteredRequest {
    private RFileId fileId;
    private Boolean flatHierarchy;
    private RUserdocHierarchyFilter filter;
}
