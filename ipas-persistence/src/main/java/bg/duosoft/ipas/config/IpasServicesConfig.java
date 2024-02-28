package bg.duosoft.ipas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath*:ipas-ws.xml", "classpath*:ipas-service-core.xml"})
public class IpasServicesConfig {

}