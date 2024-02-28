package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CEnotifMark;
import bg.duosoft.ipas.persistence.model.entity.mark.EnotifMark;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {EnotifMapper.class})
public abstract class EnotifMarkMapper {

    @Mapping(target = "id",  source = "pk.id")
    @Mapping(target = "transaction", source = "transaction")
    @Mapping(target = "transcationType",  source = "transcationType")
    @Mapping(target = "originalLanguage", source = "originalLanguage")
    @Mapping(target = "originalCountry", source = "originalCountry")
    @Mapping(target = "basicRegistrationNumber", source = "basicRegistrationNumber")
    @Mapping(target = "designationType", source = "designationType")
    @Mapping(target = "enotif",  source = "enotif")
    @BeanMapping(ignoreByDefault = true)
    public abstract CEnotifMark toCore(EnotifMark enotifMark);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract EnotifMark toEntity(CEnotifMark cEnotifMark);
}
