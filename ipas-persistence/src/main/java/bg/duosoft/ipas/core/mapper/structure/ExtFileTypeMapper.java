package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.core.model.structure.CExtFileType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfExtFileType;
import org.mapstruct.*;
import java.util.List;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {})
public abstract class ExtFileTypeMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "fileTyp",  source = "fileTyp")
    @Mapping(target = "name",  source = "name")
    @Mapping(target = "order",  source = "order")
    public abstract CExtFileType toCore(CfExtFileType fileType);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfExtFileType toEntity(CExtFileType fileType);


    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CExtFileType> toCoreList(List<CfExtFileType> fileTypes);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfExtFileType> toEntityList(List<CExtFileType> fileTypes);
}
