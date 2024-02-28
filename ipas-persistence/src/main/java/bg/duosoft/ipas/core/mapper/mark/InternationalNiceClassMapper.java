package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CInternationalNiceClass;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpInternationalNiceClasses;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class InternationalNiceClassMapper {

    @Mapping(target = "tagCode", source = "tagCode")
    @Mapping(target = "tagDescription", source = "tagDescription")
    @Mapping(target = "niceClassCode", source = "niceClassCode")
    @Mapping(target = "niceClassDescription", source = "niceClassDescription")
    @BeanMapping(ignoreByDefault = true)
    public abstract CInternationalNiceClass toCore(IpInternationalNiceClasses ipInternationalNiceClasses);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpInternationalNiceClasses toEntity(CInternationalNiceClass cInternationalNiceClass);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpInternationalNiceClasses> toEntityList(List<CInternationalNiceClass> cInternationalNiceClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CInternationalNiceClass> toCoreList(List<IpInternationalNiceClasses> ipInternationalNiceClasses);

    @AfterMapping
    protected void afterToCore(IpInternationalNiceClasses source, @MappingTarget CInternationalNiceClass target) {
        target.generateInitialTermsFromDescription();
    }

}
