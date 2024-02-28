package bg.duosoft.ipas.integration.payments.mapper;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.payments.CLiabilityRecord;
import bg.duosoft.ipas.integration.payments.model.LiabilityRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class LiabilityRecordMapper extends BaseObjectMapper<LiabilityRecord, CLiabilityRecord> {
}
