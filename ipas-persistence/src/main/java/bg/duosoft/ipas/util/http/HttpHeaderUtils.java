package bg.duosoft.ipas.util.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpHeaderUtils {

    public static void setJsonContentType(HttpHeaders headers) {
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
    }

    public static void setBearerAuthorization(HttpHeaders headers, String token) {
        final String BEARER_PREFIX = "Bearer ";
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token);
    }

    public static void setMultipartFormDataContentType(HttpHeaders headers) {
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    }

}
