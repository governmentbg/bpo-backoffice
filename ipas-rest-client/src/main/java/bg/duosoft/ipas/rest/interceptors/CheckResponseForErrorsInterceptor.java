package bg.duosoft.ipas.rest.interceptors;

import bg.duosoft.ipas.rest.client.IpasRestServiceException;
import bg.duosoft.ipas.rest.client.IpasRestServiceValidationException;
import bg.duosoft.ipas.rest.custommodel.RestApiError;
import bg.duosoft.ipas.rest.custommodel.RestApiValidationError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * User: Georgi
 * Date: 15.7.2020 г.
 * Time: 12:45
 */
public class CheckResponseForErrorsInterceptor extends AbstractPhaseInterceptor<Message> {
    public CheckResponseForErrorsInterceptor() {
        super(Phase.POST_LOGICAL);
    }
    private static class JsonParseException extends Exception {
        public JsonParseException(Throwable cause) {
            super(cause);
        }
    }
    private static <C> C readJson(String json, Class<C> clazz) throws JsonParseException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
        if (!isErrorResponse(responseCode)) {
            return;
        }
        InputStream is = message.getContent(InputStream.class);
        //Stranno zashto ne mojah da procheta content-a s message.getContent(List.class) - bi trqbvalo da go ima celiq RestApiResponse objkect... Sega parse-vam response-a i syzdavam tozi object ot nego, koeto na teoriq e
        //resursoemka zadacha i se pravi 2 pyti...
        try {
            if (is != null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                IOUtils.copy(is, os);
                os.flush();
                is.close();
                message.setContent(InputStream.class, new ByteArrayInputStream(os.toByteArray()));

                if (responseCode == 422) {//unprocessable entity
                    RestApiValidationError r = readJson(os.toString(), RestApiValidationError.class);
                    //if the response's  status in not success, a bad request response is getting generated. It's getting processed by IpasRestServiceExceptionMapper, which rethrows the IpasRestServiceException, generated here!
                    Response response = Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(new IpasRestServiceValidationException(r))
                            .build();
                    message.getExchange().put(Response.class, response);

                } else if (responseCode == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()){
                    RestApiError r = readJson(os.toString(), RestApiError.class);
                    if (r != null) {
                        //if the response's  status in not success, a bad request response is getting generated. It's getting processed by IpasRestServiceExceptionMapper, which rethrows the IpasRestServiceException, generated here!
                        Response response = Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new IpasRestServiceException(r))
                                .build();
                        message.getExchange().put(Response.class, response);
                    }
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JsonParseException e) {//if the RestApiResponse/RestApiValidationError  object cannot be created, nothing is done. The response is processed the standard way
            e.printStackTrace();
        }
    }
    private boolean isErrorResponse(int responseCode) {
        return responseCode == 422 || //unprocessable entity
                responseCode == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }
}
