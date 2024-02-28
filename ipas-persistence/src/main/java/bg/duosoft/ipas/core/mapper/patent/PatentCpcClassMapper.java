package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.mapper.miscellaneous.CpcClassMapper;
import bg.duosoft.ipas.core.model.patent.CPatentCpcClass;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentCpcClasses;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class PatentCpcClassMapper {
    @Autowired
    private CpcClassMapper cpcClassMapper;
    @Mapping(target = "cpcQualification", source = "pk.cpcQualificationCode")
    @Mapping(target = "cpcSymbolPosition", source = "cpcSymbolPosition")
    @Mapping(target = "cpcSymbolCaptureDate", source = "cpcSymbolCaptureDate")
    @Mapping(target = "cpcWPublishValidated", source = "cpcWPublishValidated")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPatentCpcClass toCore(IpPatentCpcClasses ipPatentCpcClasses);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "pk.cpcClassCode", source = "cpcClass")
    @Mapping(target = "pk.cpcEditionCode", source = "cpcEdition")
    @Mapping(target = "pk.cpcSectionCode", source = "cpcSection")
    @Mapping(target = "pk.cpcSubclassCode", source = "cpcSubclass")
    @Mapping(target = "pk.cpcGroupCode", source = "cpcGroup")
    @Mapping(target = "pk.cpcQualificationCode", source = "cpcQualification")
    @Mapping(target = "pk.cpcSubgroupCode", source = "cpcSubgroup")
    public abstract IpPatentCpcClasses toEntity(CPatentCpcClass cPatentCpcClass);

    @AfterMapping
    public void afterToCore(IpPatentCpcClasses ipPatentCpcClasses, @MappingTarget CPatentCpcClass target) {
        cpcClassMapper.toCore(ipPatentCpcClasses.getCfClassCpc(), target);
    }

    @AfterMapping
    public void afterToEntity(CPatentCpcClass cPatentCpcClass, @MappingTarget IpPatentCpcClasses target) {
        target.setCfClassCpc(cpcClassMapper.toEntity(cPatentCpcClass));
    }

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CPatentCpcClass> toCoreList(List<IpPatentCpcClasses> ipPatentCpcClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpPatentCpcClasses> toEntityList(List<CPatentCpcClass> cPatentCpcClasses);


}
