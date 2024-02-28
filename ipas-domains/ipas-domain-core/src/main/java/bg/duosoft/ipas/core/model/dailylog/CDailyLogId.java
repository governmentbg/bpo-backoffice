package bg.duosoft.ipas.core.model.dailylog;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Georgi
 * Date: 28.5.2020 г.
 * Time: 12:02
 */
@Data
public class CDailyLogId implements Serializable {
    protected Date dailyLogDate;
    protected String docLog;
    protected String docOrigin;
}
