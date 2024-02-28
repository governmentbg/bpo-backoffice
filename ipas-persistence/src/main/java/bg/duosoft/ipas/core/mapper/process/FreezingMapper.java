package bg.duosoft.ipas.core.mapper.process;

import bg.duosoft.ipas.core.model.process.CProcessFreezing;
import bg.duosoft.ipas.core.model.process.CProcessFrozen;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcFreezes;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public abstract class FreezingMapper {

    /*TODO
       String freezingProcessDescription
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "freezingProcessId.processType", source = "pk.freezingProcTyp")
    @Mapping(target = "freezingProcessId.processNbr", source = "pk.freezingProcNbr")
    @Mapping(target = "indContinueWhenEnd", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipProcFreezes.getIndFreezeContinueWhenEnd()))")
    @Mapping(target = "indNoOffidoc", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipProcFreezes.getIndFreezeNoOffidoc()))")
    @Mapping(target = "freezingProcessId", ignore = true)//Removes warning message
    public abstract CProcessFreezing toCore(IpProcFreezes ipProcFreezes);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CProcessFreezing> toCoreList(List<IpProcFreezes> ipProcFreezes);

    @AfterMapping
    protected void afterToCore(@MappingTarget CProcessFrozen target, IpProcFreezes source) {
        //TODO description
    }

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "indFreezeContinueWhenEnd", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cProcessFreezing.getIndContinueWhenEnd()))")
    @Mapping(target = "indFreezeNoOffidoc", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cProcessFreezing.getIndNoOffidoc()))")
    @Mapping(target = "rowVersion", constant = "1")
    public abstract IpProcFreezes toEntity(CProcessFreezing cProcessFreezing);

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    public abstract List<IpProcFreezes> toEntityList(List<CProcessFreezing> cProcessFreezings);

}
