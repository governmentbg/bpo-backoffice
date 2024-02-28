package bg.duosoft.ipas.integration.decisiondesktop.model.backoffice;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 06.01.2022
 * Time: 15:29
 */
@Data
public class DesignImageWrapperVariable {

    private String designId;
    private DesignImageVariable[] designImageVariables;
}
