package bg.duosoft.ipas.exception;

public class MarkDivisionException extends RuntimeException {

    public MarkDivisionException() {
        super();
    }

    public MarkDivisionException(String message) {
        super(message);
    }

    public MarkDivisionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarkDivisionException(Throwable cause) {
        super(cause);
    }

    protected MarkDivisionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
