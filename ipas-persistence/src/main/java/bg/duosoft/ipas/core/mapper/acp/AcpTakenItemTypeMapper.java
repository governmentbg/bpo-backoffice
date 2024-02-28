package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpTakenItemType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpTakenItemType;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {})
public abstract class AcpTakenItemTypeMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "name",  source = "name")
    public abstract CAcpTakenItemType toCore(CfAcpTakenItemType type);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfAcpTakenItemType toEntity(CAcpTakenItemType type);


    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAcpTakenItemType> toCoreList(List<CfAcpTakenItemType> types);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfAcpTakenItemType> toEntityList(List<CAcpTakenItemType> types);


}

