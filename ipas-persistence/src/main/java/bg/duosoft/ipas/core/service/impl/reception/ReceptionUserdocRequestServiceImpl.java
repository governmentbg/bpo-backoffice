package bg.duosoft.ipas.core.service.impl.reception;


import bg.duosoft.ipas.core.mapper.reception.ReceptionUserdocRequestMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdocRequest;
import bg.duosoft.ipas.core.service.reception.ReceptionUserdocRequestService;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionUserdocCorrespondent;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionUserdocRequest;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.SubmissionType;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.CorrespondentTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.ReceptionUserdocCorrespondentRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.ReceptionUserdocRequestRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.SubmissionTypeRepository;
import bg.duosoft.ipas.util.filter.ReceptionUserdocListFilter;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.reception.ReceptionCorrespondentUtils;
import bg.duosoft.ipas.util.reception.ReceptionRequestConverterUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@Transactional
@LogExecutionTime
public class ReceptionUserdocRequestServiceImpl implements ReceptionUserdocRequestService {

    @Autowired
    private ReceptionUserdocRequestRepository receptionUserdocRequestRepository;

    @Autowired
    private ReceptionUserdocCorrespondentRepository receptionUserdocCorrespondentRepository;

    @Autowired
    private SubmissionTypeRepository submissionTypeRepository;

    @Autowired
    private IpPersonRepository ipPersonRepository;

    @Autowired
    private CorrespondentTypeRepository correspondentTypeRepository;

    @Autowired
    private ReceptionUserdocRequestMapper receptionUserdocRequestMapper;

    @Override
    public CReceptionUserdocRequest selectReceptionByDocumentId(String docOri, String docLog, Integer docSer, Integer docNbr) {
        if (StringUtils.isEmpty(docOri) || StringUtils.isEmpty(docLog) || Objects.isNull(docSer) || Objects.isNull(docNbr))
            return null;

        ReceptionUserdocRequest receptionUserdocRequest = receptionUserdocRequestRepository.findByDocOriAndDocLogAndDocSerAndDocNbr(docOri, docLog, docSer, docNbr);
        if (Objects.isNull(receptionUserdocRequest))
            return null;

        return receptionUserdocRequestMapper.toCore(receptionUserdocRequest);
    }

    @Override
    public void saveUserdocReceptionRequestRecord(Integer externalId, String externalSystemId, CReception receptionForm, CReceptionResponse receiveUserdocResponse, CFileId euPatentFileId) {
        SubmissionType submissionType = submissionTypeRepository.findById(receptionForm.getSubmissionType()).orElse(null);
        ReceptionUserdocRequest receptionUserdocRequest = ReceptionRequestConverterUtils.convertToUserdocReceptionRequest(externalId, externalSystemId, receptionForm, receiveUserdocResponse, submissionType, euPatentFileId);
        ReceptionUserdocRequest userdocRequest = receptionUserdocRequestRepository.save(receptionUserdocRequest);
        List<ReceptionUserdocCorrespondent> receptionCorrespondents = ReceptionCorrespondentUtils.getReceptionUserdocCorrespondents(userdocRequest, receptionForm, ipPersonRepository, correspondentTypeRepository);
        if (!CollectionUtils.isEmpty(receptionCorrespondents))
            receptionUserdocCorrespondentRepository.saveAll(receptionCorrespondents);
    }

    @Override
    public List<CReceptionUserdocRequest> selectOriginalExpectedUserdoc(String relatedObject, String userdocType) {
        if (StringUtils.isEmpty(relatedObject) || StringUtils.isEmpty(userdocType))
            return null;

        CFileId cFileId = BasicUtils.createCFileId(relatedObject);
        if (Objects.isNull(cFileId))
            return null;

        List<ReceptionUserdocRequest> receptionUserdocRequests = receptionUserdocRequestRepository.selectOriginalExpectedUserdocs(cFileId.getFileSeq(), cFileId.getFileType(), cFileId.getFileSeries(), cFileId.getFileNbr(), userdocType);
        if (CollectionUtils.isEmpty(receptionUserdocRequests))
            return null;

        return receptionUserdocRequestMapper.toCoreList(receptionUserdocRequests);
    }

    @Override
    public void updateUserdocSubmissionType(Integer submissionType, CDocumentId documentId) {
        if (Objects.nonNull(submissionType) && Objects.nonNull(documentId)) {
            receptionUserdocRequestRepository.updateSubmissionType(submissionType, documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
        }
    }

    @Override
    public List<UserdocSimpleResult> selectUserdocReceptions(ReceptionUserdocListFilter filter) {
        return receptionUserdocRequestRepository.selectUserdocReceptions(filter);
    }

    @Override
    public int selectUserdocReceptionsCount(ReceptionUserdocListFilter filter) {
        return receptionUserdocRequestRepository.selectUserdocReceptionsCount(filter);
    }

    @Override
    public Map<String, String> getUserdocStatuses(ReceptionUserdocListFilter filter) {
        return receptionUserdocRequestRepository.getUserdocStatuses(filter);
    }

    @Override
    public Map<String, String> getUserdocObjectTypes(ReceptionUserdocListFilter filter) {
        return receptionUserdocRequestRepository.getUserdocObjectTypes(filter);
    }

    @Override
    public Map<String, String> getUserdocTypes(ReceptionUserdocListFilter filter) {
        return receptionUserdocRequestRepository.getUserdocTypes(filter);
    }

}
