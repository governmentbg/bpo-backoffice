package bg.duosoft.ipas.core.mapper.design;

import bg.duosoft.ipas.core.model.design.CSingleDesignExtended;
import bg.duosoft.ipas.persistence.model.entity.ext.design.SingleDesignExtended;
import org.mapstruct.*;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {ImageViewTypeMapper.class})
public abstract class SingleDesignExtendedMapper {

    @Mapping(target = "drawingNbr",        source = "pk.drawingNbr")
    @Mapping(target = "imageRefused",      source = "imageRefused")
    @Mapping(target = "imagePublished",    source = "imagePublished")
    @Mapping(target = "imageViewType",     source = "imageViewType")
    @BeanMapping(ignoreByDefault = true)
    public abstract CSingleDesignExtended toCore(SingleDesignExtended singleDesignExtended);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract SingleDesignExtended toEntity(CSingleDesignExtended cSingleDesignExtended);

    @AfterMapping
    protected void afterToEntity(@MappingTarget SingleDesignExtended target, CSingleDesignExtended source) {
        if(Objects.isNull(source.getImagePublished())){
            target.setImagePublished(false);
        }
        if(Objects.isNull(source.getImageRefused())){
            target.setImageRefused(false);
        }
    }

}
