package bg.duosoft.ipas.core.service.impl.structure;

import bg.duosoft.ipas.core.mapper.structure.OfficeSectionMapper;
import bg.duosoft.ipas.core.mapper.structure.OfficeSectionPkMapper;
import bg.duosoft.ipas.core.model.structure.OfficeSection;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.service.structure.OfficeSectionService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.structure.ArchiveSectionValidator;
import bg.duosoft.ipas.core.validation.structure.SaveSectionValidator;
import bg.duosoft.ipas.core.validation.structure.TransferSectionValidator;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentPK;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSectionExtended;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSectionPK;
import bg.duosoft.ipas.persistence.repository.entity.structure.OfficeSectionExtendedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OfficeSectionServiceImpl extends StructureServiceBaseImpl implements OfficeSectionService {
    @Autowired
    private OfficeSectionExtendedRepository officeSectionExtendedRepository;
    @Autowired
    private OfficeSectionPkMapper officeSectionPkMapper;
    @Autowired
    private OfficeSectionMapper officeSectionMapper;


    @Cacheable(value = "sections", key = "{#divisionCode, #departmentCode, #sectionCode}")
    public OfficeSection getSection(String divisionCode, String departmentCode, String sectionCode) {
        Optional<CfOfficeSectionExtended> res = officeSectionExtendedRepository.findById(new CfOfficeSectionPK(divisionCode, departmentCode, sectionCode));
        return res.isPresent() ? officeSectionMapper.toCore(res.get()) : null;
    }

    @Override
    public List<OfficeSection> getSectionsOfDepartment(String divisionCode, String departmentCode, boolean onlyActive) {
        return officeSectionMapper.toCoreList(officeSectionExtendedRepository.getByOfficeDivisionCodeAndOfficeDepartmentCode(divisionCode, departmentCode, onlyActive));
    }

    @Override
    public List<OfficeSection> getSectionsByPartOfName(String partOfName, boolean onlyActive) {
        return officeSectionMapper.toCoreList(officeSectionExtendedRepository.findByOfficeSectionNameContainingIgnoreCase(partOfName, onlyActive));
    }

    @Override
    @IpasValidatorDefinition({TransferSectionValidator.class})
    @CacheEvict(value = "sections",  allEntries = true /*key = "{#sections.officeStructureId.officeDivisionCode, #sections.officeStructureId.officeDepartmentCode, #sections.officeStructureId.officeSectionCode}"*/)
    public List<OfficeSection> transferSections(List<OfficeSection> sections, String divisionCode, String departmentCode) {
        return sections.stream().map(s ->transferSection(s, divisionCode, departmentCode)).collect(Collectors.toList());
    }
    private OfficeSection transferSection(OfficeSection section, String divisionCode, String departmentCode) {
        if (section.getOfficeStructureId().getOfficeDivisionCode().equals(divisionCode) && section.getOfficeStructureId().getOfficeDepartmentCode().equals(departmentCode)) {
            return section;//trying to transfer to the same department!
        }

        CfOfficeSectionPK oldPk = officeSectionPkMapper.toEntity(section.getOfficeStructureId());
        CfOfficeSectionPK newPk = officeSectionExtendedRepository.transferSection(oldPk, new CfOfficeDepartmentPK(divisionCode, departmentCode), true);
        return officeSectionMapper.toCore(officeSectionExtendedRepository.getOne(newPk));
    }




    @CacheEvict(value = "sections", key = "{#section.officeStructureId.officeDivisionCode, #section.officeStructureId.officeDepartmentCode, #section.officeStructureId.officeSectionCode}")
    @IpasValidatorDefinition({ArchiveSectionValidator.class})
    public void archiveSection(OfficeSection section) {
        CfOfficeSectionExtended e = officeSectionExtendedRepository.getOne(officeSectionPkMapper.toEntity(section.getOfficeStructureId()));
        e.setIndInactive("S");
        updateUpdatableEntity(e);
    }

    @CacheEvict(value = "sections", key = "{#section.officeStructureId.officeDivisionCode, #section.officeStructureId.officeDepartmentCode, #section.officeStructureId.officeSectionCode}")
    @IpasValidatorDefinition({SaveSectionValidator.class})
    public void updateSection(OfficeSection section) {
        CfOfficeSectionExtended dbSection = officeSectionExtendedRepository.getOne(officeSectionPkMapper.toEntity(section.getOfficeStructureId()));
        officeSectionMapper.fillEntityBean(section, dbSection);
        updateUpdatableEntity(dbSection);
    }

    @IpasValidatorDefinition({SaveSectionValidator.class})
    public OfficeStructureId insertSection(OfficeSection section) {

        CfOfficeSectionExtended entity = officeSectionMapper.toEntity(section);

        //if the new section does not have sectionCode, a new one is getting generated and it's set to the two primary keys!
        if (entity.getCfOfficeSectionPK().getOfficeSectionCode() == null) {
            String sectionCode = officeSectionExtendedRepository.getNextSectionCode(section.getOfficeStructureId().getOfficeDivisionCode(), section.getOfficeStructureId().getOfficeDepartmentCode());
            entity.getCfOfficeSectionPK().setOfficeSectionCode(sectionCode);
        }

        insertInsertableEntity(entity);
        return officeSectionPkMapper.toCore(entity.getCfOfficeSectionPK());
    }

}
