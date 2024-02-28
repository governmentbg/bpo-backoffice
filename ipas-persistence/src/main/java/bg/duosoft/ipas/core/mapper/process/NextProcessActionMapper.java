package bg.duosoft.ipas.core.mapper.process;

import bg.duosoft.ipas.core.model.process.CNextProcessAction;
import bg.duosoft.ipas.enums.NextProcessActionType;
import bg.duosoft.ipas.persistence.model.entity.vw.VwSelectNextProcessActions;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class NextProcessActionMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "actionType", source = "id.actionTyp")
    @Mapping(target = "actionTypeName", source = "actionTypeName")
    @Mapping(target = "statusCode", source = "statusCode")
    @Mapping(target = "statusName", source = "statusName")
    @Mapping(target = "generatedOffidoc", source = "generatedOffidoc")
    @Mapping(target = "automaticActionWcode", source = "automaticActionWcode")
    @Mapping(target = "actionTypeGroup", source = "actionTypeGroup")
    @Mapping(target = "actionTypeListCode", source = "actionTypeListCode")
    @Mapping(target = "restrictLawCode", source = "restrictLawCode")
    @Mapping(target = "restrictFileTyp", source = "restrictFileTyp")
    @Mapping(target = "restrictApplicationType", source = "restrictApplicationType")
    @Mapping(target = "restrictApplicationSubType", source = "restrictApplicationSubType")
    @Mapping(target = "userdocListCodeInclude", source = "userdocListCodeInclude")
    @Mapping(target = "userdocListCodeExclude", source = "userdocListCodeExclude")
    public abstract CNextProcessAction toCore(VwSelectNextProcessActions vwSelectNextProcessActions);

    public abstract List<CNextProcessAction> toCoreList(List<VwSelectNextProcessActions> vwSelectNextProcessActions);

    @AfterMapping
    protected void afterToCore(VwSelectNextProcessActions source, @MappingTarget CNextProcessAction target) {
        target.setContainNotes(source.getContainNotes() == 1);
        target.setContainManualDueDate(source.getContainManualDueDate() == 1);
        target.setCalculateTermFromActionDate(source.getCalculateTermFromActionDate() == 1);
        target.setProcessActionType(NextProcessActionType.valueOf(source.getProcessActionType()));
    }

}
