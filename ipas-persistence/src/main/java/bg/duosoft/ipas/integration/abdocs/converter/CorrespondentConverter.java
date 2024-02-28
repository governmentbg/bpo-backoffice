package bg.duosoft.ipas.integration.abdocs.converter;

import bg.duosoft.abdocs.model.*;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.miscellaneous.CSettlement;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.reception.CCorrespondentType;
import bg.duosoft.ipas.core.model.reception.CReceptionCorrespondent;
import bg.duosoft.ipas.core.service.nomenclature.SettlementService;
import bg.duosoft.ipas.integration.abdocs.utils.CorrespondentUtils;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.person.OwnerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CorrespondentConverter {

    @Autowired
    private SettlementService settlementService;

    public List<DocCorrespondents> convertRepresentationDataToDocCorrespondent(CRepresentationData representationData) {
        List<CRepresentative> representativeList = representationData.getRepresentativeList();
        if (CollectionUtils.isEmpty(representativeList))
            return new ArrayList<>();

        return representativeList.stream()
                .filter(cRepresentative -> Objects.nonNull(cRepresentative.getPerson()))
                .map(cRepresentative -> convertPersonToDocCorrespondent(cRepresentative.getPerson(), DocCorrespondentType.Representative))
                .collect(Collectors.toList());
    }

    public COwnershipData convertToCOwnershipData(List<DocCorrespondents> correspondents) {
        if (CollectionUtils.isEmpty(correspondents)) {
            return null;
        }
        return OwnerUtils.createOwnershipData(correspondents.stream()
                .map(this::convertToCPerson)
                .collect(Collectors.toList())
        );
    }

    public List<DocCorrespondents> convertOwnershipDataToDocCorrespondent(COwnershipData cOwnershipData) {
        List<COwner> ownerList = cOwnershipData.getOwnerList();
        if (CollectionUtils.isEmpty(ownerList))
            return new ArrayList<>();

        return ownerList.stream()
                .filter(cOwner -> Objects.nonNull(cOwner.getPerson()))
                .map(cOwner -> convertPersonToDocCorrespondent(cOwner.getPerson(), DocCorrespondentType.Applicant))
                .collect(Collectors.toList());
    }

    public DocCorrespondents convertPersonToDocCorrespondent(CPerson person, DocCorrespondentType type) {
        DocCorrespondents docCorrespondent = new DocCorrespondents();
        Correspondent correspondent = convertPersonToCorrespondent(person);
        docCorrespondent.setCorrespondent(correspondent);
        docCorrespondent.setCorrespondentContactId(String.valueOf(person.getAddressNbr()));
        docCorrespondent.setDocCorrespondentType(type);
        return docCorrespondent;
    }

    public Correspondent convertPersonToCorrespondent(CPerson person) {
        Correspondent correspondent = new Correspondent();
        correspondent.setCorrespondentType(CorrespondentUtils.generateCorrespondentType(person.getNationalityCountryCode(), person.getIndCompany()));
        correspondent.setCorrespondentGroupId(3);//TODO
        correspondent.setExternalId(person.getPersonNbr());

        Boolean indCompany = person.getIndCompany();
        if (Objects.nonNull(indCompany) && indCompany) {
            correspondent.setName(person.getPersonName());
        } else {
            CorrespondentUtils.fillNameParts(correspondent, false, person.getPersonName());
        }

        CorrespondentContact correspondentContact = new CorrespondentContact();
        correspondentContact.setEmail(person.getEmail());
        correspondentContact.setName(person.getPersonName());
        correspondentContact.setPhone(person.getTelephone());
        correspondentContact.setAddress(person.getAddressStreet());
        correspondentContact.setExternalId(person.getAddressNbr());
        correspondentContact.setPostCode(person.getZipCode());
        correspondentContact.setCountry(new CountryDto(person.getNationalityCountryCode()));

        String cityName = person.getCityName();
        if (!StringUtils.isEmpty(cityName)) {
            if (person.getNationalityCountryCode().equals(DefaultValue.BULGARIA_CODE)) {
                List<CSettlement> cSettlements = settlementService.selectByNameOrSettlementName(cityName);
                if (!CollectionUtils.isEmpty(cSettlements) && cSettlements.size() == 1) {
                    correspondentContact.setSettlementId(cSettlements.get(0).getId());
                } else {
                    String[] split = cityName.split(",");
                    if (split.length == 3) {
                        CSettlement cSettlement = settlementService.selectBySettlementMunicipalityAndDistrict(split[0].trim(), split[1].trim(), split[2].trim());
                        if (Objects.nonNull(cSettlement)) {
                            correspondentContact.setSettlementId(cSettlement.getId());
                        } else {
                            concatenateSettlementToAddress(correspondentContact, cityName);
                        }
                    } else {
                        concatenateSettlementToAddress(correspondentContact, cityName);
                    }
                }
            } else {
                correspondentContact.setForeignerSettlement(cityName);
            }
        }

        correspondent.setCorrespondentContacts(Collections.singletonList(correspondentContact));
        return correspondent;
    }

    private void concatenateSettlementToAddress(CorrespondentContact correspondentContact, String settlementName) {
        String address = correspondentContact.getAddress();
        correspondentContact.setAddress(address + ", " + settlementName);
    }

    public CReceptionCorrespondent convertToReceptionCorrespondent(DocCorrespondents docCorrespondent, Integer receptionRequestId, Integer receptionUserdocRequestId) {
        if (Objects.isNull(docCorrespondent))
            return null;

        CReceptionCorrespondent receptionCorrespondent = new CReceptionCorrespondent();
        receptionCorrespondent.setPersonNbr(docCorrespondent.getCorrespondent().getExternalId());

        List<CorrespondentContact> correspondentContacts = docCorrespondent.getCorrespondent().getCorrespondentContacts();
        if (!CollectionUtils.isEmpty(correspondentContacts)) {
            CorrespondentContact correspondentContact = correspondentContacts.get(0);
            receptionCorrespondent.setAddressNbr(correspondentContact.getExternalId());
        }

        DocCorrespondentType docCorrespondentType = docCorrespondent.getDocCorrespondentType();
        switch (docCorrespondentType) {
            case Applicant:
            case Representative:
                receptionCorrespondent.setCorrespondentType(CCorrespondentType.builder().id(docCorrespondentType.value()).build());
                break;
            case WithoutType:
                receptionCorrespondent.setCorrespondentType(CCorrespondentType.builder().id(DocCorrespondentType.Applicant.value()).build());
                break;
        }

        receptionCorrespondent.setReceptionUserdocRequestId(receptionUserdocRequestId);
        receptionCorrespondent.setReceptionRequestId(receptionRequestId);
        return receptionCorrespondent;
    }

    public CPerson convertToCPerson(DocCorrespondents docCorrespondent) {
        if (Objects.isNull(docCorrespondent))
            return null;

        CPerson person = new CPerson();
        Correspondent correspondent = docCorrespondent.getCorrespondent();
        List<CorrespondentContact> correspondentContacts = docCorrespondent.getCorrespondent().getCorrespondentContacts();

        person.setPersonNbr(correspondent.getExternalId());
        if (!CollectionUtils.isEmpty(correspondentContacts)) {
            CorrespondentContact firstContact = correspondentContacts.get(0);
            person.setAddressNbr(firstContact.getExternalId());
            person.setNationalityCountryCode(Objects.isNull(firstContact.getCountry()) ? DefaultValue.BULGARIA_CODE : firstContact.getCountry().getCode());
            person.setResidenceCountryCode(person.getNationalityCountryCode());// Same as nationalityCountry
            person.setAddressStreet(firstContact.getAddress());
            person.setZipCode(firstContact.getPostCode());
            person.setEmail(firstContact.getEmail());
            person.setTelephone(firstContact.getPhone());

                if (person.getNationalityCountryCode().equalsIgnoreCase(DefaultValue.BULGARIA_CODE)) {
                    Integer settlementId = firstContact.getSettlementId();
                    if (Objects.nonNull(settlementId)) {
                        CSettlement cSettlement = settlementService.selectById(settlementId);
                        if (Objects.nonNull(cSettlement)) {
                            Boolean isdistrict = cSettlement.getIsdistrict();
                            if (Objects.nonNull(isdistrict) && isdistrict) {
                                person.setCityName(cSettlement.getSettlementname());
                            } else {
                                String personCity = cSettlement.getName() + ", " + cSettlement.getMunicipalitycode().getName() + ", " + cSettlement.getDistrictcode().getName();
                                person.setCityName(personCity);
                            }
                        }
                    }
                } else {
                    person.setCityName(firstContact.getForeignerSettlement());
                }


        }

        person.setPersonName(docCorrespondent.getCorrespondent().getName());
        person.setIndCompany(CorrespondentUtils.isCorrespondentLegalEntity(docCorrespondent.getCorrespondent().getCorrespondentType()));
        return person;
    }

}
