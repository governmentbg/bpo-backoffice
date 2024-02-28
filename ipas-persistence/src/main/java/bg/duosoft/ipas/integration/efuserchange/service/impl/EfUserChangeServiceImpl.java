package bg.duosoft.ipas.integration.efuserchange.service.impl;

import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import bg.duosoft.ipas.integration.efuserchange.model.FailureModel;
import bg.duosoft.ipas.integration.efuserchange.service.EfUserChangeService;
import bg.duosoft.ipas.properties.PropertyAccess;
import bg.duosoft.ipas.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class EfUserChangeServiceImpl implements EfUserChangeService {
    @Autowired
    private PropertyAccess propertyAccess;

    @Autowired
    @Qualifier("esUserChangeRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean notifyUserChanged(String oldOwner, String newOwner, String applicationNumber) {
        boolean validNotifyAction = true;
        if (StringUtils.hasText(oldOwner) && StringUtils.hasText(newOwner) && StringUtils.hasText(applicationNumber) && !oldOwner.equals(newOwner)) {
            String loggedUsername = SecurityUtils.getLoggedUsername();
            String actionTitle = messageSource.getMessage("error.action.notify.es.user.change", null, LocaleContextHolder.getLocale());

            log.debug("Notify user change by user:" + loggedUsername + " with old owner" + oldOwner + " and new owner:" + newOwner + " and application number:" + applicationNumber + "!");

            String url = propertyAccess.getEfuserchangeUrl().concat(NOTIFY_USER_CHANGED_URL).replace("{callerUser}", loggedUsername).
                    replace("{oldOwner}", oldOwner).replace("{newOwner}", newOwner).replace("{applicationNumber}", applicationNumber);

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                ResponseEntity<FailureModel[]> responseEntity = this.restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(headers), FailureModel[].class);
                List<FailureModel> failures = List.of(responseEntity.getBody());
                if (!CollectionUtils.isEmpty(failures)) {
                    validNotifyAction = false;
                    for (FailureModel failure : failures) {
                        errorLogService.createNewRecord(ErrorLogAbout.EF_USERCHANGE, actionTitle, failure.getAbout().concat("-").concat(failure.getDescription()), "", true, "", ErrorLogPriority.HIGH);
                    }
                    log.debug("Notification error - user change by user:" + loggedUsername + " with old owner" + oldOwner + " and new owner:" + newOwner + " and application number:" + applicationNumber + "!");
                } else {
                    log.debug("Notified user change by user:" + loggedUsername + " with old owner" + oldOwner + " and new owner:" + newOwner + " and application number:" + applicationNumber + "!");
                }
            } catch (Exception e) {
                errorLogService.createNewRecord(ErrorLogAbout.EF_USERCHANGE, actionTitle, e.getMessage(), "", true, "", ErrorLogPriority.HIGH);
                log.debug("Notification error - user change by user:" + loggedUsername + " with old owner" + oldOwner + " and new owner:" + newOwner + " and application number:" + applicationNumber + "!");
                validNotifyAction = false;
            }
        }
        return validNotifyAction;
    }
}
