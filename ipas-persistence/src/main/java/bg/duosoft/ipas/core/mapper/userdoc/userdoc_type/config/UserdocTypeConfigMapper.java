package bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.config;


import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.userdoc.config.CUserdocTypeConfig;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocTypeConfig;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        UserdocTypeDepartmentMapper.class,
        StringToBooleanMapper.class})
public abstract class UserdocTypeConfigMapper {

    @Mapping(target = "userdocTyp",  source = "userdocTyp")
    @Mapping(target = "registerToProcess",  source = "registerToProcess")
    @Mapping(target = "markInheritResponsibleUser",  source = "markInheritResponsibleUser")
    @Mapping(target = "inheritResponsibleUser",  source = "inheritResponsibleUser")
    @Mapping(target = "abdocsUserTargetingOnRegistration",  source = "abdocsUserTargetingOnRegistration")
    @Mapping(target = "abdocsUserTargetingOnResponsibleUserChange",  source = "abdocsUserTargetingOnResponsibleUserChange")
    @Mapping(target = "departments",  source = "departments")
    @Mapping(target = "hasPublicLiabilities",  source = "publicLiabilities")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocTypeConfig toCore(CfUserdocTypeConfig cfUserdocTypeConfig);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfUserdocTypeConfig toEntity(CUserdocTypeConfig cUserdocTypeConfig);

}
