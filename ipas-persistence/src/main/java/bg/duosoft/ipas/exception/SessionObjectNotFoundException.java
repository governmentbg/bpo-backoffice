package bg.duosoft.ipas.exception;

public class SessionObjectNotFoundException extends RuntimeException {

    public SessionObjectNotFoundException() {
        super();
    }

    public SessionObjectNotFoundException(String message) {
        super(message);
    }

    public SessionObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionObjectNotFoundException(Throwable cause) {
        super(cause);
    }

    protected SessionObjectNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
