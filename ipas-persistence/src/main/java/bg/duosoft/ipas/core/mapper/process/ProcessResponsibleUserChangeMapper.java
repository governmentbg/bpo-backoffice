package bg.duosoft.ipas.core.mapper.process;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.process.CProcessResponsibleUserChange;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcResponsibleUserChanges;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProcessResponsibleUserChangeMapper extends BaseObjectMapper<IpProcResponsibleUserChanges, CProcessResponsibleUserChange> {

}
