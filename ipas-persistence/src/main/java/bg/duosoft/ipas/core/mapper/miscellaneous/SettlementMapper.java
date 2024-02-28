package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CSettlement;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfSettlement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MunicipalityMapper.class, DistrictMapper.class})
public abstract class SettlementMapper extends BaseObjectMapper<CfSettlement, CSettlement> {

}
