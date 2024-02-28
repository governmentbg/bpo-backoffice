package bg.duosoft.ipas.util.person;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OwnerUtils {

    public static Integer selectNextOrderNumber(List<COwner> ownerList) {
        Integer maxOrderNumber = ownerList.stream()
                .map(COwner::getOrderNbr)
                .filter(Objects::nonNull)
                .max(Comparator.comparing(Integer::valueOf))
                .orElse(DefaultValue.DEFAULT_ORDER_NUMBER);

        return maxOrderNumber + DefaultValue.ORDER_NUMBER_INCREMENTAL;
    }

    public static Integer getMinOrderNumber(List<COwner> cOwners) {
        if (CollectionUtils.isEmpty(cOwners))
            return null;

        Integer minOrderNumber = cOwners.stream()
                .map(COwner::getOrderNbr)
                .filter(Objects::nonNull)
                .min(Comparator.comparing(Integer::valueOf))
                .orElse(null);

        if (Objects.isNull(minOrderNumber))
            return DefaultValue.DEFAULT_ORDER_NUMBER + DefaultValue.ORDER_NUMBER_INCREMENTAL;

        return minOrderNumber;
    }

    public static COwner convertToOwner(COwnershipData ownershipData, CPerson person) {
        COwner owner = new COwner();
        owner.setOrderNbr(selectNextOrderNumber(ownershipData.getOwnerList()));
        owner.setPerson(person);
        return owner;
    }

    public static List<CPerson> convertToCPersonList(COwnershipData ownershipData) {
        if (Objects.isNull(ownershipData)) {
            return null;
        }

        List<COwner> ownerList = ownershipData.getOwnerList();
        if (CollectionUtils.isEmpty(ownerList)) {
            return null;
        }

        return ownerList.stream().map(COwner::getPerson).collect(Collectors.toList());
    }

    public static COwnershipData createOwnershipData(List<CPerson> persons) {
        if (CollectionUtils.isEmpty(persons))
            return null;

        COwnershipData ownershipData = new COwnershipData();
        ownershipData.setOwnerList(new ArrayList<>());

        for (CPerson person : persons) {
            COwner existingOwner = selectOwnerFromList(person.getPersonNbr(), person.getAddressNbr(), ownershipData.getOwnerList());
            if (Objects.isNull(existingOwner)) {
                COwner cOwner = convertToOwner(ownershipData, person);
                ownershipData.getOwnerList().add(cOwner);
            }
        }

        return ownershipData;
    }

    public static COwner selectOwnerFromList(Integer personNbr, Integer addressNbr, List<COwner> ownerList) {
        if (CollectionUtils.isEmpty(ownerList))
            return null;

        return ownerList.stream()
                .filter(cOwner -> Objects.equals(cOwner.getPerson().getPersonNbr(), personNbr) && Objects.equals(cOwner.getPerson().getAddressNbr(), addressNbr))
                .findFirst()
                .orElse(null);
    }

    public static boolean isCorrespondenceAddressOwner(CPerson servicePerson, COwnershipData ownershipData) {
        if (Objects.isNull(servicePerson) || Objects.isNull(ownershipData))
            return false;

        Integer personNbr = servicePerson.getPersonNbr();
        Integer addressNbr = servicePerson.getAddressNbr();
        COwner cOwner = selectOwnerFromList(personNbr, addressNbr, ownershipData.getOwnerList());
        return Objects.nonNull(cOwner);
    }

    public static COwner selectFirstOwner(COwnershipData ownershipData) {
        if (Objects.isNull(ownershipData))
            return null;

        List<COwner> ownerList = ownershipData.getOwnerList();
        if (CollectionUtils.isEmpty(ownerList))
            return null;

        return ownerList.get(DefaultValue.FIRST_RESULT);
    }

    public static CPerson selectFirstCPersonOwner(COwnershipData ownershipData) {
        COwner firstOwner = selectFirstOwner(ownershipData);
        if (Objects.isNull(firstOwner))
            return null;

        return firstOwner.getPerson();
    }
}