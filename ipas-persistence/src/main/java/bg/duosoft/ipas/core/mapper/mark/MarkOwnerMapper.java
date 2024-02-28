package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.mapper.ipbase.IntellectualPropertyOwnerMapper;
import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkOwners;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PersonAddressMapper.class)
public abstract class MarkOwnerMapper extends IntellectualPropertyOwnerMapper<IpMarkOwners> {

}
