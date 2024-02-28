package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.mapper.design.SingleDesignExtendedMapper;
import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentDrawings;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {SingleDesignExtendedMapper.class})
public abstract class DrawingMapper {

    @Mapping(target = "drawingNbr",  source = "pk.drawingNbr")
    @Mapping(target = "drawingData", expression = "java(loadFileContent ? ipPatentDrawings.getDrawingData() : null)")
    @Mapping(target = "drawingType", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.imageFormatWCodeToLogoType(ipPatentDrawings.getImageFormatWCode()))")
    @Mapping(target = "singleDesignExtended",     source = "singleDesignExtended")
    @BeanMapping(ignoreByDefault = true)
    public abstract CDrawing toCore(IpPatentDrawings ipPatentDrawings,@Context Boolean loadFileContent);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "imageFormatWCode", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.logoTypeToFormatWCode(cDrawing.getDrawingType()))")
    @Mapping(target = "drawingData",source = "drawingData")
    public abstract IpPatentDrawings toEntity(CDrawing cDrawing);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CDrawing> toCoreList(List<IpPatentDrawings> ipPatentDrawingsList,@Context Boolean loadFileContent);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpPatentDrawings> toEntityList(List<CDrawing> cDrawingList);


}
