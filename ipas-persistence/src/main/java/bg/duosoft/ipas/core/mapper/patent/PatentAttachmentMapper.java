package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.model.patent.CPatentAttachment;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentAttachment;
import org.mapstruct.*;
import java.util.List;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {
        AttachmentTypeMapper.class})
public abstract class PatentAttachmentMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "pk.id")
    @Mapping(target = "attachmentType", source = "attachmentType")
    @Mapping(target = "dateCreated", source = "dateCreated")
    @Mapping(target = "attachmentName", source = "attachmentName")
    @Mapping(target = "hasBookmarks", source = "hasBookmarks")
    @Mapping(target = "contentLoaded", expression = "java(loadFileContent)")
    @Mapping(target = "attachmentContent", expression = "java(loadFileContent ? patentAttachment.getAttachmentContent() : null)")
    public abstract CPatentAttachment toCore(IpPatentAttachment patentAttachment,@Context Boolean loadFileContent);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attachmentContent", source = "attachmentContent")
    @Mapping(target = "pk.attachmentTypeId", source = "attachmentType.id")
    public abstract IpPatentAttachment toEntity(CPatentAttachment cPatentAttachment);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CPatentAttachment> toCoreList(List<IpPatentAttachment> ipPatentAttachments,@Context Boolean loadFileContent);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpPatentAttachment> toEntityList(List<CPatentAttachment> cPatentAttachments);

}
