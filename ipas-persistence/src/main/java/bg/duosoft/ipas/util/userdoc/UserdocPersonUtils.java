package bg.duosoft.ipas.util.userdoc;

import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.userdoc.*;
import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserdocPersonUtils {

    public static List<CUserdocPerson> selectRepresentatives(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.REPRESENTATIVE);
    }

    public static List<CUserdocPerson> selectOldRepresentatives(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.OLD_REPRESENTATIVE);
    }

    public static List<CUserdocPerson> selectNewRepresentatives(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.NEW_REPRESENTATIVE);
    }

    public static List<CUserdocPerson> selectRepresentativesOfTheOwner(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.REPRESENTATIVE_OF_THE_OWNER);
    }

    public static List<CUserdocPerson> selectOldOwners(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.OLD_OWNER);
    }

    public static List<CUserdocPerson> selectNewOwners(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.NEW_OWNER);
    }

    public static List<CUserdocPerson> selectOldCorrespondenceAddress(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.OLD_CORRESPONDENCE_ADDRESS);
    }

    public static List<CUserdocPerson> selectNewCorrespondenceAddress(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.NEW_CORRESPONDENCE_ADDRESS);
    }

    public static List<CUserdocPerson> selectPledgers(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.PLEDGER);
    }

    public static List<CUserdocPerson> selectCreditors(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.CREDITOR);
    }

    public static List<CUserdocPerson> selectPayers(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.PAYER);
    }

    public static List<CUserdocPerson> selectPayees(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.PAYEE);
    }

    public static List<CUserdocPerson> selectGrantees(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.GRANTEE);
    }

    public static List<CUserdocPerson> selectGrantors(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.GRANTOR);
    }

    public static List<CUserdocPerson> selectApplicants(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.APPLICANT);
    }

    public static List<CUserdocPerson> selectAffectedInventors(CUserdocPersonData userdocPersonData) {
        return selectUserdocPersonsByRole(userdocPersonData, UserdocPersonRole.AFFECTED_INVENTOR);
    }

    public static List<CUserdocPerson> selectUserdocPersonsByRole(CUserdocPersonData userdocPersonData, UserdocPersonRole role) {
        if (Objects.isNull(userdocPersonData) || CollectionUtils.isEmpty(userdocPersonData.getPersonList()))
            return new ArrayList<>();

        return userdocPersonData.getPersonList()
                .stream()
                .filter(userdocPerson -> userdocPerson.getRole() == role)
                .collect(Collectors.toList());
    }

    public static boolean checkIfCAExistInUserdocPersons(CUserdoc userdoc) {
        CPerson servicePerson = userdoc.getServicePerson();
        if (Objects.nonNull(servicePerson)) {
            List<CUserdocPerson> personList = userdoc.getUserdocPersonData().getPersonList();
            return isCAExistsInUserdocPersons(servicePerson, personList);
        }
        return false;
    }

    public static boolean isCAExistsInUserdocPersons(CPerson servicePerson, List<CUserdocPerson> userdocPersonList) {
        if (!CollectionUtils.isEmpty(userdocPersonList)) {
            CUserdocPerson existingRepresentative = userdocPersonList.stream()
                    .filter(cUserdocPerson ->
                            Objects.equals(cUserdocPerson.getPerson().getPersonNbr(), servicePerson.getPersonNbr()) &&
                                    Objects.equals(cUserdocPerson.getPerson().getAddressNbr(), servicePerson.getAddressNbr()))
                    .findFirst()
                    .orElse(null);

            if (Objects.nonNull(existingRepresentative))
                return true;
        }
        return false;
    }

    public static boolean isCurrentPersonCA(Integer personNbr, Integer addressNbr, CUserdoc userdoc) {
        CPerson servicePerson = userdoc.getServicePerson();
        if (Objects.isNull(servicePerson))
            return false;

        return Objects.equals(personNbr, servicePerson.getPersonNbr()) && Objects.equals(addressNbr, servicePerson.getAddressNbr());
    }

    public static CUserdocPerson convertToUserdocPerson(CRepresentative representative, UserdocPersonRole role, Boolean indMain, String notes) {
        CUserdocPerson userdocPerson = new CUserdocPerson();
        userdocPerson.setPerson(representative.getPerson());
        userdocPerson.setRole(role);
        userdocPerson.setIndMain(indMain);
        userdocPerson.setNotes(notes);
        userdocPerson.setRepresentativeType(representative.getRepresentativeType());
        userdocPerson.setAttorneyPowerTerm(representative.getAttorneyPowerTerm());
        userdocPerson.setReauthorizationRight(representative.getReauthorizationRight());
        userdocPerson.setPriorReprsRevocation(representative.getPriorReprsRevocation());
        userdocPerson.setAuthorizationCondition(representative.getAuthorizationCondition());
        return userdocPerson;
    }

    public static CUserdocPerson convertToUserdocPerson(CPerson person, UserdocPersonRole role, Boolean indMain, String notes, RepresentativeType representativeType) {
        CUserdocPerson userdocPerson = new CUserdocPerson();
        userdocPerson.setPerson(person);
        userdocPerson.setRole(role);
        userdocPerson.setIndMain(indMain);
        userdocPerson.setNotes(notes);
        if (Objects.nonNull(representativeType)) {
            userdocPerson.setRepresentativeType(representativeType.getValue());
        }
        return userdocPerson;
    }

    public static CUserdocPerson convertToUserdocPerson(CUserdocPerson userdocPerson, UserdocPersonRole role, Boolean indMain, String notes, RepresentativeType representativeType) {
        CUserdocPerson convertedUserdocPerson = new CUserdocPerson();
        convertedUserdocPerson.setPerson(userdocPerson.getPerson());
        convertedUserdocPerson.setRole(role);
        convertedUserdocPerson.setIndMain(indMain);
        convertedUserdocPerson.setNotes(notes);
        if (Objects.nonNull(representativeType)) {
            convertedUserdocPerson.setRepresentativeType(representativeType.getValue());
        }
        convertedUserdocPerson.setAttorneyPowerTerm(userdocPerson.getAttorneyPowerTerm());
        convertedUserdocPerson.setReauthorizationRight(userdocPerson.getReauthorizationRight());
        convertedUserdocPerson.setPriorReprsRevocation(userdocPerson.getPriorReprsRevocation());
        convertedUserdocPerson.setAuthorizationCondition(userdocPerson.getAuthorizationCondition());
        return convertedUserdocPerson;
    }

    public static List<CUserdocPerson> convertToUserdocPerson(List<CPerson> persons, UserdocPersonRole role, RepresentativeType representativeType) {
        if (CollectionUtils.isEmpty(persons))
            return null;

        List<CUserdocPerson> userdocPersons = new ArrayList<>();
        for (CPerson person : persons) {
            userdocPersons.add(convertToUserdocPerson(person, role, false, null, representativeType));
        }

        return userdocPersons;
    }

    public static List<CUserdocPerson> convertToUserdocPerson(List<CRepresentative> representatives, UserdocPersonRole role) {
        if (CollectionUtils.isEmpty(representatives))
            return null;

        List<CUserdocPerson> userdocPersons = new ArrayList<>();
        for (CRepresentative representative : representatives) {
            userdocPersons.add(convertToUserdocPerson(representative, role, false, null));
        }

        return userdocPersons;
    }

    public static List<UserdocPersonRole> selectPersonRoleEnums(List<CUserdocPersonRole> cUserdocPersonRoles) {
        return cUserdocPersonRoles.stream()
                .map(CUserdocPersonRole::getRole)
                .collect(Collectors.toList());
    }

    public static void removeUserdocPersonsByRole(CUserdocPersonData userdocPersonData, UserdocPersonRole role) {
        userdocPersonData.getPersonList().removeIf(cUserdocPerson -> cUserdocPerson.getRole() == role);
    }

    public static CUserdocPersonRole selectPersonRoleObject(UserdocPersonRole role, CUserdoc userdoc) {
        if (Objects.isNull(userdoc)) {
            return null;
        }
        CUserdocType userdocType = userdoc.getUserdocType();
        if (Objects.isNull(userdocType)) {
            return null;
        }
        List<CUserdocPersonRole> roles = userdocType.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        return roles.stream()
                .filter(userdocPersonRole -> userdocPersonRole.getRole() == role)
                .findFirst()
                .orElse(null);
    }

    public static boolean isNewOwnerConnectedToMainObjectOwner(CPerson newOwnerPerson, List<CPerson> mainObjectOwners) {
        if (CollectionUtils.isEmpty(mainObjectOwners))
            return false;

        Integer newOwnerLastVersion = newOwnerPerson.getPersonLastVersion();
        CPerson mainObjectOwnerWithSameLastVersion = mainObjectOwners.stream()
                .filter(cPerson -> cPerson.getPersonLastVersion().equals(newOwnerLastVersion))
                .findFirst()
                .orElse(null);

        return Objects.nonNull(mainObjectOwnerWithSameLastVersion);
    }


    public static boolean checkAgentAndPartnershipIfValidType(List<CUserdocPerson> userdocPersons) {
        if (CollectionUtils.isEmpty(userdocPersons)){
            return true;
        }
        boolean result = true;
        for (CUserdocPerson userdocPerson : userdocPersons) {
            UserdocPersonRole role = userdocPerson.getRole();
            switch (role) {
                case OLD_REPRESENTATIVE:
                case NEW_REPRESENTATIVE:
                case REPRESENTATIVE_OF_THE_OWNER:
                case REPRESENTATIVE:
                    if (Objects.nonNull(userdocPerson.getRepresentativeType()) &&
                            !userdocPerson.getRepresentativeType().equals(RepresentativeType.PARTNERSHIP.getValue()) &&
                            !userdocPerson.getRepresentativeType().equals(RepresentativeType.NATURAL_PERSON.getValue()) &&
                            Objects.nonNull(userdocPerson.getPerson()) && (Objects.nonNull(userdocPerson.getPerson().getAgentCode()) || Objects.nonNull(userdocPerson.getPerson().getPartnershipType()))) {
                        result = false;
                        break;
                    }
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    public static boolean areAllLawyersPhysicalPersons(List<CUserdocPerson> userdocPersons) {
        if (CollectionUtils.isEmpty(userdocPersons))
            return true;

        boolean result = true;
        for (CUserdocPerson userdocPerson : userdocPersons) {
            UserdocPersonRole role = userdocPerson.getRole();
            switch (role) {
                case OLD_REPRESENTATIVE:
                case NEW_REPRESENTATIVE:
                case REPRESENTATIVE_OF_THE_OWNER:
                case REPRESENTATIVE:
                    if (RepresentativeType.LAWYER.getValue().equals(userdocPerson.getRepresentativeType())) {
                        CPerson person = userdocPerson.getPerson();
                        if (Objects.nonNull(person)) {
                            Boolean indCompany = person.getIndCompany();
                            if (Objects.isNull(indCompany) || indCompany) {
                                result = false;
                                break;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    public static List<COwner> selectNewOwnersAsCOwners(CUserdocPersonData personData) {
        List<CUserdocPerson> newOwners = selectNewOwners(personData);
        if (CollectionUtils.isEmpty(newOwners))
            return null;

        int counter = 1;
        List<COwner> result = new ArrayList<>();
        for (CUserdocPerson newOwner : newOwners) {
            COwner cOwner = new COwner();
            cOwner.setPerson(newOwner.getPerson());
            cOwner.setOrderNbr(counter);
            result.add(cOwner);
            counter++;
        }

        return result;
    }

    public static List<CRepresentative> selectNewAndCurrentRepresentativesAsCRepresentatives(CUserdocPersonData personData) {
        List<CUserdocPerson> newRepresentatives = selectNewRepresentatives(personData);
        List<CUserdocPerson> representatives = selectRepresentatives(personData);
        List<CUserdocPerson> allRepresentatives = Stream.of(newRepresentatives, representatives).flatMap(Collection::stream).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(allRepresentatives)) {
            return null;
        }

        List<CRepresentative> result = new ArrayList<>();
        for (CUserdocPerson representative : allRepresentatives) {
            CRepresentative cRepresentative = new CRepresentative();
            cRepresentative.setPerson(representative.getPerson());
            cRepresentative.setRepresentativeType(representative.getRepresentativeType());
            cRepresentative.setAttorneyPowerTerm(representative.getAttorneyPowerTerm());
            cRepresentative.setReauthorizationRight(representative.getReauthorizationRight());
            cRepresentative.setPriorReprsRevocation(representative.getPriorReprsRevocation());
            cRepresentative.setAuthorizationCondition(representative.getAuthorizationCondition());
            result.add(cRepresentative);
        }

        return result;
    }

}
