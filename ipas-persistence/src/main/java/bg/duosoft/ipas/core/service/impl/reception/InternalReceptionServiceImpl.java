package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.abdocs.exception.AbdocsServiceException;
import bg.duosoft.abdocs.model.DocCreation;
import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.impl.reception.ebd.EbdReceptionException;
import bg.duosoft.ipas.core.service.impl.reception.ebd.EuPatentReceptionServiceImpl;
import bg.duosoft.ipas.core.service.impl.reception.ebd.ReceiveEuropeanPatentResult;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.*;
import bg.duosoft.ipas.core.validation.config.*;
import bg.duosoft.ipas.core.validation.reception.ReceptionValidator;
import bg.duosoft.ipas.integration.abdocs.converter.DocCreationConverter;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.custom.SequenceRepository;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfFileTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfUserdocTypeRepository;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.reception.ReceptionCorrespondentUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@LogExecutionTime
public class InternalReceptionServiceImpl implements InternalReceptionService {

    private final Object lockReception = new Object();
    @Autowired
    protected ReceptionRequestService receptionRequestService;
    @Autowired
    private ReceptionUserdocRequestService receptionUserdocRequestService;
    @Autowired
    private PersonService personService;
    @Autowired
    private AbdocsService abdocsService;
    @Autowired
    private AbdocsService abdocsServiceAdmin;
    @Autowired
    private MissingAbdocsDocumentService missingAbdocsDocumentService;
    @Autowired
    private DocCreationConverter docCreationConverter;
    @Autowired
    private IpObjectReceptionServiceImpl ipObjectReceptionService;
    @Autowired
    private EuPatentReceptionServiceImpl euPatentReceptionService;
    @Autowired
    private UserdocReceptionServiceImpl userdocReceptionService;
    @Autowired
    private CfFileTypeRepository cfFileTypeRepository;
    @Autowired
    private IpFileRepository ipFileRepository;
    @Autowired
    private CfUserdocTypeRepository cfUserdocTypeRepository;
    @Autowired
    private SequenceRepository sequenceRepository;
    @Autowired
    private ProcessService processService;


    @IpasValidatorDefinition({ReceptionValidator.class})
    public CReceptionResponse createReception(CReception receptionForm) {
        synchronized (lockReception) {
            CReception reception = (CReception) SerializationUtils.clone(receptionForm);//cloning the object, because some fields in the request are getting changed, which is not ok
            CReceptionResponse res;
            if (reception.isEuPatentRequest()) {
                res = createEuPatentReception(reception);
            } else if (reception.isUserdocRequest()) {
                res = createUserdocReception(reception);
            } else {
                res = createFileReception(reception);
            }
            return res;
        }

    }

    private void connectDocuments(CReception receptionForm, Integer newDocumentId) {
        if (Objects.nonNull(receptionForm.getInitialDocumentId())) {
            List<Integer> ids = new ArrayList<>();
            ids.add(newDocumentId);
            this.getAbdocsService(receptionForm).connectDocuments(receptionForm.getInitialDocumentId(), ids);
        }
    }

    private CReceptionResponse createUserdocReception(CReception reception) {
        Document document = null;
        try {
            processReceptionCorrespondents(reception);
            if (reception.getDocflowSystemId() != null || reception.isRegisterInDocflowSystem()) {
                document = getOrRegisterDocumentInAbdocs(reception);
                connectDocuments(reception, document.getDocId());
                reception.setExternalSystemId(document.getRegUri());
            }
            prepareDocumentSequence(reception);
            CReceptionResponse res = userdocReceptionService.insertUserdocReception(reception);
            res.setDocflowDocumentId(document == null ? null : document.getDocId());
            res.setExternalSystemId(reception.getExternalSystemId());
            if (reception.isRegisterReceptionRequest()) {
                receptionUserdocRequestService.saveUserdocReceptionRequestRecord(document.getDocId(), reception.getExternalSystemId(), reception, res, null);
            }
            registerAttachmentsInDocflowSystem(reception, res);
            return res;
        } catch (Exception e) {
            deleteAbdocsDocument(document == null ? null : document.getDocId(), e);
            throw e;
        }
    }

