package com.duosoft.ipas.controller.abdocs;

import bg.duosoft.abdocs.model.Correspondent;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CPersonAbdocsSync;
import bg.duosoft.ipas.core.service.person.AbdocsSyncPersonService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.integration.abdocs.converter.CorrespondentConverter;
import bg.duosoft.ipas.util.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
public class PersonSyncController {

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private AbdocsSyncPersonService abdocsSyncPersonService;

    @Autowired
    private PersonService personService;

    @Autowired
    private CorrespondentConverter correspondentConverter;

    @Scheduled(fixedDelayString = "${ipas.properties.abdocs.person.sync.fixedDelay}")
    public void reportCurrentTime() {
        log.info("ABDOCS person synchronization cron start time is {}", DateUtils.TIME_FORMATTER_HOUR_MINUTE_SECOND.format(new Date()));
        synchronizePersons();
        log.info("ABDOCS person synchronization cron end time is {}", DateUtils.TIME_FORMATTER_HOUR_MINUTE_SECOND.format(new Date()));
    }

    private void synchronizePersons() {
        List<CPersonAbdocsSync> notProcessed = abdocsSyncPersonService.selectNotProcessed();
        if (!CollectionUtils.isEmpty(notProcessed)) {
            for (CPersonAbdocsSync personAbdocsSync : notProcessed) {
                try {
                    CPerson ipasPerson = personService.selectPersonByPersonNumberAndAddressNumber(personAbdocsSync.getPersonNbr(), personAbdocsSync.getAddrNbr());
                    if (Objects.nonNull(ipasPerson)) {
                        Correspondent correspondent = correspondentConverter.convertPersonToCorrespondent(ipasPerson);
                        abdocsServiceAdmin.updateCorrespondent(correspondent);
                        abdocsSyncPersonService.markAsProcessedAndSynchronized(personAbdocsSync);
                    } else {
                        abdocsSyncPersonService.markAsProcessedAndNotSynchronized(personAbdocsSync);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    abdocsSyncPersonService.markAsProcessedAndNotSynchronized(personAbdocsSync);
                }
            }
        }
    }
}
