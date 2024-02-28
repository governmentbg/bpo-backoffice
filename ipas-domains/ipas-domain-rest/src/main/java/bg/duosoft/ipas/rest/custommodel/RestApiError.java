package bg.duosoft.ipas.rest.custommodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestApiError {
    private String message;
    private String exception;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")// UTC
    private Date timestamp;
    private Integer status;
}