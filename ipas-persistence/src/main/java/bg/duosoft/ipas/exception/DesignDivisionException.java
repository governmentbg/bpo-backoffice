package bg.duosoft.ipas.exception;

public class DesignDivisionException extends RuntimeException {

    public DesignDivisionException() {
        super();
    }

    public DesignDivisionException(String message) {
        super(message);
    }

    public DesignDivisionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DesignDivisionException(Throwable cause) {
        super(cause);
    }

    protected DesignDivisionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
