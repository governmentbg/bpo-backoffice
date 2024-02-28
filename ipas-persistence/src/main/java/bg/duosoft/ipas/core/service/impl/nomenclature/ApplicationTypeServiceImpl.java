package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.ApplicationSubTypesMapper;
import bg.duosoft.ipas.core.model.CApplicationSubType;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.enums.ApplSubTyp;
import bg.duosoft.ipas.enums.ApplTyp;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtypePK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationType;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfApplicationSubTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfApplicationTypeRepository;
import bg.duosoft.ipas.util.map.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationTypeServiceImpl implements ApplicationTypeService {

    @Autowired
    private CfApplicationTypeRepository cfApplicationTypeRepository;

    @Autowired
    private CfApplicationSubTypeRepository cfApplicationSubTypeRepository;

    @Autowired
    private ApplicationSubTypesMapper applicationSubTypesMapper;
    private static final Map<String, String> DEFAULT_APPLICATION_SUBTYPE = new HashMap<>();

    static {
        DEFAULT_APPLICATION_SUBTYPE.put(ApplTyp.NATIONAL_MARK_TYPE, "ИМ");
        DEFAULT_APPLICATION_SUBTYPE.put(ApplTyp.EUPATENT_TYPE, "ЕВ");
        DEFAULT_APPLICATION_SUBTYPE.put(ApplTyp.UTILITY_MODEL_TYPE, "ПМ");
        DEFAULT_APPLICATION_SUBTYPE.put(ApplTyp.NATIONAL_PATENT_TYPE, "ПТ");
        DEFAULT_APPLICATION_SUBTYPE.put(ApplTyp.DESIGN_APP_TYPE, "ПД");
        DEFAULT_APPLICATION_SUBTYPE.put(ApplTyp.GEOGRAPHICAL_INDICATION_TYPE, "ГУ");
        DEFAULT_APPLICATION_SUBTYPE.put(ApplTyp.SPC_TYPE, "ЛП");
        DEFAULT_APPLICATION_SUBTYPE.put(ApplTyp.BREEDS_VARIETIES_TYPE, "СР");
        DEFAULT_APPLICATION_SUBTYPE.put(ApplTyp.INTERNATIONAL_DESIGN_APP_TYPE, "МД");
        DEFAULT_APPLICATION_SUBTYPE.put(ApplTyp.ACP_SIGNAL, ApplSubTyp.ACP_SIGNAL_SUB_TYPE);
    }

    @Override
    @Cacheable(value = "applicationTypes", key = "{'applicaitonTypesPerTableName', #tableName}")
    public Map<String, String> getApplicationTypesMap(String tableName) {
        List<CfApplicationType> cfApplicationTypes = cfApplicationTypeRepository.findAllByTableName(tableName);
        if (CollectionUtils.isEmpty(cfApplicationTypes))
            return null;

        Map<String, String> collected = cfApplicationTypes.stream()
                .collect(Collectors.toMap(CfApplicationType::getApplTyp, CfApplicationType::getApplTypeName));

        return MapUtils.sortMapByValue(collected);
    }

    @Override
    @Cacheable(value = "applicationTypes", key = "{'applicaitonTypesPerTableNameAndFileTypes', #tableName, #fileTypes}")
    public Map<String, String> getApplicationTypesMap(String tableName, List<String> fileTypes) {
        List<CfApplicationType> cfApplicationTypes = cfApplicationTypeRepository.findAllByTableNameAndFileTypIn(tableName, fileTypes);
        if (CollectionUtils.isEmpty(cfApplicationTypes))
            return null;

        Map<String, String> collected = cfApplicationTypes.stream()
                .collect(Collectors.toMap(CfApplicationType::getApplTyp, CfApplicationType::getApplTypeName));

        return MapUtils.sortMapByValue(collected);
    }

    @Override
    @Cacheable(value = "applicationSubTypes", key = "{'byApplicationType', #appTyp}")
    public List<CApplicationSubType> findAllApplicationSubTypesByApplTyp(String appTyp) {
        List<CfApplicationSubtype> cfApplicationSubtypes = cfApplicationSubTypeRepository.findAllByPk_ApplTyp(appTyp);
        if (CollectionUtils.isEmpty(cfApplicationSubtypes))
            return null;

        List<CApplicationSubType> cApplicationSubTypes = applicationSubTypesMapper.toCoreList(cfApplicationSubtypes);
        cApplicationSubTypes.sort(Comparator.comparing(CApplicationSubType::getApplicationSubTypeName));
        return cApplicationSubTypes;
    }

    @Override
    public List<CApplicationSubType> findAllSingleDesignApplicationSubTypesByMasterApplTyp(String applTyp) {
        String singleDesignApplTyp = null;
        if (Objects.nonNull(applTyp) && (Objects.equals(applTyp, ApplTyp.DESIGN_APP_TYPE) || Objects.equals(applTyp, ApplTyp.DIVIDED_NATIONAL_DESIGN))) {
            singleDesignApplTyp = ApplTyp.SINGLE_DESIGN_TYPE;
        } else if (Objects.nonNull(applTyp) && Objects.equals(applTyp, ApplTyp.INTERNATIONAL_DESIGN_APP_TYPE)) {
            singleDesignApplTyp = ApplTyp.INTERNATIONAL_SINGLE_DESIGN_APP_TYPE;
        } else {
            throw new RuntimeException("Unrecognized applTyp for design : " + applTyp);
        }
        return findAllApplicationSubTypesByApplTyp(singleDesignApplTyp);
    }

    @Override
    @Cacheable(value = "applicationSubTypes", key = "{'byApplicationTypeAndSubtype', #appTyp, #applSubTyp}")
    public CApplicationSubType getApplicationSubtype(String applTyp, String applSubTyp) {
        CfApplicationSubtypePK cfApplicationSubtypePK = new CfApplicationSubtypePK();
        cfApplicationSubtypePK.setApplTyp(applTyp);
        cfApplicationSubtypePK.setApplSubtyp(applSubTyp);
        return applicationSubTypesMapper.toCore(cfApplicationSubTypeRepository.findById(cfApplicationSubtypePK).orElse(null));
    }

    @Override
    @Cacheable(value = "applicationTypes", key = "{'tableNamePerFileType', #fileTyp}")
//Ne sym siguren kolko e udachno da se polzva edin i sy6t cache za vsicki methods vyv ApplicationTypeService...
    public String selectTableNameByFileType(String fileTyp) {
        if (StringUtils.isEmpty(fileTyp))
            return null;

        return cfApplicationTypeRepository.selectTableNameByFileType(fileTyp);
    }

    @Override
    @Cacheable(value = "applicationTypes", key = "{'applicaitonTypePerFileType', #fileTyp}")
    public String selectApplicationTypeByFileType(String fileTyp) {
        if (StringUtils.isEmpty(fileTyp))
            return null;

        CfApplicationType byFileTyp = cfApplicationTypeRepository.findByFileTyp(fileTyp);
        return byFileTyp.getApplTyp();
    }

    public String getDefaultApplicationSubtype(String applicationType) {
        return DEFAULT_APPLICATION_SUBTYPE.get(applicationType);
    }

    @Override
    public String getFileTypeByApplicationType(String applicationType) {
        return cfApplicationTypeRepository.getOne(applicationType).getFileTyp();
    }

    @Override
    @Cacheable(value = "applicationTypesOrder", key = "{#fileTypes}")
    public Map<String, String> getApplicationTypesMapOrderByApplTypeName(List<String> fileTypes) {
        List<CfApplicationType> cfApplicationTypes = cfApplicationTypeRepository.findAllByFileTypInOrderByApplTypeNameAsc(fileTypes);
        if (CollectionUtils.isEmpty(cfApplicationTypes))
            return null;

        return cfApplicationTypes.stream()
                .collect(Collectors.toMap(CfApplicationType::getApplTyp, CfApplicationType::getApplTypeName));
    }

    @Override
    @Cacheable(value = "applicationSubTypesOrder")
    public List<CApplicationSubType> getSubTypesByFileTypesOrderByApplTypeName(List<String> fileTypes) {
        List<CfApplicationSubtype> cfApplicationSubtypes =
                cfApplicationSubTypeRepository.findAllByFileTypesOrderByApplTypeNameAsc(fileTypes);

        if (CollectionUtils.isEmpty(cfApplicationSubtypes))
            return null;

        return applicationSubTypesMapper.toCoreList(cfApplicationSubtypes);
    }

    @Override
    @Cacheable(value = "applicationSubTypesOrder")
    public List<CApplicationSubType> getSubTypesByFileTypesApplTypOrderByApplTypeName(List<String> fileTypes, String applTyp) {
        List<CfApplicationSubtype> cfApplicationSubtypes =
                cfApplicationSubTypeRepository.findAllByFileTypesAndApplTypOrderByApplTypeNameAsc(fileTypes, applTyp);

        if (CollectionUtils.isEmpty(cfApplicationSubtypes))
            return null;

        return applicationSubTypesMapper.toCoreList(cfApplicationSubtypes);
    }
}