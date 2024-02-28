package bg.duosoft.ipas.core.mapper.action;

import bg.duosoft.ipas.core.mapper.miscellaneous.SimpleUserMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.StatusMapper;
import bg.duosoft.ipas.core.mapper.offidoc.OffidocMapper;
import bg.duosoft.ipas.core.model.action.CAction;
import bg.duosoft.ipas.core.model.action.CJournal;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import org.mapstruct.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {
        ActiontIdMapper.class,
        SimpleUserMapper.class,
        JournalMapper.class,
        StatusMapper.class,
        ActionTypeMapper.class,
        OffidocMapper.class,
})
public abstract class ActionMapper {

    /*TODO
        Integer deleteUserId
        Date deleteDate
        String deleteReason
      */

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "actionId", source = "pk")
    @Mapping(target = "actionDate", source = "actionDate")
    @Mapping(target = "notes1", source = "notes1")
    @Mapping(target = "notes2", source = "notes2")
    @Mapping(target = "notes3", source = "notes3")
    @Mapping(target = "notes4", source = "notes4")
    @Mapping(target = "notes5", source = "notes5")
    @Mapping(target = "notes",  source = "actionNotes")
    @Mapping(target = "indCancelled", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipAction.getIndCancelled()))")
    @Mapping(target = "indSignaturePending", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipAction.getIndSignaturePending()))")
    @Mapping(target = "indDeleted", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipAction.getIndDeleted()))")
    @Mapping(target = "indChk1",  expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipAction.getIndchk1()))")
    @Mapping(target = "indChk2",  expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipAction.getIndchk2()))")
    @Mapping(target = "indChk3",  expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipAction.getIndchk3()))")
    @Mapping(target = "indChk4",  expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipAction.getIndchk4()))")
    @Mapping(target = "indChk5",  expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipAction.getIndchk5()))")
    @Mapping(target = "responsibleUser",  source = "responsibleUser")
    @Mapping(target = "captureUser",  source = "captureUser")
    @Mapping(target = "journal",  source = "journal")
    @Mapping(target = "oldStatus",  source = "priorStatusCode")
    @Mapping(target = "newStatus",  source = "newStatusCode")
    @Mapping(target = "actionType",  source = "actionTyp")
    @Mapping(target = "offidoc",  source = "ipOffidoc")
    public abstract CAction toCore(IpAction ipAction);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CAction> toCoreList(List<IpAction> ipActions);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "indCancelled", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cAction.getIndCancelled()))")
    @Mapping(target = "indSignaturePending", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cAction.getIndSignaturePending()))")
    @Mapping(target = "indchk1", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cAction.getIndChk1()))")
    @Mapping(target = "indchk2", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cAction.getIndChk2()))")
    @Mapping(target = "indchk3", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cAction.getIndChk3()))")
    @Mapping(target = "indchk4", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cAction.getIndChk4()))")
    @Mapping(target = "indchk5", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cAction.getIndChk5()))")
    public abstract IpAction toEntity(CAction cAction);

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    public abstract List<IpAction> toEntityList(List<CAction> cActionTypes);


    @InheritConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract void fillActionFields(CAction cAction, @MappingTarget IpAction target);

    @AfterMapping
    protected void afterToEntity(@MappingTarget IpAction target, CAction source) {
        CJournal journal = source.getJournal();
        if (Objects.isNull(journal) || StringUtils.isEmpty(journal.getJournalCode())) {
            target.setJournal(null);
        }
    }

}
