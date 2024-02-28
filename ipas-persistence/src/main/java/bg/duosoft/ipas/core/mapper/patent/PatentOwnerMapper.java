package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.mapper.ipbase.IntellectualPropertyOwnerMapper;
import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentOwners;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PersonAddressMapper.class)
public abstract class PatentOwnerMapper extends IntellectualPropertyOwnerMapper<IpPatentOwners> {

}
