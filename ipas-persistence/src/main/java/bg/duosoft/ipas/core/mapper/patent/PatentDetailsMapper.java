package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.model.patent.CPatentDetails;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        PatentAttachmentMapper.class})
public abstract class PatentDetailsMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "drawings", source = "drawings")
    @Mapping(target = "drawingPublications", source = "drawingPublications")
    @Mapping(target = "descriptionPages", source = "descriptionPages")
    @Mapping(target = "inventionsGroup", source = "inventionsGroup")
    @Mapping(target = "claims", source = "claims")
    @Mapping(target = "patentAttachments", source = "patentAttachments")
    @Mapping(target = "notInForceDate", source = "notInForceDate")
    public abstract CPatentDetails toCore(IpPatentDetails patentDetails, @Context Boolean loadFileContent);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpPatentDetails toEntity(CPatentDetails patentDetails);
}
