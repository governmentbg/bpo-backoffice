package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonData;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.file.CFileSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

@Controller
@RequestMapping("/person")
public class PersonController {

    @PostMapping("/update-person-data")
    public String updatePersonData(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                processMarkPersons(request, model, isCancel, sessionIdentifier);
                return "ipobjects/marklike/common/person/person_panel :: persons";
            case PATENT:
                processPatentPersons(request, model, isCancel, sessionIdentifier);
                return "ipobjects/patentlike/common/person/person_panel :: persons";
            case USERDOC:
                processUserdocPersons(request, model, isCancel, sessionIdentifier);
                return "ipobjects/userdoc/person/person_panel :: userdoc-persons";
            default:
                throw new RuntimeException("Cannot save/cancel person panel for session identifier: " + sessionIdentifier);
        }
    }

    private void processMarkPersons(HttpServletRequest request, Model model, Boolean isCancel, String sessionIdentifier) {
        CFile sessionObjectFile = processCFilePersons(request, isCancel, sessionIdentifier);
        PersonSessionUtils.removePersonPanelSessionObjects(request, sessionIdentifier);
        model.addAttribute("file", sessionObjectFile);
    }

    private CFile processCFilePersons(HttpServletRequest request, Boolean isCancel, String sessionIdentifier) {
        CFile sessionObjectFile = CFileSessionUtils.getSessionFile(request, sessionIdentifier);
        if (!isCancel)
            PersonSessionUtils.setSessionPersonsToCFile(sessionObjectFile, request, sessionIdentifier);
        return sessionObjectFile;
    }

    private void processUserdocPersons(HttpServletRequest request, Model model, Boolean isCancel, String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        if (!isCancel) {
            CUserdocPersonData newUserdocPersonData = new CUserdocPersonData();
            newUserdocPersonData.setPersonList(new ArrayList<>());
            sessionUserdoc.setUserdocPersonData(newUserdocPersonData);
            PersonSessionUtils.setSessionPersonsToUserdoc(request, sessionIdentifier, sessionUserdoc);
        }

        PersonSessionUtils.removePersonPanelSessionObjects(request, sessionIdentifier);
        model.addAttribute("userdoc", sessionUserdoc);
    }

    private void processPatentPersons(HttpServletRequest request, Model model, Boolean isCancel, String sessionIdentifier) {
        CFile sessionObjectFile = processCFilePersons(request, isCancel, sessionIdentifier);
        editInventorsPersonPanel(model, request, isCancel, sessionIdentifier);
        PersonSessionUtils.removePersonPanelSessionObjects(request, sessionIdentifier);
        model.addAttribute("file", sessionObjectFile);
    }

    private void editInventorsPersonPanel(Model model, HttpServletRequest request, Boolean isCancel, String sessionIdentifier) {
        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        if (SessionObjectUtils.isSessionObjectPatent(fullSessionIdentifier)) {
            CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
            if (Objects.nonNull(patent)) {
                CAuthorshipData authorshipData = PersonSessionUtils.getSessionAuthorshipData(request, sessionIdentifier);
                if (!isCancel) {
                    if (Objects.nonNull(authorshipData)) {
                        patent.setAuthorshipData(authorshipData);
                    }
                }
                model.addAttribute("authorships", patent.getAuthorshipData());
            }
        }
    }

}
