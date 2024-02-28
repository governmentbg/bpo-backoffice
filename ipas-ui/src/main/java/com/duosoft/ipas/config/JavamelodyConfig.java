package com.duosoft.ipas.config;

import net.bull.javamelody.MonitoringFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSessionListener;

/**
 * User: ggeorgiev
 * Date: 14.04.2021
 * Time: 14:42
 */
@Configuration
@ImportResource({"classpath*:net/bull/javamelody/monitoring-spring-datasource.xml"})
public class JavamelodyConfig implements ServletContextInitializer {
    @Value(value = "${javamelody.storage-directory}")
    String jmStorageDir;

    @Bean
    public HttpSessionListener javaMelodyListener() {
        return new net.bull.javamelody.SessionListener();
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration monitoringFilter = servletContext.addFilter("monitoringFilter", new MonitoringFilter());

        monitoringFilter.setInitParameter("storage-directory", jmStorageDir);
        monitoringFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}
