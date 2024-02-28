package bg.duosoft.ipas.core.service.dd;

import bg.duosoft.ipas.core.model.decisiondesktop.CDdCategorizationTags;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 01.06.2021
 * Time: 16:18
 */
public interface DdCategorizationTagsService {

    CDdCategorizationTags getCategorizationTags(String fileType, String userdocType);
}
