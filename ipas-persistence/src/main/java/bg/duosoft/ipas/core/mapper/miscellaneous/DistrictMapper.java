package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CDistrict;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfDistrict;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class DistrictMapper extends BaseObjectMapper<CfDistrict, CDistrict> {

}
