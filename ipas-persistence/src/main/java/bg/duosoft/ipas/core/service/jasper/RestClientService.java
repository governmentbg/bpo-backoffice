package bg.duosoft.ipas.core.service.jasper;

import bg.duosoft.ipas.util.jasper.ReportParam;
import bg.duosoft.ipas.util.jasper.ReportStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;

public interface RestClientService {
    ReportParam runReport(ReportParam reportParam) throws JsonProcessingException;

    ReportStatus checkReport(ReportParam reportParam);

    byte[] getReport(ReportParam reportParam);
}
