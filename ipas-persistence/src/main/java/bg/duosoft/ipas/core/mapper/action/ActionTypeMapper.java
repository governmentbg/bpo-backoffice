package bg.duosoft.ipas.core.mapper.action;

import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfActionType;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public abstract class ActionTypeMapper {

    /*TODO
       Boolean indManualDueDateRequired
       Boolean indAcceptReferencedPriority
       Boolean indRejectReferencedPriority
       String listName2
       String listName3
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "actionType", source = "actionTyp")
    @Mapping(target = "actionName", source = "actionTypeName")
    @Mapping(target = "listCode", source = "list.listCode")
    @Mapping(target = "listName", source = "list.listName")
    @Mapping(target = "notes1Prompt", source = "notes1Prompt")
    @Mapping(target = "notes2Prompt", source = "notes2Prompt")
    @Mapping(target = "notes3Prompt", source = "notes3Prompt")
    @Mapping(target = "notes4Prompt", source = "notes4Prompt")
    @Mapping(target = "notes5Prompt", source = "notes5Prompt")
    @Mapping(target = "restrictApplicationType", source = "restrictApplTyp")
    @Mapping(target = "restrictApplicationSubtype", source = "restrictApplSubtyp")
    @Mapping(target = "restrictLawCode", source = "restrictLawCode")
    @Mapping(target = "restrictFileType", source = "restrictFileTyp")
    @Mapping(target = "chk1Prompt", source = "chk1Prompt")
    @Mapping(target = "chk2Prompt", source = "chk2Prompt")
    @Mapping(target = "chk3Prompt", source = "chk3Prompt")
    @Mapping(target = "chk4Prompt", source = "chk4Prompt")
    @Mapping(target = "chk5Prompt", source = "chk5Prompt")
    @Mapping(target = "listCode2", source = "listCode2")
    @Mapping(target = "listCode3", source = "listCode3")
    @Mapping(target = "journalPublicationWcode", source = "journalPublicationWcode")
    @Mapping(target = "automaticActionWcode", source = "automaticActionWcode")
    public abstract CActionType toCore(CfActionType cfActionType);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CActionType> toCoreList(List<CfActionType> cfActionTypes);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfActionType toEntity(CActionType cActionType);

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    public abstract List<CfActionType> toEntityList(List<CActionType> cActionTypes);

}
