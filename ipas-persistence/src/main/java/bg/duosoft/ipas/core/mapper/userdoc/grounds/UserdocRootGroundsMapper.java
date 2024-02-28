package bg.duosoft.ipas.core.mapper.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpUserdocRootGrounds;
import org.mapstruct.*;
import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {
        UserdocSubGroundsMapper.class,
        EarlierRightTypesMapper.class,
        MarkGroundDataMapper.class,
        PatentGroundDataMapper.class,
        SingleDesignGroundDataMapper.class,
        ApplicantAuthorityMapper.class})
public abstract class UserdocRootGroundsMapper {

    @Mapping(target = "rootGroundId",  source = "pk.rootGroundId")
    @Mapping(target = "motives",  source = "motives")
    @Mapping(target = "groundCommonText",  source = "groundCommonText")
    @Mapping(target = "userdocSubGrounds",  source = "userdocSubGrounds")
    @Mapping(target = "earlierRightType",  source = "earlierRightType")
    @Mapping(target = "applicantAuthority",  source = "applicantAuthority")
    @Mapping(target = "markGroundData",  source = "markGroundData")
    @Mapping(target = "patentGroundData",  source = "patentGroundData")
    @Mapping(target = "singleDesignGroundData",  source = "singleDesignGroundData")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocRootGrounds toCore(IpUserdocRootGrounds ipUserdocRootGround,@Context Boolean loadFileContent);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocRootGrounds toEntity(CUserdocRootGrounds cUserdocRootGround);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CUserdocRootGrounds> toCoreList(List<IpUserdocRootGrounds> ipUserdocRootGrounds,@Context Boolean loadFileContent);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpUserdocRootGrounds> toEntityList(List<CUserdocRootGrounds> cUserdocRootGrounds);

}