    private CReceptionResponse createEuPatentReception(CReception reception) {
//        processReceptionCorrespondents(reception);//tezi applicants se process-vat posle. Pri insertEuPatentReception se pravqt dopylnitelni receptions
        try {
            ReceiveEuropeanPatentResult res = euPatentReceptionService.insertEuPatentReception(reception);
            CReceptionResponse result = res.getInsertFinalUserdocResponse();

            if (res.getInsertPatentReceptionResponse() == null) {//ako se insert-va patent, userdocReceptions se pravqt vytre v LocalEbdPatentIpasServiceImpl, za da stane v obshta tranzakcia s insert na patenta!!!! Sy6toto vaji i za attachments kym zaqvkata. Te se vyvejdat v delovodnata vytre v LocalEbdPatentIpasServiceImpl. Ako neshto ot insert-a na userdocs/attachments grymne, no e minal insert-a na EP v delovodnata, to shte se hvyrli EbdReceptionException v koito ima docflowDocumentId i shte se napravi deleteHierarchy v delovodnata!!!!
                try {
                    registerAttachmentsInDocflowSystem(reception, result);
                    connectDocuments(reception, result.getDocflowDocumentId());
                } catch (Exception e) {
                    deleteAbdocsDocument(res.getInsertFinalUserdocResponse().getDocflowDocumentId(), e);//iztriva se final useredoc-a ot abdocs
                    throw e;
                }
                try {
                    CFileId fileId = res.getInsertPatentReceptionResponse() == null ? processService.selectTopProcessFileId(processService.selectUserdocProcessId(res.getInsertFinalUserdocResponse().getDocId())) : res.getInsertPatentReceptionResponse().getFileId();
                    result.setUserdocReceptionResponses(_createUserdocReceptionsRelatedToFile(reception, fileId));
                } catch (CreateUserdocReceptionsException e) {
                    deleteAbdocsDocument(res.getInsertFinalUserdocResponse().getDocflowDocumentId(), e);//iztriva se final useredoc-a ot abdocs
                    //iztrivat se vsichki inserted userdocs ot abdocs
                    if (!CollectionUtils.isEmpty(e.getInsertedReceptions())) {
                        e.getInsertedReceptions().stream().map(r -> r.getDocflowDocumentId()).forEach(r -> deleteAbdocsDocument(r, e));
                    }

                    throw (e.getCause() != null && e.getCause() instanceof IpasValidationException) ? (IpasValidationException) e.getCause() : new RuntimeException(e.getMessage(), e);
                }
            }

            return result;
        } catch (EbdReceptionException e) {
            if (e.getDocflowSystemId() != null) {//ako ima docflowSystemId, znachi e insert-nat cql EP, ne samo Userdoc kym nego, togava se trie cqlata ierarhiq
                abdocsServiceAdmin.deleteDocumentHierarchy(e.getDocflowSystemId());
            }

            throw (e.getCause() != null && e.getCause() instanceof IpasValidationException) ? (IpasValidationException) e.getCause() : new RuntimeException(e.getMessage(), e);
        }
    }
    @Data
    @AllArgsConstructor
    private static class CreateUserdocReceptionsException extends Exception {
        private List<CReceptionResponse> insertedReceptions;
        private List<CReception> notInsertedReceptions;

        public CreateUserdocReceptionsException(Throwable cause, List<CReceptionResponse> insertedReceptions, List<CReception> notInsertedReceptions) {
            super(cause);
            this.insertedReceptions = insertedReceptions;
            this.notInsertedReceptions = notInsertedReceptions;
        }
    }
    private List<CReceptionResponse> _createUserdocReceptionsRelatedToFile(CReception reception, CFileId fileId) throws CreateUserdocReceptionsException {
        List<CReceptionResponse> res = null;
        if (!CollectionUtils.isEmpty(reception.getUserdocReceptions())) {
            res = new ArrayList<>();
            for (int i = 0; i < reception.getUserdocReceptions().size(); i++) {
                CReception userdoc = reception.getUserdocReceptions().get(i);
                try {
                    if (fileId == null) {
                        throw new RuntimeException("fileId should not be null");
                    }
                    userdoc.getUserdoc().setFileId(fileId);
                    res.add(createReception(userdoc));
                } catch (Exception e) {
                    log.error("Cannot insert userdoc to reception", e);
                    List<CReception> notInserted = reception.getUserdocReceptions().stream().skip(i + 1).collect(Collectors.toList());
                    throw new CreateUserdocReceptionsException(e, res, notInserted);
                }

            }
        }
        return res;
    }

