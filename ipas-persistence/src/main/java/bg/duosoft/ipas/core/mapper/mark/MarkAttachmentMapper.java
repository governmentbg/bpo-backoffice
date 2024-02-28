package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.enums.AttachmentType;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkAttachment;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        ViennaClassMapper.class,
})
public abstract class MarkAttachmentMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "mimeType", source = "mimeType")
    @Mapping(target = "colourDescription", source = "colourDescription")
    @Mapping(target = "colourDescriptionInOtherLang", source = "colourDescriptionLang2")
    @Mapping(target = "viennaClassList", source = "viennaClassList")
    @Mapping(target = "data", expression = "java(addAttachments ? ipMarkAttachment.getData() : null)")
    @BeanMapping(ignoreByDefault = true)
    public abstract CMarkAttachment toCore(IpMarkAttachment ipMarkAttachment, @Context Boolean addAttachments);

    @AfterMapping
    protected void afterToCore(IpMarkAttachment source, @MappingTarget CMarkAttachment target) {
        String mimeType = source.getMimeType();
        target.setAttachmentType(AttachmentType.selectByMimeType(mimeType));
    }

    @InheritInverseConfiguration
    @Mapping(target = "data", source = "data")
    @Mapping(target = "rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpMarkAttachment toEntity(CMarkAttachment markAttachment);

    @AfterMapping
    protected void afterToEntity(CMarkAttachment source, @MappingTarget IpMarkAttachment target) {
    }
}
