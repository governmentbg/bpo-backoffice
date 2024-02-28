package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.model.ReceivedOriginalState;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.service.reception.OriginalExpectedService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.integration.abdocs.converter.DocCreationConverter;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionRequest;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionUserdocRequest;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocRepository;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.ReceptionRequestRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.ReceptionUserdocRequestRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.general.BasicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class OriginalExpectedServiceImpl implements OriginalExpectedService {

    @Autowired
    private IpDocRepository ipDocRepository;

    @Autowired
    private ReceptionRequestRepository receptionRequestRepository;

    @Autowired
    private ReceptionUserdocRequestRepository receptionUserdocRequestRepository;

    @Autowired
    private IpFileRepository ipFileRepository;

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private DocCreationConverter docCreationConverter;

    @Override
    public Integer updateOriginalExpectedOnReception(CReception receptionForm, Integer receptionRequestId) throws IpasValidationException {
        if (receptionForm.isUserdocRequest() || receptionForm.isEuPatentRequest()) {
            ReceptionUserdocRequest reception = receptionUserdocRequestRepository.findById(receptionRequestId).orElse(null);
            if (Objects.isNull(reception))
                return null;

            String objectNumber = reception.getDocOri() + "/" + reception.getDocLog() + "/" + reception.getDocSer() + "/" + reception.getDocNbr();
            Integer parentDocumentId = docCreationConverter.getParentDocumentId(objectNumber);
            abdocsService.receiveOriginal(parentDocumentId);

            ipDocRepository.updateIndFaxRecetpion(DefaultValue.IPAS_NO, reception.getDocOri(), reception.getDocLog(), reception.getDocSer(), reception.getDocNbr());
            reception.setOriginalExpected(false);
            receptionUserdocRequestRepository.save(reception);
            return parentDocumentId;
        } else {
            ReceptionRequest reception = receptionRequestRepository.findById(receptionRequestId).orElse(null);
            if (Objects.isNull(reception))
                return null;

            String filingNumber = BasicUtils.createFilingNumber(reception.getFileSeq(), reception.getFileType(), reception.getFileSer(), reception.getFileNbr());
            Integer parentDocumentId = docCreationConverter.getParentDocumentId(filingNumber);
            abdocsService.receiveOriginal(parentDocumentId);

            IpFilePK ipFilePK = new IpFilePK(reception.getFileSeq(), reception.getFileType(), reception.getFileSer(), reception.getFileNbr());
            IpFile file = ipFileRepository.findById(ipFilePK).orElse(null);
            if (Objects.nonNull(file)) {
                IpDocPK ipDocPK = file.getIpDoc().getPk();
                if (Objects.isNull(ipDocPK))
                    return null;

                ipDocRepository.updateIndFaxRecetpion(DefaultValue.IPAS_NO, ipDocPK.getDocOri(), ipDocPK.getDocLog(), ipDocPK.getDocSer(), ipDocPK.getDocNbr());
            }

            reception.setOriginalExpected(false);
            receptionRequestRepository.save(reception);
            return parentDocumentId;
        }
    }

    @Override
    public void updateIpObjectOriginalExpectedOnAbdocsNotification(CFileId fileId, Document document) {
        ReceivedOriginalState receivedOriginalState = document.getReceivedOriginalState();
        if (Objects.nonNull(receivedOriginalState)) {
            boolean originalExpected = (receivedOriginalState == ReceivedOriginalState.WaitingForOriginal);
            IpDocPK ipDocPK = ipFileRepository.selectDocumentId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
            ipDocRepository.updateIndFaxRecetpion(originalExpected ? DefaultValue.IPAS_YES : DefaultValue.IPAS_NO, ipDocPK.getDocOri(), ipDocPK.getDocLog(), ipDocPK.getDocSer(), ipDocPK.getDocNbr());

            ReceptionRequest receptionRequest = receptionRequestRepository.findReceptionRequestByFileSeqAndFileTypeAndFileSerAndFileNbr(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
            if (Objects.nonNull(receptionRequest)) {
                receptionRequest.setOriginalExpected(originalExpected);
                receptionRequestRepository.save(receptionRequest);
            }
        }
    }

    @Override
    public void updateUserdocOriginalExpectedOnAbdocsNotification(CDocumentId userdocId, Document document) {
        ReceivedOriginalState receivedOriginalState = document.getReceivedOriginalState();
        if (Objects.nonNull(receivedOriginalState)) {
            boolean originalExpected = (receivedOriginalState == ReceivedOriginalState.WaitingForOriginal);
            ipDocRepository.updateIndFaxRecetpion(originalExpected ? DefaultValue.IPAS_YES : DefaultValue.IPAS_NO, userdocId.getDocOrigin(), userdocId.getDocLog(), userdocId.getDocSeries(), userdocId.getDocNbr());

            ReceptionUserdocRequest userdocReceptionRequest = receptionUserdocRequestRepository.findByDocOriAndDocLogAndDocSerAndDocNbr(userdocId.getDocOrigin(), userdocId.getDocLog(), userdocId.getDocSeries(), userdocId.getDocNbr());
            if (Objects.nonNull(userdocReceptionRequest)) {
                userdocReceptionRequest.setOriginalExpected(originalExpected);
                receptionUserdocRequestRepository.save(userdocReceptionRequest);
            }
        }
    }

}
