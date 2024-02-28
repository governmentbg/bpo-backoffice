package bg.duosoft.ipas.core.mapper.dailylog;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.dailylog.CDailyLogId;
import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDailyLogPK;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: Georgi
 * Date: 28.5.2020 г.
 * Time: 12:04
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class DailyLogIdMapper extends BaseObjectMapper<IpDailyLogPK, CDailyLogId> {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "dailyLogDate",   source = "dailyLogDate")
    @Mapping(target = "docLog",         source = "docLog")
    @Mapping(target = "docOrigin",      source = "docOri")
    public abstract CDailyLogId toCore(IpDailyLogPK ipDailyLogPK);


}
