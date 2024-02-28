package bg.duosoft.ipas.core.mapper.person;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.person.CPersonIdType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfPersonIdType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = StringToBooleanMapper.class)
public abstract class PersonIdTypeMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", source = "rowVersion")
    @Mapping(target = "personIdTyp", source = "personIdTyp")
    @Mapping(target = "personIdName", source = "personIdName")
    @Mapping(target = "indGeneralId", source = "indGeneralId")
    @Mapping(target = "indIndividualId", source = "indIndividualId")
    public abstract CPersonIdType toCore(CfPersonIdType entity);

    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    public abstract CfPersonIdType toEntity(CPersonIdType core);

    @AfterMapping
    protected void afterToCore(@MappingTarget CPersonIdType target, CfPersonIdType source) {

    }

}
