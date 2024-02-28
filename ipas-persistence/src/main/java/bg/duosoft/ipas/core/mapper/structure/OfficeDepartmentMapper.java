package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanInverseMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.SimpleUserMapper;
import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentExtended;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                SimpleUserMapper.class,
                StringToBooleanInverseMapper.class,
                OfficeDepartmentPkMapper.class
})
public abstract class OfficeDepartmentMapper extends OfficeStructureBaseMapper {

    @Mapping(target = "name",                                       source = "name")
    @Mapping(target = "active",                                     source = "indInactive")
    @Mapping(target = "signatureUser",                              source = "signatureUser")
    @Mapping(target = "officeStructureId",                          source = "cfOfficeDepartmentPK")
    @BeanMapping(ignoreByDefault = true)
    public abstract OfficeDepartment toCore(CfOfficeDepartmentExtended cfOfficeDepartment);


    @InheritInverseConfiguration(name = "toCore")
    @Mapping(target = "rowVersion",                                 constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract CfOfficeDepartmentExtended toEntity(OfficeDepartment officeDepartment);


    @Mapping(target = "name",                                       source = "name")
    @Mapping(target = "indInactive",            source = "active")
    @Mapping(target = "signatureUser",                              source = "signatureUser")
    @BeanMapping(ignoreByDefault = true)
    public abstract void fillEntityBean(OfficeDepartment source, @MappingTarget CfOfficeDepartmentExtended target);


    public abstract List<OfficeDepartment> toCoreList(List<CfOfficeDepartmentExtended> cfOfficeDepartmentList);

    @InheritInverseConfiguration
    public abstract List<CfOfficeDepartmentExtended> toEntityList(List<OfficeDepartment> officeDepartmentList);


}
