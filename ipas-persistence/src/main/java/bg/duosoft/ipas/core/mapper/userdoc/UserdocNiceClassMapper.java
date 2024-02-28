package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocNiceClasses;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfProcessResultType;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfProcessResultTypeRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {
    StringToBooleanMapper.class
})
public abstract class UserdocNiceClassMapper {

    @Autowired
    private CfProcessResultTypeRepository cfProcessResultTypeRepository;

    @Mapping(source = "pk.niceClassCode", target = "niceClassNbr")
    @Mapping(source = "pk.niceClassStatusWcode", target = "niceClassDetailedStatus")
//    @Mapping(source = "allTermsDeclaration", target = "allTermsDeclaration")//allTermsDeclaration is null for userdocs
    @Mapping(source = "niceClassEdition", target = "niceClassEdition")
    @Mapping(source = "niceClassDescription", target = "niceClassDescription")
    @Mapping(source = "niceClassDescrLang2", target = "niceClassDescriptionInOtherLang")
    @Mapping(source = "niceClassVersion", target = "niceClassVersion")
    @BeanMapping(ignoreByDefault = true)
    public abstract CNiceClass toCore(IpUserdocNiceClasses ipUserdocNiceClasses);

    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocNiceClasses toEntity(CNiceClass cNiceClass);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpUserdocNiceClasses> toEntityList(List<CNiceClass> cNiceClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CNiceClass> toCoreList(List<IpUserdocNiceClasses> ipUserdocNiceClasses);

    @AfterMapping
    protected void afterToCore(IpUserdocNiceClasses source, @MappingTarget CNiceClass target) {
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
