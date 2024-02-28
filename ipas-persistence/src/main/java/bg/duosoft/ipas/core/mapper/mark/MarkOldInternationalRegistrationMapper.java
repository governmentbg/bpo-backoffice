package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.mark.CMarkOldInternationalRegistration;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpMarkOldInternationalRegistration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class MarkOldInternationalRegistrationMapper extends BaseObjectMapper<IpMarkOldInternationalRegistration, CMarkOldInternationalRegistration> {

}
