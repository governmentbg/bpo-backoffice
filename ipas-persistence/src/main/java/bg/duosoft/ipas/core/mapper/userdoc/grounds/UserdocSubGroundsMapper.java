package bg.duosoft.ipas.core.mapper.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocSubGrounds;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpUserdocSubGrounds;
import org.mapstruct.*;
import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class UserdocSubGroundsMapper {


    @Mapping(target = "rootGroundId",  source = "pk.rootGroundId")
    @Mapping(target = "legalGroundTypeId",  source = "pk.legalGroundTypeId")
    @Mapping(target = "legalGroundTypeTitle",  source = "cfLegalGroundType.title")
    @Mapping(target = "legalGroundTypeDescription",  source = "cfLegalGroundType.description")
    @Mapping(target = "version",  source = "cfLegalGroundType.version")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocSubGrounds toCore(IpUserdocSubGrounds ipUserdocSubGround);

    @InheritInverseConfiguration
    @Mapping(target = "cfLegalGroundType.id",  source = "legalGroundTypeId")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocSubGrounds toEntity(CUserdocSubGrounds cUserdocSubGround);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CUserdocSubGrounds> toCoreList(List<IpUserdocSubGrounds> ipUserdocSubGrounds);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpUserdocSubGrounds> toEntityList(List<CUserdocSubGrounds> cUserdocSubGrounds);

}
