package bg.duosoft.ipas.persistence.model;

import bg.duosoft.ipas.util.security.SecurityUtils;
import org.hibernate.envers.RevisionListener;

/**
 * User: Georgi
 * Date: 25.10.2020 г.
 * Time: 22:03
 */
public class IpasRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object o) {
        IpasRevisionEntity e = (IpasRevisionEntity) o;
        e.setUserId(SecurityUtils.getLoggedUserId());
    }
}
