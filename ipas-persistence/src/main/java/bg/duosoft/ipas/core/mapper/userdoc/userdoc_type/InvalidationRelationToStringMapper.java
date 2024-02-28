package bg.duosoft.ipas.core.mapper.userdoc.userdoc_type;

import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocInvalidationRelation;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocInvalidationRelationPK;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class InvalidationRelationToStringMapper {

    public String toCore(CfUserdocInvalidationRelation invalidationRelation) {
        return invalidationRelation.getPk().getInvalidatedUserdocType();
    }

    public CfUserdocInvalidationRelation toEntity(String invalidatedUserdocType) {
        CfUserdocInvalidationRelation ir = new CfUserdocInvalidationRelation();
        ir.setPk(new CfUserdocInvalidationRelationPK());
        ir.getPk().setInvalidatedUserdocType(invalidatedUserdocType);
        return ir;
    }

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<String> toCoreList(List<CfUserdocInvalidationRelation> invalidationRelations);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfUserdocInvalidationRelation> toEntityList(List<String> invalidationUserdocTypes);
}
