package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.mapper.ipbase.IntellectualPropertyPriorityMapper;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentPriorities;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PatentPriorityMapper extends IntellectualPropertyPriorityMapper<IpPatentPriorities> {

}
