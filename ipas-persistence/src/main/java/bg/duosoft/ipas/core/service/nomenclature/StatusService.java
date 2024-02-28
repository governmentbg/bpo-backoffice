package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.miscellaneous.CStatusId;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface StatusService {

    CStatus getStatus(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr);

    List<CStatus> selectStatusesByProcessType(String processType);

    CStatus findById(String procType, String statusCode);

    List<CStatus> getAllByProcessTypes(List<String> procTypes);

    List<CStatus> getAllByProcessTypesOrder(List<String> procTypes);

    List<CStatus> getAllByFileTypesOrder(List<String> fileTypes);

    List<CStatus> getInitialOrFinalStatusesByProcType(Integer processType);

    Map<String, String> getStatusMap();

    @Cacheable(value = "status")
    CStatus getStatus(String procTyp, String statusCode);

    Map<String, String> selectIpObjectsStatusMapByUsersIncludeCount(List<Integer> users);

    Map<String, String> selectIpObjectsStatusMapByUsersAndFileTypesIncludeCount(List<Integer> users,List<String> fileTypes);

    Map<String, String> selectUserdocsStatusMapByResponsibleUser(Integer responsibleUserId);

    boolean isPatentInSecretStatus(String processType, String statusCode);

}
