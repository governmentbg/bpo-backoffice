package bg.duosoft.ipas.core.mapper.userdoc.userdoc_type;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.config.UserdocTypeConfigMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                StringToBooleanMapper.class,
                InvalidationRelationToStringMapper.class,
                UserdocGroupToStringMapper.class,
                UserdocToUiPanelMapper.class,
                UserdocTypeConfigMapper.class,
                UserdocToPersonRoleMapper.class
        })
public abstract class UserdocTypeNomenclatureMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "userdocType", source = "userdocTyp")
    @Mapping(target = "userdocName", source = "userdocName")
    @Mapping(target = "generateProcTyp", source = "generateProcTyp")
    @Mapping(target = "indInactive", source = "indInactive")
    @Mapping(target = "indChangesOwner", source = "indChangesOwner")
    @Mapping(target = "indChangesRepres", source = "indChangesRepres")
    @Mapping(target = "indRenewal", source = "indRenewal")
    @Mapping(target = "invalidatedUserdocTypes", source = "invalidationRelations")
    @Mapping(target = "userdocGroup", source = "userdocGroupName")
    @Mapping(target = "panels", source = "panels")
    @Mapping(target = "roles", source = "personRoles")
    @Mapping(target = "userdocTypeConfig", source = "userdocTypeConfig")
    public abstract CUserdocType toCore(CfUserdocType cfUserdocType);

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    public abstract CfUserdocType toEntity(CUserdocType cUserdocType);

    @InheritConfiguration(name = "toEntity")
    public abstract void fillEntityFields(CUserdocType source, @MappingTarget CfUserdocType target);

    @AfterMapping
    protected void afterToEntityMapper(CUserdocType source, @MappingTarget CfUserdocType target){
        target.getInvalidationRelations().forEach(r -> r.getPk().setUserdocType(source.getUserdocType()));
        target.getPanels().forEach(p -> p.getPk().setUserdocTyp(source.getUserdocType()));
        target.getPersonRoles().forEach(r -> r.getPk().setUserdocTyp(source.getUserdocType()));
    }

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CUserdocType> toCoreList(List<CfUserdocType> cfUserdocTypes);

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    public abstract List<CfUserdocType> toEntityList(List<CUserdocType> cUserdocTypes);

}
