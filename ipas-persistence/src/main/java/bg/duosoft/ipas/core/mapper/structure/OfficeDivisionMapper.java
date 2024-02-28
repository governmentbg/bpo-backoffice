package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanInverseMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.SimpleUserMapper;
import bg.duosoft.ipas.core.model.structure.OfficeDivision;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDivisionExtended;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                SimpleUserMapper.class,
                StringToBooleanInverseMapper.class
        })
public abstract class OfficeDivisionMapper extends OfficeStructureBaseMapper {

    @Mapping(target = "officeStructureId.officeDivisionCode",       source = "officeDivisionCode")
    @Mapping(target = "name",                                       source = "name")
    @Mapping(target = "active",                                     source = "indInactive")
    @Mapping(target = "signatureUser",                              source = "signatureUser")

    @BeanMapping(ignoreByDefault = true)
    public abstract OfficeDivision toCore(CfOfficeDivisionExtended cfOfficeDivision);

    @InheritInverseConfiguration
    @Mapping(target = "rowVersion",                                 constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract CfOfficeDivisionExtended toEntity(OfficeDivision officeDivision);


    @Mapping(target = "name",                                       source = "name")
    @Mapping(target = "indInactive",                                source = "active")
    @Mapping(target = "signatureUser",                              source = "signatureUser")
    @BeanMapping(ignoreByDefault = true)
    public abstract void fillEntityBean(OfficeDivision source, @MappingTarget CfOfficeDivisionExtended target);

    public abstract List<OfficeDivision> toCoreList(List<CfOfficeDivisionExtended> cfOfficeDivisionList);

    @InheritInverseConfiguration
    public abstract List<CfOfficeDivisionExtended> toEntityList(List<OfficeDivision> officeDivisionList);
}
