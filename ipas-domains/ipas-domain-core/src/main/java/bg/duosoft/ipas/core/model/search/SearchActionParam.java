package bg.duosoft.ipas.core.model.search;

import java.util.Date;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 30.03.2021
 * Time: 11:26
 */
public interface SearchActionParam {
    List<String> getActionTypes();

    Date getFromActionDate();

    Date getToActionDate();

    Integer getActionResponsibleUserId();

    Integer getActionCaptureUserId();
}
