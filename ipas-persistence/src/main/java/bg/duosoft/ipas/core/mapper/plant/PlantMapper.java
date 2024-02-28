package bg.duosoft.ipas.core.mapper.plant;

import bg.duosoft.ipas.core.model.plant.CPlant;
import bg.duosoft.ipas.persistence.model.entity.ext.plant.Plant;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PlantTaxonNomenclatureMapper.class})
public abstract class PlantMapper {

    @Mapping(target = "proposedDenomination",       source = "proposedDenomination")
    @Mapping(target = "proposedDenominationEng",    source = "proposedDenominationEng")
    @Mapping(target = "publDenomination",           source = "publDenomination")
    @Mapping(target = "publDenominationEng",        source = "publDenominationEng")
    @Mapping(target = "apprDenomination",           source = "apprDenomination")
    @Mapping(target = "apprDenominationEng",        source = "apprDenominationEng")
    @Mapping(target = "rejDenomination",            source = "rejDenomination")
    @Mapping(target = "rejDenominationEng",         source = "rejDenominationEng")
    @Mapping(target = "features",                   source = "features")
    @Mapping(target = "stability",                  source = "stability")
    @Mapping(target = "testing",                    source = "testing")
    @Mapping(target = "plantNumenclature",          source = "plantNumenclature")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPlant toCore(Plant plant);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract Plant toEntity(CPlant cPlant);
}
