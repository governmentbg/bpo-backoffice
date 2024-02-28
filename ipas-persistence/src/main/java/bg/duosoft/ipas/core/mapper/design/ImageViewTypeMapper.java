package bg.duosoft.ipas.core.mapper.design;

import bg.duosoft.ipas.core.model.design.CImageViewType;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfImageViewType;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ImageViewTypeMapper {

    @Mapping(target = "viewTypeId",    source = "viewTypeId")
    @Mapping(target = "viewTypeName",  source = "viewTypeName")
    @BeanMapping(ignoreByDefault = true)
    public abstract CImageViewType toCore(CfImageViewType cfImageViewType);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfImageViewType toEntity(CImageViewType cImageViewType);
}
