package bg.duosoft.ipas.util.jasper;


import bg.duosoft.ipas.core.service.impl.jasper.RestClientServiceImpl;
import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportParam<T> {
    public final static String BASIC = "Basic ";
    public final static String REQUEST_DATA_TYPE = "json";
    private final static String REQUEST_ID = "request-id";
    private final static String EXPORT_ID = "export-id";
    private final static String J_COOKIE = "j_cookie";
    private final static String EXPORT_FILE_FORMAT = "export-file-format";
    private final static String DEFAULT_EXPORT_FILE_FORMAT = "xlsx";

    private String requestId;

    private String exportId;

    private String status;

    private List<? extends String> cookie = new ArrayList<>();

    private T requestBody;

    @JsonIgnoreProperties
    private String reportUrl;

    private String exportFileFormat;

    @JsonIgnoreProperties
    private Integer leadingZero;

    @JsonIgnoreProperties
    private String requestForValidationType;

    @JsonIgnoreProperties
    private String fileTypes;

    @JsonIgnoreProperties
    private String authorization;

    public HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        if (cookie == null || cookie.isEmpty()) {
            headers.add(HttpHeaders.AUTHORIZATION, BASIC + authorization);
        } else {
            headers.addAll(HttpHeaders.COOKIE, getCookie());
        }

        return headers;
    }

    public RequestParam getRequestParam() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(requestBody);

        RequestParam.Params.Param paramJson = new RequestParam.Params.Param();
        paramJson.setName(REQUEST_DATA_TYPE);
        paramJson.getValue().add(json);

        RequestParam.Params.Param paramLeadingZero = new RequestParam.Params.Param();
        paramLeadingZero.setName("leadingZero");
        paramLeadingZero.getValue().add(leadingZero);

        RequestParam.Params.Param paramRequestForValidationType = new RequestParam.Params.Param();
        paramRequestForValidationType.setName("requestForValidationType");
        paramRequestForValidationType.getValue().add(requestForValidationType);

        RequestParam.Params.Param paramFileTypes = new RequestParam.Params.Param();
        paramFileTypes.setName("fileTypes");
        paramFileTypes.getValue().add(fileTypes);

        RequestParam requestParam = new RequestParam();
        requestParam.setReportUnitUri(reportUrl);
        requestParam.setOutputFormat(exportFileFormat);
        requestParam.getParameters().getReportParameter().add(paramJson);
        requestParam.getParameters().getReportParameter().add(paramLeadingZero);
        requestParam.getParameters().getReportParameter().add(paramRequestForValidationType);
        requestParam.getParameters().getReportParameter().add(paramFileTypes);

        return requestParam;
    }

    public static ReportParam getReportParam(HttpServletRequest request) {
        String requestId = request.getParameter(REQUEST_ID);
        String exportId = request.getParameter(EXPORT_ID);
        String cookieStr = request.getParameter(J_COOKIE);
        String exportType = request.getParameter(EXPORT_FILE_FORMAT);

        List<String> cookieList = new ArrayList<>();
        cookieList.add(cookieStr);

        ReportParam reportParam = new ReportParam();
        reportParam.setRequestId(requestId);
        reportParam.setExportId(exportId);
        reportParam.setCookie(cookieList);
        if (!(Objects.isNull(exportId) || exportId.isBlank())) {
            reportParam.setExportFileFormat(exportType);
        }

        return reportParam;
    }
}
