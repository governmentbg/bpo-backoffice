package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocApprovedNiceClasses;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkIntlReplacementNiceClasses;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfProcessResultType;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfProcessResultTypeRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class MarkIntlReplacementNiceClassMapper {
    @Autowired
    private CfProcessResultTypeRepository cfProcessResultTypeRepository;

    @Mapping(source = "pk.niceClassCode", target = "niceClassNbr")
    @Mapping(source = "pk.niceClassStatusWcode", target = "niceClassDetailedStatus")
    @Mapping(source = "niceClassEdition", target = "niceClassEdition")
    @Mapping(source = "niceClassDescrLang2", target = "niceClassDescriptionInOtherLang")
    @Mapping(source = "niceClassDescription", target = "niceClassDescription")
    @Mapping(source = "niceClassVersion", target = "niceClassVersion")
    @BeanMapping(ignoreByDefault = true)
    public abstract CNiceClass toCore(IpMarkIntlReplacementNiceClasses intlReplacementNiceClasses);

    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "pk.niceClassCode", source = "niceClassNbr")
    @Mapping(target = "pk.niceClassStatusWcode", source = "niceClassDetailedStatus")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpMarkIntlReplacementNiceClasses toEntity(CNiceClass cNiceClass);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpMarkIntlReplacementNiceClasses> toEntityList(List<CNiceClass> cNiceClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CNiceClass> toCoreList(List<IpMarkIntlReplacementNiceClasses> ipMarkIntlReplacementNiceClasses);

    @AfterMapping
    protected void afterToCore(IpMarkIntlReplacementNiceClasses source, @MappingTarget CNiceClass target) {
        Optional<CfProcessResultType> processResultType = cfProcessResultTypeRepository.findById(source.getPk().getNiceClassStatusWcode());
        String globalStatus = "N";
        if (processResultType.isPresent()) {
            if ("S".equals(processResultType.get().getIndPending())) {
                globalStatus = "P";
            } else if ("S".equals(processResultType.get().getIndSuccess())){
                globalStatus = "R";
            }
        }
        target.setNiceClassGlobalStatus(globalStatus);
        target.generateInitialTermsFromDescription(CommonTerm.UNKNOWN);
    }
}
