package bg.duosoft.ipas.integration.ebddownload.mapper;

import bg.bpo.ebd.ebddpersistence.entity.PersonBase;
import bg.duosoft.ipas.core.model.person.CPerson;
import org.mapstruct.*;

import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class EbdPersonBaseMapper {

    @Mapping(target = "personName", source = "fullName")
    @Mapping(target = "nationalityCountryCode", source = "country")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "addressStreet", source = "address")
    @Mapping(target = "telephone", source = "telephone")
    @Mapping(target = "cityName", source = "city")
    @Mapping(target = "cityCode", source = "cityCode")
    @Mapping(target = "stateName", source = "state")
    @Mapping(target = "residenceCountryCode", source = "country")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPerson toCore(PersonBase personBase);

    @AfterMapping
    protected void afterToCore(PersonBase source, @MappingTarget CPerson target) {
        Integer ntincorp = source.getNtincorp();
        if (Objects.nonNull(ntincorp)) {
            if (ntincorp == 9 || ntincorp == 7 || ntincorp == 6 || ntincorp == 5 || ntincorp == 4) {
                target.setIndCompany(true);
            } else {
                target.setIndCompany(false);
            }
        }
    }


}
