package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/person/gral-import")
public class GralPersonImportController extends BasePersonActionController {

    @Autowired
    private SearchApplicationService searchApplicationService;

    @Autowired
    private PersonService personService;

    @PostMapping
    public String importGralPerson(HttpServletRequest request, Model model,
                                   @RequestParam String sessionIdentifier,
                                   @RequestParam Integer personKind) {
        PersonKind personKindEnum = PersonKind.selectByCode(personKind);
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case USERDOC:
                CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
                UserdocPersonRole role = PersonUtils.selectUserdocPersonRoleByPersonKind(personKindEnum);
                List<CUserdocPerson> sessionPersons = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
                importUserdocGralPerson(personKindEnum, sessionUserdoc, sessionPersons, role);
                break;
        }
        return personListPage(request, model, sessionIdentifier, personKind);
    }

    private void importUserdocGralPerson(PersonKind personKindEnum, CUserdoc userdoc, List<CUserdocPerson> sessionPersons, UserdocPersonRole role) {
        switch (personKindEnum) {
            case NewOwner:
                CFileId fileId = UserdocUtils.selectUserdocMainObject(userdoc.getUserdocParentData());
                IpasApplicationSearchResult ipasApplicationSearchResult = searchApplicationService.selectApplication(fileId);
                if (Objects.isNull(ipasApplicationSearchResult)) {
                    throw new RuntimeException("Cannot find userdoc main object " + fileId);
                }
                List<CPerson> owners = ipasApplicationSearchResult.getOwners();
                if (!CollectionUtils.isEmpty(owners)) {
                    List<CPerson> newCPersonOwners = new ArrayList<>();
                    for (CPerson owner : owners) {
                        String agentCode = owner.getAgentCode();
                        if (!StringUtils.isEmpty(agentCode)) {
                            newCPersonOwners.add(owner);
                        } else {
                            CPerson lastVersionOfPerson = personService.selectLastVersionOfPerson(owner.getPersonNbr());
                            if (Objects.nonNull(lastVersionOfPerson)) {
                                CPerson cloning = (CPerson) SerializationUtils.clone(lastVersionOfPerson);
                                cloning.setTempParentPersonNbr(lastVersionOfPerson.getPersonNbr());
                                newCPersonOwners.add(cloning);
                            }
                        }
                    }
                    List<CUserdocPerson> newUserdocPersons = UserdocPersonUtils.convertToUserdocPerson(newCPersonOwners, role,null);
                    if (!CollectionUtils.isEmpty(newUserdocPersons)) {
                        sessionPersons.clear();
                        sessionPersons.addAll(newUserdocPersons);
                    }
                }
                break;
        }
    }

}
