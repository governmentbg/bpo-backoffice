package bg.duosoft.ipas.util.person;

import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.services.core.Representative;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RepresentativeUtils {

    public static List<CPerson> convertToCPersonList(CRepresentationData representationData) {
        if (Objects.isNull(representationData)) {
            return null;
        }

        List<CRepresentative> representativeList = representationData.getRepresentativeList();
        if (CollectionUtils.isEmpty(representativeList)) {
            return null;
        }

        return representativeList.stream().map(CRepresentative::getPerson).collect(Collectors.toList());
    }

    public static CRepresentative selectRepresentativeFromList(Integer personNbr, Integer addressNbr, List<CRepresentative> representativeList) {
        if (CollectionUtils.isEmpty(representativeList))
            return null;

        return representativeList.stream()
                .filter(cRepresentative -> Objects.equals(cRepresentative.getPerson().getPersonNbr(), personNbr) && Objects.equals(cRepresentative.getPerson().getAddressNbr(), addressNbr))
                .findFirst()
                .orElse(null);
    }

    public static CRepresentative convertToRepresentative(CPerson person, RepresentativeType representativeType) {
        CRepresentative cRepresentative = new CRepresentative();
        cRepresentative.setPerson(person);

        if (Objects.nonNull(representativeType)) {
            cRepresentative.setRepresentativeType(representativeType.getValue());
        } else {
            if (Objects.nonNull(person.getAgentCode())) {
                if (person.getAgentCode().startsWith(DefaultValue.PARTNERSHIP_PREFIX)) {
                    cRepresentative.setRepresentativeType(RepresentativeType.PARTNERSHIP.getValue());
                } else {
                    cRepresentative.setRepresentativeType(RepresentativeType.NATURAL_PERSON.getValue());
                }
            }
        }
        return cRepresentative;
    }

    public static RepresentativeType selectDefaultRepresentativeType(CPerson person) {
        if (Objects.nonNull(person.getAgentCode())) {
            if (person.getAgentCode().startsWith(DefaultValue.PARTNERSHIP_PREFIX)) {
                return RepresentativeType.PARTNERSHIP;
            } else {
                return RepresentativeType.NATURAL_PERSON;
            }
        }
        return null;
    }

    public static RepresentativeType convertRepresentativeTypeValueToEnum(String value) {
        if (StringUtils.isEmpty(value))
            return null;

        return RepresentativeType.selectByCode(value);
    }

    public static boolean isCorrespondenceAddressRepresentative(CPerson servicePerson, CRepresentationData representationData) {
        if (Objects.isNull(servicePerson) || Objects.isNull(representationData))
            return false;

        Integer personNbr = servicePerson.getPersonNbr();
        Integer addressNbr = servicePerson.getAddressNbr();
        CRepresentative representative = selectRepresentativeFromList(personNbr, addressNbr, representationData.getRepresentativeList());
        return Objects.nonNull(representative);
    }

    public static CRepresentative selectFirstRepresentative(CRepresentationData representationData) {
        if (Objects.isNull(representationData))
            return null;

        List<CRepresentative> representativeList = representationData.getRepresentativeList();
        if (CollectionUtils.isEmpty(representativeList))
            return null;

        return representativeList.get(DefaultValue.FIRST_RESULT);
    }

    public static CPerson selectFirstCPersonRepresentative(CRepresentationData representationData) {
        CRepresentative first = selectFirstRepresentative(representationData);
        if (Objects.isNull(first))
            return null;

        return first.getPerson();
    }


    public static boolean checkAgentAndPartnershipIfValidType(List<CRepresentative> representatives) {
        if (CollectionUtils.isEmpty(representatives)) {
            return true;
        }
        boolean result = true;
        for (CRepresentative representative : representatives) {
            if (Objects.nonNull(representative.getRepresentativeType()) &&
                    !representative.getRepresentativeType().equals(RepresentativeType.PARTNERSHIP.getValue()) &&
                    !representative.getRepresentativeType().equals(RepresentativeType.NATURAL_PERSON.getValue()) &&
                    Objects.nonNull(representative.getPerson()) && (Objects.nonNull(representative.getPerson().getAgentCode()) || Objects.nonNull(representative.getPerson().getPartnershipType()))) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static boolean areAllLawyersPhysicalPersons(List<CRepresentative> representatives) {
        if (CollectionUtils.isEmpty(representatives))
            return true;

        boolean result = true;
        for (CRepresentative representative : representatives) {
            if (RepresentativeType.LAWYER.getValue().equals(representative.getRepresentativeType())) {
                CPerson person = representative.getPerson();
                if (Objects.nonNull(person)) {
                    Boolean indCompany = person.getIndCompany();
                    if (Objects.isNull(indCompany) || indCompany) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static boolean areAllAgentsForIntellectualProperty(List<CRepresentative> representatives) {
        if (CollectionUtils.isEmpty(representatives))
            return true;

        boolean result = true;
        for (CRepresentative representative : representatives) {
            if (RepresentativeType.NATURAL_PERSON.getValue().equals(representative.getRepresentativeType())) {
                CPerson person = representative.getPerson();
                if (Objects.nonNull(person)) {
                    if (!isAgentForIntellectualProperty(person.getAgentCode())) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static boolean areAllAgentsForIndustrialProperty(List<CRepresentative> representatives) {
        if (CollectionUtils.isEmpty(representatives))
            return true;

        boolean result = true;
        for (CRepresentative representative : representatives) {
            if (RepresentativeType.NATURAL_PERSON.getValue().equals(representative.getRepresentativeType())) {
                CPerson person = representative.getPerson();
                if (Objects.nonNull(person)) {
                    if (isAgentForIntellectualProperty(person.getAgentCode())) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }


    public static boolean isAgentForIntellectualProperty(String agentCode) {
        if (!StringUtils.hasText(agentCode)) {
            return false;
        }

        return Integer.parseInt(agentCode) > DefaultValue.INTELLECTUAL_PROPERTY_AGENT_START_NUMBER;
    }

}
