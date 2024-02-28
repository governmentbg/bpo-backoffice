package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanInverseMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.SimpleUserMapper;
import bg.duosoft.ipas.core.model.structure.OfficeSection;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSectionExtended;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
uses = {
        SimpleUserMapper.class,
        StringToBooleanInverseMapper.class,
        OfficeSectionPkMapper.class
})
public abstract class OfficeSectionMapper extends OfficeStructureBaseMapper {
    @Mapping(target = "name",                                       source = "name")
    @Mapping(target = "active",                                     source = "indInactive")
    @Mapping(target = "signatureUser",                              source = "signatureUser")
    @Mapping(target = "officeStructureId",                          source = "cfOfficeSectionPK")
    @BeanMapping(ignoreByDefault = true)
    public abstract OfficeSection toCore(CfOfficeSectionExtended cfOfficeSection);

    @InheritInverseConfiguration(name = "toCore")
    @Mapping(target = "rowVersion",                                 constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract CfOfficeSectionExtended toEntity(OfficeSection officeSection);


    @Mapping(target = "name",                                       source = "name")
    @Mapping(target = "indInactive",                                source = "active")
    @Mapping(target = "signatureUser",                              source = "signatureUser")
    @BeanMapping(ignoreByDefault = true)
    public abstract void fillEntityBean(OfficeSection officeSection, @MappingTarget CfOfficeSectionExtended target);


    public abstract List<OfficeSection> toCoreList(List<CfOfficeSectionExtended> cfOfficeSectionList);

    @InheritInverseConfiguration
    public abstract List<CfOfficeSectionExtended> toEntityList(List<OfficeSection> officeSectionList);
}
