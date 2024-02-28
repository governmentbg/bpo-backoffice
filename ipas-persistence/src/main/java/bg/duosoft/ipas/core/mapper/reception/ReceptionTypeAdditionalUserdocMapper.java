package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.model.reception.CReceptionTypeAdditionalUserdoc;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionTypeAdditionalUserdoc;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class ReceptionTypeAdditionalUserdocMapper {

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Mapping(target = "userdocType", source = "pk.userdocType")
    @Mapping(target = "receptionType", source = "pk.receptionType")
    @Mapping(target = "seqNbr", source = "seqNbr")
    public abstract CReceptionTypeAdditionalUserdoc toCore(ReceptionTypeAdditionalUserdoc entity);

    public abstract List<CReceptionTypeAdditionalUserdoc> toCoreList(List<ReceptionTypeAdditionalUserdoc> entityList);

    @InheritInverseConfiguration
    @Mapping(target = "pk.receptionType", source = "receptionType")
    @Mapping(target = "pk.userdocType", source = "userdocType")
    @Mapping(target = "seqNbr", source = "seqNbr")
    public abstract ReceptionTypeAdditionalUserdoc toEntity(CReceptionTypeAdditionalUserdoc core);

    public abstract List<ReceptionTypeAdditionalUserdoc> toEntityList(List<CReceptionTypeAdditionalUserdoc> coreList);

    @AfterMapping
    protected void afterToCore(@MappingTarget CReceptionTypeAdditionalUserdoc target, ReceptionTypeAdditionalUserdoc source) {
        Map<String, String> userdocTypesMap = userdocTypesService.selectUserdocTypesMap();
        if (Objects.nonNull(userdocTypesMap)) {
            target.setUserdocTypeName(userdocTypesMap.get(source.getPk().getUserdocType()));
        }
    }


}
