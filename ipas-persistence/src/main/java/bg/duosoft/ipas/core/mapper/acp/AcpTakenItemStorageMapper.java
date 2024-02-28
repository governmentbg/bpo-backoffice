package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpTakenItemStorage;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpTakenItemStorage;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {})
public abstract class AcpTakenItemStorageMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "name",  source = "name")
    public abstract CAcpTakenItemStorage toCore(CfAcpTakenItemStorage storage);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfAcpTakenItemStorage toEntity(CAcpTakenItemStorage storage);


    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAcpTakenItemStorage> toCoreList(List<CfAcpTakenItemStorage> storages);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfAcpTakenItemStorage> toEntityList(List<CAcpTakenItemStorage> storages);


}

