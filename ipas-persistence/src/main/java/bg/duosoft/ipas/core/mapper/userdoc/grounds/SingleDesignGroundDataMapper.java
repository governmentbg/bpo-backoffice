package bg.duosoft.ipas.core.mapper.userdoc.grounds;


import bg.duosoft.ipas.core.mapper.design.SingleDesignMapper;
import bg.duosoft.ipas.core.model.userdoc.grounds.CSingleDesignGroundData;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocSubGrounds;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpSingleDesignGroundData;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpUserdocSubGrounds;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {
        SingleDesignMapper.class})
public abstract class SingleDesignGroundDataMapper {
    @Mapping(target = "singleDesign",  source = "singleDesign")
    @BeanMapping(ignoreByDefault = true)
    public abstract CSingleDesignGroundData toCore(IpSingleDesignGroundData ipSingleDesignGroundData, @Context Boolean loadFileContent);

    @Mapping(target = "pk.fileNbr",  source = "singleDesign.file.fileId.fileNbr")
    @Mapping(target = "pk.fileSeq",  source = "singleDesign.file.fileId.fileSeq")
    @Mapping(target = "pk.fileTyp",  source = "singleDesign.file.fileId.fileType")
    @Mapping(target = "pk.fileSer",  source = "singleDesign.file.fileId.fileSeries")
    @Mapping(target = "singleDesign.pk.fileNbr",  source = "singleDesign.file.fileId.fileNbr")
    @Mapping(target = "singleDesign.pk.fileSeq",  source = "singleDesign.file.fileId.fileSeq")
    @Mapping(target = "singleDesign.pk.fileTyp",  source = "singleDesign.file.fileId.fileType")
    @Mapping(target = "singleDesign.pk.fileSer",  source = "singleDesign.file.fileId.fileSeries")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpSingleDesignGroundData toEntity(CSingleDesignGroundData cSingleDesignGroundData);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CSingleDesignGroundData> toCoreList(List<IpSingleDesignGroundData> ipSingleDesignGroundDatas,@Context Boolean loadFileContent);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpSingleDesignGroundData> toEntityList(List<CSingleDesignGroundData> cSingleDesignGroundData);
}
