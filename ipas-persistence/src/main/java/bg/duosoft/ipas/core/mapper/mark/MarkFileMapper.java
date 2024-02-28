package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.mapper.ipbase.IntellectualPropertyFileMapper;
import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {MarkParisPriorityMapper.class,
                MarkOwnerMapper.class,
                MarkRepresentativeMapper.class,
                StringToBooleanMapper.class,
                PersonAddressMapper.class
        })
public abstract class MarkFileMapper extends IntellectualPropertyFileMapper<IpMark> {

    @InheritConfiguration(name = "convertToCoreObjectBase")
//    @Mapping(target = "notes",                                          source = "notes")
    @Mapping(target = "priorityData.hasExhibitionData",                 expression = "java(ipMark.getExhibitionDate() != null || !org.apache.commons.lang.StringUtils.isEmpty(ipMark.getExhibitionNotes()))")
    @Mapping(target = "priorityData.hasParisPriorityData",              expression = "java(ipMark.getPriorities() != null && ipMark.getPriorities().size() > 0)")
    @BeanMapping(ignoreByDefault = true)
    public abstract void toCore(IpMark ipMark, @MappingTarget CFile file);

    //ne mojah da razbera kak da inherit-na ednovremenno inverseConfiguration=convertToCoreObject i configuration=fillIntellectualPropertyEntityDetailsBase, zatova trqbva da se opishat vsichki inverse mappings ot convertToCoreObject
//    @InheritInverseConfiguration(name = "convertToCoreObject")
    @InheritConfiguration(name = "convertToEntityObjectBase")
//    @Mapping(target = "notes",                                          source = "notes")
    @BeanMapping(ignoreByDefault = true)
    public abstract void toEntity(CFile file, @MappingTarget IpMark ipMark);


}
