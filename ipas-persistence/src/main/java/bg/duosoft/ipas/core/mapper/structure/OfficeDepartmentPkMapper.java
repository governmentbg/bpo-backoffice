package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentPK;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: ggeorgiev
 * Date: 8.7.2019 г.
 * Time: 14:03
 */
@Mapper(componentModel = "spring")
public abstract class OfficeDepartmentPkMapper {
    @Mapping(target = "officeDivisionCode",       source = "officeDivisionCode")
    @Mapping(target = "officeDepartmentCode",     source = "officeDepartmentCode")
    @BeanMapping(ignoreByDefault = true)
    public abstract OfficeStructureId toCore(CfOfficeDepartmentPK entity);

    @InheritInverseConfiguration
    public abstract CfOfficeDepartmentPK toEntity(OfficeStructureId core);
}
