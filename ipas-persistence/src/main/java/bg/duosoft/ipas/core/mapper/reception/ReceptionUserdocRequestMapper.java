package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdocRequest;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionUserdocRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SubmissionTypeMapper.class, ReceptionUserdocCorrespondentMapper.class})
public abstract class ReceptionUserdocRequestMapper extends BaseObjectMapper<ReceptionUserdocRequest, CReceptionUserdocRequest> {

}
