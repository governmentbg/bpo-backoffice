package bg.duosoft.ipas.core.mapper.reception.userdoc;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.mapper.reception.ReceptionMapperHelper;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import org.mapstruct.*;

/**
 * User: Georgi
 * Date: 9.6.2020 г.
 * Time: 12:58
 */
@Mapper(componentModel = "spring", uses = {ReceptionMapperHelper.class})
public abstract class UserdocReceptionProcMapper extends BaseObjectMapper<IpProc, CReception> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
    @Mapping(target = "userdocTyp.userdocTyp", source = "userdoc.userdocType")
    @Mapping(target = "statusDate", source = "entryDate")
    public abstract IpProc toEntity(CReception core);
    @AfterMapping
    public void afterToEntity(CReception source, @MappingTarget IpProc target) {

    }
}
