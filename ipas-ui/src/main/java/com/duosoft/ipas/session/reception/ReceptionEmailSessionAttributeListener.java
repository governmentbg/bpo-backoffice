package com.duosoft.ipas.session.reception;

import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.session.reception.model.ReceptionEmailSession;
import com.duosoft.ipas.session.reception.model.ReceptionEmailSessions;
import com.duosoft.ipas.util.session.reception.ReceptionSessionUtils;
import com.duosoft.ipas.webmodel.ReceptionDocflowEmailDocumentForm;
import com.duosoft.ipas.webmodel.ReceptionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.*;
import java.util.Date;
import java.util.Objects;

public class ReceptionEmailSessionAttributeListener implements HttpSessionAttributeListener {

    @Autowired
    private ReceptionEmailSessions receptionEmailSessions;

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        addReceptionEmail(event);
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        removeReceptionEmail(event);
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        addReceptionEmail(event);
    }

    private void addReceptionEmail(HttpSessionBindingEvent event) {
        String sessionObjectName = event.getName();
        if (!StringUtils.isEmpty(sessionObjectName) && sessionObjectName.contains(ReceptionSessionUtils.SESSION_RECEPTION_FORM)) {
            if (event.getValue() instanceof ReceptionForm) {
                Integer loggedUserId = SecurityUtils.getLoggedUserId();
                if (Objects.nonNull(loggedUserId)) {
                    receptionEmailSessions.getReceptionEmailSessionList().removeIf(receptionEmailSession -> receptionEmailSession.getUserId().equals(loggedUserId));
                    ReceptionForm receptionForm = (ReceptionForm) event.getValue();
                    ReceptionDocflowEmailDocumentForm emailDocument = receptionForm.getDocflowEmailDocument();
                    if (Objects.nonNull(emailDocument)) {
                        receptionEmailSessions.getReceptionEmailSessionList().add(
                                ReceptionEmailSession.builder()
                                        .sessionObjectName(sessionObjectName)
                                        .userId(loggedUserId)
                                        .username(SecurityUtils.getLoggedUsername())
                                        .userFullName(SecurityUtils.getLoggedUserFullName())
                                        .emailId(emailDocument.getEmailId())
                                        .emailSubject(emailDocument.getSubject())
                                        .date(new Date())
                                        .build()
                        );
                    }
                }
            }
        }
    }

    private void removeReceptionEmail(HttpSessionBindingEvent se) {
        String sessionObjectName = se.getName();
        if (!StringUtils.isEmpty(sessionObjectName) && sessionObjectName.contains(ReceptionSessionUtils.SESSION_RECEPTION_FORM)) {
            receptionEmailSessions.getReceptionEmailSessionList().removeIf(receptionEmailSession -> receptionEmailSession.getSessionObjectName().equals(sessionObjectName));
        }
    }
}
