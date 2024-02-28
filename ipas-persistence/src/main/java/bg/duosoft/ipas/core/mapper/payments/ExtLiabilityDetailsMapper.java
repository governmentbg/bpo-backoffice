package bg.duosoft.ipas.core.mapper.payments;

import bg.duosoft.ipas.core.model.CExtLiabilityDetail;
import bg.duosoft.ipas.persistence.model.entity.ext.ExtLiabilityDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ExtLiabilityDetailsMapper extends ExtLiabilityDetailsBaseMapper<ExtLiabilityDetails, CExtLiabilityDetail> {
    @Mapping(target = "id", source = "pk.id")
    public abstract CExtLiabilityDetail toCore(ExtLiabilityDetails entity);
}
