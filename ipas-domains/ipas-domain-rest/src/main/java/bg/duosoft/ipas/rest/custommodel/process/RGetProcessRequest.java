package bg.duosoft.ipas.rest.custommodel.process;

import bg.duosoft.ipas.rest.model.file.RProcessId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: Georgi
 * Date: 16.7.2020 г.
 * Time: 23:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RGetProcessRequest {
    private RProcessId processId;
    private boolean addProcessEvents;
}
