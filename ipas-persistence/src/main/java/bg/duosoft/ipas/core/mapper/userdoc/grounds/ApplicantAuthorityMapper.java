package bg.duosoft.ipas.core.mapper.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.grounds.CApplicantAuthority;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicantAuthority;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ApplicantAuthorityMapper {

    @Mapping(target = "id",  source = "id")
    @Mapping(target = "name",  source = "name")
    @BeanMapping(ignoreByDefault = true)
    public abstract CApplicantAuthority toCore(CfApplicantAuthority cfApplicantAuthority);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfApplicantAuthority toEntity(CApplicantAuthority cMarkGroundType);
}
