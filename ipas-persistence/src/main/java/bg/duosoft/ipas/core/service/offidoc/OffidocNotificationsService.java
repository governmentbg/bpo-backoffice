package bg.duosoft.ipas.core.service.offidoc;

import bg.duosoft.ipas.core.model.search.OffidocNotificationSearchParam;
import bg.duosoft.ipas.persistence.model.nonentity.OffidocNotification;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 13.05.2022
 * Time: 16:42
 */
public interface OffidocNotificationsService {

    List<OffidocNotification> findOffidocNotifications(OffidocNotificationSearchParam searchParam);
    Integer countOffidocNotifications(OffidocNotificationSearchParam searchParam);
}
