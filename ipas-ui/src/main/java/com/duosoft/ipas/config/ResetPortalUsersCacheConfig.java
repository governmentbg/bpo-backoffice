package com.duosoft.ipas.config;

import bg.duosoft.cronjob.cron.JobStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * User: ggeorgiev
 * Date: 24.01.2023
 * Time: 13:53
 */
@Component
@Slf4j
public class ResetPortalUsersCacheConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private JobStarter jobStarter;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            jobStarter.startJob("ResetPortalUsersCacheCron");
        } catch (Exception e) {
            log.error("Error trying to reset portal users cache!!", e);
        }
    }
}
