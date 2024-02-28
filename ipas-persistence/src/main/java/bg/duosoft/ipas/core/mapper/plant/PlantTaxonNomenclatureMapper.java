package bg.duosoft.ipas.core.mapper.plant;

import bg.duosoft.ipas.core.model.plant.CPlantTaxonNomenclature;
import bg.duosoft.ipas.persistence.model.entity.ext.plant.PlantTaxonNomenclature;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PlantTaxonNomenclatureMapper {

    @Mapping(target = "id",                         source = "id")
    @Mapping(target = "taxonCode",                  source = "taxonCode")
    @Mapping(target = "commonClassifyBul",          source = "commonClassifyBul")
    @Mapping(target = "commonClassifyEng",          source = "commonClassifyEng")
    @Mapping(target = "latinClassify",              source = "latinClassify")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPlantTaxonNomenclature toCore(PlantTaxonNomenclature plantNomenclature);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract PlantTaxonNomenclature toEntity(CPlantTaxonNomenclature cPlantNomenclature);
}
