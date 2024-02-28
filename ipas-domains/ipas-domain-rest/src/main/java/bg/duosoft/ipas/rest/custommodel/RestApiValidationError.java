package bg.duosoft.ipas.rest.custommodel;

import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * User: Georgi
 * Date: 15.9.2020 г.
 * Time: 14:08
 */
@Data
@NoArgsConstructor
public class RestApiValidationError extends RestApiError {

    private List<ValidationErrorDetail> validationErrors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationErrorDetail {
        private String pointer;
        private String messageCode;
        private String message;
        private Object invalidValue;
    }

    public RestApiValidationError(String message, String exception, Date timestamp, Integer status, List<ValidationErrorDetail> validationErrors) {
        super(message, exception, timestamp, status);
        this.validationErrors = validationErrors;
    }
}
