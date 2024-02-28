package bg.duosoft.ipas.core.mapper.dailylog;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.dailylog.CDailyLog;
import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDailyLog;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: Georgi
 * Date: 28.5.2020 г.
 * Time: 12:03
 */
@Mapper(componentModel = "spring", uses = {DailyLogIdMapper.class, StringToBooleanMapper.class})
public abstract class DailyLogMapper extends BaseObjectMapper<IpDailyLog, CDailyLog> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "affectedFilesReadyDate",         source = "affectedFilesReadyDate")
    @Mapping(target = "certificationReadyDate",         source = "certificationReadyDate")
    @Mapping(target = "dailyLogId",                     source = "pk")
    @Mapping(target = "digitalizationReadyDate",        source = "digitalizationReadyDate")
    @Mapping(target = "fileCaptureReadyDate",           source = "fileCaptureReadyDate")
    @Mapping(target = "firstDocNbr",                    source = "firstDocNbr")
    @Mapping(target = "indClosed",                      source = "indClosed")
    @Mapping(target = "indOpen",                        source = "indOpen")
    @Mapping(target = "lastDocNbr",                     source = "lastDocNbr")
    @Mapping(target = "logoCaptureReadyDate",           source = "logoCaptureReadyDate")
    @Mapping(target = "viennaCodesReadyDate",           source = "viennaCodesReadyDate")
    @Mapping(target = "archivingDate",                  source = "archivingDate")
    @Mapping(target = "userdocCaptureReadyDate",        source = "userdocCaptureReadyDate")
    public abstract CDailyLog toCore(IpDailyLog ipDailyLogPK);


    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion",                     constant = "1")
    public abstract IpDailyLog toEntity(CDailyLog cDailyLog);
}
