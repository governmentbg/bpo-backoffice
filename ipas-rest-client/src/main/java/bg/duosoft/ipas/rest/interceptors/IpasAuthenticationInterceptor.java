package bg.duosoft.ipas.rest.interceptors;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 15:35
 */
public class IpasAuthenticationInterceptor extends AbstractPhaseInterceptor<Message> {
    private String username;
    private String password;
    public IpasAuthenticationInterceptor(String username, String password) {
        super(Phase.PRE_PROTOCOL);
        this.username = username;
        this.password = password;
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Map<String, List<String>> headers = CastUtils.cast((Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS));


        String userpassword = username + ":" + password;
        String authorization = "Basic " + new String(Base64.getEncoder().encode(userpassword.getBytes()));

        List<String> authHeaders = headers.get("Authorization");
        if (authHeaders == null) {
            headers.put("Authorization", Arrays.asList(new String[]{authorization}));
        } else {
            authHeaders.add(authorization);
        }
    }
}
