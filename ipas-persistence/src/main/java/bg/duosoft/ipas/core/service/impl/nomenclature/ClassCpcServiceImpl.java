package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.miscellaneous.CpcClassMapper;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.miscellaneous.CCpcClass;
import bg.duosoft.ipas.core.service.nomenclature.ClassCpcService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassCpc;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassCpcPK;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfClassCpcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClassCpcServiceImpl implements ClassCpcService {

    @Autowired
    private CpcClassMapper cpcClassMapper;

    @Autowired
    private CfClassCpcRepository cfClassCpcRepository;

    @Autowired
    private ConfigParamService configParamService;


    @Override
    public CCpcClass findById(String editionCode, String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode) {
        CfClassCpcPK pk = new CfClassCpcPK(editionCode, sectionCode, classCode, subclassCode, groupCode, subgroupCode);
        CfClassCpc cfClassCpc = cfClassCpcRepository.findById(pk).orElse(null);
        if (Objects.isNull(cfClassCpc)) {
            return null;
        }
        CCpcClass result = new CCpcClass();
        cpcClassMapper.toCore(cfClassCpc, result);
        return result;
    }

    @Override
    public List<CCpcClass> findBySectionClassSubclassGroupAndSubgroup(String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode) {
        List<CfClassCpc> list = cfClassCpcRepository.findBySectionClassSubclassGroupAndSubgroup(sectionCode, classCode, subclassCode, groupCode, subgroupCode);
        return ObjectUtils.isEmpty(list) ? null : list.stream().map(r -> {
            CCpcClass res = new CCpcClass();
            cpcClassMapper.toCore(r, res);
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CCpcClass> findCpcClassesByCpcNumber(String cpcNumber, int maxResults) {
        CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.CPC_LATEST_VERSION);
        Pageable pageable = PageRequest.of(0, maxResults);
        return cfClassCpcRepository.findCpcClassesByCpcNumber("%" + cpcNumber + "%", cConfigParam.getValue(), pageable).stream().map(r -> {
            CCpcClass res = new CCpcClass();
            cpcClassMapper.toCore(r, res);
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CCpcClass> findCpcClassesByCpcNumberForSpcs(String cpcNumber, int maxResults, String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        Pageable pageable = PageRequest.of(0, maxResults);
        CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.CPC_LATEST_VERSION);
        return cfClassCpcRepository.findCpcClassesByCpcNumberForSpcs("%" + cpcNumber + "%", cConfigParam.getValue(), fileNbr, fileSeq, fileTyp, fileSer, pageable).stream().map(r -> {
            CCpcClass res = new CCpcClass();
            cpcClassMapper.toCore(r, res);
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public CCpcClass save(CCpcClass cpcClass) {
        if (Objects.isNull(cpcClass))
            return null;

        CfClassCpc cfClassCpc = cpcClassMapper.toEntity(cpcClass);
        CfClassCpc savedRecord = cfClassCpcRepository.save(cfClassCpc);
        CCpcClass result = new CCpcClass();
        cpcClassMapper.toCore(savedRecord, result);
        return result;
    }

    @Override
    public List<CCpcClass> getValidCpcsById(String editionCode, String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode) {
        CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.CPC_LATEST_VERSION);
        List<CCpcClass> resultList = new ArrayList<>();
        List<CfClassCpc> validCpcsById = cfClassCpcRepository.getValidCpcsById(editionCode, sectionCode, classCode, subclassCode, groupCode, subgroupCode, cConfigParam.getValue());
        if (!CollectionUtils.isEmpty(validCpcsById)) {
            for (CfClassCpc validCpc : validCpcsById) {
                CCpcClass result = new CCpcClass();
                cpcClassMapper.toCore(validCpc, result);
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CCpcClass> findAllCpcClassesByCpcNumber(String cpcNumber, int maxResults) {
        Pageable pageable = PageRequest.of(0, maxResults);
        return cfClassCpcRepository.findAllCpcClassesByCpcNumber("%" + cpcNumber + "%", pageable).stream().map(r -> {
            CCpcClass res = new CCpcClass();
            cpcClassMapper.toCore(r, res);
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public CCpcClass findCpcClassByCpcNumber(String cpcNumber) {
        List<CCpcClass> result = cfClassCpcRepository.findCpcClassByCpcNumber(cpcNumber).stream().map(r -> {
            CCpcClass res = new CCpcClass();
            cpcClassMapper.toCore(r, res);
            return res;
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }
}
