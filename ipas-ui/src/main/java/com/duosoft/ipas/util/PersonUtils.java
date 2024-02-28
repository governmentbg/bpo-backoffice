package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.person.*;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.model.util.PersonAddressResult;
import bg.duosoft.ipas.core.model.util.TempID;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.person.OwnerUtils;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import bg.duosoft.ipas.util.person.SearchPersonUtils;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class PersonUtils {


    public static boolean checkIfCAExistInAcpPersons(CFile cFile) {

        if (Objects.isNull(cFile.getAcpPersonsData())) {
            return false;
        }

        CPerson servicePerson = cFile.getAcpPersonsData().getServicePerson();
        if (Objects.nonNull(servicePerson)) {

            if (Objects.nonNull(cFile.getAcpPersonsData().getRepresentationData()) && !CollectionUtils.isEmpty(cFile.getAcpPersonsData().getRepresentationData().getRepresentativeList())) {
                for (CRepresentative representative : cFile.getAcpPersonsData().getRepresentationData().getRepresentativeList()) {
                    if (representative.getPerson().getPersonNbr().equals(servicePerson.getPersonNbr()) && representative.getPerson().getAddressNbr().equals(servicePerson.getAddressNbr())) {
                        return true;
                    }
                }
            }

            CPerson infringer = cFile.getAcpPersonsData().getInfringerPerson();
            if (Objects.nonNull(infringer)) {
                if (infringer.getPersonNbr().equals(servicePerson.getPersonNbr()) && infringer.getAddressNbr().equals(servicePerson.getAddressNbr())) {
                    return true;
                }
            }

        }


        return false;
    }

    //TODO Inventor ?
    public static boolean checkIfCAExistInPersons(CFile cFile) {
        CPerson servicePerson = cFile.getServicePerson();
        if (Objects.nonNull(servicePerson)) {
            COwnershipData ownershipData = cFile.getOwnershipData();
            if (Objects.nonNull(ownershipData)) {
                COwner owner = OwnerUtils.selectOwnerFromList(servicePerson.getPersonNbr(), servicePerson.getAddressNbr(), ownershipData.getOwnerList());
                if (Objects.nonNull(owner))
                    return true;
            }

            CRepresentationData representationData = cFile.getRepresentationData();
            if (Objects.nonNull(representationData)) {
                CRepresentative representative = RepresentativeUtils.selectRepresentativeFromList(servicePerson.getPersonNbr(), servicePerson.getAddressNbr(), representationData.getRepresentativeList());
                if (Objects.nonNull(representative))
                    return true;
            }
        }
        return false;
    }

    public static boolean isAcpCurrentPersonCA(Integer personNbr, Integer addressNbr, CFile cFile) {
        CPerson servicePerson = cFile.getAcpPersonsData().getServicePerson();
        if (Objects.isNull(servicePerson))
            return false;

        return Objects.equals(personNbr, servicePerson.getPersonNbr()) && Objects.equals(addressNbr, servicePerson.getAddressNbr());
    }


    public static boolean isCurrentPersonCA(Integer personNbr, Integer addressNbr, CFile cFile) {
        CPerson servicePerson = cFile.getServicePerson();
        if (Objects.isNull(servicePerson))
            return false;

        return Objects.equals(personNbr, servicePerson.getPersonNbr()) && Objects.equals(addressNbr, servicePerson.getAddressNbr());
    }

    public static void addRepresentativeToRepresentationData(String agentCodeAndName, CRepresentationData representationData, PersonService personService, RepresentativeType representativeType, CReprsPowerOfAttorney attorneyData) {
        CPerson cPerson = selectAgentByAgentCodeAndName(agentCodeAndName, personService);
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            if (Objects.isNull(representativeList)) {
                representativeList = new ArrayList<>();
                representationData.setRepresentativeList(representativeList);// Important for session object
            }

            CRepresentative selectedRepresentative = RepresentativeUtils.selectRepresentativeFromList(cPerson.getPersonNbr(), cPerson.getAddressNbr(), representativeList);
            if (Objects.isNull(selectedRepresentative)) {
                CRepresentative cRepresentative = RepresentativeUtils.convertToRepresentative(cPerson, representativeType);
                if (Objects.nonNull(attorneyData)) {
                    cRepresentative.setAttorneyPowerTerm(attorneyData.getAttorneyPowerTerm());
                    cRepresentative.setReauthorizationRight(attorneyData.getReauthorizationRight());
                    cRepresentative.setPriorReprsRevocation(attorneyData.getPriorReprsRevocation());
                    cRepresentative.setAuthorizationCondition(attorneyData.getAuthorizationCondition());
                }
                representativeList.add(cRepresentative);
            }
        }
    }

    public static void addRepresentativeToUserdocPersons(String agentCodeAndName, List<CUserdocPerson> userdocRepresentatives, PersonService personService, RepresentativeType representativeType, UserdocPersonRole role, CReprsPowerOfAttorney attorneyData) {
        CPerson cPerson = selectAgentByAgentCodeAndName(agentCodeAndName, personService);
        CUserdocPerson selectedRepresentative = PersonUtils.selectUserdocPerson(cPerson.getPersonNbr(), cPerson.getAddressNbr(), userdocRepresentatives, role);
        if (Objects.isNull(selectedRepresentative)) {
            CUserdocPerson representative = new CUserdocPerson();
            representative.setPerson(cPerson);
            representative.setRole(role);
            representative.setRepresentativeType(Objects.isNull(representativeType) ? null : representativeType.getValue());

            if (UserdocPersonRole.REPRESENTATIVE_OF_THE_OWNER == role) {
                userdocRepresentatives.clear();
            }

            if (Objects.nonNull(attorneyData) && UserdocPersonRole.hasAttorneyData(role)) {
                representative.setAttorneyPowerTerm(attorneyData.getAttorneyPowerTerm());
                representative.setReauthorizationRight(attorneyData.getReauthorizationRight());
                representative.setPriorReprsRevocation(attorneyData.getPriorReprsRevocation());
                representative.setAuthorizationCondition(attorneyData.getAuthorizationCondition());
            }
            userdocRepresentatives.add(representative);
        }
    }

    private static CPerson selectAgentByAgentCodeAndName(String agentCodeAndName, PersonService personService) {
        if (StringUtils.isEmpty(agentCodeAndName)) {
            throw new RuntimeException("AgentCodeAndName value is empty ! ");
        }

        int firstIndexOfComma = agentCodeAndName.indexOf(DefaultValue.COMMA);
        String agentCode = agentCodeAndName.substring(0, firstIndexOfComma).trim();

        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setOnlyAgent(true);
        cCriteriaPerson.setAgentCode(agentCode);

        List<CPerson> persons = personService.findPersons(cCriteriaPerson);
        if (CollectionUtils.isEmpty(persons) || persons.size() != 1)
            throw new RuntimeException("Problem with agent findPersons result !");

        return persons.get(0);
    }

    public static CUserdocPerson selectUserdocPerson(Integer personNbr, Integer addressNbr, List<CUserdocPerson> personList, UserdocPersonRole role) {
        return personList.stream()
                .filter(userdocPerson -> userdocPerson.getRole() == role)
                .filter(userdocPerson -> Objects.equals(userdocPerson.getPerson().getPersonNbr(), personNbr) && Objects.equals(userdocPerson.getPerson().getAddressNbr(), addressNbr))
                .findFirst()
                .orElse(null);
    }

    public static CAuthor selectAuthor(Integer personNbr, Integer addressNbr, List<CAuthor> cAuthors) {
        if (CollectionUtils.isEmpty(cAuthors))
            return null;

        return cAuthors.stream()
                .filter(author -> Objects.equals(author.getPerson().getPersonNbr(), personNbr) && Objects.equals(author.getPerson().getAddressNbr(), addressNbr))
                .findFirst()
                .orElse(null);
    }

    public static CAuthor selectAuthorBySequenceNumber(Long number, List<CAuthor> cAuthors) {
        if (CollectionUtils.isEmpty(cAuthors))
            return null;

        return cAuthors.stream()
                .filter(cAuthor -> Objects.nonNull(cAuthor.getAuthorSeq()))
                .filter(author -> author.getAuthorSeq().equals(number))
                .findFirst()
                .orElse(null);
    }

    public static String concatenatePersonNames(List<CPerson> people) {
        if (CollectionUtils.isEmpty(people))
            return null;

        return people.stream()
                .map(CPerson::getPersonName)
                .collect(Collectors.joining(","));
    }

    public static void saveApplicant(HttpServletRequest request, CPerson cPerson, String sessionIdentifier, PersonService personService, TempID tempID, boolean importFirstMatchedPerson) {
        COwnershipData ownershipData = PersonSessionUtils.getSessionOwnershipData(request, sessionIdentifier);
        if (Objects.nonNull(cPerson.getPersonNbr()) && Objects.nonNull(cPerson.getAddressNbr())) {
            List<COwner> ownerList = ownershipData.getOwnerList();
            if (CollectionUtils.isEmpty(ownerList))
                throw new RuntimeException("How is possible to update owner when owner list is empty ?");

            COwner cOwner = OwnerUtils.selectOwnerFromList(cPerson.getPersonNbr(), cPerson.getAddressNbr(), ownerList);
            if (Objects.isNull(cOwner))
                throw new RuntimeException("Cannot find owner for update !");

            cOwner.setPerson(cPerson);
        } else {
            fillPersonID(cPerson, personService, tempID, importFirstMatchedPerson);
            addPersonToOwnershipData(cPerson, ownershipData);
        }
    }

    public static void saveRepresentative(HttpServletRequest request, RepresentativeType representativeType, CPerson cPerson, String sessionIdentifier,
                                          PersonService personService, TempID tempID, boolean importFirstMatchedPerson, CRepresentationData representationDataParam) {
        CRepresentationData representationData = null;
        if (Objects.nonNull(representationDataParam)) {
            representationData = representationDataParam;
        } else {
            representationData = PersonSessionUtils.getSessionRepresentationData(request, sessionIdentifier);
        }

        if (Objects.nonNull(cPerson.getPersonNbr()) && Objects.nonNull(cPerson.getAddressNbr())) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            if (CollectionUtils.isEmpty(representativeList))
                throw new RuntimeException("How is possible to update representative when representative list is empty ? PN: " + cPerson.getPersonNbr());

            CRepresentative cRepresentative = RepresentativeUtils.selectRepresentativeFromList(cPerson.getPersonNbr(), cPerson.getAddressNbr(), representativeList);
            if (Objects.isNull(cRepresentative))
                throw new RuntimeException("Cannot find representative for update ! PN: " + cPerson.getPersonNbr());

            boolean hasAgentCode = cRepresentative.getPerson().hasAgentCode();
            if (hasAgentCode)
                throw new RuntimeException("It is not allowed to update representatives with agentCode! PN: " + cPerson.getPersonNbr());

            cRepresentative.setPerson(cPerson);
        } else {
            fillPersonID(cPerson, personService, tempID, importFirstMatchedPerson);
            addPersonToRepresentationData(representativeType, cPerson, representationData,request,sessionIdentifier);
        }
    }

    public static void addPersonToRepresentationData(RepresentativeType representativeType, CPerson cPerson, CRepresentationData representationData,HttpServletRequest request,String sessionIdentifier) {
        List<CRepresentative> representativeList = representationData.getRepresentativeList();
        if (Objects.isNull(representativeList)) {
            representativeList = new ArrayList<>();
            representationData.setRepresentativeList(representativeList);// Important for session object
        }

        CRepresentative existingRepresentative = RepresentativeUtils.selectRepresentativeFromList(cPerson.getPersonNbr(), cPerson.getAddressNbr(), representativeList);
        if (Objects.isNull(existingRepresentative)) {
            CRepresentative cRepresentative = new CRepresentative();
            cRepresentative.setRepresentativeType(representativeType.getValue());
            cRepresentative.setPerson(cPerson);
            CReprsPowerOfAttorney attorneyData = PersonSessionUtils.getSessionAttorneyData(request, sessionIdentifier);
            if (Objects.nonNull(attorneyData)) {
                cRepresentative.setAttorneyPowerTerm(attorneyData.getAttorneyPowerTerm());
                cRepresentative.setReauthorizationRight(attorneyData.getReauthorizationRight());
                cRepresentative.setPriorReprsRevocation(attorneyData.getPriorReprsRevocation());
                cRepresentative.setAuthorizationCondition(attorneyData.getAuthorizationCondition());
            }
            PersonSessionUtils.removeSessionAttorneyData(request, sessionIdentifier);
            representativeList.add(cRepresentative);
        }
    }


    public static void addPersonToUserdocPersonListByRole(CPerson cPerson, List<CUserdocPerson> persons, UserdocPersonRole role, RepresentativeType representativeType, HttpServletRequest request, String sessionIdentifier) {
        CUserdocPerson existing = PersonUtils.selectUserdocPerson(cPerson.getPersonNbr(), cPerson.getAddressNbr(), persons, role);
        if (Objects.isNull(existing)) {
            CUserdocPerson cUserdocPerson = new CUserdocPerson();
            cUserdocPerson.setRole(role);
            cUserdocPerson.setPerson(cPerson);
            if (Objects.nonNull(representativeType)) {
                cUserdocPerson.setRepresentativeType(representativeType.getValue());
            }
            if (UserdocPersonRole.hasAttorneyData(role)) {
                CReprsPowerOfAttorney attorneyData = PersonSessionUtils.getSessionAttorneyData(request, sessionIdentifier);
                if (Objects.nonNull(attorneyData)) {
                    cUserdocPerson.setAttorneyPowerTerm(attorneyData.getAttorneyPowerTerm());
                    cUserdocPerson.setReauthorizationRight(attorneyData.getReauthorizationRight());
                    cUserdocPerson.setPriorReprsRevocation(attorneyData.getPriorReprsRevocation());
                    cUserdocPerson.setAuthorizationCondition(attorneyData.getAuthorizationCondition());
                }
                PersonSessionUtils.removeSessionAttorneyData(request, sessionIdentifier);
            }

            persons.add(cUserdocPerson);
        }
    }


    public static void fillOwnershipDataPersonIds(COwnershipData ownershipData, PersonService personService, TempID tempID, boolean importFirstMatchedPerson) {
        if (Objects.nonNull(ownershipData) && !CollectionUtils.isEmpty(ownershipData.getOwnerList())) {
            for (COwner owner : ownershipData.getOwnerList()) {
                if (Objects.nonNull(owner) && Objects.nonNull(owner.getPerson())) {
                    fillPersonID(owner.getPerson(), personService, tempID, importFirstMatchedPerson);
                }
            }
        }
    }

    public static void addPersonToOwnershipData(CPerson cPerson, COwnershipData ownershipData) {
        List<COwner> ownerList = ownershipData.getOwnerList();
        if (Objects.isNull(ownerList)) {
            ownerList = new ArrayList<>();
            ownershipData.setOwnerList(ownerList);// Important for session object
        }

        COwner existingOwner = OwnerUtils.selectOwnerFromList(cPerson.getPersonNbr(), cPerson.getAddressNbr(), ownerList);
        if (Objects.isNull(existingOwner)) {
            COwner cOwner = new COwner();
            cOwner.setOrderNbr(OwnerUtils.selectNextOrderNumber(ownerList));
            cOwner.setPerson(cPerson);
            ownerList.add(cOwner);
        }
    }

    public static Comparator<PersonAddressResult> agentComparator() {
        return (o1, o2) -> {
            String agentCode = o1.getAgentCode();
            String anotherAgentCode = o2.getAgentCode();
            if (agentCode.startsWith(DefaultValue.PARTNERSHIP_PREFIX) || anotherAgentCode.startsWith(DefaultValue.PARTNERSHIP_PREFIX)) {
                return agentCode.compareTo(anotherAgentCode);
            } else {
                int agentCodeInt = Integer.parseInt(agentCode);
                int anotherAgentCodeInt = Integer.parseInt(anotherAgentCode);
                return anotherAgentCodeInt - agentCodeInt;
            }
        };
    }

    public static void addInventorToList(CPerson person, CAuthorshipData authorshipData) {
        List<CAuthor> authorList = authorshipData.getAuthorList();
        if (Objects.isNull(authorList)) {
            authorList = new ArrayList<>();
            authorshipData.setAuthorList(authorList);// Important for session object
        }

        CAuthor existingInventor = PersonUtils.selectAuthor(person.getPersonNbr(), person.getAddressNbr(), authorList);
        if (Objects.isNull(existingInventor)) {
            CAuthor cAuthor = new CAuthor();
            Long maxAuthorSeq = authorList.stream()
                    .map(CAuthor::getAuthorSeq)
                    .filter(Objects::nonNull)
                    .max(Comparator.comparing(Long::valueOf))
                    .orElse(Long.valueOf(0));

            cAuthor.setAuthorSeq(++maxAuthorSeq);
            cAuthor.setPerson(person);
            authorList.add(cAuthor);
        }

    }

    public static void fillPersonID(CPerson cPerson, PersonService personService, TempID tempID, boolean importFirstMatchedPerson) {
        CCriteriaPerson cCriteriaPerson = SearchPersonUtils.createPersonSearchCriteria(cPerson);
        if (SearchPersonUtils.isOnlyNameProvidedInSearchCriteria(cCriteriaPerson)) {// It is not good idea to auto import person only by name
            setTemporaryPersonId(cPerson, tempID);
        } else {
            List<CPerson> persons = personService.findPersons(cCriteriaPerson);
            if (!CollectionUtils.isEmpty(persons)) {
                if (persons.size() == DefaultValue.ONE_RESULT_SIZE || importFirstMatchedPerson) {
                    CPerson person = persons.get(DefaultValue.FIRST_RESULT);
                    cPerson.setPersonNbr(person.getPersonNbr());
                    cPerson.setAddressNbr(person.getAddressNbr());
                } else {
                    throw new RuntimeException("Person result list size is greater than 1 ! Please enter correct data or validate it earlier !");
                }
            } else {
                setTemporaryPersonId(cPerson, tempID);
            }
        }
    }

    public static void setTemporaryPersonId(CPerson cPerson, TempID tempID) {
        //Set temporary person and address number! Negative values !
        Integer value = tempID.getValue();
        cPerson.setPersonNbr(value);
        cPerson.setAddressNbr(value);
        tempID.setValue(--value);
    }

    public static UserdocPersonRole selectUserdocPersonRoleByPersonKind(PersonKind kind) {
        switch (kind) {
            case Applicant:
                return UserdocPersonRole.APPLICANT;
            case Grantor:
                return UserdocPersonRole.GRANTOR;
            case Grantee:
                return UserdocPersonRole.GRANTEE;
            case Payer:
                return UserdocPersonRole.PAYER;
            case Payee:
                return UserdocPersonRole.PAYEE;
            case Pledger:
                return UserdocPersonRole.PLEDGER;
            case Creditor:
                return UserdocPersonRole.CREDITOR;
            case NewOwner:
                return UserdocPersonRole.NEW_OWNER;
            case OldOwner:
                return UserdocPersonRole.OLD_OWNER;
            case Representative:
                return UserdocPersonRole.REPRESENTATIVE;
            case OldRepresentative:
                return UserdocPersonRole.OLD_REPRESENTATIVE;
            case NewRepresentative:
                return UserdocPersonRole.NEW_REPRESENTATIVE;
            case OldCorrespondenceAddress:
                return UserdocPersonRole.OLD_CORRESPONDENCE_ADDRESS;
            case RepresentativeOfTheOwner:
                return UserdocPersonRole.REPRESENTATIVE_OF_THE_OWNER;
            case NewCorrespondenceAddress:
                return UserdocPersonRole.NEW_CORRESPONDENCE_ADDRESS;
            case AffectedInventor:
                return UserdocPersonRole.AFFECTED_INVENTOR;
            default:
                throw new RuntimeException("Cannot find userdoc perso role for kind " + kind);
        }
    }

    public static void synchronizeAllUserdocPersonsWithSameId(HttpServletRequest request, String sessionIdentifier, CPerson newPerson, UserdocPersonRole personRole) {
        Integer newPersonNbr = newPerson.getPersonNbr();
        Integer newAddressNumber = newPerson.getAddressNbr();
        if (Objects.nonNull(newPersonNbr) && Objects.nonNull(newAddressNumber)) {
            List<UserdocPersonRole> roles = UserdocPersonRole.selectAll();
            for (UserdocPersonRole role : roles) {
                List<CUserdocPerson> sessionPersons = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
                if (!CollectionUtils.isEmpty(sessionPersons)) {
                    for (CUserdocPerson userdocPerson : sessionPersons) {
                        CPerson person = userdocPerson.getPerson();
                        if (Objects.nonNull(person)) {
                            if (Objects.equals(newPersonNbr, person.getPersonNbr()) && Objects.equals(newAddressNumber, person.getAddressNbr())) {
                                userdocPerson.setPerson(newPerson);
                            } else if ((Objects.nonNull(newPerson.getTempParentPersonNbr()) && person.getPersonNbr().equals(newPerson.getTempParentPersonNbr()))) {
                                userdocPerson.setPerson(newPerson);
                            }
                        }
                    }
                }
            }
            PersonSessionUtils.updateSessionServicePerson(request, newPerson, sessionIdentifier);
        }
    }

    public static String getIndividualTypeLabelForPersonFrom(MessageSource messageSource, CPerson person) {
        if (person.isCompany()) {
            if (person.isForeigner()) {
                return DefaultValue.EMPTY_STRING;
            } else {
                return messageSource.getMessage("person.eik", null, LocaleContextHolder.getLocale());
            }
        } else {
            if (person.isForeigner()) {
                return messageSource.getMessage("person.lnch", null, LocaleContextHolder.getLocale());
            } else {
                return messageSource.getMessage("person.egn", null, LocaleContextHolder.getLocale());
            }
        }
    }
}
