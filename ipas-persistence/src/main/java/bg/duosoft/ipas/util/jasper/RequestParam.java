package bg.duosoft.ipas.util.jasper;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RequestParam<T> {
    private final static String OUTPUT_FORMAT = "xlsx";

    private String reportUnitUri;
    private Boolean async = true;
    private Boolean freshData = true;
    private Boolean saveDataSnapshot = false;
    private String outputFormat = OUTPUT_FORMAT;
    private Boolean interactive = false;
    private Boolean ignorePagination = true;
    private Params<T> parameters = new Params<T>();

    @Getter
    @Setter
    public static class Params<T> {
        private List<Param<T>> reportParameter = new ArrayList<>();

        @Getter
        @Setter
        public static class Param<T> {
            private String name;
            private List<T> value = new ArrayList<>();
        }
    }
       /* {
            "reportUnitUri": "/reports/search_ipos",
                "async": false,
                "freshData": true,
                "saveDataSnapshot": false,
                "outputFormat": "xlsx",
                "interactive": false,
                "ignorePagination": true,
                "parameters": {
            "reportParameter": [
            {
                "name": "json",
                    "value": ["{}"]
            }
        ]
        }
        }*/
}