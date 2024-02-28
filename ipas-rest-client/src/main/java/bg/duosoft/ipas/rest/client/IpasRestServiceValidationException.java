package bg.duosoft.ipas.rest.client;

import bg.duosoft.ipas.rest.custommodel.RestApiValidationError;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Georgi
 * Date: 15.7.2020 г.
 * Time: 15:53
 */
public class IpasRestServiceValidationException extends IpasRestServiceException {
    private List<ValidationErrorDetail> validationErrorDetails;

    public IpasRestServiceValidationException(RestApiValidationError error) {
        super(error);
        this.validationErrorDetails = error.getValidationErrors() == null ? null : error.getValidationErrors().stream().map(r -> new ValidationErrorDetail(r.getPointer(), r.getMessageCode(), r.getMessage(), r.getInvalidValue())).collect(Collectors.toList());
    }

    public List<ValidationErrorDetail> getValidationErrorDetails() {
        return validationErrorDetails;
    }

    public static class ValidationErrorDetail {
        private String pointer;
        private String messageCode;
        private String message;
        private Object invalidValue;

        public ValidationErrorDetail(String pointer, String messageCode, String message, Object invalidValue) {
            this.pointer = pointer;
            this.messageCode = messageCode;
            this.message = message;
            this.invalidValue = invalidValue;
        }

        public String getPointer() {
            return pointer;
        }

        public String getMessageCode() {
            return messageCode;
        }

        public String getMessage() {
            return message;
        }

        public Object getInvalidValue() {
            return invalidValue;
        }

        @Override
        public String toString() {
            return "ValidationErrorDetail{" +
                    "pointer='" + pointer + '\'' +
                    ", messageCode='" + messageCode + '\'' +
                    ", message='" + message + '\'' +
                    ", invalidValue=" + invalidValue +
                    '}';
        }
    }

    @Override
    public String toString() {
        String res = super.toString();
        if (!CollectionUtils.isEmpty(validationErrorDetails)) {
            res += "\nValidationErrors:";
            res += validationErrorDetails.stream().map(r -> r.toString()).collect(Collectors.joining("\n"));
        }
        return res;
    }
}
