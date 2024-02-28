package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CReprsPowerOfAttorney;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/person")
public class PersonImportController extends BasePersonActionController {

    @Autowired
    private PersonService personService;

    @PostMapping("/import-person")
    public String savePerson(HttpServletRequest request,
                             Model model,
                             @RequestParam Integer personKind,
                             @RequestParam Integer personNbr,
                             @RequestParam Integer addressNbr,
                             @RequestParam(required = false) Integer tempParentPersonNbr,
                             @RequestParam(required = false) String representativeType,
                             @RequestParam String sessionIdentifier) {
        if (Objects.nonNull(personNbr) && Objects.nonNull(addressNbr) && Objects.nonNull(personKind)) {
            CPerson person = personService.selectPersonByPersonNumberAndAddressNumber(personNbr, addressNbr);

            PersonKind personKindEnum = PersonKind.selectByCode(personKind);
            switch (personKindEnum) {
                case Applicant:
                    importExistingApplicant(request, sessionIdentifier, person);
                    break;
                case Representative:
                    importExistingRepresentative(request, sessionIdentifier, person, representativeType);
                    break;
                case Inventor:
                    importExistingInventor(request, sessionIdentifier, person);
                    break;
                case CorrespondenceAddress:
                    importExistingCorrespondenceAddress(request, sessionIdentifier, person);
                    break;
                case AcpCorrespondenceAddress:
                    if (Objects.nonNull(person)) {
                        HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_ACP_SERVICE_PERSON, sessionIdentifier, person, request);
                    }
                    break;
                case AcpInfringer:
                    if (Objects.nonNull(person)) {
                        HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_ACP_INFRINGER, sessionIdentifier, person, request);
                    }
                    break;
                case AcpRepresentative:
                    if (Objects.nonNull(person)) {
                        importExistingAcpRepresentative(request, sessionIdentifier, person, representativeType);
                    }
                    break;
                case NewCorrespondenceAddress:
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.NEW_CORRESPONDENCE_ADDRESS);
                    break;
                case Grantor:
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.GRANTOR);
                    break;
                case Grantee:
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.GRANTEE);
                    break;
                case Payer:
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.PAYER);
                    break;
                case Payee:
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.PAYEE);
                    break;
                case Pledger:
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.PLEDGER);
                    break;
                case Creditor:
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.CREDITOR);
                    break;
                case NewOwner: {
                    if (Objects.nonNull(tempParentPersonNbr)) {
                        person.setTempParentPersonNbr(tempParentPersonNbr);
                    }
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.NEW_OWNER);
                    PersonUtils.synchronizeAllUserdocPersonsWithSameId(request, sessionIdentifier, person, UserdocPersonRole.NEW_OWNER);
                    break;
                }
                case OldOwner:
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.OLD_OWNER);
                    break;
                case NewRepresentative:
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.NEW_REPRESENTATIVE, Enum.valueOf(RepresentativeType.class, representativeType));
                    break;
                case RepresentativeOfTheOwner: {
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.REPRESENTATIVE_OF_THE_OWNER, Enum.valueOf(RepresentativeType.class, representativeType));
                    break;
                }
                case AffectedInventor:
                    importExistingUserdocPerson(request, sessionIdentifier, person, UserdocPersonRole.AFFECTED_INVENTOR);
                    break;
                default:
                    throw new RuntimeException("Importing on existing person is not enable for person kind: " + personKind);
            }

            return personListPage(request, model, sessionIdentifier, personKind);
        } else
            throw new RuntimeException("Cannot import existing person !");

    }

    private void importExistingApplicant(HttpServletRequest request, String sessionIdentifier, CPerson person) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case RECEPTION:
            case MARK:
            case PATENT:
                addApplicantToSessionOwnershipData(request, sessionIdentifier, person);
                break;
            case USERDOC:
                addUserdocPersonToList(request, sessionIdentifier, person, UserdocPersonRole.APPLICANT, null);
                break;
        }
    }

    private void importExistingAcpRepresentative(HttpServletRequest request, String sessionIdentifier, CPerson person, String representativeType) {
        RepresentativeType representativeTypeEnum = Enum.valueOf(RepresentativeType.class, representativeType);
        CRepresentationData representationData = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_REPRESENTATIVES, sessionIdentifier, request);
        PersonUtils.addPersonToRepresentationData(representativeTypeEnum, person, representationData,request,sessionIdentifier);
    }

    private void importExistingRepresentative(HttpServletRequest request, String sessionIdentifier, CPerson person, String representativeType) {
        RepresentativeType representativeTypeEnum = Enum.valueOf(RepresentativeType.class, representativeType);
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case RECEPTION:
            case MARK:
            case PATENT:
                addRepresentativeToSessionRepresentationData(request, sessionIdentifier, person, representativeTypeEnum);
                break;
            case USERDOC:
                addUserdocPersonToList(request, sessionIdentifier, person, UserdocPersonRole.REPRESENTATIVE, representativeTypeEnum);
                break;
        }
    }

    private void importExistingUserdocPerson(HttpServletRequest request, String sessionIdentifier, CPerson person, UserdocPersonRole role) {
        importExistingUserdocPerson(request, sessionIdentifier, person, role, null);
    }

    private void importExistingUserdocPerson(HttpServletRequest request, String sessionIdentifier, CPerson person, UserdocPersonRole role, RepresentativeType representativeType) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        if (sessionObjectType == SessionObjectType.USERDOC)
            addUserdocPersonToList(request, sessionIdentifier, person, role, representativeType);
    }

    private void importExistingInventor(HttpServletRequest request, String sessionIdentifier, CPerson person) {
        CAuthorshipData authorshipData = PersonSessionUtils.getSessionAuthorshipData(request, sessionIdentifier);
        PersonUtils.addInventorToList(person, authorshipData);
    }

    private void importExistingCorrespondenceAddress(HttpServletRequest request, String sessionIdentifier, CPerson person) {
        if (Objects.nonNull(person)) {
            PersonSessionUtils.setSessionServicePerson(request, person, sessionIdentifier);
        }
    }

    private void addApplicantToSessionOwnershipData(HttpServletRequest request, String sessionIdentifier, CPerson person) {
        COwnershipData ownershipData = PersonSessionUtils.getSessionOwnershipData(request, sessionIdentifier);
        PersonUtils.addPersonToOwnershipData(person, ownershipData);
    }

    private void addRepresentativeToSessionRepresentationData(HttpServletRequest request, String sessionIdentifier, CPerson person, RepresentativeType representativeType) {
        CRepresentationData representationData = PersonSessionUtils.getSessionRepresentationData(request, sessionIdentifier);
        PersonUtils.addPersonToRepresentationData(representativeType, person, representationData,request,sessionIdentifier);
    }

    private void addUserdocPersonToList(HttpServletRequest request, String sessionIdentifier, CPerson person, UserdocPersonRole role, RepresentativeType representativeType) {
        List<CUserdocPerson> sessionPersons = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
        if (role == UserdocPersonRole.NEW_CORRESPONDENCE_ADDRESS) {
            sessionPersons.clear();
        }

        Integer tempParentPersonNbr = person.getTempParentPersonNbr();//Replace person
        if (Objects.nonNull(tempParentPersonNbr)) {
            sessionPersons.removeIf(cUserdocPerson -> tempParentPersonNbr.equals(cUserdocPerson.getPerson().getTempParentPersonNbr()));
        }

        PersonUtils.addPersonToUserdocPersonListByRole(person, sessionPersons, role, representativeType, request,sessionIdentifier);
    }

}
