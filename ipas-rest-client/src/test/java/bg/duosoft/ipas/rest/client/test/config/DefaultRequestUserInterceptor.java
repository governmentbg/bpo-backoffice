package bg.duosoft.ipas.rest.client.test.config;


import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.util.List;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 16:12
 */
public class DefaultRequestUserInterceptor extends AbstractPhaseInterceptor<Message> {
    private String username;

    public DefaultRequestUserInterceptor(String username) {
        super(Phase.PRE_LOGICAL);
        this.username = username;
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        List content = message.getContent(List.class);
        if (content != null) {
            for (Object o : content) {
                if (o instanceof RestApiRequest) {
                    RestApiRequest r = (RestApiRequest) o;
                    if (r.getUsername() == null) {
                        r.setUsername(username);
                    }
                }
            }
        }
    }
}
