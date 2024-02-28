package com.duosoft.ipas.controller.ipobjects.common.reception.create.email;

import com.duosoft.ipas.session.reception.model.ReceptionEmailSession;
import com.duosoft.ipas.session.reception.model.ReceptionEmailSessions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reception/email")
public class ReceptionEmailSessionController {

    private final ReceptionEmailSessions receptionEmailSessions;

    @GetMapping("/unblock")
    public String unblock() {
        String unblockedEmails = receptionEmailSessions.getReceptionEmailSessionList().stream()
                .map(ReceptionEmailSession::getEmailId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        receptionEmailSessions.getReceptionEmailSessionList().clear();
        return "Unblocked emails: " + (StringUtils.isEmpty(unblockedEmails) ? "none" : unblockedEmails);
    }

    @GetMapping("/view")
    public List<ReceptionEmailSession> view() {
        return receptionEmailSessions.getReceptionEmailSessionList();
    }

}
