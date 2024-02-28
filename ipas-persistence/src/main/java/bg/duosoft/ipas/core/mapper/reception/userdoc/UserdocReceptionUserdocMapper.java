package bg.duosoft.ipas.core.mapper.reception.userdoc;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.mapper.reception.ReceptionMapperHelper;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import org.mapstruct.*;

/**
 * User: Georgi
 * Date: 8.6.2020 г.
 * Time: 14:56
 */
@Mapper(componentModel = "spring", uses = {ReceptionMapperHelper.class, UserdocReceptionDocMapper.class})
public abstract class UserdocReceptionUserdocMapper extends BaseObjectMapper<IpUserdoc, CReception> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion",             constant = "1")
    @Mapping(target = "captureUser.userId",     source = ".", qualifiedByName = "loggedUserMapper")
    @Mapping(target = "captureDate",            expression = "java(new java.util.Date())")
    @Mapping(target = "ipDoc",                  source = ".")
    public abstract IpUserdoc toEntity(CReception core);

    @AfterMapping
    public void afterToEntity(CReception source, @MappingTarget IpUserdoc target) {

    }
}
