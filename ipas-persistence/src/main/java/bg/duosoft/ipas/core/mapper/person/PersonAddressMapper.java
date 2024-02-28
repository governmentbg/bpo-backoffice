package bg.duosoft.ipas.core.mapper.person;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.common.StringToLongMapper;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.enums.PartnershipType;
import bg.duosoft.ipas.persistence.model.entity.ext.agent.CfPartnershipType;
import bg.duosoft.ipas.persistence.model.entity.ext.agent.ExtendedPartnership;
import bg.duosoft.ipas.persistence.model.entity.person.IpAgent;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static bg.duosoft.ipas.util.DefaultValue.PARTNERSHIP_PREFIX;

@Mapper(componentModel = "spring", uses = {StringToLongMapper.class})
public abstract class PersonAddressMapper {

    @Autowired
    private PersonService personService;

    @Mapping(source = "ipPerson.personName", target = "personName")
    @Mapping(source = "ipPerson.nationalityCountryCode", target = "nationalityCountryCode")
    @Mapping(source = "ipPerson.legalNature", target = "legalNature")
    @Mapping(source = "addrStreet", target = "addressStreet")
    @Mapping(source = "zipcode", target = "zipCode")
    @Mapping(source = "stateName", target = "stateName")
    @Mapping(source = "stateCode", target = "stateCode")
    @Mapping(source = "addrZone", target = "addressZone")
    @Mapping(source = "ipPerson.email", target = "email")
    @Mapping(source = "ipPerson.telephone", target = "telephone")
    @Mapping(source = "ipPerson.companyRegisterDate", target = "companyRegisterRegistrationDate")
    @Mapping(source = "ipPerson.companyRegisterNbr", target = "companyRegisterRegistrationNbr")
    @Mapping(source = "residenceCountry.countryCode", target = "residenceCountryCode")
    @Mapping(source = "cityCode", target = "cityCode")
    @Mapping(source = "cityName", target = "cityName")
    @Mapping(source = "ipPerson.personNameLang2", target = "personNameInOtherLang")
    @Mapping(source = "ipPerson.legalNatureLang2", target = "legalNatureInOtherLang")
    @Mapping(source = "addrStreetLang2", target = "addressStreetInOtherLang")
    @Mapping(source = "pk.personNbr", target = "personNbr")
    @Mapping(source = "pk.addrNbr", target = "addressNbr")
    @Mapping(source = "ipPerson.gralPersonIdNbr", target = "gralPersonIdNbr")
    @Mapping(source = "ipPerson.gralPersonIdTyp", target = "gralPersonIdTyp")
    @Mapping(source = "ipPerson.indiPersonIdNbr", target = "individualIdNbr")
    @Mapping(source = "ipPerson.indiPersonIdTxt", target = "individualIdTxt")
    @Mapping(source = "ipPerson.indiPersonIdTyp", target = "individualIdType")
    @Mapping(source = "tempParentPersonNbr", target = "tempParentPersonNbr")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPerson toCore(IpPersonAddresses personAddress);

    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "ipPerson.personNbr", source = "personNbr")
    @Mapping(target = "ipPerson.rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpPersonAddresses toEntity(CPerson cPerson);

    @AfterMapping
    protected void afterToCore(IpPersonAddresses source, @MappingTarget CPerson target) {
        target.setIndCompany("M".equals(source.getIpPerson().getPersonWcode()));
        if (source.getIpPerson().getIpAgent() != null) {
            target.setAgentCode(source.getIpPerson().getIpAgent().getAgentCode().toString());
            target.setIsAgentInactive(MapperHelper.getTextAsBoolean(source.getIpPerson().getIpAgent().getIndInactive()));
        } else {
            ExtendedPartnership extendedPartnership = source.getIpPerson().getExtendedPartnership();
            if (Objects.nonNull(extendedPartnership)) {
                target.setAgentCode(extendedPartnership.getPartnershipCode());
                target.setIsAgentInactive(MapperHelper.getTextAsBoolean(extendedPartnership.getIndInactive()));
                setAfterToCorePartnershipType(target, source);
            }
        }

        setPersonLastVersion(target);
    }

    private void setAfterToCorePartnershipType(CPerson target, IpPersonAddresses source) {
        ExtendedPartnership extendedPartnership = source.getIpPerson().getExtendedPartnership();
        CfPartnershipType partnershipType = extendedPartnership.getPartnershipType();
        if (Objects.nonNull(partnershipType)) {
            target.setPartnershipType(PartnershipType.selectByCode(partnershipType.getId()));
        }
    }

    @AfterMapping
    protected void afterToEntity(CPerson source, @MappingTarget IpPersonAddresses target) {
        if (Objects.nonNull(source.getIndCompany())) {
            target.getIpPerson().setPersonWcode(source.getIndCompany() ? "M" : "F");
        }
        if (!StringUtils.isEmpty(source.getAgentCode())) {
            if (source.getAgentCode().startsWith(PARTNERSHIP_PREFIX)) {
                target.getIpPerson().setExtendedPartnership(new ExtendedPartnership());
                target.getIpPerson().getExtendedPartnership().setPersonNbr(source.getPersonNbr());
                target.getIpPerson().getExtendedPartnership().setPartnershipCode(source.getAgentCode());
                setAfterToEntityPartnershipType(source, target);
            } else {
                target.getIpPerson().setIpAgent(new IpAgent());
                target.getIpPerson().getIpAgent().setAgentCode(Integer.parseInt(source.getAgentCode()));
            }

        }
    }

    private void setAfterToEntityPartnershipType(CPerson source, IpPersonAddresses target) {
        PartnershipType partnershipType = source.getPartnershipType();
        if (Objects.nonNull(partnershipType)) {
            target.getIpPerson().getExtendedPartnership().setPartnershipType(new CfPartnershipType(partnershipType.code(), null, null));
        }
    }

    private void setPersonLastVersion(@MappingTarget CPerson target) {
        if (target.isOldVersion()) {
            target.setPersonLastVersion(personService.selectPersonNumberOfLastVersionOfPerson(target.getPersonNbr()));
        } else {
            target.setPersonLastVersion(target.getPersonNbr());
        }
    }
}
