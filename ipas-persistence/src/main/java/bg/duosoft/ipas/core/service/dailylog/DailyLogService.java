package bg.duosoft.ipas.core.service.dailylog;


import bg.duosoft.ipas.core.model.dailylog.CDailyLog;
import bg.duosoft.ipas.services.core.IpasServiceException;

import java.util.Date;

public interface DailyLogService {

    void changeWorkingDate(Date date) throws DailyLogServiceException;

    void performCloseDailyLogChecks() throws DailyLogServiceException;

    Date getWorkingDate();

    CDailyLog getOpenedDailyLog(String docOri);
}
