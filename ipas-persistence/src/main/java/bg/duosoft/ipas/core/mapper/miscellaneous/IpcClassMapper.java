package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.model.miscellaneous.CIpcClass;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassIpc;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class IpcClassMapper {
    @Mapping(target = "ipcEdition", source = "pk.ipcEditionCode")
    @Mapping(target = "ipcSection", source = "pk.ipcSectionCode")
    @Mapping(target = "ipcClass", source = "pk.ipcClassCode")
    @Mapping(target = "ipcSubclass", source = "pk.ipcSubclassCode")
    @Mapping(target = "ipcGroup", source = "pk.ipcGroupCode")
    @Mapping(target = "ipcSubgroup", source = "pk.ipcSubgroupCode")
    @Mapping(target = "ipcVersionCalculated", source = "ipcLatestVersion")
    @Mapping(target = "ipcEditionOriginal", source = "pk.ipcEditionCode")
    @Mapping(target = "ipcSymbolDescription", source = "ipcName")
    @BeanMapping(ignoreByDefault = true)
    public abstract void toCore(CfClassIpc cfClassIpc, @MappingTarget CIpcClass cIpcClass);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    public abstract CfClassIpc toEntity(CIpcClass cIpcClass);
}
