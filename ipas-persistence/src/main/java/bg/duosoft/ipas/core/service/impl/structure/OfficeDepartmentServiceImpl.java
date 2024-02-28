package bg.duosoft.ipas.core.service.impl.structure;


import bg.duosoft.ipas.core.mapper.structure.OfficeDepartmentMapper;
import bg.duosoft.ipas.core.mapper.structure.OfficeDepartmentPkMapper;
import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.service.structure.OfficeDepartmentService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.structure.ArchiveDepartmentValidator;
import bg.duosoft.ipas.core.validation.structure.SaveDepartmentValidator;
import bg.duosoft.ipas.core.validation.structure.TransferDepartmentValidator;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentExtended;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentPK;
import bg.duosoft.ipas.persistence.repository.entity.structure.OfficeDepartmentExtendedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
@Transactional
public class OfficeDepartmentServiceImpl extends StructureServiceBaseImpl implements OfficeDepartmentService {
    @Autowired
    private OfficeDepartmentExtendedRepository officeDepartmentExtendedRepository;
    @Autowired
    private OfficeDepartmentMapper officeDepartmentMapper;
    @Autowired
    private OfficeDepartmentPkMapper officeDepartmentPkMapper;

    @Cacheable(value = "departments", key = "{#divisionCode, #departmentCode}")
    public OfficeDepartment getDepartment(String divisionCode, String departmentCode) {
        Optional<CfOfficeDepartmentExtended> dep = officeDepartmentExtendedRepository.findById(generatePk(divisionCode, departmentCode));
        return dep.isEmpty() ? null : officeDepartmentMapper.toCore(dep.get());

    }

    public List<OfficeDepartment> getDepartmentsByPartOfName(String partOfName, boolean onlyActive) {
        return officeDepartmentMapper.toCoreList(officeDepartmentExtendedRepository.findByOfficeDepartmentNameContainingIgnoreCase(partOfName, onlyActive));
    }


    @Override
    public List<OfficeDepartment> getDepartmentsOfDivision(String divisionCode, boolean onlyActive) {
        return officeDepartmentMapper.toCoreList(officeDepartmentExtendedRepository.getByOfficeDivisionCodeAndOnlyActive(divisionCode, onlyActive));
    }

    private CfOfficeDepartmentPK generatePk(String divisionCode, String departmentCode) {
        CfOfficeDepartmentPK pk = new CfOfficeDepartmentPK();
        pk.setOfficeDepartmentCode(departmentCode);
        pk.setOfficeDivisionCode(divisionCode);
        return pk;
    }


    @CacheEvict(value = "departments", key = "{#department.officeStructureId.officeDivisionCode, #department.officeStructureId.officeDepartmentCode}")
    @IpasValidatorDefinition({ArchiveDepartmentValidator.class})
    public void archiveDepartment(OfficeDepartment department) {
        CfOfficeDepartmentExtended entity = officeDepartmentMapper.toEntity(department);
        entity.setIndInactive("S");
        updateUpdatableEntity(entity);
    }

    @Override
    @IpasValidatorDefinition({TransferDepartmentValidator.class})
    @CacheEvict(value = "departments", allEntries = true /*key = "{#sections.officeStructureId.officeDivisionCode, #sections.officeStructureId.officeDepartmentCode, #sections.officeStructureId.officeSectionCode}"*/)
    public List<OfficeDepartment> transferDepartments(List<OfficeDepartment> departments, String divisionCode) {
        return departments.stream().map(r -> transferDepartment(r, divisionCode)).collect(Collectors.toList());
    }

    @Override
    public List<OfficeDepartment> findAll() {
        return officeDepartmentMapper.toCoreList(officeDepartmentExtendedRepository.findAll());
    }

    private OfficeDepartment transferDepartment(OfficeDepartment department, String divisionCode) {
        if (department.getOfficeStructureId().getOfficeDivisionCode().equals(divisionCode)) {
            return department;//trying to transfer to the same division!
        }

        CfOfficeDepartmentPK oldPk = officeDepartmentPkMapper.toEntity(department.getOfficeStructureId());
        CfOfficeDepartmentPK newPk = officeDepartmentExtendedRepository.transferDepartment(oldPk, divisionCode);
        return officeDepartmentMapper.toCore(officeDepartmentExtendedRepository.getOne(newPk));
    }

    @Override
    @CacheEvict(value = "departments", key = "{#department.officeStructureId.officeDivisionCode, #department.officeStructureId.officeDepartmentCode}")
    @IpasValidatorDefinition(SaveDepartmentValidator.class)
    public void updateDepartment(OfficeDepartment department) {
        CfOfficeDepartmentExtended dep = officeDepartmentExtendedRepository.getOne(officeDepartmentPkMapper.toEntity(department.getOfficeStructureId()));
        officeDepartmentMapper.fillEntityBean(department, dep);
        updateUpdatableEntity(dep);
    }

    @Override
    @IpasValidatorDefinition(SaveDepartmentValidator.class)
    public OfficeStructureId insertDepartment(OfficeDepartment department) {

        CfOfficeDepartmentExtended entity = officeDepartmentMapper.toEntity(department);

        //if the new section does not have sectionCode, a new one is getting generated and it's set to the two primary keys!
        if (entity.getCfOfficeDepartmentPK().getOfficeDepartmentCode() == null) {
            String departmentCode = officeDepartmentExtendedRepository.getNextDepartmentCode(department.getOfficeStructureId().getOfficeDivisionCode());
            entity.getCfOfficeDepartmentPK().setOfficeDepartmentCode(departmentCode);
        }

        insertInsertableEntity(entity);
        return officeDepartmentPkMapper.toCore(entity.getCfOfficeDepartmentPK());
    }
}
