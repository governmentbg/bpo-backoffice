package bg.duosoft.ipas.core.mapper.process;

import bg.duosoft.ipas.core.model.process.CProcessFrozen;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcFreezes;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public abstract class FrozenMapper {

    /*TODO
       String frozenProcessDescription
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "frozenProcessId.processType", source = "pk.frozenProcTyp")
    @Mapping(target = "frozenProcessId.processNbr", source = "pk.frozenProcNbr")
    @Mapping(target = "indContinueWhenEnd", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipProcFreezes.getIndFreezeContinueWhenEnd()))")
    @Mapping(target = "indNoOffidoc", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipProcFreezes.getIndFreezeNoOffidoc()))")
    @Mapping(target = "frozenProcessId", ignore = true)//Removes warning message
    public abstract CProcessFrozen toCore(IpProcFreezes ipProcFreezes);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CProcessFrozen> toCoreList(List<IpProcFreezes> ipProcFreezes);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "indFreezeContinueWhenEnd", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cProcessFrozen.getIndContinueWhenEnd()))")
    @Mapping(target = "indFreezeNoOffidoc", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cProcessFrozen.getIndNoOffidoc()))")
    @Mapping(target = "rowVersion", constant = "1")
    public abstract IpProcFreezes toEntity(CProcessFrozen cProcessFrozen);

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    public abstract List<IpProcFreezes> toEntityList(List<CProcessFrozen> cProcessFrozens);

}
