package bg.duosoft.ipas.exception;

public class AcceptUserdocException extends RuntimeException {

    public AcceptUserdocException() {
    }

    public AcceptUserdocException(String message) {
        super(message);
    }

    public AcceptUserdocException(String message, Throwable cause) {
        super(message, cause);
    }

    public AcceptUserdocException(Throwable cause) {
        super(cause);
    }

    public AcceptUserdocException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
