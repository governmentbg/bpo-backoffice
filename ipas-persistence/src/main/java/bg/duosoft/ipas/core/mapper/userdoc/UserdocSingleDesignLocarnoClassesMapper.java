package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.LocarnoClassesMapper;
import bg.duosoft.ipas.core.model.design.CLocarnoClasses;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocSingleDesignLocarnoClasses;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {
        StringToBooleanMapper.class, LocarnoClassesMapper.class
})
public abstract class UserdocSingleDesignLocarnoClassesMapper {

    @Mapping(target = "locarnoClassCode", source = "pk.locarnoClassCode")
    @Mapping(target = "locarnoEditionCode", source = "locarnoEditionCode")
    @Mapping(target = "locarnoName", source = "cfClassLocarno.locarnoName")
    @BeanMapping(ignoreByDefault = true)
    public abstract CLocarnoClasses toCore(IpUserdocSingleDesignLocarnoClasses ipUserdocSingleDesignLocarnoClasses);

    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "pk.locarnoClassCode", source = "locarnoClassCode")
    @Mapping(target = "locarnoEditionCode", source = "locarnoEditionCode")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocSingleDesignLocarnoClasses toEntity(CLocarnoClasses locarnoClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpUserdocSingleDesignLocarnoClasses> toEntityList(List<CLocarnoClasses> locarnoClassesList);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CLocarnoClasses> toCoreList(List<IpUserdocSingleDesignLocarnoClasses> ipUserdocSingleDesignLocarnoClassesList);

    @AfterMapping
    protected void afterToCore(IpUserdocSingleDesignLocarnoClasses source, @MappingTarget CLocarnoClasses target) {

    }

    @AfterMapping
    protected void afterToEntity(CLocarnoClasses source, @MappingTarget IpUserdocSingleDesignLocarnoClasses target) {

    }

}
