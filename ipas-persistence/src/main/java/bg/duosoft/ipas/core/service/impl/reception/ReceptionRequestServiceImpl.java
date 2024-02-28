package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.mapper.reception.ReceptionRequestMapper;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.*;
import bg.duosoft.ipas.persistence.model.nonentity.ReceptionRequestSimpleResult;
import bg.duosoft.ipas.persistence.repository.entity.custom.ReceptionRequestRepositoryCustom;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.*;
import bg.duosoft.ipas.util.filter.ReceptionListFilter;
import bg.duosoft.ipas.util.reception.ReceptionCorrespondentUtils;
import bg.duosoft.ipas.util.reception.ReceptionRequestConverterUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@LogExecutionTime
public class ReceptionRequestServiceImpl implements ReceptionRequestService {

    @Autowired
    private ProcessService processService;

    @Autowired
    private ReceptionRequestRepository receptionRequestRepository;

    @Autowired
    private ReceptionCorrespondentRepository receptionCorrespondentRepository;

    @Autowired
    private IpPersonRepository ipPersonRepository;

    @Autowired
    private CorrespondentTypeRepository correspondentTypeRepository;

    @Autowired
    private SubmissionTypeRepository submissionTypeRepository;

    @Autowired
    private ReceptionRequestRepositoryCustom receptionRequestRepositoryCustom;

    @Autowired
    private ReceptionRequestMapper receptionRequestMapper;

    @Override
    public List<ReceptionRequestSimpleResult> getFirstReceptionsWithoutStatus() {
        List<ReceptionRequestSimpleResult> firstReceptionsWithoutStatus = receptionRequestRepositoryCustom.getFirstReceptionsWithoutStatus();
        if (CollectionUtils.isEmpty(firstReceptionsWithoutStatus))
            return null;
        return firstReceptionsWithoutStatus;
    }

    @Override
    public List<ReceptionRequestSimpleResult> getReceptionsWithoutStatus(ReceptionListFilter filter) {
        return receptionRequestRepositoryCustom.getReceptionsWithoutStatus(filter);
    }

    @Override
    public Integer getReceptionsWithoutStatusCount() {
        return receptionRequestRepositoryCustom.getReceptionsWithoutStatusCount();
    }

    @Override
    public CReceptionRequest selectReceptionByFileId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr) {
        if (StringUtils.isEmpty(fileSeq) || StringUtils.isEmpty(fileType) || Objects.isNull(fileSer) || Objects.isNull(fileNbr))
            return null;

        ReceptionRequest receptionRequest = receptionRequestRepository.findReceptionRequestByFileSeqAndFileTypeAndFileSerAndFileNbr(fileSeq, fileType, fileSer, fileNbr);
        if (Objects.isNull(receptionRequest))
            return null;

        return receptionRequestMapper.toCore(receptionRequest);
    }

    @Override
    public CReceptionRequest update(CReceptionRequest cReceptionRequest) {
        if (Objects.isNull(cReceptionRequest))
            return null;

        ReceptionRequest receptionRequest = receptionRequestMapper.toEntity(cReceptionRequest);
        ReceptionRequest save = receptionRequestRepository.save(receptionRequest);
        return receptionRequestMapper.toCore(save);
    }

    @Override
    public void saveReceptionRequestRecord(Integer externalId, CReception receptionForm, CFileId fileId) {
        SubmissionType submissionType = submissionTypeRepository.findById(receptionForm.getSubmissionType()).orElse(null);
        ReceptionRequest receptionRequest = ReceptionRequestConverterUtils.convertToReceptionRequest(externalId, receptionForm, submissionType, fileId);
        ReceptionRequest savedReceptionRequest = receptionRequestRepository.save(receptionRequest);
        List<ReceptionCorrespondent> receptionCorrespondents = ReceptionCorrespondentUtils.getReceptionCorrespondents(savedReceptionRequest, receptionForm, ipPersonRepository, correspondentTypeRepository);
        if (!CollectionUtils.isEmpty(receptionCorrespondents))
            receptionCorrespondentRepository.saveAll(receptionCorrespondents);
    }

    @Override
    public List<CReceptionRequest> selectOriginalExpectedByNameAndFileType(String name, String fileType) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(fileType))
            return null;

        List<ReceptionRequest> receptionRequests = receptionRequestRepository.selectOriginalExpectedByNameAndFileType(name, fileType);
        if (CollectionUtils.isEmpty(receptionRequests))
            return null;

        return receptionRequestMapper.toCoreList(receptionRequests);
    }

    @Override
    public void updateIpObjectSubmissionType(Integer submissionType, CFileId fileId) {
        if (Objects.nonNull(submissionType) && Objects.nonNull(fileId)) {
            receptionRequestRepository.updateSubmissionType(submissionType, fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
        }
    }

}
