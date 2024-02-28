package bg.duosoft.ipas.core.model.decisiondesktop;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 27.04.2021
 * Time: 15:11
 */
@Data
public class CDecisionTemplate implements Serializable {

    private String id;
    private String name;
    private String status;
}
