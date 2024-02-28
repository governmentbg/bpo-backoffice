package bg.duosoft.ipas.core.mapper.search;

import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwActionIndex;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: ggeorgiev
 * Date: 11.03.2021
 * Time: 12:46
 */
@Mapper(componentModel = "spring")
public abstract class ActionIndexMapper implements IndexMapper<IpAction, VwActionIndex>{
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pk",                             source = "pk")
    @Mapping(target = "actionTyp.actionTyp",            source = "actionTyp")
    @Mapping(target = "actionDate",                     source = "actionDate")
    @Mapping(target = "newStatusCode.pk.procTyp",       source = "pk.procTyp")
    @Mapping(target = "newStatusCode.pk.statusCode",    source = "statusCode")
    @Mapping(target = "priorStatusCode.pk.procTyp",     source = "pk.procTyp")
    @Mapping(target = "priorStatusCode.pk.statusCode",  source = "priorStatusCode")
    @Mapping(target = "priorStatusDate",                source = "priorStatusDate")
    @Mapping(target = "responsibleUser.userId",         source = "responsibleUserId")
    @Mapping(target = "captureUser.userId",             source = "captureUserId")
    @Mapping(target = "vwJournal.year",                 source = "year")
    @Mapping(target = "vwJournal.sect",                 source = "sect")
    @Mapping(target = "vwJournal.buletin",              source = "buletin")
    public abstract IpAction toEntity(VwActionIndex vw);
}
