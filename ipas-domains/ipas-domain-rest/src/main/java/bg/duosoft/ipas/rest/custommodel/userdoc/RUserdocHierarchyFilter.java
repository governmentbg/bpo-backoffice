package bg.duosoft.ipas.rest.custommodel.userdoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 26.04.2022
 * Time: 11:47
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RUserdocHierarchyFilter {

    private String linkedUserdocType;
    private String requestingUser;
}
