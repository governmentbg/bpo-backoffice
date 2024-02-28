package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CSignData;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * User: ggeorgiev
 * Date: 1.3.2019 г.
 * Time: 15:35
 */
@Mapper(componentModel = "spring")
public abstract class MarkSignDataMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "file.title",         source = "markName")
    @Mapping(target = "file.titleLang2",    source = "markNameInOtherLang")
    public abstract void toEntity(CSignData signData, @MappingTarget IpMark mark);
}
