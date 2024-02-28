package bg.duosoft.ipas.core.validation.config;

import java.util.List;
import java.util.stream.Collectors;

public class IpasValidationException extends RuntimeException {
    private List<ValidationError> errors;
    public IpasValidationException(List<ValidationError> errors) {
        super("Validation errors: " + errors.stream().map(r -> r.toString()).collect(Collectors.joining("\n")));
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "IpasValidationException{" +
                "errors=\n" + (errors == null ? "" : errors.stream().map(r -> r.toString()).collect(Collectors.joining("\n"))) +
                "\n}";
    }
}
