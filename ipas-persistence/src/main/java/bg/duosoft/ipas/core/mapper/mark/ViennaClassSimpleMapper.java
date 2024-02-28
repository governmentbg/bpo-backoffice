package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CViennaClassSimple;
import bg.duosoft.ipas.persistence.model.nonentity.ViennaClassSimple;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class ViennaClassSimpleMapper {

    @Mapping(target = "viennaClass", source = "viennaClass")
    @Mapping(target = "viennaDescription", source = "viennaDescription")
    @BeanMapping(ignoreByDefault = true)
    public abstract CViennaClassSimple toCore(ViennaClassSimple source);

    @InheritInverseConfiguration
    public abstract ViennaClassSimple toEntity(CViennaClassSimple source);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<ViennaClassSimple> toEntityList(List<CViennaClassSimple> viennaClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CViennaClassSimple> toCoreList(List<ViennaClassSimple> ipViennaClasses);
}
