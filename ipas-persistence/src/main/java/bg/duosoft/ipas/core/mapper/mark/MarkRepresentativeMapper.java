package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.mapper.ipbase.IntellectualPropertyRepresentativeMapper;
import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkReprs;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PersonAddressMapper.class)
public abstract class MarkRepresentativeMapper extends IntellectualPropertyRepresentativeMapper<IpMarkReprs> {

}
