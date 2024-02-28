package bg.duosoft.ipas.core.service.impl.jasper;

import bg.duosoft.ipas.core.service.jasper.RestClientService;
import bg.duosoft.ipas.util.jasper.ReportParam;
import bg.duosoft.ipas.util.jasper.ReportStatus;
import bg.duosoft.ipas.util.jasper.RequestParam;
import bg.duosoft.ipas.util.jasper.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class RestClientServiceImpl implements RestClientService {
    @Value("${jasper.rest.username}")
    private String jasperUsername;

    @Value("${jasper.rest.password}")
    private String jasperPassword;

    @Value("${jasper.rest.url}")
    private String restUrl;

    private final static String REPORT_EXECUTION_URL = "/rest_v2/reportExecutions";
    private final static String CHECK_REPORT_URL = "/rest_v2/reportExecutions/{requestId}/status";
    private final static String DOWNLOAD_REPORT_URL =
            "/rest_v2/reportExecutions/{requestId}/exports/{exportId}/outputResource";

    public ReportParam runReport(ReportParam reportParam) throws RestClientException, JsonProcessingException {
        RestTemplate restTemplate = getRestTemplate();
        String basicAuth = new String(
                Base64.encodeBase64(
                        (jasperUsername + ":" + jasperPassword).getBytes()
                )
        );

        reportParam.setAuthorization(basicAuth);

        HttpHeaders header = reportParam.getHeader();

        RequestParam req = reportParam.getRequestParam();

        HttpEntity httpEntity = new HttpEntity(req, header);

        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                restUrl + REPORT_EXECUTION_URL,
                HttpMethod.POST,
                httpEntity,
                Response.class);

        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            List<String> cookie = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);
            String requestId = responseEntity.getBody().getRequestId();
            String exportId = "";

            // TODO
            List<Response.Export> exports = responseEntity.getBody().getExports();
            if (exports.size() > 0) {
                exportId = exports.get(0).getId();
            }

            reportParam.setRequestId(requestId);
            reportParam.setExportId(exportId);
            reportParam.setCookie(cookie);
        }
        return reportParam;
    }

    public ReportStatus checkReport(ReportParam reportParam) throws RestClientException {

        RestTemplate restTemplate = getRestTemplate();

        // set header
        HttpHeaders header = reportParam.getHeader();

        HttpEntity httpEntity = new HttpEntity(header);
        ResponseEntity<ReportStatus> res = restTemplate.exchange(
                restUrl + CHECK_REPORT_URL,
                HttpMethod.GET,
                httpEntity,
                ReportStatus.class,
                reportParam.getRequestId(),
                reportParam.getExportId());

        if (res.getStatusCodeValue() == HttpStatus.OK.value()) {
            return res.getBody();
        }
        return null;
    }

    public byte[] getReport(ReportParam reportParam) throws RestClientException {

        RestTemplate restTemplate = getRestTemplate();

        // set header
        HttpHeaders headers = reportParam.getHeader();

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<byte[]> res = restTemplate.exchange(
                restUrl + DOWNLOAD_REPORT_URL,
                HttpMethod.GET,
                httpEntity,
                byte[].class,
                reportParam.getRequestId(),
                reportParam.getExportId());

        if (res.getStatusCodeValue() == HttpStatus.OK.value()) {
            byte[] bytes = res.getBody();
            return bytes;
        }
        return null;
    }

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate;
    }
}