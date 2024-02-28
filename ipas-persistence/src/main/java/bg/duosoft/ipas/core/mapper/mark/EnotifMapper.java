package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CEnotif;
import bg.duosoft.ipas.persistence.model.entity.mark.Enotif;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class EnotifMapper {

    @Mapping(target = "gazno",  source = "gazno")
    @Mapping(target = "notDate",  source = "notDate")
    @Mapping(target = "pubDate",  source = "pubDate")
    @Mapping(target = "weekno",  source = "weekno")
    @Mapping(target = "notLang",  source = "notLang")
    @Mapping(target = "paidCount",  source = "paidCount")
    @Mapping(target = "licenceNewNameCount",  source = "licenceNewNameCount")
    @Mapping(target = "licenceBirthCount",  source = "licenceBirthCount")
    @Mapping(target = "createdCount",  source = "createdCount")
    @Mapping(target = "processedCount",  source = "processedCount")
    @Mapping(target = "correctionCount",  source = "correctionCount")
    @Mapping(target = "prolongCount",  source = "prolongCount")
    @Mapping(target = "newBaseCount",  source = "newBaseCount")
    @Mapping(target = "restrictCount",  source = "restrictCount")
    @Mapping(target = "newNameCount",  source = "newNameCount")
    @Mapping(target = "deathCount",  source = "deathCount")
    @Mapping(target = "birthCount",  source = "birthCount")
    @BeanMapping(ignoreByDefault = true)
    public abstract CEnotif toCore(Enotif enotif);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract Enotif toEntity(CEnotif enotif);

}
