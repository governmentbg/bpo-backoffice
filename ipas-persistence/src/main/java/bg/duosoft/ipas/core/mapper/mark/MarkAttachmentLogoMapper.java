package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.model.mark.CLogo;
import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.enums.AttachmentType;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogo;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkAttachment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        ViennaClassMapper.class,
})
public abstract class MarkAttachmentLogoMapper {

    @Mapping(target = "colourDescription", source = "colourDescription")
    @Mapping(target = "colourDescriptionInOtherLang", source = "colourdDescrLang2")
    @Mapping(target = "viennaClassList", source = "ipLogoViennaClassesCollection")
    @Mapping(target = "data", expression = "java(addLogo ? ipLogo.getLogoData() : null)")
    @BeanMapping(ignoreByDefault = true)
    public abstract CMarkAttachment toCore(IpLogo ipLogo, @Context Boolean addLogo);

    @AfterMapping
    protected void afterToCore(@MappingTarget CMarkAttachment target, IpLogo source) {
        String mimeType = MapperHelper.imageFormatWCodeToLogoType(source.getImageFormatWcode());
        target.setMimeType(mimeType);
        target.setAttachmentType(AttachmentType.IMAGE);
    }

    @InheritInverseConfiguration
    @Mapping(target = "logoData", source = "data")
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "indBase64", constant = "N")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpLogo toEntity(CMarkAttachment markAttachment);

    @AfterMapping
    protected void afterToEntity(@MappingTarget IpLogo target, CMarkAttachment source) {
        target.setImageFormatWcode(MapperHelper.logoTypeToFormatWCode(source.getMimeType()));
    }
}
