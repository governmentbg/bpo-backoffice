package bg.duosoft.ipas.core.mapper.action;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.CPublicationInfo;
import bg.duosoft.ipas.persistence.model.nonentity.PublicationInfoResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PublicationInfoMapper extends BaseObjectMapper<PublicationInfoResult,CPublicationInfo> {

}
