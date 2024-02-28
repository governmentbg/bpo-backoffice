package bg.duosoft.ipas.core.mapper.person;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.person.CPersonAbdocsSync;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpPersonAbdocsSync;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = StringToBooleanMapper.class)
public abstract class PersonAbdocsSyncMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "personNbr", target = "personNbr")
    @Mapping(source = "addrNbr", target = "addrNbr")
    @Mapping(source = "insertedAt", target = "insertedAt")
    @Mapping(source = "processedAt", target = "processedAt")
    @Mapping(source = "indSync", target = "indSync")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPersonAbdocsSync toCore(IpPersonAbdocsSync ipPersonAbdocsSync);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpPersonAbdocsSync toEntity(CPersonAbdocsSync personAbdocsSync);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<IpPersonAbdocsSync> toEntityList(List<CPersonAbdocsSync> personAbdocsSyncs);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<CPersonAbdocsSync> toCoreList(List<IpPersonAbdocsSync> ipPersonAbdocsSyncs);


}
