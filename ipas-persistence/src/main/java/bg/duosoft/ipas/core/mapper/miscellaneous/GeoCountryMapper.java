package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.model.miscellaneous.CGeoCountry;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfGeoCountry;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class GeoCountryMapper {

    public abstract CGeoCountry toCore(CfGeoCountry cfGeoCountry);

    @InheritInverseConfiguration
    public abstract CfGeoCountry toEntity(CGeoCountry GeoCountry);

}
