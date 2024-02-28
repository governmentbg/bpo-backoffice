package bg.duosoft.ipas.core.model.search;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 11.05.2022
 * Time: 13:07
 */
@Data
public class OffidocNotificationSearchParam extends SearchPage implements Serializable {

    public static final String NOTIFICATION_FINISHED_DATE="NOTIFICATION_FINISHED_DATE";
    public static final String EMAIL_NOTIFICATION_READ_DATE="EMAIL_NOTIFICATION_READ_DATE";
    public static final String PORTAL_NOTIFICATION_READ_DATE="PORTAL_NOTIFICATION_READ_DATE";
    public static final String DAYS="DAYS";

    public OffidocNotificationSearchParam() {
        super(NOTIFICATION_FINISHED_DATE, Sortable.DESC_ORDER);
    }

    private String registrationNumber;

    private String objectType;

    private boolean showReadInPortal;
    private boolean showReadByEmail;
    private boolean showUnread;

    private boolean eFiled;
    private boolean nonEFiled;

    private Integer captureUserId;
    private boolean onlyMine;

    private Date dateFinishedFrom;
    private Date dateFinishedTo;

    private Date dateReadFrom;
    private Date dateReadTo;

    private Integer noOlderThanDays;

    public static OffidocNotificationSearchParam createDefaultSearchParam(){
        OffidocNotificationSearchParam searchParam = new OffidocNotificationSearchParam();
        searchParam.setShowReadByEmail(false);
        searchParam.setShowReadInPortal(false);
        searchParam.setShowUnread(true);
        searchParam.setEFiled(true);
        searchParam.setNonEFiled(true);
        searchParam.setOnlyMine(true);
        searchParam.setPageSize(Pageable.DEFAULT_PAGE_SIZE);
        searchParam.setPage(Pageable.DEFAULT_PAGE);
        return searchParam;
    }
}
