package com.duosoft.ipas.session.reception;

import com.duosoft.ipas.session.reception.model.ReceptionEmailSessions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class ReceptionEmailSessionListenerConfig {

    @Bean
    public ReceptionEmailSessionAttributeListener receptionEmailSessionAttributeListener() {
        return new ReceptionEmailSessionAttributeListener();
    }

    @Bean
    public ReceptionEmailSessions receptionEmailSessions() {
        return new ReceptionEmailSessions(new ArrayList<>());
    }

}
