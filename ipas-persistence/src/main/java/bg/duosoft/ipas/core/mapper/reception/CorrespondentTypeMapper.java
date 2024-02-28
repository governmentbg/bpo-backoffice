package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.model.reception.CCorrespondentType;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.CorrespondentType;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CorrespondentTypeMapper {

    public abstract CCorrespondentType toCore(CorrespondentType correspondentType);

    @InheritInverseConfiguration
    public abstract CorrespondentType toEntity(CCorrespondentType cCorrespondentType);

}
