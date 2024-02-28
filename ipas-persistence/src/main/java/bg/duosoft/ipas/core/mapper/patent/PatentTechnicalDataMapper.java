package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.model.patent.CTechnicalData;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * User: ggeorgiev
 * Date: 1.3.2019 г.
 * Time: 15:49
 */
@Mapper(componentModel = "spring")
public abstract class PatentTechnicalDataMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "file.title",         source = "title")
    @Mapping(target = "file.titleLang2",    source = "englishTitle")
    public abstract void toEntity(CTechnicalData technicalData, @MappingTarget IpPatent patent);

}
