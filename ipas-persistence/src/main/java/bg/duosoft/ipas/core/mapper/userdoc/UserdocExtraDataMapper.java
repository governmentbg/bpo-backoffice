package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraData;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocExtraData;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserdocExtraDataTypeMapper.class})
public abstract class UserdocExtraDataMapper {

    @Mapping(source = "pk.docOri", target = "documentId.docOrigin")
    @Mapping(source = "pk.docLog", target = "documentId.docLog")
    @Mapping(source = "pk.docSer", target = "documentId.docSeries")
    @Mapping(source = "pk.docNbr", target = "documentId.docNbr")
    @Mapping(source = "extraDataType", target = "type")
    @Mapping(source = "textValue", target = "textValue")
    @Mapping(source = "numberValue", target = "numberValue")
    @Mapping(source = "booleanValue", target = "booleanValue")
    @Mapping(source = "dateValue", target = "dateValue")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocExtraData toCore(IpUserdocExtraData entity);

    @InheritInverseConfiguration
    @Mapping(source = "type.code", target = "pk.code")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocExtraData toEntity(CUserdocExtraData core);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<IpUserdocExtraData> toEntityList(List<CUserdocExtraData> coreList);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<CUserdocExtraData> toCoreList(List<IpUserdocExtraData> entityList);

}