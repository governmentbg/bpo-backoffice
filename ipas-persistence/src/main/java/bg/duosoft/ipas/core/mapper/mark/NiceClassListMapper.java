package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CNiceClassList;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfNiceClassList;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by Raya
 * 23.09.2020
 */
@Mapper(componentModel = "spring")
public abstract class NiceClassListMapper {

    @Mapping(target = "niceClassCode", source = "niceClassCode")
    @Mapping(target = "alphaList", source = "alphaList")
    @Mapping(target = "heading", source = "heading")
    @BeanMapping(ignoreByDefault = true)
    public abstract CNiceClassList toCore(CfNiceClassList source);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfNiceClassList toEntity(CNiceClassList source);
}
