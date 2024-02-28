package bg.duosoft.ipas.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.TimeZone;

/**
 * Created by Raya
 * 07.03.2019
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate noAuthRestTemplate() {
        Jaxb2RootElementHttpMessageConverter jaxbConverter = new Jaxb2RootElementHttpMessageConverter();
        jaxbConverter.setSupportDtd(true);
        jaxbConverter.setDefaultCharset(Charset.forName("UTF-8"));

        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));

        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(mapper);

        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();

        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(Arrays.asList(formHttpMessageConverter, jaxbConverter, stringConverter, jsonMessageConverter, byteArrayHttpMessageConverter));
        return restTemplate;
    }
}
