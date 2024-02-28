package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.mapper.miscellaneous.LocarnoClassesMapper;
import bg.duosoft.ipas.core.model.design.CPatentLocarnoClasses;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentLocarnoClasses;
import org.mapstruct.*;
import java.util.List;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {LocarnoClassesMapper.class})
public abstract class PatentLocarnoClassesMapper {

    @Mapping(target = "locarnoClassCode",         source = "pk.locarnoClassCode")
    @Mapping(target = "locarnoEditionCode",       source = "locarnoEditionCode")
    @Mapping(target = "locWPublishValidated",     source = "locWPublishValidated")
    @Mapping(target = "locarnoClasses",           source = "locarnoClasses")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPatentLocarnoClasses toCore(IpPatentLocarnoClasses ipPatentLocarnoClasses);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    public abstract IpPatentLocarnoClasses toEntity(CPatentLocarnoClasses cPatentLocarnoClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CPatentLocarnoClasses> toCoreList(List<IpPatentLocarnoClasses> ipPatentLocarnoClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpPatentLocarnoClasses> toEntityList(List<CPatentLocarnoClasses> cPatentLocarnoClasses);
}
