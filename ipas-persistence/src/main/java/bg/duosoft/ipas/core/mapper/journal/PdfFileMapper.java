package bg.duosoft.ipas.core.mapper.journal;

import bg.duosoft.ipas.core.model.journal.CPdfFile;
import bg.duosoft.ipas.persistence.model.entity.journal.PdfFile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PdfFileMapper {

    @Mapping(target = "pdfNbr", source = "pdfNbr")
    @Mapping(target = "pdfContent", expression = "java(loadFileContent ? pdfFile.getPdfContent() : null)")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPdfFile toCore(PdfFile pdfFile, @Context Boolean loadFileContent);
}
