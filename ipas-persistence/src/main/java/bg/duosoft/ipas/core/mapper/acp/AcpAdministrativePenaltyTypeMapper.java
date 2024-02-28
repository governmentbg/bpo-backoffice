package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenaltyType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpAdministrativePenaltyType;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {})
public abstract class AcpAdministrativePenaltyTypeMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "description",  source = "description")
    public abstract CAcpAdministrativePenaltyType toCore(CfAcpAdministrativePenaltyType type);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfAcpAdministrativePenaltyType toEntity(CAcpAdministrativePenaltyType type);


    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAcpAdministrativePenaltyType> toCoreList(List<CfAcpAdministrativePenaltyType> types);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfAcpAdministrativePenaltyType> toEntityList(List<CAcpAdministrativePenaltyType> types);


}

