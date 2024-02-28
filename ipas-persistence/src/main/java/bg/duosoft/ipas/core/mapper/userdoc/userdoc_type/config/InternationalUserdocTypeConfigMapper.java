package bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.config;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.userdoc.config.CInternationalUserdocTypeConfig;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfInternationalUserdocTypeConfig;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        StringToBooleanMapper.class
})
public abstract class InternationalUserdocTypeConfigMapper {

    @Mapping(target = "userdocTyp",  source = "userdocTyp")
    @Mapping(target = "changeMarkOwner",  source = "changeMarkOwner")
    @Mapping(target = "changeMarkRepresentative",  source = "changeMarkRepresentative")
    @Mapping(target = "changeMarkRenewalExpirationDate",  source = "changeMarkRenewalExpirationDate")
    @Mapping(target = "insertMarkActionProtectionSurrender",  source = "insertMarkActionProtectionSurrender")
    @Mapping(target = "insertMarkActionCancellation",  source = "insertMarkActionCancellation")
    @Mapping(target = "insertMarkActionNonRenewalOfTrademark",  source = "insertMarkActionNonRenewalOfTrademark")
    @Mapping(target = "insertMarkActionNonRenewalOfContractingParty",  source = "insertMarkActionNonRenewalOfContractingParty")
    @Mapping(target = "internationalRegistration",  source = "internationalRegistration")
    @Mapping(target = "generateUserdocType",  source = "generateUserdocType")
    @BeanMapping(ignoreByDefault = true)
    public abstract CInternationalUserdocTypeConfig toCore(CfInternationalUserdocTypeConfig cfInternationalUserdocTypeConfig);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfInternationalUserdocTypeConfig toEntity(CInternationalUserdocTypeConfig cInternationalUserdocTypeConfig);
}
