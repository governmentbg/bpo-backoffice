
package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.model.reception.CReceptionCorrespondent;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionCorrespondent;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = CorrespondentTypeMapper.class)
public abstract class ReceptionCorrespondentMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "receptionRequestId",source = "pk.receptionRequestId")
    @Mapping(target = "personNbr",source = "pk.personNbr")
    @Mapping(target = "addressNbr",source = "pk.addressNbr")
    @Mapping(target = "correspondentType",source = "correspondentType")
    @Mapping(target = "representativeType",source = "representativeType")
    public abstract CReceptionCorrespondent toCore(ReceptionCorrespondent receptionCorrespondent);

    @InheritInverseConfiguration
    public abstract ReceptionCorrespondent toEntity(CReceptionCorrespondent cReceptionCorrespondent);

    public abstract List<CReceptionCorrespondent> toCoreList(List<ReceptionCorrespondent> correspondentEntities);

    @InheritInverseConfiguration
    public abstract List<ReceptionCorrespondent> toEntityList(List<CReceptionCorrespondent> cReceptionCorrespondents);

}
