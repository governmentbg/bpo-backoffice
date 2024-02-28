package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.UserdocTypeMapper;
import bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.UserdocTypeNomenclatureMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.userdoc.UserdocPanelService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPersonRoleService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.userdoc.*;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocTypes;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfUserdocTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocTypesRepository;
import bg.duosoft.ipas.util.filter.UserdocTypesFilter;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserdocTypesServiceImpl implements UserdocTypesService {

    @Autowired
    private IpUserdocTypesRepository ipUserdocTypesRepository;

    @Autowired
    private CfUserdocTypeRepository cfUserdocTypeRepository;

    @Autowired
    private UserdocTypeMapper userdocTypeMapper;

    @Autowired
    private UserdocTypeNomenclatureMapper userdocTypeNomenclatureMapper;

    @Autowired
    private UserdocPanelService userdocPanelService;

    @Autowired
    private UserdocPersonRoleService userdocPersonRoleService;


    @Override
    public CUserdocType selectUserdocTypeByDocId(CDocumentId documentId) {
        if (Objects.isNull(documentId))
            return null;

        IpUserdocTypes ipUserdocTypes = ipUserdocTypesRepository.selectByDocId(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
        if (Objects.isNull(ipUserdocTypes))
            return null;

        return userdocTypeMapper.toCore(ipUserdocTypes);
    }

    @Override
    @Cacheable(value = "userdocTypes", key = "{'all'}")
    public Map<String, String> selectUserdocTypesMap() {
        List<CfUserdocType> all = cfUserdocTypeRepository.findAll();
        if (CollectionUtils.isEmpty(all))
            return null;

        return createAndSortUserdocTypeMap(all);
    }

    @Override
//    @Cacheable(value = "userdocTypes", key = "{'id', #userdocType}")
    public CUserdocType selectUserdocTypeById(String userdocType) {
        if (Objects.isNull(userdocType))
            return null;

        CfUserdocType cfUserdocType = cfUserdocTypeRepository.findById(userdocType).orElse(null);
        if (Objects.isNull(cfUserdocType))
            return null;

        return userdocTypeNomenclatureMapper.toCore(cfUserdocType);
    }

    @Override
    public Map<String, String> selectAllowedUserdocTypesForChange(CUserdoc userdoc) {
        String type = UserdocUtils.selectParentUserdocType(userdoc.getUserdocParentData());
        List<CfUserdocType> types = cfUserdocTypeRepository.selectUserdocTypesMapByUserdocParentType(type);
        if (CollectionUtils.isEmpty(types))
            return null;

        CUserdocType userdocType = userdoc.getUserdocType();
        boolean isCurrentUserdocTypeMissing = Objects.isNull(types.stream().filter(cfUserdocType -> cfUserdocType.getUserdocTyp().equalsIgnoreCase(userdocType.getUserdocType())).findAny().orElse(null));
        if (isCurrentUserdocTypeMissing) {
            types.add(userdocTypeNomenclatureMapper.toEntity(userdocType));
        }

        return createAndSortUserdocTypeMap(types);
    }

    private Map<String, String> createAndSortUserdocTypeMap(List<CfUserdocType> types) {
        types.sort(Comparator.comparing(CfUserdocType::getUserdocName));
        Map<String, String> map = types.stream().collect(Collectors.toMap(CfUserdocType::getUserdocTyp, CfUserdocType::getUserdocName));

        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    @Override
    public Map<String, String> selectUserdocTypesByUserdocName(String userdocName) {
        List<CfUserdocType> userdocs = cfUserdocTypeRepository.findAllByUserdocNameContainingOrderByUserdocName(userdocName);
        if (CollectionUtils.isEmpty(userdocs))
            return null;

        return createAndSortUserdocTypeMap(userdocs);
    }

    @Transactional(readOnly = true)
    public List<String> getAllProcTypes() {
        List<String> allProcTypes = cfUserdocTypeRepository.getAllProcTypes();
        if (CollectionUtils.isEmpty(allProcTypes)) {
            return null;
        }

        return allProcTypes;
    }

    public Map<String, String> selectDistinctUserdocTypeMapForUserdocsByUsersAndUserdocGroupName(List<Integer>users, String userdocGroupName) {
        List<Object[]> result = cfUserdocTypeRepository.selectDistinctUserdocTypeMapForUserdocsByUsersAndUserdocGroupName(users, userdocGroupName);
        Map<String, String> map = fillUserdoTypeMap(result);
        return map;
    }

    @Override
    public Map<String, String> selectDistinctUserdocTypeMapForUserdocsByResponsibleUser(Integer responsibleUserId) {
        List<Object[]> result = cfUserdocTypeRepository.selectDistinctUserdocTypeMapForUserdocsByResponsibleUser(responsibleUserId);
        Map<String, String> map = fillUserdoTypeMap(result);
        return map;
    }

    private Map<String, String> fillUserdoTypeMap(List<Object[]> result) {
        Map<String, String> map = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                map.put((String) obj[0], (String) obj[1]);
            }
        }
        return map;
    }

    @Override
    public Map<String, String> findAllByUserdocTypInOrderByUserdocName(List<String> userdocTypes) {
        List<CfUserdocType> allByUserdocTypInOrderByUserdocGroupName = cfUserdocTypeRepository.findAllByUserdocTypInOrderByUserdocName(userdocTypes);
        Map<String, String> map = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(allByUserdocTypInOrderByUserdocGroupName)) {
            for (CfUserdocType cfUserdocType : allByUserdocTypInOrderByUserdocGroupName) {
                map.put(cfUserdocType.getUserdocTyp(), cfUserdocType.getUserdocName());
            }
        }
        return map;
    }

    @Override
    public List<CUserdocType> selectUserdocTypesMapByUserdocParentType(String mainType) {
        if (StringUtils.isEmpty(mainType))
            return null;

        List<CfUserdocType> types = cfUserdocTypeRepository.selectUserdocTypesMapByUserdocParentType(mainType);
        if (CollectionUtils.isEmpty(types))
            return null;

        return userdocTypeNomenclatureMapper.toCoreList(types);
    }

    @Override
    public List<CUserdocType> selectUserdocTypes(UserdocTypesFilter filter) {
        List<CfUserdocType> userdocTypes = cfUserdocTypeRepository.getUserdocTypes(filter);
        return userdocTypeNomenclatureMapper.toCoreList(userdocTypes);
    }

    @Override
    public Integer selectUserdocTypesCount(UserdocTypesFilter filter) {
        return cfUserdocTypeRepository.getUserdocTypesCount(filter);
    }

    @Override
    public Map<String, String> selectInvalidatedUserdocNames(List<String> invalidatedUserdocTypes) {
        List<CfUserdocType> invalidatedUserdocs =  cfUserdocTypeRepository.findAllByUserdocTypInOrderByUserdocName(invalidatedUserdocTypes);
        if (CollectionUtils.isEmpty(invalidatedUserdocs)) {
            return null;
        }

        return invalidatedUserdocs.stream().collect(Collectors.toMap(CfUserdocType::getUserdocTyp, CfUserdocType::getUserdocName));
    }

    @Override
    public List<CUserdocType> selectAutocompleteUserdocTypesForInvalidation(String invalidateTypeTerm, String currentUserdocType) {
        Optional<CfUserdocType> userdocType = cfUserdocTypeRepository.findById(currentUserdocType);

        if (userdocType.isPresent()) {
            List<String> excludeUserdocTypes = userdocType.get().getInvalidationRelations().stream().map(r -> r.getPk().getInvalidatedUserdocType()).collect(Collectors.toList());
            excludeUserdocTypes.add(currentUserdocType);
            List<CUserdocType> cUserdocTypes = userdocTypeNomenclatureMapper.toCoreList(cfUserdocTypeRepository.selectAutocompleteUserdocTypesForInvalidation(excludeUserdocTypes, invalidateTypeTerm.trim()));
            cUserdocTypes.sort(Comparator.comparing(CUserdocType::getUserdocName));
            return cUserdocTypes;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> selectUserdocTypesForInvalidation(String currentUserdocType) {
        return cfUserdocTypeRepository.selectUserdocTypesForInvalidation(currentUserdocType);
    }

    @Override
    @IpasValidatorDefinition({AddInvalidatedUserdocTypeValidator.class})
    public CUserdocType addInvalidatedUserdocType(String userdocType, String invalidateUserdocType) {
       CUserdocType cUserdocType = selectUserdocTypeById(userdocType);
       cUserdocType.getInvalidatedUserdocTypes().add(invalidateUserdocType);
       return saveUserdocType(cUserdocType);
    }

    @Override
    @IpasValidatorDefinition({DeleteInvalidatedUserdocTypeValidator.class})
    public CUserdocType deleteInvalidatedUserdocType(String userdocType, String invalidateUserdocType) {
        CUserdocType cUserdocType = selectUserdocTypeById(userdocType);
        cUserdocType.getInvalidatedUserdocTypes().remove(invalidateUserdocType);
        return saveUserdocType(cUserdocType);
    }

    @Override
    @CacheEvict(value = "userdocTypes", key = "{'all'}")
    public CUserdocType saveUserdocType(CUserdocType userdocType) {
        CfUserdocType cfUserdocType = cfUserdocTypeRepository.getOne(userdocType.getUserdocType());
        userdocTypeNomenclatureMapper.fillEntityFields(userdocType, cfUserdocType);

        CfUserdocType updated = cfUserdocTypeRepository.save(cfUserdocType);
        return userdocTypeNomenclatureMapper.toCore(updated);
    }

    @Override
    public List<String> selectUserdocsInvalidatingCurrentUserdoc(String currentUserdocType) {
        List<String> userdocNames = cfUserdocTypeRepository.selectUserdocsInvalidatingCurrentUserdoc(currentUserdocType);
        return userdocNames;
    }

    @Override
    @IpasValidatorDefinition({AddPanelForUserdocTypeValidator.class})
    public CUserdocType addPanelForUserdocType(String userdocType, String panel) {
        CUserdocType cUserdocType = selectUserdocTypeById(userdocType);
        CUserdocPanel cUserdocPanel = userdocPanelService.findPanelById(panel);
        cUserdocType.getPanels().add(cUserdocPanel);
        return saveUserdocType(cUserdocType);
    }

    @Override
    @IpasValidatorDefinition({DeletePanelForUserdocTypeValidator.class})
    public CUserdocType deletePanelForUserdocType(String userdocType, String panel) {
        CUserdocType cUserdocType = selectUserdocTypeById(userdocType);
        CUserdocPanel cUserdocPanel = userdocPanelService.findUserdocPanelByPanelAndUserdocType(panel, userdocType);
        cUserdocType.getPanels().remove(cUserdocPanel);
        return saveUserdocType(cUserdocType);
    }

    @Override
    @IpasValidatorDefinition({AddPersonRoleForUserdocTypeValidator.class})
    public CUserdocType addRoleForUserdocType(String userdocType, UserdocPersonRole role) {
        CUserdocType cUserdocType = selectUserdocTypeById(userdocType);
        CUserdocPersonRole cUserdocPersonRole = userdocPersonRoleService.selectByRole(role);
        cUserdocType.getRoles().add(cUserdocPersonRole);
        return saveUserdocType(cUserdocType);
    }

    @Override
    @IpasValidatorDefinition({DeletePersonRoleForUserdocTypeValidator.class})
    public CUserdocType deleteRoleForUserdocType(String userdocType, UserdocPersonRole role) {
        CUserdocType cUserdocType = selectUserdocTypeById(userdocType);
        CUserdocPersonRole cUserdocPersonRole = userdocPersonRoleService.findPersonRoleByRoleAndUserdocType(role, userdocType);
        cUserdocType.getRoles().remove(cUserdocPersonRole);
        return saveUserdocType(cUserdocType);
    }

    @Override
    @Cacheable(value = "userdocTypesByListCode", key = "{#userdocListCode}")
    public List<String> selectUserdocTypesByListCode(String userdocListCode) {
        if (StringUtils.isEmpty(userdocListCode))
            return null;

        return cfUserdocTypeRepository.selectUserdocTypesByListCode(userdocListCode);
    }
}