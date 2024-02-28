package bg.duosoft.ipas.core.model.logging;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 03.12.2021
 * Time: 14:00
 */
@Data
@AllArgsConstructor
public class CLogChangeDetail implements Serializable {
    private String key;
    private List<CLogChangeElement> before;
    private List<CLogChangeElement> after;
}
