package bg.duosoft.ipas.core.mapper.patent;


import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentInventors;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;


@Mapper(componentModel = "spring" , uses = PersonAddressMapper.class)
public abstract class AuthorMapper {

    @Mapping(target = "person", source = "ipPersonAddresses")
    @Mapping(target = "authorSeq", source = "seqNbr")
    @BeanMapping(ignoreByDefault = true)
    public abstract CAuthor toCore(IpPatentInventors inv);

    @InheritInverseConfiguration
    @Mapping(target = "pk.personNbr", source = "person.personNbr")
    @Mapping(target = "pk.addrNbr", source = "person.addressNbr")
    @Mapping(target = "rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpPatentInventors toEntity(CAuthor author);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAuthor> toCoreList(List<IpPatentInventors> ipPatentInventorsList);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpPatentInventors> toEntityList(List<CAuthor> cAuthors);

}



