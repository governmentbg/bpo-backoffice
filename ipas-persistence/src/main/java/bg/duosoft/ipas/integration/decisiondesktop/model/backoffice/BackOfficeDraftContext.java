package bg.duosoft.ipas.integration.decisiondesktop.model.backoffice;

import lombok.*;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 19.04.2021
 * Time: 16:51
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackOfficeDraftContext {

    private String dossierId;

    private Map<String, Object> templateVariables;
}
