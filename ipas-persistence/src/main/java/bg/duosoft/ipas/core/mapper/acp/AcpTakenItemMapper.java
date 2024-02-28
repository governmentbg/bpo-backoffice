package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpTakenItem;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpTakenItem;
import org.mapstruct.*;
import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {AcpTakenItemStorageMapper.class,AcpTakenItemTypeMapper.class})
public abstract class AcpTakenItemMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",  source = "pk.id")
    @Mapping(target = "type",  source = "type")
    @Mapping(target = "typeDescription",  source = "typeDescription")
    @Mapping(target = "count",  source = "count")
    @Mapping(target = "storage",  source = "storage")
    @Mapping(target = "storageDescription",  source = "storageDescription")
    @Mapping(target = "forDestruction",  source = "forDestruction")
    @Mapping(target = "returned",  source = "returned")
    @Mapping(target = "inStock",  source = "inStock")
    public abstract CAcpTakenItem toCore(AcpTakenItem acpTakenItem);


    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract AcpTakenItem toEntity(CAcpTakenItem cAcpTakenItem);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAcpTakenItem> toCoreList(List<AcpTakenItem> acpTakenItems);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<AcpTakenItem> toEntityList(List<CAcpTakenItem> cAcpTakenItems);

}

