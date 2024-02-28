package bg.duosoft.ipas.core.mapper;

import bg.duosoft.ipas.core.model.CApplicationSubType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationSubTypesMapper {

    @Mapping(target = "applicationType", source = "pk.applTyp")
    @Mapping(target = "applicationSubType", source = "pk.applSubtyp")
    @Mapping(target = "applicationSubTypeName", source = "applSubtypeName")
    CApplicationSubType toCore(CfApplicationSubtype cfApplicationSubType);

    List<CApplicationSubType> toCoreList(List<CfApplicationSubtype> cfApplicationSubTypes);

}
