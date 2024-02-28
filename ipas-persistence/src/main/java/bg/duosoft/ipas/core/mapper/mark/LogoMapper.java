package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CLogo;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogo;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        ViennaClassMapper.class,

})
public abstract class LogoMapper {
    @Mapping(target = "colourDescription",              source = "colourDescription")
    @Mapping(target = "colourDescriptionInOtherLang",   source = "colourdDescrLang2")
    @Mapping(target = "viennaClassList",                source = "ipLogoViennaClassesCollection")
    @Mapping(target = "logoType",                       expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.imageFormatWCodeToLogoType(ipLogo.getImageFormatWcode()))")
    @Mapping(target = "logoData", expression = "java(addLogo ? ipLogo.getLogoData() : null)")
    @BeanMapping(ignoreByDefault = true)
    public abstract CLogo toCore(IpLogo ipLogo, @Context Boolean addLogo);


    @InheritInverseConfiguration
    @Mapping(target = "logoData",                       source = "logoData")
    @Mapping(target = "imageFormatWcode",               expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.logoTypeToFormatWCode(logo.getLogoType()))")
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "indBase64", constant = "N")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpLogo toEntity(CLogo logo);

    @AfterMapping
    protected void afterToCore(IpLogo source, @MappingTarget CLogo target) {
    }

    @AfterMapping
    protected void afterToEntity(CLogo source, @MappingTarget IpLogo target) {
    }
}
