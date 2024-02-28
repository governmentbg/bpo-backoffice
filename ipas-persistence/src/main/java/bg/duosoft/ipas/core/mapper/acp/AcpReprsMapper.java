package bg.duosoft.ipas.core.mapper.acp;


import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpReprs;
import org.mapstruct.*;
import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {PersonAddressMapper.class})
public abstract class AcpReprsMapper {

    @Mapping(source = "ipPersonAddresses", target = "person")
    @Mapping(source = "pk.representativeTyp", target = "representativeType")
    @BeanMapping(ignoreByDefault = true)
    public abstract CRepresentative toCore(AcpReprs repr);

    @InheritInverseConfiguration
    @Mapping(target = "pk.personNbr", source = "person.personNbr")
    @Mapping(target = "pk.addrNbr", source = "person.addressNbr")
    @BeanMapping(ignoreByDefault = true)
    public abstract AcpReprs toEntity(CRepresentative representative);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<AcpReprs> toEntityList(List<CRepresentative> representatives);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CRepresentative> toCoreList(List<AcpReprs> reprs);
}
