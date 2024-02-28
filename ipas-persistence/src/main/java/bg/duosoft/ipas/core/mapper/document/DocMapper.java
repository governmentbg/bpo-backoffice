package bg.duosoft.ipas.core.mapper.document;

import bg.duosoft.ipas.core.mapper.reception.CorrespondentTypeMapper;
import bg.duosoft.ipas.core.model.reception.CReceptionCorrespondent;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionUserdocCorrespondent;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = CorrespondentTypeMapper.class)
public abstract class DocMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "receptionUserdocRequestId",source = "pk.receptionUserdocRequestId")
    @Mapping(target = "personNbr",source = "pk.personNbr")
    @Mapping(target = "addressNbr",source = "pk.addressNbr")
    @Mapping(target = "correspondentType",source = "correspondentType")
    public abstract CReceptionCorrespondent toCore(ReceptionUserdocCorrespondent receptionUserdocCorrespondent);

    @InheritInverseConfiguration
    public abstract ReceptionUserdocCorrespondent toEntity(CReceptionCorrespondent cReceptionCorrespondent);

    public abstract List<CReceptionCorrespondent> toCoreList(List<ReceptionUserdocCorrespondent> receptionUserdocCorrespondents);

    @InheritInverseConfiguration
    public abstract List<ReceptionUserdocCorrespondent> toEntityList(List<CReceptionCorrespondent> cReceptionCorrespondents);

}
