package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CAbdocsDocumentType;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.CfAbdocsDocumentType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AbdocsDocumentTypeMapper extends BaseObjectMapper<CfAbdocsDocumentType, CAbdocsDocumentType> {

}
