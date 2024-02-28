package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CViennaClass;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogoViennaClasses;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkAttachmentViennaClasses;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.*;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfClassViennaDivisionRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfClassViennaSectionRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class ViennaClassMapper {
    @Autowired
    private CfClassViennaSectionRepository cfClassViennaSectionRepository;
    @Autowired
    private CfClassViennaDivisionRepository cfClassViennaDivisionRepository;

    @Mapping(target = "viennaVersion", source = "viennaEditionCode")
    @Mapping(target = "vclWpublishValidated", source = "vclWpublishValidate")

    @Mapping(target = "viennaCategory", source = "pk.viennaClassCode")
    @Mapping(target = "viennaDivision", source = "pk.viennaGroupCode")
    @Mapping(target = "viennaSection", source = "pk.viennaElemCode")
    @BeanMapping(ignoreByDefault = true)
    public abstract CViennaClass toCore(IpLogoViennaClasses source);

    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpLogoViennaClasses toEntity(CViennaClass source);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpLogoViennaClasses> toEntityList(List<CViennaClass> viennaClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CViennaClass> toCoreList(List<IpLogoViennaClasses> ipViennaClasses);

    @AfterMapping
    protected void afterToCore(IpLogoViennaClasses source, @MappingTarget CViennaClass target) {
//        source.getPk().get
        Optional<CfClassViennaSect> section = cfClassViennaSectionRepository.findById(new CfClassViennaSectPK(target.getViennaCategory(), target.getViennaDivision(), target.getViennaSection(), target.getViennaVersion()));
        if (section.isPresent()) {
            target.setViennaDescription(section.get().getViennaSectionName());
        } else {
            Optional<CfClassViennaDivis> division = cfClassViennaDivisionRepository.findById(new CfClassViennaDivisPK(target.getViennaCategory(), target.getViennaDivision(), target.getViennaVersion()));
            division.ifPresent(d -> target.setViennaDescription(d.getViennaDivisionName()));
        }
    }

    /**
     * Get selected fields of CfClassViennaCateg and set it to CViennaClass
     * @param source
     * @return new instance of object CViennaClass
     * @author Denislav Veizov
     */
    @Mapping(target = "viennaVersion", source = "pk.viennaEditionCode")
    @Mapping(target = "viennaCategory", source = "pk.viennaCategoryCode")
    @Mapping(target = "viennaDescription", source = "viennaCategoryName")
    @BeanMapping(ignoreByDefault = true)
    public abstract CViennaClass toCore(CfClassViennaCateg source);

    /**
     * Get selected fields of CfClassViennaDivis and set it to CViennaClass
     * @param source
     * @return new instance of object CViennaClass
     * @author Denislav Veizov
     */
    @Mapping(target = "viennaVersion", source = "pk.viennaEditionCode")
    @Mapping(target = "viennaCategory", source = "pk.viennaCategoryCode")
    @Mapping(target = "viennaDivision", source = "pk.viennaDivisionCode")
    @Mapping(target = "viennaDescription", source = "viennaDivisionName")
    @BeanMapping(ignoreByDefault = true)
    public abstract CViennaClass toCore(CfClassViennaDivis source);


    /**
     * Get selected fields of CfClassViennaSect and set it to CViennaClass
     * @param source
     * @return new instance of object CViennaClass
     * @author Denislav Veizov
     */
    @Mapping(target = "viennaVersion", source = "pk.viennaEditionCode")
    @Mapping(target = "viennaCategory", source = "pk.viennaCategoryCode")
    @Mapping(target = "viennaDivision", source = "pk.viennaDivisionCode")
    @Mapping(target = "viennaSection", source = "pk.viennaSectionCode")
    @Mapping(target = "viennaDescription", source = "viennaSectionName")
    @BeanMapping(ignoreByDefault = true)
    public abstract CViennaClass toCore(CfClassViennaSect source);


    @Mapping(target = "viennaVersion", source = "viennaEditionCode")
    @Mapping(target = "vclWpublishValidated", source = "vclWpublishValidated")
    @Mapping(target = "viennaCategory", source = "pk.viennaClassCode")
    @Mapping(target = "viennaDivision", source = "pk.viennaGroupCode")
    @Mapping(target = "viennaSection", source = "pk.viennaElemCode")
    @BeanMapping(ignoreByDefault = true)
    public abstract CViennaClass toCoreMarkAttachment(IpMarkAttachmentViennaClasses source);

    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpMarkAttachmentViennaClasses toEntityMarkAttachment(CViennaClass source);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpMarkAttachmentViennaClasses> toEntityMarkAttachmentList(List<CViennaClass> viennaClasses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CViennaClass> toCoreMarkAttachmentList(List<IpMarkAttachmentViennaClasses> ipViennaClasses);

    @AfterMapping
    protected void afterToCoreMarkAttachment(IpMarkAttachmentViennaClasses source, @MappingTarget CViennaClass target) {
        afterToCore(null, target);
    }


}
