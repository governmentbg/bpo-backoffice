package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.miscellaneous.IpcClassMapper;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.miscellaneous.CClassCpcIpcConcordance;
import bg.duosoft.ipas.core.model.miscellaneous.CCpcClass;
import bg.duosoft.ipas.core.model.miscellaneous.CIpcClass;
import bg.duosoft.ipas.core.model.patent.CPatentCpcClass;
import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;
import bg.duosoft.ipas.core.service.nomenclature.ClassCpcIpcConcordanceService;
import bg.duosoft.ipas.core.service.nomenclature.ClassIpcService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassIpc;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassIpcPK;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfClassIpcRepository;
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
public class ClassIpcServiceImpl implements ClassIpcService {

    @Autowired
    private IpcClassMapper ipcClassMapper;

    @Autowired
    private CfClassIpcRepository cfClassIpcRepository;

    @Autowired
    private ConfigParamService configParamService;

    @Autowired
    private ClassCpcIpcConcordanceService classCpcIpcConcordanceService;

    @Override
    public CIpcClass findById(String editionCode, String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode) {
        CfClassIpcPK pk = new CfClassIpcPK(editionCode, sectionCode, classCode, subclassCode, groupCode, subgroupCode);
        CfClassIpc cfClassIpc = cfClassIpcRepository.findById(pk).orElse(null);
        if (Objects.isNull(cfClassIpc)) {
            return null;
        }
        CIpcClass result = new CIpcClass();
        ipcClassMapper.toCore(cfClassIpc, result);
        return result;
    }