    private CReceptionResponse createFileReception(CReception receptionForm) throws IpasValidationException {
        processReceptionCorrespondents(receptionForm);
        Document document = null;
        try {
            if (receptionForm.getDocflowSystemId() != null || receptionForm.isRegisterInDocflowSystem()) {
                document = getOrRegisterDocumentInAbdocs(receptionForm);
                connectDocuments(receptionForm, document.getDocId());
                CFileId fileId = BasicUtils.createCFileId(document.getRegUri());
                if (Objects.isNull(fileId)) {
                    throw new RuntimeException("Registration number is empty !");
                }
                receptionForm.getFile().setFileId(fileId);
                receptionForm.setExternalSystemId(document.getRegUri());
            }

            CFileId fileId = receptionForm.getFile().getFileId();
            IpFile existingFile = ipFileRepository.findById(new IpFilePK(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr())).orElse(null);
            if (Objects.nonNull(existingFile)) {
                throw new RuntimeException("Duplicated number ! There is an ipobject with the same registration number already registered in IPAS ! IPOBJECT: " + fileId.createFilingNumber());
            }

            prepareDocumentSequence(receptionForm);
            CReceptionResponse res = ipObjectReceptionService.insertIpObjectReception(receptionForm);
            if (receptionForm.isRegisterReceptionRequest()) {
                receptionRequestService.saveReceptionRequestRecord(document.getDocId(), receptionForm, receptionForm.getFile().getFileId());
            }
            res.setDocflowDocumentId(document == null ? null : document.getDocId());
            res.setExternalSystemId(receptionForm.getExternalSystemId());

            registerAttachmentsInDocflowSystem(receptionForm, res);

            res.setUserdocReceptionResponses(_createUserdocReceptionsRelatedToFile(receptionForm, fileId));
            return res;

        } catch (CreateUserdocReceptionsException e) {
          deleteAbdocsDocumentHierarchy(document == null ? null : document.getDocId(), e);
          throw new RuntimeException(e);
        } catch (Exception e) {
            deleteAbdocsDocument(document == null ? null : document.getDocId(), e);
            throw e;
        }
    }

    private void deleteAbdocsDocument(Integer documentId, Exception e) {
        if (documentId != null) {
            try {
                abdocsServiceAdmin.deleteDocument(documentId);
            } catch (AbdocsServiceException abdocsServiceException) {
                e.addSuppressed(abdocsServiceException);
            }
        }
    }

    private void deleteAbdocsDocumentHierarchy(Integer documentId, Exception e) {
        if (documentId != null) {
            try {
                abdocsServiceAdmin.deleteDocumentHierarchy(documentId);
            } catch (AbdocsServiceException abdocsServiceException) {
                e.addSuppressed(abdocsServiceException);
            }
        }
    }
    private void processReceptionCorrespondents(CReception receptionForm) {
        ReceptionCorrespondentUtils.mergeOrInsertCorrespondents(receptionForm, personService);
    }
    private void registerAttachmentsInDocflowSystem(CReception reception, CReceptionResponse res) {
        if (reception.isRegisterInDocflowSystem() && !CollectionUtils.isEmpty(reception.getAttachments())) {
            Integer docflowDocumentId = res.getDocflowDocumentId();
            if (docflowDocumentId == null) {
                throw new RuntimeException("Dcoflow document id Should not be null!");
            }
            try {
                for (CAttachment att : reception.getAttachments()) {
                    this.getAbdocsService(reception).uploadFileToExistingDocument(docflowDocumentId, att.getData(), att.getFileName(), att.getDescription(), false, DocFileVisibility.PublicAttachedFile);
                }
            } catch (AbdocsServiceException e) {
                log.error("Cannot register attachment to the docflow system", e);
                throw e;
            }
        }
    }

