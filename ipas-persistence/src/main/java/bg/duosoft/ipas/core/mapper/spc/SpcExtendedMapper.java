package bg.duosoft.ipas.core.mapper.spc;


import bg.duosoft.ipas.core.model.spc.CSpcExtended;
import bg.duosoft.ipas.persistence.model.entity.ext.spc.SpcExtended;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class SpcExtendedMapper {

    @Mapping(target = "bgPermitNumber",                         source = "bgPermitNumber")
    @Mapping(target = "bgPermitDate",                           source = "bgPermitDate")
    @Mapping(target = "euPermitNumber",                         source = "euPermitNumber")
    @Mapping(target = "euPermitDate",                           source = "euPermitDate")
    @Mapping(target = "productClaims",                           source = "productClaims")
    @Mapping(target = "bgNotificationDate",                      source = "bgNotificationDate")
    @Mapping(target = "euNotificationDate",                      source = "euNotificationDate")
    @BeanMapping(ignoreByDefault = true)
    public abstract CSpcExtended toCore(SpcExtended spcExtended);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract SpcExtended toEntity(CSpcExtended cSpcExtended);
}
