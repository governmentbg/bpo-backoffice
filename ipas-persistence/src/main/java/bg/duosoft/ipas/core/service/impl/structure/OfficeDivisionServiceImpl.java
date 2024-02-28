package bg.duosoft.ipas.core.service.impl.structure;

import bg.duosoft.ipas.core.mapper.structure.OfficeDivisionMapper;
import bg.duosoft.ipas.core.model.structure.OfficeDivision;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.service.structure.OfficeDivisionService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.structure.ArchiveDivisionValidator;
import bg.duosoft.ipas.core.validation.structure.SaveDivisionValidator;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDivisionExtended;
import bg.duosoft.ipas.persistence.repository.entity.structure.OfficeDivisionExtendedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OfficeDivisionServiceImpl extends StructureServiceBaseImpl implements OfficeDivisionService {
    @Autowired
    private OfficeDivisionMapper officeDivisionMapper;

    @Autowired
    private OfficeDivisionExtendedRepository officeDivisionExtendedRepository;

    @Cacheable(value = "divisions", key = "{#id}")
    public OfficeDivision getDivision(String id) {
        Optional<CfOfficeDivisionExtended> div = officeDivisionExtendedRepository.findById(id);
        return div.isEmpty() ? null : toOfficeDivision(div.get());
    }

    @Override
    public List<OfficeDivision> getDivisionsByPartOfName(String partOfName, boolean onlyActive) {
        return officeDivisionMapper.toCoreList(officeDivisionExtendedRepository.findByOfficeDivisionNameContainingIgnoreCase(partOfName, onlyActive));
    }

    @Override
    @IpasValidatorDefinition(ArchiveDivisionValidator.class)
    @CacheEvict(value = "divisions", key = "{#division.officeStructureId.officeDivisionCode}")
    public void archiveDivision(OfficeDivision division) {
        CfOfficeDivisionExtended entity = officeDivisionMapper.toEntity(division);
        entity.setIndInactive("S");
        updateUpdatableEntity(entity);
    }


    @Override
    @IpasValidatorDefinition(SaveDivisionValidator.class)
    @CacheEvict(value = "divisions", key = "{#division.officeStructureId.officeDivisionCode}")
    public void updateDivision(OfficeDivision division) {
        CfOfficeDivisionExtended dbEntity = officeDivisionExtendedRepository.getOne(division.getOfficeStructureId().getOfficeDivisionCode());
        officeDivisionMapper.fillEntityBean(division, dbEntity);
        updateUpdatableEntity(dbEntity);
    }

    @Override
    @IpasValidatorDefinition(SaveDivisionValidator.class)
    public OfficeStructureId insertDivision(OfficeDivision division) {
        CfOfficeDivisionExtended entity = officeDivisionMapper.toEntity(division);
        String divisionCode = entity.getOfficeDivisionCode();
        //if the new section does not have sectionCode, a new one is getting generated and it's set to the two primary keys!
        if (divisionCode == null) {
            divisionCode = officeDivisionExtendedRepository.getNextDivisionCode();
            entity.setOfficeDivisionCode(divisionCode);
        }

        insertInsertableEntity(entity);
        return new OfficeStructureId(divisionCode, null, null);
    }

    private OfficeDivision toOfficeDivision(CfOfficeDivisionExtended cfOfficeDivision) {
        if (cfOfficeDivision == null) {
            return null;
        }
        return officeDivisionMapper.toCore(cfOfficeDivision);
    }
}
