package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SubmissionTypeMapper.class, ReceptionCorrespondentMapper.class})
public abstract class ReceptionRequestMapper extends BaseObjectMapper<ReceptionRequest, CReceptionRequest> {

}
