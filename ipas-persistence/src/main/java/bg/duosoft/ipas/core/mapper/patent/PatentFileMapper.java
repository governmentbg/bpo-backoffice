package bg.duosoft.ipas.core.mapper.patent;


import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.mapper.ipbase.IntellectualPropertyFileMapper;
import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import org.mapstruct.*;


@Mapper(componentModel = "spring",
        uses = {
                PatentOwnerMapper.class,
                PersonAddressMapper.class,
                PatentRepresentativeMapper.class,
                StringToBooleanMapper.class,
                PatentPriorityMapper.class
        })
public abstract class PatentFileMapper extends IntellectualPropertyFileMapper<IpPatent> {

    @BeanMapping(ignoreByDefault = true)
    @InheritConfiguration(name = "convertToCoreObjectBase")
    @Mapping(target = "priorityData.hasExhibitionData",                 expression = "java(ipPatent.getExhibitionDate() != null || !org.apache.commons.lang.StringUtils.isEmpty(ipPatent.getExhibitionNotes()))")
    @Mapping(target = "priorityData.hasParisPriorityData",              expression = "java(ipPatent.getPriorities() != null && ipPatent.getPriorities().size() > 0)")
    @Mapping(target = "notes",                                          source = "file.ipDoc.notes")
    public abstract void toCore(IpPatent ipPatent, @MappingTarget CFile file);


    //vajno - tyj kato ne mojah da izmislq kak toq mapper da inherit-va  convertToEntityObjectBase i v sy6toto vreme da inheritReverse=convertToCoreObject, ako v convertToCoreObject se pishat nqkakvi custom mappings, reverse variantite im trqbva da se slojat i tuk!!!!!
    @BeanMapping(ignoreByDefault = true)
    @InheritConfiguration(name = "convertToEntityObjectBase")
    @Mapping(target = "receptionDate",          source = "filingData.receptionDate")
    public abstract void toEntity(CFile file, @MappingTarget IpPatent ipPatent);


}
