package bg.duosoft.ipas.integration.decisiondesktop.service;

import bg.duosoft.ipas.core.model.decisiondesktop.CDecisionTemplate;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 22.04.2021
 * Time: 15:15
 */
public interface AdmintoolService {

    List<CDecisionTemplate> getTemplates(String username, COffidoc offidoc);
}
