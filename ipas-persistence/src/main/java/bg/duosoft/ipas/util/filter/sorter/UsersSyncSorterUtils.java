package bg.duosoft.ipas.util.filter.sorter;

import bg.duosoft.ipas.integration.portal.model.core.CPortalUser;
import bg.duosoft.ipas.integration.portal.utils.PortalUserUtils;
import bg.duosoft.ipas.util.filter.UsersSyncFilter;
import bg.duosoft.ipas.core.model.search.Sortable;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class UsersSyncSorterUtils {

    public static final String SCREEN_NAME = "screenName";
    public static final String FULL_NAME = "fullName";
    public static final String SYNC_STATUS = "syncStatus";
    public static final String EMAIL = "email";

    public static void sort(List<CPortalUser> users, UsersSyncFilter filter) {
        String sortColumn = filter.getSortColumn();
        if (!StringUtils.isEmpty(sortColumn)) {
            String sortOrder = filter.getSortOrder();
            Comparator<CPortalUser> comparator = null;
            switch (sortColumn) {
                case SCREEN_NAME:
                    comparator = Comparator.comparing(CPortalUser::getScreenName);
                    break;
                case FULL_NAME:
                    comparator = Comparator.comparing(PortalUserUtils::createFullName);
                    break;
                case SYNC_STATUS:
                    comparator = Comparator.comparing(CPortalUser::getSyncStatus);
                    break;
                case EMAIL:
                    comparator = Comparator.comparing(CPortalUser::getEmailAddress);
                    break;
            }
            if (Objects.nonNull(comparator)) {
                users.sort(sortOrder.equalsIgnoreCase(Sortable.ASC_ORDER) ? comparator : comparator.reversed());
            }
        }
    }

}
