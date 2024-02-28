package bg.duosoft.ipas.core.mapper.action;

import bg.duosoft.ipas.core.model.action.CJournal;
import bg.duosoft.ipas.persistence.model.entity.action.IpJournal;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public abstract class JournalMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "journalCode", source = "journalCode")
    @Mapping(target = "journalName", source = "jornalName")
    @Mapping(target = "notificationDate", source = "notificationDate")
    @Mapping(target = "publicationDate", source = "publicationDate")
    @Mapping(target = "indClosed", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipJournal.getIndClosed()))")
    public abstract CJournal toCore(IpJournal ipJournal);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CJournal> toCoreList(List<IpJournal> ipJournals);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpJournal toEntity(CJournal cJournal);

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    public abstract List<IpJournal> toEntityList(List<CJournal> cJournals);

}
