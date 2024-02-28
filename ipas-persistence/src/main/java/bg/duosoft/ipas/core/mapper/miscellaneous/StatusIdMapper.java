package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.model.miscellaneous.CStatusId;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatusPK;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatusIdMapper {

    @Mapping(source = "procTyp", target = "processType")
    @Mapping(source = "statusCode", target = "statusCode")
    @BeanMapping(ignoreByDefault = true)
    CStatusId toCore(CfStatusPK cfStatusPK);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    CfStatusPK toEntity(CStatusId cStatusId);


}
