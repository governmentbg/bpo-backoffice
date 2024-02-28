package bg.duosoft.ipas.core.mapper.file;

import bg.duosoft.ipas.core.model.file.CRelationshipType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfRelationshipType;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RelationshipTypeMapper {

    @Mapping(target = "relationshipType", source = "relationshipTyp")
    @Mapping(target = "relationshipName", source = "relationshipName")
    @Mapping(target = "relationshipInverseName", source = "inverseRelationshipName")
    @Mapping(target = "relationshipDirectName", source = "directRelationshipName")
    @BeanMapping(ignoreByDefault = true)
    public abstract CRelationshipType toCore(CfRelationshipType cfRelationshipType);

    public abstract List<CRelationshipType> toCoreList(List<CfRelationshipType> cfRelationshipTypeList);

    @InheritInverseConfiguration
    public abstract CfRelationshipType toEntity(CRelationshipType cRelationshipType);

    @InheritInverseConfiguration
    public abstract List<CfRelationshipType> toEntityList(List<CRelationshipType> cRelationshipTypeList);

}
