package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CMunicipality;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfMunicipality;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class MunicipalityMapper extends BaseObjectMapper<CfMunicipality, CMunicipality> {

}
