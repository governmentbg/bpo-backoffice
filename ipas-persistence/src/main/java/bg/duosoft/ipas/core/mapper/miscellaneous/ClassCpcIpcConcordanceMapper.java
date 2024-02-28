package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.model.miscellaneous.CClassCpcIpcConcordance;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassCpcIpcConcordance;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class ClassCpcIpcConcordanceMapper {

    @Mapping(target = "cpcSectionCode", source = "pk.cpcSectionCode")
    @Mapping(target = "cpcClassCode", source = "pk.cpcClassCode")
    @Mapping(target = "cpcSubclassCode", source = "pk.cpcSubclassCode")
    @Mapping(target = "cpcGroupCode", source = "pk.cpcGroupCode")
    @Mapping(target = "cpcSubgroupCode", source = "pk.cpcSubgroupCode")
    @Mapping(target = "cpcSymbol", source = "cpcSymbol")
    @Mapping(target = "ipcSectionCode", source = "ipcSectionCode")
    @Mapping(target = "ipcClassCode", source = "ipcClassCode")
    @Mapping(target = "ipcSubclassCode", source = "ipcSubclassCode")
    @Mapping(target = "ipcGroupCode", source = "ipcGroupCode")
    @Mapping(target = "ipcSubgroupCode", source = "ipcSubgroupCode")
    @Mapping(target = "ipcSymbol", source = "ipcSymbol")
    @Mapping(target = "latestVersion", source = "latestVersion")
    @BeanMapping(ignoreByDefault = true)
    public abstract CClassCpcIpcConcordance toCore(CfClassCpcIpcConcordance cfClassCpcIpcConcordance);

}
