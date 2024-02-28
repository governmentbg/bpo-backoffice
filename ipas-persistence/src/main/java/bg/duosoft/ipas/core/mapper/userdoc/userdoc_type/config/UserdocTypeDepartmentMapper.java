package bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.config;


import bg.duosoft.ipas.core.mapper.structure.OfficeDepartmentMapper;
import bg.duosoft.ipas.core.model.userdoc.config.CUserdocTypeDepartment;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocTypeDepartment;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {
        OfficeDepartmentMapper.class})
public abstract class UserdocTypeDepartmentMapper {

    @Mapping(target = "userdocTyp",  source = "pk.userdocTyp")
    @Mapping(target = "department",  source = "department")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocTypeDepartment toCore(CfUserdocTypeDepartment cfUserdocTypeDepartment);

    @InheritInverseConfiguration
    @Mapping(target = "pk.officeDepartmentCode",  source = "department.officeStructureId.officeDepartmentCode")
    @Mapping(target = "pk.officeDivisionCode",  source = "department.officeStructureId.officeDivisionCode")
    @BeanMapping(ignoreByDefault = true)
    public abstract CfUserdocTypeDepartment toEntity(CUserdocTypeDepartment cUserdocTypeDepartment);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CUserdocTypeDepartment> toCoreList(List<CfUserdocTypeDepartment> cfUserdocTypeDepartments);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfUserdocTypeDepartment> toEntityList(List<CUserdocTypeDepartment> cUserdocTypeDepartments);
}
