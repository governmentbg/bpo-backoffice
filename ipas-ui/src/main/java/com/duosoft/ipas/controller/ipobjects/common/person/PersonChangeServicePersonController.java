package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.person.OwnerUtils;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/person")
public class PersonChangeServicePersonController extends BasePersonActionController {

    @PostMapping("/change-service-person")
    public String changePersonCA(HttpServletRequest request, Model model,
                                 @RequestParam String sessionIdentifier,
                                 @RequestParam Integer personKind,
                                 @RequestParam Integer personNbr,
                                 @RequestParam Integer addressNbr) {

        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
            case PATENT:
                processMarkAndPatentChangeCA(request, sessionIdentifier, personKind, personNbr, addressNbr);
                break;
            case USERDOC:
                processUserdocChangeCA(request, sessionIdentifier, personKind, personNbr, addressNbr);
                break;
        }
        return personListPage(request, model, sessionIdentifier, personKind);
    }

    private void processMarkAndPatentChangeCA(HttpServletRequest request, String sessionIdentifier, Integer personKind, Integer personNbr, Integer addressNbr) {
        processCOwnershipDataApplicantCA(request, sessionIdentifier, personKind, personNbr, addressNbr);
        processRepresentativeCA(request, sessionIdentifier, personKind, personNbr, addressNbr);
        processAcpRepresentativeCA(request, sessionIdentifier, personKind, personNbr, addressNbr);
        processAcpInfringerCA(request, sessionIdentifier, personKind, personNbr, addressNbr);
    }

    private void processCOwnershipDataApplicantCA(HttpServletRequest request, String sessionIdentifier, Integer personKind, Integer personNbr, Integer addressNbr) {
        if (Objects.equals(PersonKind.Applicant.code(), personKind)) {
            COwnershipData ownershipData = PersonSessionUtils.getSessionOwnershipData(request, sessionIdentifier);
            if (Objects.nonNull(ownershipData)) {
                List<COwner> ownerList = ownershipData.getOwnerList();
                COwner selectedOwner = OwnerUtils.selectOwnerFromList(personNbr, addressNbr, ownerList);
                if (Objects.isNull(selectedOwner) || Objects.isNull(selectedOwner.getPerson()))
                    throw new RuntimeException("Cannot find selected CA person !");

                PersonSessionUtils.setSessionServicePerson(request, selectedOwner.getPerson(), sessionIdentifier);
            }
        }
    }


    private void processAcpRepresentativeCA(HttpServletRequest request, String sessionIdentifier, Integer personKind, Integer personNbr, Integer addressNbr) {
        if (Objects.equals(PersonKind.AcpRepresentative.code(), personKind)) {
            CRepresentationData representationData = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_REPRESENTATIVES, sessionIdentifier, request);
            if (Objects.nonNull(representationData)) {
                List<CRepresentative> representativeList = representationData.getRepresentativeList();
                CRepresentative selectedRepresentative = RepresentativeUtils.selectRepresentativeFromList(personNbr, addressNbr, representativeList);
                if (Objects.isNull(selectedRepresentative) || Objects.isNull(selectedRepresentative.getPerson())) {
                    throw new RuntimeException("Cannot find selected CA person !");
                }
                HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_ACP_SERVICE_PERSON, sessionIdentifier, selectedRepresentative.getPerson(), request);
            }

        }
    }

    private void processAcpInfringerCA(HttpServletRequest request, String sessionIdentifier, Integer personKind, Integer personNbr, Integer addressNbr) {
        if (Objects.equals(PersonKind.AcpInfringer.code(), personKind)) {
            CPerson infringer = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_INFRINGER, sessionIdentifier, request);
            if (Objects.nonNull(infringer)) {
                if (infringer.getPersonNbr().equals(personNbr) && infringer.getAddressNbr().equals(addressNbr)) {
                    HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_ACP_SERVICE_PERSON, sessionIdentifier, infringer, request);
                }
            }
        }
    }

    private void processRepresentativeCA(HttpServletRequest request, String sessionIdentifier, Integer personKind, Integer personNbr, Integer addressNbr) {
        if (Objects.equals(PersonKind.Representative.code(), personKind)) {
            CRepresentationData representationData = PersonSessionUtils.getSessionRepresentationData(request, sessionIdentifier);
            if (Objects.nonNull(representationData)) {
                List<CRepresentative> representativeList = representationData.getRepresentativeList();
                CRepresentative selectedRepresentative = RepresentativeUtils.selectRepresentativeFromList(personNbr, addressNbr, representativeList);
                if (Objects.isNull(selectedRepresentative) || Objects.isNull(selectedRepresentative.getPerson()))
                    throw new RuntimeException("Cannot find selected CA person !");

                PersonSessionUtils.setSessionServicePerson(request, selectedRepresentative.getPerson(), sessionIdentifier);
            }
        }
    }

    private void processUserdocChangeCA(HttpServletRequest request, String sessionIdentifier, Integer personKind, Integer personNbr, Integer addressNbr) {
        UserdocPersonRole userdocPersonRole = PersonUtils.selectUserdocPersonRoleByPersonKind(PersonKind.selectByCode(personKind));
        List<CUserdocPerson> sessionPersons = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, userdocPersonRole);
        CUserdocPerson selectedPerson = PersonUtils.selectUserdocPerson(personNbr, addressNbr, sessionPersons, userdocPersonRole);
        if (Objects.isNull(selectedPerson) || Objects.isNull(selectedPerson.getPerson())) {
            throw new RuntimeException("Cannot find selected CA person !");
        }

        PersonSessionUtils.setSessionServicePerson(request, selectedPerson.getPerson(), sessionIdentifier);
    }

}
