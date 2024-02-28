package bg.duosoft.ipas.rest.config;

import bg.duosoft.ipas.rest.interceptors.CheckResponseForErrorsInterceptor;
import bg.duosoft.ipas.rest.interceptors.IpasAuthenticationInterceptor;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 18:53
 */
public class IpasRestClientInterceptorsConfiguration {
    public List<Interceptor<Message>> getInInterceptors() {
        return null;
    }
    public List<Interceptor<Message>> getOutInterceptors() {
        return null;
    }
    public List<Interceptor<Message>> getInFaultInterceptors() {
        return null;
    }
    public List<Interceptor<Message>> getOutFaultInterceptors() {
        return null;
    }

    public Set<Class<? extends Interceptor<Message>>> getDefaultInterceptors() {
        List<Class<? extends Interceptor<Message>>> res = Arrays.asList(IpasAuthenticationInterceptor.class, CheckResponseForErrorsInterceptor.class);
        return new HashSet<>(res);
    }
}
