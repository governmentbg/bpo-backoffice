package bg.duosoft.ipas.test.service.report;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.report.ReportService;
import bg.duosoft.ipas.services.core.IpasReportService;
import bg.duosoft.ipas.services.core.IpasServiceException;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * User: Georgi
 * Date: 23.9.2020 г.
 * Time: 14:45
 */
public class ReportServiceTest extends TestBase {
    @Autowired
    private ReportService reportService;


    @Ignore//this method is only for test purposes. It should always be ignored
    @Test
    @Transactional
    public void testGetFolder() {
        reportService.getReportTemplateFileNames().stream().forEach(System.out::println);
    }

    @Ignore//this method is only for test purposes. It should always be ignored
    @Test
    @Transactional
    public void getnerateFileFromTemplate() throws IpasServiceException, IOException {
        byte[] res = reportService.generateDocument("C:\\ompi\\temp\\Reports\\списък марки.doc", Arrays.asList(new CFileId("BG", "N", 2013, 130109), new CFileId("BG", "N", 2013, 130110)), null, null, null, IpasReportService.CONTENT_TYPE_DOC);
        Files.write(Paths.get("d:/temp/spisak_marki.doc"), res);
    }
}