    public Document getOrRegisterDocumentInAbdocs(CReception receptionForm) {
        try {
            if (receptionForm.getDocflowSystemId() != null) {
                return this.getAbdocsService(receptionForm).selectDocumentById(receptionForm.getDocflowSystemId());
            }
            if (receptionForm.isUserdocRequest()) {
                String objectNumber = receptionForm.getUserdoc().getFileNumberOrDocumentNumber();
                missingAbdocsDocumentService.insertMissingDocument(objectNumber);
            }
            DocCreation docCreation = docCreationConverter.createDocCreationForReception(receptionForm);
            Document document = this.getAbdocsService(receptionForm).registerDocument(docCreation, receptionForm.getEntryDate());
            if (Objects.isNull(document))
                throw new RuntimeException("ABDOCS document is empty !");

            return document;
        } catch (AbdocsServiceException ex) {
            log.error(ex.getMessage(), ex);
            throw new IpasValidationException(Collections.singletonList(ValidationError.builder().pointer("abdocsSystem").messageCode("abdocs.register.document.error").build()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IpasValidationException(Collections.singletonList(ValidationError.builder().pointer("abdocsSystem").messageCode("abdocs.technical.error").build()));
        }
    }

    private static List<Pattern> EXTERNAL_SYSTEM_ID_PATTERNS = new ArrayList<>();

    static {
        EXTERNAL_SYSTEM_ID_PATTERNS.add(Pattern.compile("^.*?-(?<year>\\d+)\\/(?<number>\\d+)$"));//ЕМИВ-2020/0009
        EXTERNAL_SYSTEM_ID_PATTERNS.add(Pattern.compile("^.*?\\/.*?\\/(?<year>\\d+)\\/(?<number>\\d+)$"));//BG/N/2013/00001
    }

    private void prepareDocumentSequence(CReception reception) {
        String externalSystemId;
        String docSeqType;
        if (reception.isFileRequest()) {
            docSeqType = cfFileTypeRepository.getOne(reception.getFile().getFileId().getFileType()).getDocSeqTyp();
            //ako ne e minavano prez delovodnata sistema (reception.externalSystemId == null), no ima file.fileId, to externalSystemId e fileId-to! Tova si e malko zakyprvane...
            externalSystemId = !StringUtils.isEmpty(reception.getExternalSystemId()) ? reception.getExternalSystemId() : (reception.getFile().getFileId() == null ? null : reception.getFile().getFileId().createFilingNumber());
        } else if (reception.isUserdocRequest()) {
            docSeqType = cfUserdocTypeRepository.getOne(reception.getUserdoc().getUserdocType()).getDocSeqTyp();
            externalSystemId = reception.getExternalSystemId();
        } else {
            throw new RuntimeException("Unknown reception type...");
        }

        if (!StringUtils.isEmpty(externalSystemId)) {
            if (sequenceRepository.isGenerateNextDocSequenceFromExternalSystemId(docSeqType)) {//ako se generira ot externalSystemId
                boolean found = false;
                for (Pattern p : EXTERNAL_SYSTEM_ID_PATTERNS) {
                    Matcher m = p.matcher(externalSystemId);
                    if (m.find()) {
                        CDocumentSeqId documentSeqId = new CDocumentSeqId(docSeqType, Integer.parseInt(m.group("year")), Integer.parseInt(m.group("number")));
                        reception.setDocumentSeqId(documentSeqId);
                        found = true;
                        break;
                    }
                }
                if (!found) {//ako DocSeqId shte se generira ot externalSystemId, no toj ne e s pravilniq PATTERN, se hvyrlq exception!
                    throw new RuntimeException("Cannot parse number " + externalSystemId);
                }
            }
        } else {
            //Do nothing... The default IPAS's sequence generation mechanism will be used.
        }
    }

    private AbdocsService getAbdocsService(CReception reception) {
        if (reception.isRegisterAsAdministrator()) {
            return this.abdocsServiceAdmin;
        } else {
            return this.abdocsService;
        }
    }
}
