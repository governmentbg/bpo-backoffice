package bg.duosoft.ipas.core.mapper.payments;

import bg.duosoft.ipas.core.model.CExtLiabilityDetailExtended;
import bg.duosoft.ipas.persistence.model.nonentity.ExtLiabilityDetailsExtended;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: ggeorgiev
 * Date: 18.08.2021
 * Time: 11:14
 */
@Mapper(componentModel = "spring")
public abstract class ExtLiabilityDetailsExtendedMapper extends ExtLiabilityDetailsBaseMapper<ExtLiabilityDetailsExtended, CExtLiabilityDetailExtended> {

    @Mapping(target = "id", source = "pk.id")
    @Mapping(target = "externalSystemId", source = "externalSystemId")
    public abstract CExtLiabilityDetailExtended toCore(ExtLiabilityDetailsExtended entity);

}
