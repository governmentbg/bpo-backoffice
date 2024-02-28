package com.duosoft.ipas.config.logback;

import bg.duosoft.abdocs.util.JsonUtil;
import bg.duosoft.ipas.util.date.DateUtils;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SlackAppender extends AppenderBase<ILoggingEvent> {

    private static final String DEFAULT_LOG_LEVEL = "ERROR";
    private static final RestTemplate restTemplate;

    @Getter
    @Setter
    private String webhook;

    @Getter
    private Level level;

    static {
        restTemplate = new RestTemplateBuilder().build();
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (Objects.isNull(this.level)) {
            setLevel(DEFAULT_LOG_LEVEL);
        }

        if (StringUtils.isEmpty(this.webhook)) {
            return;
        }

        boolean isMessageLogAllowed = iLoggingEvent.getLevel().isGreaterOrEqual(this.level);
        if (!isMessageLogAllowed)
            return;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");

            StringBuilder sb = new StringBuilder();
            sb.append("*[")
                    .append(DateUtils.formatDateTime(new Date()))
                    .append(" ")
                    .append(iLoggingEvent.getLevel())
                    .append("]* ")
                    .append(iLoggingEvent.getMessage())
                    .append("\n");

            if (iLoggingEvent.getLevel() == Level.ERROR) {
                IThrowableProxy throwableProxy = iLoggingEvent.getThrowableProxy();
                StackTraceElementProxy[] stackTraceElementProxyArray = throwableProxy.getStackTraceElementProxyArray();
                if (!CollectionUtils.isEmpty(Arrays.asList(stackTraceElementProxyArray))) {
                    String stackTrace = Arrays.stream(stackTraceElementProxyArray)
                            .map(StackTraceElementProxy::getSTEAsString)
                            .limit(10)
                            .collect(Collectors.joining("\n"));

                    sb.append("*")
                            .append(throwableProxy.getClassName())
                            .append(throwableProxy.getMessage())
                            .append("*").append("\n")
                            .append(stackTrace).append("\n")
                            .append("------------------------------------------------------------------------------------------");
                }
            }

            String json = JsonUtil.createJson(
                    Message.builder().text(sb.toString()).build()
            );
            restTemplate.postForObject(this.webhook, new HttpEntity<>(json, headers), String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setLevel(String level) {
        this.level = Level.toLevel(level);
    }

    @Getter
    @Setter
    @Builder
    private static class Message {
        private String text;
    }

}
