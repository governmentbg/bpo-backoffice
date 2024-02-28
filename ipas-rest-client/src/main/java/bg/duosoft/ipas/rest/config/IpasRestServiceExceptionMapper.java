package bg.duosoft.ipas.rest.config;

import bg.duosoft.ipas.rest.client.IpasRestServiceException;
import bg.duosoft.ipas.rest.client.IpasRestServiceValidationException;
import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;

import javax.ws.rs.core.Response;

/**
 * User: Georgi
 * Date: 18.7.2020 г.
 * Time: 23:33
 */
public class IpasRestServiceExceptionMapper implements ResponseExceptionMapper {
    @Override
    public Throwable fromResponse(Response r) {
        if (r.getEntity() != null && r.getEntity() instanceof IpasRestServiceException) {
            throw (IpasRestServiceException) r.getEntity();
        }
        return null;
    }
}
