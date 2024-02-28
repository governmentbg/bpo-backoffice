package bg.duosoft.ipas.core.model.dailylog;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Georgi
 * Date: 28.5.2020 г.
 * Time: 11:58
 */
@Data
public class CDailyLog implements Serializable {
    protected Date affectedFilesReadyDate;
    protected Date certificationReadyDate;
    protected CDailyLogId dailyLogId;
    protected Date digitalizationReadyDate;
    protected Date fileCaptureReadyDate;
    protected Integer firstDocNbr;
    protected Boolean indClosed;
    protected Boolean indOpen;
    protected Integer lastDocNbr;
    protected Date logoCaptureReadyDate;
    protected Date viennaCodesReadyDate;
    protected Date archivingDate;
    protected Date userdocCaptureReadyDate;
}
