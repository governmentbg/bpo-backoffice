package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.mapper.ipbase.IntellectualPropertyRepresentativeMapper;
import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentReprs;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PersonAddressMapper.class)
public abstract class PatentRepresentativeMapper extends IntellectualPropertyRepresentativeMapper<IpPatentReprs> {

}
