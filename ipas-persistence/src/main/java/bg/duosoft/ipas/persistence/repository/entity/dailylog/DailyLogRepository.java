package bg.duosoft.ipas.persistence.repository.entity.dailylog;

import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDailyLog;
import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDailyLogPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * User: Georgi
 * Date: 27.5.2020 г.
 * Time: 17:57
 */
public interface DailyLogRepository extends BaseRepository<IpDailyLog, IpDailyLogPK> {
    @Query("select e from IpDailyLog e where e.indOpen = 'S' and (e.indClosed is null or e.indClosed = 'N') and e.pk.docOri = ?1")
    public Optional<IpDailyLog> getOpenedDailyLog(String docOri);
}
