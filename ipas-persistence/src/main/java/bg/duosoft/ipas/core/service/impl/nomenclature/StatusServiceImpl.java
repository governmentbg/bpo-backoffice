package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.miscellaneous.StatusMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.enums.ProcessResultType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatusPK;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfStatusRepository;
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
public class StatusServiceImpl implements StatusService {

    @Autowired
    private CfStatusRepository cfStatusRepository;

    @Autowired
    private StatusMapper statusMapper;

    @Override
    public CStatus getStatus(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        CfStatus cfStatus = cfStatusRepository.getStatusByFileId(fileSeq, fileTyp, fileSer, fileNbr);
        if (Objects.isNull(cfStatus))
            return null;

        return statusMapper.toCore(cfStatus);
    }

    @Override
    public List<CStatus> selectStatusesByProcessType(String processType) {
        if (StringUtils.isEmpty(processType))
            return null;

        List<CfStatus> statusList = cfStatusRepository.findAllByPk_ProcTyp(processType);
        if (CollectionUtils.isEmpty(statusList))
            return null;

        List<CStatus> cStatuses = statusMapper.toCoreList(statusList);
        cStatuses.sort(Comparator.comparing(CStatus::getStatusName));
        return cStatuses;
    }

    @Override
    public CStatus findById(String procType, String statusCode) {
        if (StringUtils.isEmpty(procType) || StringUtils.isEmpty(statusCode))
            return null;

        CfStatusPK pk = new CfStatusPK();
        pk.setProcTyp(procType);
        pk.setStatusCode(statusCode);

        CfStatus cfStatus = cfStatusRepository.findById(pk).orElse(null);
        if (Objects.isNull(cfStatus))
            return null;

        return statusMapper.toCore(cfStatus);
    }

    public List<CStatus> getAllByProcessTypes(List<String> procTypes) {
        return statusMapper.toCoreList(cfStatusRepository.findAllByPk_ProcTypIn(procTypes));
    }

    public List<CStatus> getAllByProcessTypesOrder(List<String> procTypes) {
        return statusMapper.toCoreList(cfStatusRepository.findAllByPk_ProcTypInOrderByStatusName(procTypes));
    }

    public List<CStatus> getAllByFileTypesOrder(List<String> fileTypes) {
        return statusMapper.toCoreList(cfStatusRepository.findAllByFileTypesOrderByStatusName(fileTypes));
    }

    @Override
    public List<CStatus> getInitialOrFinalStatusesByProcType(Integer processType) {
        List<CStatus> cStatuses = statusMapper.toCoreList(cfStatusRepository.getInitialOrFinalStatusesByProcType(processType));
        if (!CollectionUtils.isEmpty(cStatuses)){
            cStatuses.sort(Comparator.comparing(CStatus::getStatusName));
        }
        return cStatuses;
    }

    @Override
    @Cacheable(value = "statusMap")
    public Map<String, String> getStatusMap() {
        List<CfStatus> all = cfStatusRepository.findAll();
        return all.stream()
                .collect(Collectors.toMap(cfStatus -> cfStatus.getPk().getProcTyp() + "-" + cfStatus.getPk().getStatusCode(), CfStatus::getStatusName));
    }

    @Override
    @Cacheable(value = "statusById")
    public CStatus getStatus(String procTyp, String statusCode) {
        CfStatus res = cfStatusRepository.findById(new CfStatusPK(procTyp, statusCode)).orElse(null);
        return res == null ? null : statusMapper.toCore(res);
    }

    @Override
    public Map<String, String> selectIpObjectsStatusMapByUsersIncludeCount(List<Integer> users) {
        List<Object[]> result = cfStatusRepository.selectIpObjectsStatusMapByUsersIncludeCount(users);
        return convertStatusMapByResponsibleUserResultToMap(result);
    }


    @Override
    public Map<String, String> selectIpObjectsStatusMapByUsersAndFileTypesIncludeCount(List<Integer> users, List<String> fileTypes) {
        List<Object[]> result = cfStatusRepository.selectIpObjectsStatusMapByUsersAndFileTypesIncludeCount(users,fileTypes);
        return convertStatusMapByResponsibleUserResultToMap(result);
    }



    @Override
    public Map<String, String> selectUserdocsStatusMapByResponsibleUser(Integer responsibleUserId) {
        List<Object[]> result = cfStatusRepository.selectUserdocsStatusMapByResponsibleUser(responsibleUserId);
        return convertStatusMapByResponsibleUserResultToMap(result);
    }

    @Override
    public boolean isPatentInSecretStatus(String processType, String statusCode) {
        String processResultType = cfStatusRepository.selectProcessResultType(processType, statusCode);
        if (StringUtils.isEmpty(processResultType))
            return false;

        return processResultType.equals(ProcessResultType.SECRET_PATENTS_UM.code());
    }

    private Map<String, String> convertStatusMapByResponsibleUserResultToMap(List<Object[]> result) {
        Map<String, String> map = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                map.put((String) obj[0], (String) obj[1]);
            }
        }
        return map;
    }
}