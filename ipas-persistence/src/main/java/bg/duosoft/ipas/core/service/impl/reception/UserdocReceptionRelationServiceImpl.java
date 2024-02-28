package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.mapper.reception.UserdocReceptionRelationMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.reception.CUserdocReceptionRelation;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.reception.UserdocReceptionRelationService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.userdoc.AddReceptionRelationForUserdocTypeValidator;
import bg.duosoft.ipas.core.validation.userdoc.DeleteReceptionRelationForUserdocTypeValidator;
import bg.duosoft.ipas.core.validation.userdoc.UpdateReceptionRelationValidator;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.UserdocReceptionRelation;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.UserdocReceptionRelationPK;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocMainTypeSimpleResult;
import bg.duosoft.ipas.persistence.repository.entity.reception.UserdocReceptionRelationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@Transactional
public class UserdocReceptionRelationServiceImpl implements UserdocReceptionRelationService {

    @Autowired
    private UserdocReceptionRelationRepository userdocReceptionRelationRepository;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private UserdocReceptionRelationMapper userdocReceptionRelationMapper;

    @Override
    @Cacheable(value = "userdocReceptionRelation")
    public List<CUserdocReceptionRelation> selectUserdocsByMainType(String fileType, boolean onlyVisible) {
        if (StringUtils.isEmpty(fileType))
            return null;

        List<UserdocReceptionRelation> result = userdocReceptionRelationRepository.selectUserdocReceptionRelationByMainType(fileType, onlyVisible ? "S" : null, onlyVisible ? "N" : null);
        if (CollectionUtils.isEmpty(result))
            return null;
        List<CUserdocReceptionRelation> cUserdocReceptionRelations = userdocReceptionRelationMapper.toCoreList(result);
        cUserdocReceptionRelations.sort(Comparator.comparing(CUserdocReceptionRelation::getUserdocName));
        return cUserdocReceptionRelations;
    }

    @Override
    public Map<String, String> selectMainTypesMap() {
        return userdocReceptionRelationRepository.selectMainTypesMap();
    }

    @Override
    public List<UserdocMainTypeSimpleResult> selectAllMainTypesForUserdocType(String userdocType) {
        return userdocReceptionRelationRepository.selectMainTypesByUserdocType(userdocType);
    }

    @Override
    @IpasValidatorDefinition({AddReceptionRelationForUserdocTypeValidator.class})
    public void addReceptionRelationForUserdocType(String userdocType, String mainType, Boolean isVisible) {
        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(userdocType);
        CUserdocReceptionRelation cUserdocReceptionRelation = new CUserdocReceptionRelation();
        cUserdocReceptionRelation.setUserdocType(userdocType);
        cUserdocReceptionRelation.setUserdocName(cUserdocType.getUserdocName());
        cUserdocReceptionRelation.setMainType(mainType);
        cUserdocReceptionRelation.setIsVisible(isVisible);

        saveReceptionRelation(cUserdocReceptionRelation);
    }

    @Override
    @IpasValidatorDefinition({DeleteReceptionRelationForUserdocTypeValidator.class})
    public void deleteReceptionRelationForUserdocType(String userdocType, String mainType) {
        userdocReceptionRelationRepository.deleteById(new UserdocReceptionRelationPK(userdocType, mainType));
    }

    @Override
    public Boolean existsById(String userdocType, String mainType) {
        return userdocReceptionRelationRepository.existsById(new UserdocReceptionRelationPK(userdocType, mainType));
    }

    @Override
    public CUserdocReceptionRelation selectById(String userdocType, String mainType) {
        if (StringUtils.isEmpty(userdocType) || StringUtils.isEmpty(mainType)) {
            return null;
        }

        UserdocReceptionRelation userdocReceptionRelation = userdocReceptionRelationRepository.findById(new UserdocReceptionRelationPK(userdocType, mainType)).orElse(null);
        if (Objects.isNull(userdocReceptionRelation)) {
            return null;
        }

        return userdocReceptionRelationMapper.toCore(userdocReceptionRelation);
    }

    @Override
    @IpasValidatorDefinition({UpdateReceptionRelationValidator.class})
    public void updateReceptionRelation(String userdocType, String mainType, Boolean isVisible) {
        CUserdocReceptionRelation cUserdocReceptionRelation = selectById(userdocType, mainType);
        cUserdocReceptionRelation.setIsVisible(isVisible);
        saveReceptionRelation(cUserdocReceptionRelation);
    }

    private void saveReceptionRelation(CUserdocReceptionRelation cUserdocReceptionRelation) {
        UserdocReceptionRelation userdocReceptionRelation = userdocReceptionRelationMapper.toEntity(cUserdocReceptionRelation);
        userdocReceptionRelationRepository.save(userdocReceptionRelation);
    }

}
