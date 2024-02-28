package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPerson;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPersonPK;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = PersonAddressMapper.class)
public abstract class UserdocPersonMapper {

    @Mapping(source = "ipPersonAddresses", target = "person")
    @Mapping(source = "pk.role", target = "role")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "representativeType", target = "representativeType")
    @Mapping(source = "attorneyPowerTerm", target = "attorneyPowerTerm")
    @Mapping(source = "reauthorizationRight", target = "reauthorizationRight")
    @Mapping(source = "priorReprsRevocation", target = "priorReprsRevocation")
    @Mapping(source = "authorizationCondition", target = "authorizationCondition")
    @Mapping(expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipUserdocPerson.getIndMain()))", target = "indMain")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocPerson toCore(IpUserdocPerson ipUserdocPerson);

    @InheritInverseConfiguration
    @Mapping(target = "pk.personNbr", source = "person.personNbr")
    @Mapping(target = "pk.addrNbr", source = "person.addressNbr")
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(source = "representativeType", target = "representativeType")
    @Mapping(expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(userdocPerson.getIndMain()))", target = "indMain")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocPerson toEntity(CUserdocPerson userdocPerson);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpUserdocPerson> toEntityList(List<CUserdocPerson> userdocPersonList);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CUserdocPerson> toCoreList(List<IpUserdocPerson> ipUserdocPersonList);

    @AfterMapping
    protected void afterToCore(@MappingTarget CUserdocPerson target, IpUserdocPerson source) {

    }


}
