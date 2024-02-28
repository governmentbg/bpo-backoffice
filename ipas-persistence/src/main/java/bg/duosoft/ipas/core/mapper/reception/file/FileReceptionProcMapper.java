package bg.duosoft.ipas.core.mapper.reception.file;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.mapper.reception.ReceptionMapperHelper;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import org.aspectj.lang.annotation.After;
import org.mapstruct.*;

/**
 * User: Georgi
 * Date: 27.5.2020 г.
 * Time: 16:52
 */
@Mapper(componentModel = "spring", uses = {ReceptionMapperHelper.class})
public abstract class FileReceptionProcMapper extends BaseObjectMapper<IpProc, CReception> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
    @Mapping(target = "applTyp",    source = "file.applicationType")
    @Mapping(target = "statusDate", source = "entryDate")
    public abstract IpProc toEntity(CReception core);
    @AfterMapping
    public void afterToEntity(CReception source, @MappingTarget IpProc target) {

    }
}
