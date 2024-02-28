package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.mapper.ipbase.IntellectualPropertyPriorityMapper;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkPriorities;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class MarkParisPriorityMapper extends IntellectualPropertyPriorityMapper<IpMarkPriorities> {

}
