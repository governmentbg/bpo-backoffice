package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.model.patent.CPatentCitation;
import bg.duosoft.ipas.persistence.model.entity.patent.*;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class RefExamMapper {

    @Mapping(target = "refNumber",         source = "pk.refNumber")
    @Mapping(target = "refDescription",       source = "refDescription")
    @Mapping(target = "refClaims",     source = "refClaims")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPatentCitation toCore(IpPatentRefExam source);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "refCategory", constant = "D")
    public abstract IpPatentRefExam toEntity(CPatentCitation source);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CPatentCitation> toCoreList(List<IpPatentRefExam> source);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpPatentRefExam> toEntityList(List<CPatentCitation> source);
}
