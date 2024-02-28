package bg.duosoft.ipas.core.mapper.userdoc.court_appeal;

import bg.duosoft.ipas.core.mapper.miscellaneous.CourtsMapper;
import bg.duosoft.ipas.core.model.userdoc.court_appeal.CUserdocCourtAppeal;
import bg.duosoft.ipas.persistence.model.entity.userdoc.court_appeals.IpUserdocCourtAppeal;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {
        CourtsMapper.class,
        JudicialActTypeMapper.class})
public abstract class UserdocCourtAppealMapper {


    @Mapping(target = "courtCaseNbr",     source = "courtCaseNbr")
    @Mapping(target = "courtCaseDate",    source = "courtCaseDate")
    @Mapping(target = "judicialActNbr",   source = "judicialActNbr")
    @Mapping(target = "judicialActDate",  source = "judicialActDate")
    @Mapping(target = "courtLink",    source = "courtLink")
    @Mapping(target = "court",            source = "court")
    @Mapping(target = "judicialActType",  source = "judicialActType")
    @Mapping(target = "courtAppealId",    source = "pk.courtAppealId")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocCourtAppeal toCore(IpUserdocCourtAppeal ipUserdocCourtAppeal);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocCourtAppeal toEntity(CUserdocCourtAppeal cUserdocCourtAppeal);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CUserdocCourtAppeal> toCoreList(List<IpUserdocCourtAppeal> ipUserdocCourtAppeals);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpUserdocCourtAppeal> toEntityList(List<CUserdocCourtAppeal> cUserdocCourtAppeals);

}
