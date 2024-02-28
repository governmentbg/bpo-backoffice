package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.mapper.miscellaneous.IpcClassMapper;
import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentIpcClasses;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class PatentIpcClassMapper {
    @Autowired
    private IpcClassMapper ipcClassMapper;
    @Mapping(target = "ipcQualification", source = "pk.ipcQualificationCode")
    @Mapping(target = "ipcSymbolPosition", source = "ipcSymbolPosition")
    @Mapping(target = "ipcSymbolCaptureDate", source = "ipcSymbolCaptureDate")
    @Mapping(target = "ipcWpublishValidated", source = "ipcWPublishValidated")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPatentIpcClass toCore(IpPatentIpcClasses ipPatentIpcClasses);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "pk.ipcClassCode", source = "ipcClass")
    @Mapping(target = "pk.ipcEditionCode", source = "ipcEdition")
    @Mapping(target = "pk.ipcSectionCode", source = "ipcSection")
    @Mapping(target = "pk.ipcSubclassCode", source = "ipcSubclass")
    @Mapping(target = "pk.ipcGroupCode", source = "ipcGroup")
    @Mapping(target = "pk.ipcQualificationCode", source = "ipcQualification")
    @Mapping(target = "pk.ipcSubgroupCode", source = "ipcSubgroup")
    public abstract IpPatentIpcClasses toEntity(CPatentIpcClass cPatentIpcClass);

    @AfterMapping
    public void afterToCore(IpPatentIpcClasses ipPatentIpcClasses, @MappingTarget CPatentIpcClass target) {
        ipcClassMapper.toCore(ipPatentIpcClasses.getCfClassIpc(), target);
    }

    @AfterMapping
    public void afterToEntity(CPatentIpcClass cPatentIpcClass, @MappingTarget IpPatentIpcClasses target) {
        target.setCfClassIpc(ipcClassMapper.toEntity(cPatentIpcClass));
    }

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CPatentIpcClass> toCoreList(List<IpPatentIpcClasses> ipPatentIpcClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpPatentIpcClasses> toEntityList(List<CPatentIpcClass> cPatentIpcClasses);


}
