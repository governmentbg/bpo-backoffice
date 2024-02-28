package bg.duosoft.ipas.core.mapper.ipbase;

import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyOwner;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/**
 * User: ggeorgiev
 * Date: 5.4.2019 г.
 * Time: 11:42
 */
public abstract class IntellectualPropertyOwnerMapper<T extends IntellectualPropertyOwner> {
    @Mapping(target = "person", source = "ipPersonAddresses")
    @Mapping(target = "orderNbr", source = "orderNbr")
    @Mapping(target = "ownershipNotes", source = "notes")
    @BeanMapping(ignoreByDefault = true)
    public abstract COwner toCore(T owner);


    @InheritInverseConfiguration(name = "toCore")
    @Mapping(target = "pk.personNbr", source = "person.personNbr")
    @Mapping(target = "pk.addrNbr", source = "person.addressNbr")
    @Mapping(target = "rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract T toEntity(COwner owner);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<COwner> toCoreList(List<T> ipPatentOwners);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<T> toEntityList(List<COwner> cOwners);
}
