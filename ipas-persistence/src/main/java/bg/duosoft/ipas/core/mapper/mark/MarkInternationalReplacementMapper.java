package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CMarkInternationalReplacement;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkInternationalReplacement;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MarkIntlReplacementNiceClassMapper.class})
public abstract class MarkInternationalReplacementMapper {

    @Mapping(target = "fileId.fileSeq",  source = "pk.fileSeq")
    @Mapping(target = "fileId.fileType",  source = "pk.fileTyp")
    @Mapping(target = "fileId.fileSeries",  source = "pk.fileSer")
    @Mapping(target = "fileId.fileNbr",  source = "pk.fileNbr")
    @Mapping(source = "registrationNumber", target = "registrationNumber")
    @Mapping(source = "registrationDup", target = "registrationDup")
    @Mapping(source = "replacementFilingNumber", target = "replacementFilingNumber")
    @Mapping(source = "isAllServices", target = "isAllServices")
    @Mapping(source = "replacementNiceClasses", target = "replacementNiceClasses")
    @BeanMapping(ignoreByDefault = true)
    public abstract CMarkInternationalReplacement toCore(IpMarkInternationalReplacement internationalReplacement);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpMarkInternationalReplacement toEntity(CMarkInternationalReplacement internationalReplacement);
}
