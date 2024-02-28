package com.duosoft.ipas.util.session.person;

import bg.duosoft.ipas.core.model.acp.CAcpPersonsData;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CReprsPowerOfAttorney;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.exception.SessionObjectNotFoundException;
import bg.duosoft.ipas.util.person.InventorUtils;
import bg.duosoft.ipas.util.person.OwnerUtils;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.reception.ReceptionSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PersonSessionUtils {


    public static CPerson getSessionPerson(HttpServletRequest request, String sessionIdentifier, Integer personNumber, Integer addressNumber) {
        List<CPerson> personList;
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case RECEPTION:
                personList = ReceptionSessionUtils.getSessionReceptionPersons(request, sessionIdentifier);
                break;
            case MARK:
                personList = getSessionMarkPersons(request, sessionIdentifier);
                break;
            case PATENT:
                personList = getSessionPatentPersons(request, sessionIdentifier);
                break;
            case USERDOC:
                personList = getSessionUserdocPersons(request, sessionIdentifier);
                break;
            default:
                throw new SessionObjectNotFoundException("Cannot find person session object for remove operation " + sessionIdentifier);
        }

        if (CollectionUtils.isEmpty(personList)) {
            return null;
        }

        return personList.stream()
                .filter(person -> person.getPersonNbr().equals(personNumber) && person.getAddressNbr().equals(addressNumber))
                .findFirst()
                .orElse(null);
    }

    public static List<CPerson> getSessionMarkPersons(HttpServletRequest request, String sessionIdentifier) {
        List<CPerson> list = new ArrayList<>();
        selectOwnersRepresentativesAndServicePerson(request, sessionIdentifier, list);
        selectAcpOwnersRepresentativesAndServicePerson(request, sessionIdentifier, list);
        return CollectionUtils.isEmpty(list) ? null : list;
    }

    public static List<CPerson> getSessionPatentPersons(HttpServletRequest request, String sessionIdentifier) {
        List<CPerson> list = new ArrayList<>();
        selectOwnersRepresentativesAndServicePerson(request, sessionIdentifier, list);
        List<CPerson> authors = InventorUtils.convertToCPersonList(getSessionAuthorshipData(request, sessionIdentifier));
        if (!CollectionUtils.isEmpty(authors)) {
            list.addAll(authors);
        }

        return CollectionUtils.isEmpty(list) ? null : list;
    }


    private static void selectAcpOwnersRepresentativesAndServicePerson(HttpServletRequest request, String sessionIdentifier, List<CPerson> list) {

        CRepresentationData representationData = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_REPRESENTATIVES, sessionIdentifier, request);
        List<CPerson> representatives = RepresentativeUtils.convertToCPersonList(representationData);
        if (!CollectionUtils.isEmpty(representatives)) {
            list.addAll(representatives);
        }

        CPerson servicePerson = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_SERVICE_PERSON, sessionIdentifier, request);
        if (Objects.nonNull(servicePerson)) {
            list.add(servicePerson);
        }

        CPerson infringer = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_INFRINGER, sessionIdentifier, request);
        if (Objects.nonNull(infringer)) {
            list.add(infringer);
        }

    }

    private static void selectOwnersRepresentativesAndServicePerson(HttpServletRequest request, String sessionIdentifier, List<CPerson> list) {
        List<CPerson> owners = OwnerUtils.convertToCPersonList(getSessionOwnershipData(request, sessionIdentifier));
        if (!CollectionUtils.isEmpty(owners)) {
            list.addAll(owners);
        }

        List<CPerson> representatives = RepresentativeUtils.convertToCPersonList(getSessionRepresentationData(request, sessionIdentifier));
        if (!CollectionUtils.isEmpty(representatives)) {
            list.addAll(representatives);
        }

        CPerson servicePerson = getSessionServicePerson(request, sessionIdentifier);
        if (Objects.nonNull(servicePerson)) {
            list.add(servicePerson);
        }
    }

    public static void removePersonPanelSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, MarkSessionObjects.getPersonSessionObjectNames());
                break;
            case PATENT:
                HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, PatentSessionObject.getPersonSessionObjectNames());
                break;
            case USERDOC:
                HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, UserdocSessionObjects.getPersonSessionObjectNames());
                break;
            default:
                throw new SessionObjectNotFoundException("Cannot find person session object for remove operation " + sessionIdentifier);
        }
    }

    public static void updateSessionServicePerson(HttpServletRequest request, CPerson cPerson, String sessionIdentifier) {
        CPerson sessionCAPerson = getSessionServicePerson(request, sessionIdentifier);
        if (Objects.nonNull(sessionCAPerson)) {
            if (Objects.equals(sessionCAPerson.getAddressNbr(), cPerson.getAddressNbr()) && sessionCAPerson.getPersonNbr().equals(cPerson.getPersonNbr())) {
                setSessionServicePerson(request, cPerson, sessionIdentifier);
            } else {
                Integer tempParentPersonNbr = cPerson.getTempParentPersonNbr();
                if (Objects.nonNull(tempParentPersonNbr) && sessionCAPerson.getPersonNbr().equals(tempParentPersonNbr)) {
                    setSessionServicePerson(request, cPerson, sessionIdentifier);
                }
            }
        }
    }

    public static void updateSessionApplicantPersonOnMatch(HttpServletRequest request, CPerson cPerson, String sessionIdentifier) {
        COwnershipData ownershipData = PersonSessionUtils.getSessionOwnershipData(request, sessionIdentifier);
        if (Objects.nonNull(ownershipData)) {
            COwner owner = OwnerUtils.selectOwnerFromList(cPerson.getPersonNbr(), cPerson.getAddressNbr(), ownershipData.getOwnerList());
            if (Objects.nonNull(owner))
                owner.setPerson(cPerson);
        }
    }

    public static CAuthorshipData getSessionAuthorshipData(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case PATENT:
                return HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_INVENTORS, sessionIdentifier, request);
            default:
                throw new RuntimeException("Cannot find CAutorshipData for object with identifier " + sessionIdentifier);
        }
    }

    public static COwnershipData getSessionOwnershipData(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case RECEPTION:
                return ReceptionSessionUtils.getSessionReceptionOwnershipData(request, sessionIdentifier);
            case MARK:
                return HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_OWNERS, sessionIdentifier, request);
            case PATENT:
                return HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_OWNERS, sessionIdentifier, request);
            default:
                throw new RuntimeException("Cannot find COwnershipData for object with identifier " + sessionIdentifier);
        }
    }

    public static CRepresentationData getSessionRepresentationData(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case RECEPTION:
                return ReceptionSessionUtils.getSessionReceptionRepresentationData(request, sessionIdentifier);
            case MARK:
                return HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_REPRESENTATIVES, sessionIdentifier, request);
            case PATENT:
                return HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_REPRESENTATIVES, sessionIdentifier, request);
            case USERDOC:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_REPRESENTATIVES, sessionIdentifier, request);
            default:
                throw new RuntimeException("Cannot find CRepresentationData for object with identifier " + sessionIdentifier);
        }
    }

    public static CPerson getSessionServicePerson(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                return HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_SERVICE_PERSON, sessionIdentifier, request);
            case PATENT:
                return HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_SERVICE_PERSON, sessionIdentifier, request);
            case USERDOC:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_SERVICE_PERSON, sessionIdentifier, request);
            default:
                throw new RuntimeException("Cannot find Service person for object with identifier " + sessionIdentifier);
        }
    }

    public static CReprsPowerOfAttorney getSessionAttorneyData(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                return HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ATTORNEY_DATA, sessionIdentifier, request);
            case PATENT:
                return HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTORNEY_DATA, sessionIdentifier, request);
            case USERDOC:
               return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ATTORNEY_DATA, sessionIdentifier, request);
            default:
                throw new RuntimeException("Cannot find attorney data for " + sessionIdentifier);
        }
    }

    public static void setSessionAttorneyData(HttpServletRequest request, CReprsPowerOfAttorney attorneyData, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_MARK_ATTORNEY_DATA, sessionIdentifier, attorneyData, request);
                break;
            case PATENT:
                HttpSessionUtils.setSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTORNEY_DATA, sessionIdentifier, attorneyData, request);
                break;
            case USERDOC:
                HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ATTORNEY_DATA, sessionIdentifier, attorneyData, request);
                break;
            default:
                throw new RuntimeException("Cannot find attorney data for update " + sessionIdentifier);
        }
    }

    public static void removeSessionAttorneyData(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, MarkSessionObjects.SESSION_MARK_ATTORNEY_DATA);
                break;
            case PATENT:
                HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, PatentSessionObject.SESSION_PATENT_ATTORNEY_DATA);
                break;
            case USERDOC:
                HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, UserdocSessionObjects.SESSION_USERDOC_ATTORNEY_DATA);
                break;
            default:
                throw new SessionObjectNotFoundException("Cannot find attorney for remove operation " + sessionIdentifier);
        }
    }

    public static void setSessionServicePerson(HttpServletRequest request, CPerson cPerson, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_MARK_SERVICE_PERSON, sessionIdentifier, cPerson, request);
                break;
            case PATENT:
                HttpSessionUtils.setSessionAttribute(PatentSessionObject.SESSION_PATENT_SERVICE_PERSON, sessionIdentifier, cPerson, request);
                break;
            case USERDOC:
                HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_SERVICE_PERSON, sessionIdentifier, cPerson, request);
                break;
            default:
                throw new RuntimeException("Cannot find session service person for update " + sessionIdentifier);
        }
    }

    public static List<CUserdocPerson> selectUserdocSessionPersons(HttpServletRequest request, String sessionIdentifier, UserdocPersonRole role) {
        switch (role) {
            case REPRESENTATIVE:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_REPRESENTATIVES, sessionIdentifier, request);
            case APPLICANT:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_APPLICANTS, sessionIdentifier, request);
            case GRANTOR:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_GRANTORS, sessionIdentifier, request);
            case GRANTEE:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_GRANTEES, sessionIdentifier, request);
            case PAYEE:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_PAYEE, sessionIdentifier, request);
            case PAYER:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_PAYER, sessionIdentifier, request);
            case PLEDGER:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_PLEDGER, sessionIdentifier, request);
            case CREDITOR:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_CREDITOR, sessionIdentifier, request);
            case NEW_OWNER:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_NEW_OWNERS, sessionIdentifier, request);
            case OLD_OWNER:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_OLD_OWNERS, sessionIdentifier, request);
            case NEW_REPRESENTATIVE:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_NEW_REPRESENTATIVES, sessionIdentifier, request);
            case OLD_REPRESENTATIVE:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_OLD_REPRESENTATIVES, sessionIdentifier, request);
            case REPRESENTATIVE_OF_THE_OWNER:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_REPRESENTATIVES_OF_THE_OWNER, sessionIdentifier, request);
            case OLD_CORRESPONDENCE_ADDRESS:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_OLD_CORRESPONDENCE_ADDRESS, sessionIdentifier, request);
            case NEW_CORRESPONDENCE_ADDRESS:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_NEW_CORRESPONDENCE_ADDRESS, sessionIdentifier, request);
            case AFFECTED_INVENTOR:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_AFFECTED_INVENTOR, sessionIdentifier, request);
            default:
                throw new RuntimeException("Cannot find userdoc persons for role " + role);
        }
    }

    public static List<CPerson> getSessionUserdocPersons(HttpServletRequest request, String sessionIdentifier) {
        List<CPerson> personList = new ArrayList<>();
        for (UserdocPersonRole value : UserdocPersonRole.selectAll()) {
            List<CUserdocPerson> sessionPersonsByRole = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, value);
            if (!CollectionUtils.isEmpty(sessionPersonsByRole)) {
                personList.addAll(sessionPersonsByRole.stream().map(CUserdocPerson::getPerson).filter(Objects::nonNull).collect(Collectors.toList()));
            }
        }

        CPerson sessionCAPerson = PersonSessionUtils.getSessionServicePerson(request, sessionIdentifier);
        if (Objects.nonNull(sessionCAPerson)) {
            personList.add(sessionCAPerson);
        }

        return personList;
    }

    public static void setSessionPersonsToUserdoc(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        for (UserdocPersonRole value : UserdocPersonRole.selectAll()) {
            List<CUserdocPerson> sessionPersonsByRole = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, value);
            if (!CollectionUtils.isEmpty(sessionPersonsByRole))
                userdoc.getUserdocPersonData().getPersonList().addAll(sessionPersonsByRole);
        }

        CPerson sessionCAPerson = PersonSessionUtils.getSessionServicePerson(request, sessionIdentifier);
        userdoc.setServicePerson(sessionCAPerson);
    }

    public static void setSessionPersonsToCFile(CFile cFile, HttpServletRequest request, String sessionIdentifier) {
        COwnershipData ownershipData = PersonSessionUtils.getSessionOwnershipData(request, sessionIdentifier);
        CRepresentationData representationData = PersonSessionUtils.getSessionRepresentationData(request, sessionIdentifier);
        CPerson sessionCAPerson = PersonSessionUtils.getSessionServicePerson(request, sessionIdentifier);
        CPerson acpServicePerson = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_SERVICE_PERSON, sessionIdentifier, request);
        CPerson acpInfringer = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_INFRINGER, sessionIdentifier, request);
        CRepresentationData acpRepresentationData = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_REPRESENTATIVES, sessionIdentifier, request);


        cFile.setAcpPersonsData(new CAcpPersonsData());
        cFile.getAcpPersonsData().setServicePerson(acpServicePerson);
        cFile.getAcpPersonsData().setInfringerPerson(acpInfringer);
        cFile.getAcpPersonsData().setRepresentationData(acpRepresentationData);
        cFile.setOwnershipData(ownershipData);
        cFile.setRepresentationData(representationData);
        cFile.setServicePerson(sessionCAPerson);
    }

}
