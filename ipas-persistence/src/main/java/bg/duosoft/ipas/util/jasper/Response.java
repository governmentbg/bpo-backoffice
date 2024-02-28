package bg.duosoft.ipas.util.jasper;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Response {
    private String status;
    private String requestId;
    private List<Export> exports = new ArrayList<>();

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Export {
        private String id;
    }
        /*{
            "status": "ready",
                "totalPages": 1,
                "requestId": "d90645f3-98c1-4a1f-9110-f9c5e934760e",
                "reportURI": "/reports/search_ipos",
                "exports": [
            {
                "id": "cd781f35-ca45-434f-8914-517ceb947e20",
                    "status": "ready",
                    "outputResource": {
                "contentType": "application/xls",
                        "fileName": "search_ipos.xls",
                        "outputFinal": true
            }
            }
    ]
        }*/
}