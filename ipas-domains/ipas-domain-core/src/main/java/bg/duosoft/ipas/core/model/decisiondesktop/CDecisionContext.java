package bg.duosoft.ipas.core.model.decisiondesktop;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 27.04.2021
 * Time: 15:44
 */
@Data
public class CDecisionContext implements Serializable {

    private String id;
    private String templateId;
    private String templateName;
    private String creator;
    private String language;

}
