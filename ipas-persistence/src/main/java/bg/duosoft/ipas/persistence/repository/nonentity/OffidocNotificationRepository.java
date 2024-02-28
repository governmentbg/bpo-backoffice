package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.core.model.search.OffidocNotificationSearchParam;
import bg.duosoft.ipas.persistence.model.nonentity.OffidocNotification;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 11.05.2022
 * Time: 13:50
 */
public interface OffidocNotificationRepository {

    List<OffidocNotification> findOffidocNotifications(OffidocNotificationSearchParam searchParam);
    Integer countOffidocNotifications(OffidocNotificationSearchParam searchParam);
}
