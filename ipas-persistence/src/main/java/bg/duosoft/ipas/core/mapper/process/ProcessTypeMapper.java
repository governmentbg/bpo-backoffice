package bg.duosoft.ipas.core.mapper.process;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.mapper.reception.ReceptionCorrespondentMapper;
import bg.duosoft.ipas.core.mapper.reception.SubmissionTypeMapper;
import bg.duosoft.ipas.core.model.reception.CProcessType;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionRequest;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfProcessType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProcessTypeMapper extends BaseObjectMapper<CfProcessType, CProcessType> {

}
