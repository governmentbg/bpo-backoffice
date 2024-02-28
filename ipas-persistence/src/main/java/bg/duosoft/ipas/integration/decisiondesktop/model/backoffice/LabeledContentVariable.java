package bg.duosoft.ipas.integration.decisiondesktop.model.backoffice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 05.09.2022
 * Time: 11:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabeledContentVariable {

    private String label;
    private String content;
}
