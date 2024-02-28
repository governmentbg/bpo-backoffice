package com.duosoft.ipas.controller.ipobjects.common.person;


import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.enums.PersonCopyType;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/person/copy")
public class PersonCopyController extends BasePersonActionController {

    @Autowired
    private SearchApplicationService searchApplicationService;

    @PostMapping
    public String copyPerson(HttpServletRequest request, Model model,
                             @RequestParam String sessionIdentifier,
                             @RequestParam Integer personCopyType,
                             @RequestParam Integer personKind) {
        PersonKind personKindEnum = PersonKind.selectByCode(personKind);
        PersonCopyType personCopyTypeEnum = PersonCopyType.selectByCode(personCopyType);
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case USERDOC:
                copyUserDocumentPerson(request, sessionIdentifier, personKindEnum, personCopyTypeEnum);
                //TODO
                break;
        }
        return personListPage(request, model, sessionIdentifier, personKind);
    }

    private void copyUserDocumentPerson(HttpServletRequest request, String sessionIdentifier, PersonKind personKind, PersonCopyType personCopyType) {
        UserdocPersonRole role = PersonUtils.selectUserdocPersonRoleByPersonKind(personKind);
        List<CUserdocPerson> sessionPersons = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        switch (personCopyType) {
            case FROM_MAIN_OBJECT_OWNER:
                copyFromMainObjectOwner(sessionUserdoc, role, sessionPersons);
                break;
            case FROM_MAIN_OBJECT_REPRESENTATIVE:
                copyFromMainObjectRepresentative(sessionUserdoc, role, sessionPersons);
                break;
            case FROM_CURRENT_OBJECT_APPLICANT:
                copyFromCurrentObjectApplicant(sessionUserdoc, role, sessionPersons);
                break;
            case FROM_CURRENT_OBJECT_REPRESENTATIVE:
                copyFromCurrentObjectRepresentative(sessionUserdoc, role, sessionPersons);
                break;
        }
    }

    private void copyFromCurrentObjectApplicant(CUserdoc sessionUserdoc, UserdocPersonRole role, List<CUserdocPerson> sessionPersons) {
        List<CUserdocPerson> applicants = UserdocPersonUtils.selectApplicants(sessionUserdoc.getUserdocPersonData());
        setNewUserdocPersons(role, sessionPersons, applicants);
    }

    private void copyFromCurrentObjectRepresentative(CUserdoc sessionUserdoc, UserdocPersonRole role, List<CUserdocPerson> sessionPersons) {
        List<CUserdocPerson> representatives = UserdocPersonUtils.selectRepresentatives(sessionUserdoc.getUserdocPersonData());
        setNewUserdocPersons(role, sessionPersons, representatives);
    }

    private void setNewUserdocPersons(UserdocPersonRole role, List<CUserdocPerson> sessionPersons, List<CUserdocPerson> existingPersons) {
        sessionPersons.clear();
        if (!CollectionUtils.isEmpty(existingPersons)) {
            for (CUserdocPerson existingPerson : existingPersons) {
                RepresentativeType representativeType = RepresentativeUtils.convertRepresentativeTypeValueToEnum(existingPerson.getRepresentativeType());
                CUserdocPerson newUserdocPerson = UserdocPersonUtils.convertToUserdocPerson(existingPerson, role, false, null, representativeType);
                sessionPersons.add(newUserdocPerson);
            }
        }
    }

    private void copyFromMainObjectOwner(CUserdoc userdoc, UserdocPersonRole role, List<CUserdocPerson> sessionPersons) {
        CProcessParentData userdocParentData = userdoc.getUserdocParentData();
        CFileId fileId = UserdocUtils.selectUserdocMainObject(userdocParentData);
        IpasApplicationSearchResult ipasApplicationSearchResult = searchApplicationService.selectApplication(fileId);
        if (Objects.isNull(ipasApplicationSearchResult)) {
            throw new RuntimeException("Cannot find userdoc parent object " + fileId);
        }

        sessionPersons.clear();
        List<CPerson> owners = ipasApplicationSearchResult.getOwners();
        if (!CollectionUtils.isEmpty(owners)) {
            List<CUserdocPerson> newUserdocPersons = UserdocPersonUtils.convertToUserdocPerson(owners, role,null);
            if (!CollectionUtils.isEmpty(newUserdocPersons)) {
                sessionPersons.addAll(newUserdocPersons);
            }
        }
    }

    private void copyFromMainObjectRepresentative(CUserdoc userdoc, UserdocPersonRole role, List<CUserdocPerson> sessionPersons) {
        CProcessParentData userdocParentData = userdoc.getUserdocParentData();
        CFileId fileId = UserdocUtils.selectUserdocMainObject(userdocParentData);
        IpasApplicationSearchResult ipasApplicationSearchResult = searchApplicationService.selectApplication(fileId);
        if (Objects.isNull(ipasApplicationSearchResult)) {
            throw new RuntimeException("Cannot find userdoc parent object " + fileId);
        }

        sessionPersons.clear();
        List<CRepresentative> representatives = ipasApplicationSearchResult.getRepresentatives();
        if (!CollectionUtils.isEmpty(representatives)) {
            List<CUserdocPerson> newPersons = UserdocPersonUtils.convertToUserdocPerson(representatives, role);
            if (!CollectionUtils.isEmpty(newPersons)) {
                sessionPersons.addAll(newPersons);
            }
        }
    }
}
