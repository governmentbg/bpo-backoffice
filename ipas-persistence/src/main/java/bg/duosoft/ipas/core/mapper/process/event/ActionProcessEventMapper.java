package bg.duosoft.ipas.core.mapper.process.event;

import bg.duosoft.ipas.core.mapper.action.ActionTypeMapper;
import bg.duosoft.ipas.core.mapper.action.ActiontIdMapper;
import bg.duosoft.ipas.core.mapper.action.JournalMapper;
import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.SimpleUserMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.StatusMapper;
import bg.duosoft.ipas.core.mapper.offidoc.OffidocIdMapper;
import bg.duosoft.ipas.core.mapper.offidoc.OffidocMapper;
import bg.duosoft.ipas.core.model.process.CActionProcessEvent;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        SimpleUserMapper.class,
        JournalMapper.class,
        OffidocIdMapper.class,
        StatusMapper.class,
        ActiontIdMapper.class,
        ActionTypeMapper.class,
        OffidocMapper.class,
        StringToBooleanMapper.class
})
public abstract class ActionProcessEventMapper {

    @Mapping(target = "actionDate"                      , source = "actionDate")
    @Mapping(target = "captureDate"                     , source = "captureDate")
    @Mapping(target = "notes"                           , source = "actionNotes")
    @Mapping(target = "notes1"                          , source = "notes1")
    @Mapping(target = "notes2"                          , source = "notes2")
    @Mapping(target = "notes3"                          , source = "notes3")
    @Mapping(target = "notes4"                          , source = "notes4")
    @Mapping(target = "notes5"                          , source = "notes5")
    @Mapping(target = "indCancelled"                    , source = "indCancelled")
    @Mapping(target = "indSignaturePending"             , source = "indSignaturePending")
    @Mapping(target = "responsibleUser"                 , source = "responsibleUser")
    @Mapping(target = "captureUser"                     , source = "captureUser")
    @Mapping(target = "journal"                         , source = "journal")
    @Mapping(target = "indChangesStatus"                , source = "indChangesStatus")
    @Mapping(target = "actionId"                        , source = "pk")
    @Mapping(target = "oldStatus"                       , source = "priorStatusCode")
    @Mapping(target = "newStatus"                       , source = "newStatusCode")
    @Mapping(target = "actionType"                      , source = "actionTyp")
    @Mapping(target = "oldExpirationDate"               , source = "priorDueDate")
    @Mapping(target = "oldResponsibleUser"              , source = "priorResponsibleUser")
    @Mapping(target = "oldStatusDate"                   , source = "priorStatusDate")
    @Mapping(target = "indDeleted"                      , source = "indDeleted")
    @Mapping(target = "generatedOffidoc"                , source = "ipOffidoc")
    @BeanMapping(ignoreByDefault = true)
    public abstract CActionProcessEvent toCore(IpAction ipAction);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CActionProcessEvent> toCoreList(List<IpAction> ipActions);

}
