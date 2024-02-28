package bg.duosoft.ipas.core.mapper.userdoc.court_appeal;

import bg.duosoft.ipas.core.model.userdoc.court_appeal.CJudicialActType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfJudicialActType;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class JudicialActTypeMapper {
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "actTypeName",  source = "actTypeName")
    @BeanMapping(ignoreByDefault = true)
    public abstract CJudicialActType toCore(CfJudicialActType cfJudicialActType);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfJudicialActType toEntity(CJudicialActType cJudicialActType);
}
