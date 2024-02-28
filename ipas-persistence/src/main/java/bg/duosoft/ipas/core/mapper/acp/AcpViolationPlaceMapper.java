package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpViolationPlace;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpViolationPlace;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {})
public abstract class AcpViolationPlaceMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",  source = "pk.id")
    @Mapping(target = "description",  source = "description")
    public abstract CAcpViolationPlace toCore(AcpViolationPlace acpViolationPlace);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract AcpViolationPlace toEntity(CAcpViolationPlace acpViolationPlace);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAcpViolationPlace> toCoreList(List<AcpViolationPlace> acpViolationPlaces);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<AcpViolationPlace> toEntityList(List<CAcpViolationPlace> acpViolationPlaces);

}
