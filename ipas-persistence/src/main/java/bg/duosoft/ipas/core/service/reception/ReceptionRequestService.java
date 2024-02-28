package bg.duosoft.ipas.core.service.reception;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.persistence.model.nonentity.ReceptionRequestSimpleResult;
import bg.duosoft.ipas.util.filter.ReceptionListFilter;

import java.util.List;

public interface ReceptionRequestService {

    List<ReceptionRequestSimpleResult> getFirstReceptionsWithoutStatus();

    List<ReceptionRequestSimpleResult> getReceptionsWithoutStatus(ReceptionListFilter filter);

    Integer getReceptionsWithoutStatusCount();

    CReceptionRequest selectReceptionByFileId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    CReceptionRequest update(CReceptionRequest receptionRequest);

    void saveReceptionRequestRecord(Integer externalId, CReception receptionForm, CFileId fileId);

    List<CReceptionRequest> selectOriginalExpectedByNameAndFileType(String name, String fileType);

    void updateIpObjectSubmissionType(Integer submissionType, CFileId fileId);

}
