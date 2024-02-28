package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpAffectedObject;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpAffectedObject;
import org.mapstruct.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {AcpExternalAffectedObjectMapper.class})
public abstract class AcpAffectedObjectMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fileId.fileSeq", source = "affectedObjectFileSeq")
    @Mapping(target = "fileId.fileType", source = "affectedObjectFileTyp")
    @Mapping(target = "fileId.fileSeries", source = "affectedObjectFileSer")
    @Mapping(target = "fileId.fileNbr", source = "affectedObjectFileNbr")
    @Mapping(target = "registrationNbr", source = "affectedObjectData.registrationNbr")
    @Mapping(target = "title", source = "affectedObjectData.title")
    @Mapping(target = "externalAffectedObject", source = "externalAffectedObject")
    public abstract CAcpAffectedObject toCore(AcpAffectedObject affectedObject);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "affectedObjectData.pk.fileSeq", source = "fileId.fileSeq")
    @Mapping(target = "affectedObjectData.pk.fileTyp", source = "fileId.fileType")
    @Mapping(target = "affectedObjectData.pk.fileSer", source = "fileId.fileSeries")
    @Mapping(target = "affectedObjectData.pk.fileNbr", source = "fileId.fileNbr")
    public abstract AcpAffectedObject toEntity(CAcpAffectedObject affectedObject);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAcpAffectedObject> toCoreList(List<AcpAffectedObject> acpAffectedObjects);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<AcpAffectedObject> toEntityList(List<CAcpAffectedObject> acpAffectedObjects);

    @AfterMapping
    protected void afterToEntity(@MappingTarget AcpAffectedObject target) {
        Integer id = target.getId();
        if (Objects.nonNull(id) && id < 1) {
            target.setId(null);
        }

        if (Objects.nonNull(target.getExternalAffectedObject()) && StringUtils.hasText(target.getExternalAffectedObject().getExternalId())) {
            target.setAffectedObjectData(null);
        }
    }
}

