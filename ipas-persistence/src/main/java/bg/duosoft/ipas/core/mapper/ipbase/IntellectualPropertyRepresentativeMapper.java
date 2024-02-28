package bg.duosoft.ipas.core.mapper.ipbase;

import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyRepresentative;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/**
 * User: ggeorgiev
 * Date: 5.4.2019 г.
 * Time: 11:55
 */
public abstract class IntellectualPropertyRepresentativeMapper<T extends IntellectualPropertyRepresentative> {
    @Mapping(source = "ipPersonAddresses", target = "person")
    @Mapping(source = "pk.representativeTyp", target = "representativeType")
    @Mapping(source = "attorneyPowerTerm", target = "attorneyPowerTerm")
    @Mapping(source = "reauthorizationRight", target = "reauthorizationRight")
    @Mapping(source = "priorReprsRevocation", target = "priorReprsRevocation")
    @Mapping(source = "authorizationCondition", target = "authorizationCondition")
    @BeanMapping(ignoreByDefault = true)
    public abstract CRepresentative toCore(T repr);


    @InheritInverseConfiguration
    @Mapping(target = "pk.personNbr", source = "person.personNbr")
    @Mapping(target = "pk.addrNbr", source = "person.addressNbr")
    @Mapping(target = "rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract T toEntity(CRepresentative representative);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<T> toEntityList(List<CRepresentative> representatives);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CRepresentative> toCoreList(List<T> ipMarkReprs);
}
