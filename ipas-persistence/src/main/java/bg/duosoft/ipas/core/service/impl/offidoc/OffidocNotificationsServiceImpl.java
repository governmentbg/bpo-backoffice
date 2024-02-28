package bg.duosoft.ipas.core.service.impl.offidoc;

import bg.duosoft.ipas.core.model.search.OffidocNotificationSearchParam;
import bg.duosoft.ipas.core.service.offidoc.OffidocNotificationsService;
import bg.duosoft.ipas.persistence.model.nonentity.OffidocNotification;
import bg.duosoft.ipas.persistence.repository.nonentity.OffidocNotificationRepository;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 13.05.2022
 * Time: 16:42
 */
@Service
public class OffidocNotificationsServiceImpl implements OffidocNotificationsService {

    @Autowired
    private OffidocNotificationRepository offidocNotificationRepository;

    @Override
    public List<OffidocNotification> findOffidocNotifications(OffidocNotificationSearchParam searchParam) {
        processSearchParam(searchParam);
        return offidocNotificationRepository.findOffidocNotifications(searchParam);
    }

    @Override
    public Integer countOffidocNotifications(OffidocNotificationSearchParam searchParam) {
        processSearchParam(searchParam);
        return offidocNotificationRepository.countOffidocNotifications(searchParam);
    }

    private void processSearchParam(OffidocNotificationSearchParam searchParam){
        if(searchParam.isOnlyMine()){
            searchParam.setCaptureUserId(SecurityUtils.getLoggedUserId());
        } else {
            searchParam.setCaptureUserId(null);
        }
    }
}
