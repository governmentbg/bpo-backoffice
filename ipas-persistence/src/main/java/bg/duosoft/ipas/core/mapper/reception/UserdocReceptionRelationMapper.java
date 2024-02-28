package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.reception.CUserdocReceptionRelation;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.UserdocReceptionRelation;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                StringToBooleanMapper.class
        })
public abstract class UserdocReceptionRelationMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "userdocType", source = "pk.userdocType")
    @Mapping(target = "mainType", source = "pk.mainType")
    @Mapping(target = "userdocName", source = "userdocType.userdocName")
    @Mapping(target = "isVisible", source = "isVisible")
    public abstract CUserdocReceptionRelation toCore(UserdocReceptionRelation userdocReceptionRelation);

    @InheritInverseConfiguration
    @Mapping(target = "userdocType.userdocTyp", source = "userdocType")
    public abstract UserdocReceptionRelation toEntity(CUserdocReceptionRelation cUserdocReceptionRelation);

    public abstract List<CUserdocReceptionRelation> toCoreList(List<UserdocReceptionRelation> userdocReceptionRelations);

    @InheritInverseConfiguration
    public abstract List<UserdocReceptionRelation> toEntityList(List<CUserdocReceptionRelation> cUserdocReceptionRelations);

}
