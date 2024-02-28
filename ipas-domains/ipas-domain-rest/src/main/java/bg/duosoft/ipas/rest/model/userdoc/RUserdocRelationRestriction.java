package bg.duosoft.ipas.rest.model.userdoc;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 28.04.2022
 * Time: 16:41
 */
public enum RUserdocRelationRestriction implements Serializable {

    OBJECT_ONLY, USERDOC_RELATION_OPTIONAL, USERDOC_RELATION_MANDATORY;
}
