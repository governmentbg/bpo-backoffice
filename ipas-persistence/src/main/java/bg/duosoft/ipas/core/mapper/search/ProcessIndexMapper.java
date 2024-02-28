package bg.duosoft.ipas.core.mapper.search;

import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwActionIndex;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwProcessIndex;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: ggeorgiev
 * Date: 11.03.2021
 * Time: 12:46
 */
@Mapper(componentModel = "spring")
public abstract class ProcessIndexMapper implements IndexMapper<IpProc, VwProcessIndex>{
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pk",                             source = "pk")
    @Mapping(target = "statusCode.pk.statusCode",       source = "statusCode")
    @Mapping(target = "statusCode.pk.procTyp",          source = "pk.procTyp")
    @Mapping(target = "statusDate",                     source = "statusDate")
    @Mapping(target = "responsibleUser.userId",         source = "responsibleUserId")
    public abstract IpProc toEntity(VwProcessIndex vw);
}