    @Override
    public List<CIpcClass> findBySectionClassSubclassGroupAndSubgroup(String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode) {
        List<CfClassIpc> list = cfClassIpcRepository.findBySectionClassSubclassGroupAndSubgroup(sectionCode, classCode, subclassCode, groupCode, subgroupCode);
        return ObjectUtils.isEmpty(list) ? null : list.stream().map(r -> {
            CIpcClass res = new CIpcClass();
            ipcClassMapper.toCore(r, res);
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CIpcClass> findIpcClassesByIpcNumber(String ipcNumber, int maxResults) {
        CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.IPC_LATEST_VERSION);
        Pageable pageable = PageRequest.of(0, maxResults);
        return cfClassIpcRepository.findIpcClassesByIpcNumber("%" + ipcNumber + "%", cConfigParam.getValue(), pageable).stream().map(r -> {
            CIpcClass res = new CIpcClass();
            ipcClassMapper.toCore(r, res);
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CIpcClass> findIpcClassesByIpcNumberForSpcs(String ipcNumber, int maxResults, String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        Pageable pageable = PageRequest.of(0, maxResults);
        CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.IPC_LATEST_VERSION);
        return cfClassIpcRepository.findIpcClassesByIpcNumberForSpcs("%" + ipcNumber + "%", cConfigParam.getValue(), fileNbr, fileSeq, fileTyp, fileSer, pageable).stream().map(r -> {
            CIpcClass res = new CIpcClass();
            ipcClassMapper.toCore(r, res);
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public CIpcClass save(CIpcClass ipcClass) {
        if (Objects.isNull(ipcClass))
            return null;

        CfClassIpc cfClassIpc = ipcClassMapper.toEntity(ipcClass);
        CfClassIpc savedRecord = cfClassIpcRepository.save(cfClassIpc);
        CIpcClass result = new CIpcClass();
        ipcClassMapper.toCore(savedRecord, result);
        return result;
    }

    @Override
    public List<CIpcClass> getValidIpcsById(String editionCode, String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode) {
        CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.IPC_LATEST_VERSION);
        List<CIpcClass> resultList = new ArrayList<>();
        List<CfClassIpc> validIpcsById = cfClassIpcRepository.getValidIpcsById(editionCode, sectionCode, classCode, subclassCode, groupCode, subgroupCode, cConfigParam.getValue());
        if (!CollectionUtils.isEmpty(validIpcsById)) {
            for (CfClassIpc validIpc : validIpcsById) {
                CIpcClass result = new CIpcClass();
                ipcClassMapper.toCore(validIpc, result);
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CIpcClass> findAllIpcClassesByIpcNumber(String ipcNumber, int maxResults) {
        Pageable pageable = PageRequest.of(0, maxResults);
        return cfClassIpcRepository.findAllIpcClassesByIpcNumber("%" + ipcNumber + "%", pageable).stream().map(r -> {
            CIpcClass res = new CIpcClass();
            ipcClassMapper.toCore(r, res);
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public CIpcClass findIpcClassByIpcNumber(String ipcNumber) {
        List<CIpcClass> result = cfClassIpcRepository.findIpcClassByIpcNumber(ipcNumber).stream().map(r -> {
            CIpcClass res = new CIpcClass();
            ipcClassMapper.toCore(r, res);
            return res;
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public List<CPatentIpcClass> copyFromCpcList(List<CPatentIpcClass> ipcList, List<CPatentCpcClass> cpcList) {
        List<CPatentIpcClass> copyIpcList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(ipcList)) {
            copyIpcList.addAll(ipcList);
        }

        if (!CollectionUtils.isEmpty(cpcList)) {
            for (CCpcClass cpc : cpcList) {
                CClassCpcIpcConcordance classCpcIpcConcordance = classCpcIpcConcordanceService.findById(cpc.getCpcSection(), cpc.getCpcClass(), cpc.getCpcSubclass(), cpc.getCpcGroup(), cpc.getCpcSubgroup());
                if (Objects.nonNull(classCpcIpcConcordance)) {
                    CIpcClass existingIpc = copyIpcList.stream().filter(r -> r.getIpcSection().equals(classCpcIpcConcordance.getIpcSectionCode()) && r.getIpcClass().equals(classCpcIpcConcordance.getIpcClassCode())
                            && r.getIpcSubclass().equals(classCpcIpcConcordance.getIpcSubclassCode()) && r.getIpcGroup().equals(classCpcIpcConcordance.getIpcGroupCode())
                            && r.getIpcSubgroup().equals(classCpcIpcConcordance.getIpcSubgroupCode())).findFirst().orElse(null);
                    if (Objects.isNull(existingIpc)) {
                        addIpcOnCopy(copyIpcList, classCpcIpcConcordance);
                    }
                }
            }
        }

        return copyIpcList;
    }


    private void addIpcOnCopy(List<CPatentIpcClass> copyIpcList, CClassCpcIpcConcordance classCpcIpcConcordance) {
        CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.IPC_LATEST_VERSION);
        CfClassIpc copyIpc = cfClassIpcRepository.findIpcClassByIpcNumberWithLatestEdition(classCpcIpcConcordance.getIpcSectionCode().concat(classCpcIpcConcordance.getIpcClassCode().
                concat(classCpcIpcConcordance.getIpcSubclassCode()).concat(classCpcIpcConcordance.getIpcGroupCode()).concat(classCpcIpcConcordance.getIpcSubgroupCode())), cConfigParam.getValue());
        if (Objects.nonNull(copyIpc)) {
            CPatentIpcClass cPatentIpcClass = new CPatentIpcClass();
            cPatentIpcClass.setIpcEdition(copyIpc.getPk().getIpcEditionCode());
            cPatentIpcClass.setIpcSection(copyIpc.getPk().getIpcSectionCode());
            cPatentIpcClass.setIpcClass(copyIpc.getPk().getIpcClassCode());
            cPatentIpcClass.setIpcSubclass(copyIpc.getPk().getIpcSubclassCode());
            cPatentIpcClass.setIpcGroup(copyIpc.getPk().getIpcGroupCode());
            cPatentIpcClass.setIpcSubgroup(copyIpc.getPk().getIpcSubgroupCode());
            cPatentIpcClass.setIpcSymbolDescription(copyIpc.getIpcName());
            cPatentIpcClass.setIpcVersionCalculated(cConfigParam.getValue());
            copyIpcList.add(cPatentIpcClass);
        }
    }

}
