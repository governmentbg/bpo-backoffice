package bg.duosoft.ipas.core.mapper.journal;

import bg.duosoft.ipas.core.model.journal.CJournalElement;
import bg.duosoft.ipas.persistence.model.entity.journal.JournalElement;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        PdfFileMapper.class
})
public abstract class JournalElementMapper {

    @Mapping(target = "elementNbr", source = "elementNbr")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "journalNbr", source = "journalNbr")
    @Mapping(target = "nodeNbr", source = "nodeNbr")
    @Mapping(target = "procTyp", source = "procTyp")
    @Mapping(target = "procNbr", source = "procNbr")
    @Mapping(target = "actionNbr", source = "actionNbr")
    @Mapping(target = "pdfFile", source = "pdfFile")
    @BeanMapping(ignoreByDefault = true)
    public abstract CJournalElement toCore(JournalElement journalElement, @Context Boolean loadPdfContent);

}
