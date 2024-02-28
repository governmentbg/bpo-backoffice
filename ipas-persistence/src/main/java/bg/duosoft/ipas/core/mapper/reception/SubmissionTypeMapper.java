package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.reception.CSubmissionType;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.SubmissionType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class SubmissionTypeMapper extends BaseObjectMapper<SubmissionType,CSubmissionType> {

}
