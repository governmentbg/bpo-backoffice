package bg.duosoft.ipas.core.service.action;

import bg.duosoft.ipas.core.validation.config.ValidationError;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * User: Georgi
 * Date: 16.9.2020 г.
 * Time: 12:04
 */
public class InsertActionException extends Exception {
    public InsertActionException() {
    }


    public InsertActionException(String message) {
        super(message);
    }

    public InsertActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsertActionException(Throwable cause) {
        super(cause);
    }

    public InsertActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
