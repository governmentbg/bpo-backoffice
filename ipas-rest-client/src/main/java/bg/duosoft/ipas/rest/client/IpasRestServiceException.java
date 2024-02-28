package bg.duosoft.ipas.rest.client;

import bg.duosoft.ipas.rest.custommodel.RestApiError;

import java.util.Date;

/**
 * User: Georgi
 * Date: 15.7.2020 г.
 * Time: 15:53
 */
public class IpasRestServiceException extends RuntimeException {
    private String exception;
    private Date timestamp;
    private Integer status;

    public IpasRestServiceException(RestApiError error) {
        super(error.getMessage());
        this.exception = error.getException();
        this.timestamp = error.getTimestamp();
        this.status = error.getStatus();

    }

    public String getException() {
        return exception;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        String res = super.toString() + "\n";
        res += "Exception: " + exception + "\n";
        res += "Timestamp: " + timestamp + "\n";
        res += "Status: " + status;
        return res;
    }
}
