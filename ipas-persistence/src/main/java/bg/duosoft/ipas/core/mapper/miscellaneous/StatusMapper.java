package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatus;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        StatusIdMapper.class
})
public abstract class StatusMapper {

    /*TODO
       Boolen indPending
     */
    @Mapping(target = "statusId", source = "pk")
    @Mapping(target = "statusName", source = "statusName")
    @Mapping(target = "indResponsibleRequired", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(cfStatus.getIndResponsibleReq()))")
    @Mapping(target = "triggerActivityWcode", source = "triggerActivityWcode")
    @Mapping(target = "processResultType", source = "processResultTyp")
    @BeanMapping(ignoreByDefault = true)
    public abstract CStatus toCore(CfStatus cfStatus);

    public abstract List<CStatus> toCoreList(List<CfStatus> cfStatuses);

    @InheritInverseConfiguration
    @Mapping(target = "indResponsibleReq", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cStatus.getIndResponsibleRequired()))")
    @BeanMapping(ignoreByDefault = true)
    public abstract CfStatus toEntity(CStatus cStatus);

    @InheritInverseConfiguration
    public abstract List<CfStatus> toEntityList(List<CStatus> cStatuses);
}
