package bg.duosoft.ipas.rest.custommodel.action;

import bg.duosoft.ipas.rest.model.action.RActionId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: Georgi
 * Date: 12.9.2020 г.
 * Time: 10:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RDeleteActionRequest {
    private RActionId actionId;
    private String reason;
}
