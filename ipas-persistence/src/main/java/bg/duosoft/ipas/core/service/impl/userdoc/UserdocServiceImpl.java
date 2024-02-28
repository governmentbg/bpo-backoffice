package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.abdocs.exception.AbdocsMissingDocumentException;
import bg.duosoft.abdocs.exception.AbdocsServiceException;
import bg.duosoft.abdocs.model.*;
import bg.duosoft.abdocs.model.response.CreateCorrespondentResponse;
import bg.duosoft.abdocs.model.response.DownloadFileResponse;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.mapper.userdoc.UserdocHierarchyMapper;
import bg.duosoft.ipas.core.mapper.userdoc.UserdocMapper;
import bg.duosoft.ipas.core.model.CApplicationSubType;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.model.userdoc.*;
import bg.duosoft.ipas.core.model.userdoc.config.CInternationalUserdocTypeConfig;
import bg.duosoft.ipas.core.model.userdoc.international_registration.CAcceptInternationalRegistrationRequest;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.core.service.file.FileRecordalService;
import bg.duosoft.ipas.core.service.impl.ServiceBaseImpl;
import bg.duosoft.ipas.core.service.impl.reception.ReceptionResponseWrapper;
import bg.duosoft.ipas.core.service.logging.LogChangesService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.core.service.reception.AbdocsDocumentTypeService;
import bg.duosoft.ipas.core.service.reception.InternalReceptionService;
import bg.duosoft.ipas.core.service.reception.MissingAbdocsDocumentService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPanelService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.service.userdoc.config.InternationalUserdocTypeConfigService;
import bg.duosoft.ipas.core.service.userdoc.config.UserdocTypeConfigService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.userdoc.*;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.exception.AcceptUserdocException;
import bg.duosoft.ipas.integration.abdocs.converter.DocCreationConverter;
import bg.duosoft.ipas.persistence.model.entity.UserDocumentRelatedPerson;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPerson;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocRegNumberChangeLog;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionRequest;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocHierarchyChildNode;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocIpDocSimpleResult;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocFilesRepository;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocRepository;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonAdressesRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRegNumberChangeLogRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRepository;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.mark.InternationalMarkUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@LogExecutionTime
public class UserdocServiceImpl extends ServiceBaseImpl implements UserdocService {

    @Autowired
    private DocCreationConverter docCreationConverter;

    @Autowired
    private FileRecordalService fileRecordalService;

    @Autowired
    private AbdocsDocumentTypeService abdocsDocumentTypeService;

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private IpUserdocRepository ipUserdocRepository;

    @Autowired
    private IpDocFilesRepository ipDocFilesRepository;

    @Autowired
    private IpDocRepository ipDocRepository;

    @Autowired
    private IpUserdocRegNumberChangeLogRepository ipUserdocRegNumberChangeLogRepository;

    @Autowired
    private IpPersonAdressesRepository ipPersonAdressesRepository;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private FileIdMapper fileIdMapper;

    @Autowired
    private UserdocMapper userdocMapper;

    @Autowired
    private DocumentIdMapper documentIdMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MissingAbdocsDocumentService missingAbdocsDocumentService;

    @Autowired
    private InternalReceptionService internalReceptionService;

    @Autowired
    private UserdocValidator userdocValidator;

    @Autowired
    private UserdocHierarchyMapper userdocHierarchyMapper;

    @Autowired
    private UserdocTypeConfigService userdocTypeConfigService;

    @Autowired
    private UserdocPanelService userdocPanelService;

    @Autowired
    private MarkService markService;

    @Autowired
    private DesignService designService;

    @Autowired
    private InternationalUserdocTypeConfigService internationalUserdocTypeConfigService;
    @Autowired
    private LogChangesService logChangesService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CUserdoc updateUserdoc(CUserdoc cUserdoc, boolean abdocsUpdate) {
        List<ValidationError> validationErrors = userdocValidator.validate(cUserdoc, false);
        if (!CollectionUtils.isEmpty(validationErrors))
            throw new IpasValidationException(validationErrors);

        log.debug("Trying to update userdoc with pk = " + cUserdoc.getDocumentId());
        registerUserdocInAbdocs(cUserdoc);

        IpDocPK ipDocPK = documentIdMapper.toEntity(cUserdoc.getDocumentId());
        IpUserdoc originalUserdoc = ipUserdocRepository.findById(ipDocPK).orElse(null);
        if (Objects.isNull(originalUserdoc))
            throw new RuntimeException("Userdoc does not exist...");

        CUserdoc originalCUserdoc = userdocMapper.toCore(originalUserdoc, true);
        if (!originalCUserdoc.getRowVersion().equals(cUserdoc.getRowVersion())) {
            throw new IpasValidationException(Collections.singletonList(ValidationError.builder().pointer("rowVersion").messageCode("object.already.updated").build()));
        }

        entityManager.detach(originalUserdoc);
        userdocMapper.fillUserdocFields(cUserdoc, originalUserdoc);// Override fields of original userdoc

        originalUserdoc.setRowVersion(originalUserdoc.getRowVersion() + 1);// Increase row version
        mergeOrInsertPersons(originalUserdoc);

        if (isUserdocPositionChanged(cUserdoc, originalCUserdoc)) {
            processPosition(cUserdoc);
            entityManager.flush();
        }
        if (isUserdocTypeChanged(cUserdoc, originalCUserdoc)) {
            processChangeUserdocType(cUserdoc);
            entityManager.flush();
        }

        //poradi nqkakva prichina ako se save-ne direktno IpUserdoc-a imashe problemi, zatova kysam vryzkata IpDoc -> IpUserdoc, sled koeto persistvam, IpDoc-a, rezultata ot save-a go linkvam nanovo kym IpUserdoc-a i chak togava persistvam nego
        IpDoc doc = originalUserdoc.getIpDoc();
        doc.setIpUserdoc(null);
        doc = entityManager.merge(doc);
        originalUserdoc.setIpDoc(doc);
        originalUserdoc = entityManager.merge(originalUserdoc);
        fileRecordalService.syncUserdocEffectiveAndInvalidationDate(cUserdoc);
        entityManager.flush();

        doc.setIpUserdoc(originalUserdoc);

        if (abdocsUpdate) {
            processAbdocsChanges(cUserdoc, originalCUserdoc);
        }

        IpUserdoc updatedIpUserdoc = ipUserdocRepository.findById(ipDocPK).orElse(null);
        CUserdoc newUserdoc = userdocMapper.toCore(updatedIpUserdoc, true);
        logChangesService.insertUserdocLogChanges(originalCUserdoc, cUserdoc);

        return newUserdoc;
    }

    private void registerUserdocInAbdocs(CUserdoc cUserdoc) {
        String filingNumber = cUserdoc.getDocumentId().createFilingNumber();
        Map<String, String> resultMap = missingAbdocsDocumentService.insertMissingDocument(filingNumber);
        if (Objects.nonNull(resultMap)) {
            String insertedRegNumber = resultMap.get(filingNumber);
            cUserdoc.getDocument().setExternalSystemId(insertedRegNumber);
            log.debug("Missing userdoc was inserted successfully! Registration number " + insertedRegNumber);
        }
    }

    private void processAbdocsChanges(CUserdoc userdoc, CUserdoc originalUserdoc) {
        String externalSystemId = userdoc.getDocument().getExternalSystemId();
        if (StringUtils.isEmpty(externalSystemId))
            throw new AbdocsMissingDocumentException("Registration number is empty for userdoc " + userdoc.getDocumentId().createFilingNumber());

        Integer documentId = abdocsService.selectDocumentIdByRegistrationNumber(externalSystemId);
        if (Objects.isNull(documentId)) {
            String message = "Cannot find abdocs document id for userdoc " + userdoc.getDocumentId().toString() + " with registration number = " + externalSystemId;
            throw new AbdocsMissingDocumentException(message, externalSystemId, null);
        }

        try {
            if (isUserdocTypeChanged(userdoc, originalUserdoc)) {
                updateAbdocsDocumentType(documentId, userdoc.getUserdocType().getUserdocType(), userdoc);
            }
            if (isUserdocPositionChanged(userdoc, originalUserdoc)) {
                boolean isTopProcessTheSame = isSameProcess(userdoc.getUserdocParentData().getTopProcessId(), originalUserdoc.getUserdocParentData().getTopProcessId());
                updateAbdocsDocumentPosition(documentId, userdoc, isTopProcessTheSame);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            String actionTitle = messageSource.getMessage("error.action.update.user.document.abdocs", null, LocaleContextHolder.getLocale());
            String customMessage = messageSource.getMessage("error.abdocs.update.user.document", null, LocaleContextHolder.getLocale());
            String instruction = messageSource.getMessage("instruction.abdocs.update.user.document", new String[]{String.valueOf(userdoc.getDocumentId().createFilingNumber()), String.valueOf(documentId)}, LocaleContextHolder.getLocale());
            errorLogService.createNewRecord(ErrorLogAbout.ABDOCS, actionTitle, e.getMessage(), customMessage, true, instruction, ErrorLogPriority.MEDIUM);
            throw e;
        }
    }

    private boolean isUserdocTypeChanged(CUserdoc newUserdoc, CUserdoc originalUserdoc) {
        String originalUserdocType = originalUserdoc.getUserdocType().getUserdocType();
        String newUserdocType = newUserdoc.getUserdocType().getUserdocType();
        if (StringUtils.isEmpty(newUserdocType) || StringUtils.isEmpty(originalUserdocType))
            throw new RuntimeException("Userdoc type cannot be empty !");

        return !(originalUserdocType.equalsIgnoreCase(newUserdocType));
    }

    private void processPosition(CUserdoc userdoc) {
        CProcessId userdocProcessId = userdoc.getProcessSimpleData().getProcessId();
        CProcessId newUpperProcessId = userdoc.getUserdocParentData().getProcessId();
        Integer userId = SecurityUtils.getLoggedUserId();
        ipUserdocRepository.changeUserdocPosition(userdocProcessId.getProcessType(), userdocProcessId.getProcessNbr(), newUpperProcessId.getProcessType(), newUpperProcessId.getProcessNbr(), userId);
        userdocTypeConfigService.defineResponsibleUserOnRelocation(userdoc.getDocumentId(), userdocProcessId, newUpperProcessId);
    }

    private void processChangeUserdocType(CUserdoc userdoc) {
        CDocumentId documentId = userdoc.getDocumentId();
        String newUserdocType = userdoc.getUserdocType().getUserdocType();
        ipUserdocRepository.changeUserdocType(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr(), newUserdocType);
    }

    private boolean isUserdocPositionChanged(CUserdoc cUserdoc, CUserdoc originalUserdoc) {
        CProcessId originalUpperId = originalUserdoc.getUserdocParentData().getProcessId();
        CProcessId newUpperId = cUserdoc.getUserdocParentData().getProcessId();
        return !(isSameProcess(originalUpperId, newUpperId));
    }

    private void updateAbdocsDocumentType(Integer documentId, String newUserdocType, CUserdoc userdoc) {
        Integer abdocsDocumentType = abdocsDocumentTypeService.selectAbdocsDocTypeIdByType(newUserdocType);
        if (Objects.isNull(abdocsDocumentType))
            throw new RuntimeException("Abdocs document type is empty for userdoc: " + newUserdocType);

        abdocsService.changeTypeAndDirection(documentId, abdocsDocumentType, DocDirection.Incoming.value());
        try {
            updateSubjectForChangedType(documentId, userdoc);
        } catch (Exception e) {
            log.error("Subject is not updated! Document ID: " + documentId);
            log.error(e.getMessage(), e);
        }
    }

    private void updateSubjectForChangedType(Integer documentId, CUserdoc userdoc) {
        CUserdocMainObjectData userdocMainObjectData = userdoc.getUserdocMainObjectData();
        if (!StringUtils.isEmpty(userdocMainObjectData)) {
            String userdocName = userdoc.getUserdocType().getUserdocName();
            CFileId fileId = userdoc.getUserdocMainObjectData().getFileId();
            if (Objects.nonNull(fileId) && !StringUtils.isEmpty(userdocName)) {
                String newSubject = userdocName + " към " + fileId.createFilingNumber();
                abdocsServiceAdmin.updateSubject(documentId, newSubject);
            }
        }
    }

    private void updateAbdocsDocumentPosition(Integer documentId, CUserdoc userdoc, boolean isTopProcessTheSame) {
        CDocumentId ipDocId = userdoc.getDocumentId();
        String parentFilingNumber = selectObjectNumber(userdoc);
        missingAbdocsDocumentService.insertMissingDocument(parentFilingNumber);

        Integer parentDocumentId = docCreationConverter.getParentDocumentId(parentFilingNumber);
        if (Objects.isNull(parentDocumentId))
            throw new RuntimeException("Cannot find parent document id for object  " + parentFilingNumber);

        Document document = abdocsServiceAdmin.selectDocumentById(documentId);
        if (Objects.isNull(document))
            throw new RuntimeException("Cannot find abdocs document with id = " + documentId);

        if (isTopProcessTheSame || haveToKeepTheSameRegistrationNumber(userdoc)) {
            abdocsServiceAdmin.changeParent(document.getDocId(), parentDocumentId);
        } else {
            DocCreation docCreation = docCreationConverter.createDocCreationObjectForUserdocChangePosition(document, parentDocumentId, userdoc.getUserdocType());
            Document newDocument = abdocsServiceAdmin.registerDocument(docCreation, document.getRegDate());
            if (Objects.isNull(newDocument))
                throw new RuntimeException("Cannot create new document in abdocs for userdoc " + ipDocId.createFilingNumber());

            uploadFiles(document, newDocument);
            cancelOldDocument(document, newDocument);
            setNewRegistrationNumber(userdoc, document, newDocument);
            updateSubjectOnChangeDocumentPosition(document, newDocument);
        }
    }

    private boolean haveToKeepTheSameRegistrationNumber(CUserdoc userdoc) {
        CUserdocType cUserdocType = userdoc.getUserdocType();
        if (Objects.nonNull(cUserdocType)) {
            String userdocType = cUserdocType.getUserdocType();
            if (StringUtils.hasText(userdocType)) {
                return userdocType.equalsIgnoreCase(EuPatentReceptionType.VALIDATION.code());
            }
        }
        return false;
    }

    private void updateSubjectOnChangeDocumentPosition(Document document, Document newDocument) {
        try {
            String newSubject = messageSource.getMessage("change.document.position.subject.prefix", new String[]{
                    newDocument.getRegUri(),
                    document.getDocSubject()
            }, LocaleContextHolder.getLocale());

            abdocsServiceAdmin.updateSubject(document.getDocId(), newSubject);
        } catch (Exception ex) {
            log.error("Subject is not updated! Document ID: " + document.getDocId());
            log.error(ex.getMessage(), ex);
        }
    }

    private void uploadFiles(Document document, Document newDocument) {
        List<DocFile> docFiles = document.getDocFiles();
        if (!CollectionUtils.isEmpty(docFiles)) {
            for (DocFile docFile : docFiles) {
                DownloadFileResponse file = abdocsServiceAdmin.downloadFile(docFile.getKey().toString(), docFile.getName(), docFile.getDbId());
                if (Objects.nonNull(file)) {
                    try {
                        abdocsServiceAdmin.uploadFileToExistingDocument(newDocument.getDocId(), file.getContent(), docFile.getName(), docFile.getDescription(), docFile.isPrimary(), docFile.getDocFileVisibility());
                    } catch (AbdocsServiceException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    private void setNewRegistrationNumber(CUserdoc userdoc, Document document, Document newDocument) {
        Integer documentId = document.getDocId();
        CDocumentId ipDocId = userdoc.getDocumentId();
        try {
            ipDocRepository.updateExternalSystemId(newDocument.getRegUri(), ipDocId.getDocOrigin(), ipDocId.getDocLog(), ipDocId.getDocSeries(), ipDocId.getDocNbr());
            IpUserdocRegNumberChangeLog log = new IpUserdocRegNumberChangeLog(null,
                    ipDocId.getDocOrigin(), ipDocId.getDocLog(), ipDocId.getDocSeries(), ipDocId.getDocNbr(),
                    document.getRegUri(), newDocument.getRegUri(), new Date(), SecurityUtils.getLoggedUsername());
            ipUserdocRegNumberChangeLogRepository.save(log);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            try {
                abdocsServiceAdmin.deleteDocument(newDocument.getDocId());
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            String actionTitle = messageSource.getMessage("error.action.change.userdoc.reg.number", null, LocaleContextHolder.getLocale());
            String customMessage = messageSource.getMessage("error.abdocs.change.userdoc.reg.number", new String[]{String.valueOf(document.getDocId()), String.valueOf(newDocument.getDocId())}, LocaleContextHolder.getLocale());
            String instruction = messageSource.getMessage("instruction.abdocs.update.user.document", new String[]{String.valueOf(userdoc.getDocumentId().createFilingNumber()), String.valueOf(documentId)}, LocaleContextHolder.getLocale());
            errorLogService.createNewRecord(ErrorLogAbout.ABDOCS, actionTitle, e.getMessage(), customMessage, true, instruction, ErrorLogPriority.HIGH);
        }
    }

    private void cancelOldDocument(Document document, Document newDocument) {
        try {
            abdocsServiceAdmin.cancelDocument(document.getDocId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            try {
                abdocsServiceAdmin.deleteDocument(newDocument.getDocId());
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                String actionTitle = messageSource.getMessage("error.action.abdocs.delete.document", null, LocaleContextHolder.getLocale());
                String customMessage = messageSource.getMessage("error.abdocs.delete.document", new String[]{String.valueOf(newDocument.getDocId()), String.valueOf(newDocument.getRegUri())}, LocaleContextHolder.getLocale());
                String instruction = messageSource.getMessage("instruction.abdocs.delete.document", new String[]{String.valueOf(newDocument.getDocId()), String.valueOf(document.getDocId())}, LocaleContextHolder.getLocale());
                errorLogService.createNewRecord(ErrorLogAbout.ABDOCS, actionTitle, e.getMessage(), customMessage, true, instruction, ErrorLogPriority.HIGH);
            }
            throw e;
        }
    }

    private String selectObjectNumber(CUserdoc cUserdoc) {
        String objectNumber = null;
        CProcessParentData userdocParentData = cUserdoc.getUserdocParentData();
        CFileId fileId = userdocParentData.getFileId();
        if (Objects.nonNull(fileId))
            objectNumber = fileId.createFilingNumber();

        CDocumentId userdocId = userdocParentData.getUserdocId();
        if (Objects.nonNull(userdocId))
            objectNumber = userdocId.createFilingNumber();

        if (Objects.isNull(objectNumber))
            throw new RuntimeException("Cannot find new userdoc parent number !");

        return objectNumber;
    }

    @Override
    public CUserdoc findUserdoc(String docOrigin, String docLog, Integer docSeries, Integer docNbr) {
        return findUserdoc(docOrigin, docLog, docSeries, docNbr, false);
    }

    @Override
    public CUserdoc findUserdoc(CDocumentId documentId) {
        if (Objects.isNull(documentId))
            return null;

        return findUserdoc(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
    }

    @Override
    public CUserdoc findUserdoc(String docOrigin, String docLog, Integer docSeries, Integer docNbr, boolean loadContent) {
        IpDocPK pk = new IpDocPK(docOrigin, docLog, docSeries, docNbr);
        IpUserdoc ipUserdoc = ipUserdocRepository.findById(pk).orElse(null);
        if (Objects.isNull(ipUserdoc))
            return null;

        return userdocMapper.toCore(ipUserdoc, loadContent);
    }

    @Override
    public CUserdoc findUserdoc(CDocumentId documentId, boolean loadContent) {
        if (Objects.isNull(documentId))
            return null;
        return findUserdoc(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr(), loadContent);
    }

    @Override
    public String selectUserdocTypeByDocId(CDocumentId id) {
        if (Objects.isNull(id))
            return null;

        return ipUserdocRepository.selectUserdocTypeByDocId(id.getDocOrigin(), id.getDocLog(), id.getDocSeries(), id.getDocNbr());
    }

    @Override
    public CFileId selectMainObjectIdOfUserdoc(CDocumentId id) {
        IpFilePK ipFilePK = ipDocFilesRepository.selectMainObjectIdOfUserdoc(id.getDocOrigin(), id.getDocLog(), id.getDocSeries(), id.getDocNbr());
        if (Objects.isNull(ipFilePK))
            return null;

        return fileIdMapper.toCore(ipFilePK);
    }


    @Override
    public void changeUserdocType(CDocumentId id, String newUserdocType) {
        if (Objects.nonNull(id) && !StringUtils.isEmpty(newUserdocType)) {
            ipUserdocRepository.changeUserdocType(id.getDocOrigin(), id.getDocLog(), id.getDocSeries(), id.getDocNbr(), newUserdocType);
        }
    }

    @Override
    public void updateRowVersion(CDocumentId id) {
        if (Objects.nonNull(id)) {
            ipUserdocRepository.updateRowVersion(id.getDocOrigin(), id.getDocLog(), id.getDocSeries(), id.getDocNbr());
        }
    }

    /**
     * vij komentara v {@link bg.duosoft.ipas.core.service.impl.mark.MarkServiceImpl#acceptTrademark(CMark, List, ReceptionResponseWrapper)} za ReceptionResponseWrapper
     *
     * @param userdoc
     * @param attachments
     * @param affectedId
     * @param receptionResponseWrapper
     */
    public void acceptUserdoc(CUserdoc userdoc, List<CAttachment> attachments, CFileId affectedId, Boolean relateRequestToObject, CDocumentId parentDocumentId, ReceptionResponseWrapper receptionResponseWrapper) {
        CReception receptionPatent = constructUserdocReceptionObject(userdoc, affectedId, relateRequestToObject, parentDocumentId);
        CReceptionResponse userdocReceptionResponse = internalReceptionService.createReception(receptionPatent);
        receptionResponseWrapper.setReceptionResponse(userdocReceptionResponse);
        CUserdoc userdocAfterReception = findUserdoc(userdocReceptionResponse.getDocId());
        updateUserdocData(userdoc, userdocAfterReception);
        uploadFilesToAbdocsOnAccept(userdocReceptionResponse.getDocflowDocumentId(), attachments);
    }

    /**
     * vij komentara v {@link bg.duosoft.ipas.core.service.impl.mark.MarkServiceImpl#acceptTrademark(CMark, List, ReceptionResponseWrapper)} za ReceptionResponseWrapper
     *
     * @param userdoc
     * @param attachments
     * @param affectedId
     * @param relateRequestToObject
     * @param parentDocumentId
     * @param receptionResponseWrapper
     */
    public void acceptInternationalMarkUserdoc(CUserdoc userdoc, List<CAttachment> attachments, CFileId affectedId, Boolean relateRequestToObject, CDocumentId parentDocumentId, ReceptionResponseWrapper receptionResponseWrapper) {
        CReception receptionPatent = constructUserdocReceptionObject(userdoc, affectedId, true, relateRequestToObject, parentDocumentId);
        CReceptionResponse userdocReceptionResponse = internalReceptionService.createReception(receptionPatent);
        receptionResponseWrapper.setReceptionResponse(userdocReceptionResponse);
        CUserdoc userdocAfterReception = findUserdoc(userdocReceptionResponse.getDocId());
        updateUserdocData(userdoc, userdocAfterReception);
        insertAdditionalData(userdoc, userdocAfterReception);
        uploadFilesToAbdocsOnAccept(userdocReceptionResponse.getDocflowDocumentId(), attachments);
    }

    private void updateUserdocData(CUserdoc userdoc, CUserdoc userdocAfterReception) {
        userdocAfterReception.setServicePerson(userdoc.getServicePerson());
        userdocAfterReception.setUserdocRootGrounds(userdoc.getUserdocRootGrounds());
        userdocAfterReception.setUserdocEFilingData(userdoc.getUserdocEFilingData());
        if (!CollectionUtils.isEmpty(userdoc.getUserdocExtraData())) {
            userdocAfterReception.setUserdocExtraData(userdoc.getUserdocExtraData());
        } else {
            userdocAfterReception.setUserdocExtraData(new ArrayList<>());
        }
        userdoc.getSingleDesigns().stream().forEach(sd -> {
            sd.setDocumentId(userdocAfterReception.getDocumentId());
            CFileId singleDesignPk = sd.getFileId();
            CPatent singleDesign = designService.findSingleDesign(singleDesignPk.getFileSeq(), singleDesignPk.getFileType(), singleDesignPk.getFileSeries(), singleDesignPk.getFileNbr(), false);
            if (Objects.nonNull(singleDesign)) {
                if (Objects.isNull(sd.getApplicationSubType())) {
                    sd.setApplicationSubType(new CApplicationSubType());
                }
                sd.getApplicationSubType().setApplicationType(singleDesign.getFile().getFilingData().getApplicationType());
                sd.getApplicationSubType().setApplicationSubType(singleDesign.getFile().getFilingData().getApplicationSubtype());
            }
        });
        userdocAfterReception.setSingleDesigns(userdoc.getSingleDesigns());
        userdocAfterReception.setProtectionData(userdoc.getProtectionData());
        userdocAfterReception.setInternationalNiceClasses(userdoc.getInternationalNiceClasses());
        userdocAfterReception.setIndExclusiveLicense(userdoc.getIndExclusiveLicense());
        updateUserdoc(userdocAfterReception, false);
    }

    private void insertAdditionalData(CUserdoc userdoc, CUserdoc userdocAfterReception) {
        insertInternationalMarkChangePersonData(userdoc, userdocAfterReception);
        insertInternationalMarkChangeExpirationDate(userdoc, userdocAfterReception);
    }

    private void insertInternationalMarkChangePersonData(CUserdoc userdoc, CUserdoc userdocAfterReception) {
        CInternationalUserdocTypeConfig intlUserdocTypeConfig = internationalUserdocTypeConfigService.findById(userdocAfterReception.getUserdocType().getUserdocType());
        if (Objects.nonNull(intlUserdocTypeConfig)) {
            boolean isChangePerson = intlUserdocTypeConfig.getChangeMarkOwner() || intlUserdocTypeConfig.getChangeMarkRepresentative();
            if (isChangePerson) {
                CFileId fileId = userdocAfterReception.getUserdocParentData().getTopProcessFileData().getFileId();
                CMark mark = markService.findMark(fileId, false);
                fillMarkOwnershipData(userdoc, mark);
                fillMarkRepresentativeData(userdoc, mark);
                mark.getFile().setServicePerson(userdoc.getServicePerson());
                markService.updateMarkInternal(mark);
            }
        }
    }

    private void fillMarkRepresentativeData(CUserdoc userdoc, CMark mark) {
        List<CRepresentative> newAndCurrentRepresentatives = UserdocPersonUtils.selectNewAndCurrentRepresentativesAsCRepresentatives(userdoc.getUserdocPersonData());
        mark.getFile().setRepresentationData(new CRepresentationData());
        mark.getFile().getRepresentationData().setRepresentativeList(newAndCurrentRepresentatives);
    }

    private void fillMarkOwnershipData(CUserdoc userdoc, CMark mark) {
        List<COwner> newOwners = UserdocPersonUtils.selectNewOwnersAsCOwners(userdoc.getUserdocPersonData());
        mark.getFile().setOwnershipData(new COwnershipData());
        mark.getFile().getOwnershipData().setOwnerList(newOwners);
    }

    private void insertInternationalMarkChangeExpirationDate(CUserdoc userdoc, CUserdoc userdocAfterReception) {
        CInternationalUserdocTypeConfig intlUserdocTypeConfig = internationalUserdocTypeConfigService.findById(userdocAfterReception.getUserdocType().getUserdocType());
        if (Objects.nonNull(intlUserdocTypeConfig) && intlUserdocTypeConfig.getChangeMarkRenewalExpirationDate()) {
            CFileId fileId = userdocAfterReception.getUserdocParentData().getTopProcessFileData().getFileId();
            CMark mark = markService.findMark(fileId, false);
            mark.getFile().getRegistrationData().setExpirationDate(UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.RENEWAL_NEW_EXPIRATION_DATE.name(), userdoc.getUserdocExtraData()));
            markService.updateMarkInternal(mark);
        }
    }

    public String acceptUserdocWhenParentIdIsMissing(CUserdoc userdoc, List<CAttachment> attachments, Integer relatedObjectIdentifier) {
        COwnershipData ownershipData = new COwnershipData();
        CRepresentationData representationData = new CRepresentationData();
        fillUserdocPersons(userdoc, new ArrayList<>(), ownershipData, representationData);
        DocCreation docCreation = docCreationConverter.createUserdocDocCreationWhenObjectIsMissing(userdoc, SubmissionType.ELECTRONIC.code(), relatedObjectIdentifier.toString(), false, representationData, ownershipData);
        docCreation.getCorrespondents().stream().forEach(corr -> {
            CreateCorrespondentResponse correspondentResponse = abdocsService.createCorrespondent(corr.getCorrespondent());
            corr.setCorrespondentId(correspondentResponse.getId());
        });
        Document document = abdocsService.registerDocument(docCreation, DateUtils.removeHoursFromDate(userdoc.getDocument().getFilingDate()));
        try {
            uploadFilesToAbdocsOnAccept(document.getDocId(), attachments);
        } catch (Exception e) {
            abdocsServiceAdmin.deleteDocument(document.getDocId());
            throw new RuntimeException(e);
        }
        return document.getRegUri();
    }

    @Override
    public long count() {
        return ipUserdocRepository.count();
    }

    @Override
    public List<UserdocIpDocSimpleResult> selectUserdocsFromAcstre() {
        return ipUserdocRepository.selectUserdocsFromAcstre();
    }

    @Override
    public CUserdoc selectByExternalSystemId(String externalSystemId) {
        if (StringUtils.isEmpty(externalSystemId)) {
            return null;
        }

        return userdocMapper.toCore(ipUserdocRepository.selectByExternalSystemId(externalSystemId), false);
    }

    @Override
    public CUserdoc selectByDataTextAndType(String dataText, String type) {
        return userdocMapper.toCore(ipUserdocRepository.selectByDataTextAndType(dataText, type), false);
    }

    /**
     * vij komentara v {@link bg.duosoft.ipas.core.service.impl.mark.MarkServiceImpl#acceptTrademark(CMark, List, ReceptionResponseWrapper)} za ReceptionResponseWrapper
     *
     * @param parentUserdoc
     * @param registrationRequest
     * @param rrw
     */
    public void acceptInternationalRegistrationUserdoc(CUserdoc parentUserdoc, CAcceptInternationalRegistrationRequest registrationRequest, ReceptionResponseWrapper rrw) {
        CInternationalUserdocTypeConfig userdocTypeConfig = Objects.isNull(parentUserdoc) ? null : internationalUserdocTypeConfigService.findById(parentUserdoc.getUserdocType().getUserdocType());
        if (Objects.nonNull(parentUserdoc) && Objects.nonNull(userdocTypeConfig) && userdocTypeConfig.getInternationalRegistration()) {
            updateInternationalRegistrationRequestUserdoc(parentUserdoc, registrationRequest);
            CReceptionResponse receptionResponse = insertInternationalRegistrationUserdoc(parentUserdoc, registrationRequest, userdocTypeConfig);
            updateMarkInternationalRegistrationDetails(parentUserdoc, registrationRequest);
            rrw.setReceptionResponse(receptionResponse);
        } else {
            saveMissingRegistrationRequestDocumentErrorLog(registrationRequest);
        }
    }

    private void updateMarkInternationalRegistrationDetails(CUserdoc parentUserdoc, CAcceptInternationalRegistrationRequest registrationRequest) {
        CFileId fileId = parentUserdoc.getUserdocParentData().getTopProcessFileData().getFileId();
        CMark mark = markService.findMark(fileId, false);
        mark.getMadridApplicationData().setInternationalFileNumber(UserdocExtraDataUtils.selectText(UserdocExtraDataTypeCode.INTERNATIONAL_REGISTRATION_NUMBER.name(), registrationRequest.getUserdocExtraData()));
        mark.getMadridApplicationData().setIntFilingDate(UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.INTERNATIONAL_REGISTRATION_DATE.name(), registrationRequest.getUserdocExtraData()));
        markService.updateMarkInternal(mark);
    }

    private CReceptionResponse insertInternationalRegistrationUserdoc(CUserdoc parentUserdoc, CAcceptInternationalRegistrationRequest registrationRequest, CInternationalUserdocTypeConfig userdocTypeConfig) {
        CReception userdocReception = constructRegistrationUserdocReceptionObject(parentUserdoc, registrationRequest, userdocTypeConfig);
        return internalReceptionService.createReception(userdocReception);
    }

    private CReception constructRegistrationUserdocReceptionObject(CUserdoc parentUserdoc, CAcceptInternationalRegistrationRequest registrationRequest, CInternationalUserdocTypeConfig userdocTypeConfig) {
        // fill basic data related to userdoc
        CReception receptionForm = new CReception();
        receptionForm.setEntryDate(DateUtils.removeHoursFromDate(UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.INTERNATIONAL_REGISTRATION_NOTIFICATION_DATE.name(), registrationRequest.getUserdocExtraData())));
        receptionForm.setOriginalExpected(false);
        receptionForm.setSubmissionType(SubmissionType.IMPORT.code());
        receptionForm.setUserdoc(new CReceptionUserdoc());
        receptionForm.getUserdoc().setUserdocType(userdocTypeConfig.getGenerateUserdocType());
        receptionForm.getUserdoc().setWithoutCorrespondents(true);
        receptionForm.getUserdoc().setDocumentId(parentUserdoc.getDocumentId());
        receptionForm.setRegisterInDocflowSystem(true);

        return receptionForm;
    }

    private void updateInternationalRegistrationRequestUserdoc(CUserdoc parentUserdoc, CAcceptInternationalRegistrationRequest registrationRequest) {
        if (CollectionUtils.isEmpty(parentUserdoc.getUserdocExtraData())) {
            parentUserdoc.setUserdocExtraData(registrationRequest.getUserdocExtraData());
        } else {
            parentUserdoc.getUserdocExtraData().addAll(registrationRequest.getUserdocExtraData());
        }

        updateUserdoc(parentUserdoc, false);
    }

    private void saveMissingRegistrationRequestDocumentErrorLog(CAcceptInternationalRegistrationRequest registrationRequest) {
        String errorMessage = "Userdoc with external system id = " + registrationRequest.getRegistrationRequestExternalSystemId() + " and userdoc_typ='ЗМР' doesn't exist in IPAS !";
        String actionTitle = messageSource.getMessage("error.action.international.register", null, LocaleContextHolder.getLocale());
        String customMessage = messageSource.getMessage("error.ipas.international.registration", new String[]{registrationRequest.getRegistrationRequestExternalSystemId()}, LocaleContextHolder.getLocale());
        String instruction = messageSource.getMessage("instruction.ipas.international.registration", new String[]{registrationRequest.getRegistrationRequestExternalSystemId(), UserdocExtraDataUtils.selectText(UserdocExtraDataTypeCode.INTERNATIONAL_REGISTRATION_NUMBER.name(), registrationRequest.getUserdocExtraData()), DateUtils.formatDate(UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.INTERNATIONAL_REGISTRATION_DATE.name(), registrationRequest.getUserdocExtraData()))}, LocaleContextHolder.getLocale());
        errorLogService.createNewRecord(ErrorLogAbout.IPAS, actionTitle, errorMessage, customMessage, true, instruction, ErrorLogPriority.MEDIUM);
    }

    /**
     * vij komentara v {@link bg.duosoft.ipas.core.service.impl.mark.MarkServiceImpl#acceptTrademark(CMark, List, ReceptionResponseWrapper)} za ReceptionResponseWrapper
     *
     * @param parentDocumentId
     * @param additionalUserdoc
     * @param receptionResponseWrapper
     */
    public void acceptAdditionalMadridEfilingUserdoc(CDocumentId parentDocumentId, CUserdoc additionalUserdoc, ReceptionResponseWrapper receptionResponseWrapper) {
        CReception userdocReception = constructAdditionalMadridEfilingUserdocReceptionObject(parentDocumentId, additionalUserdoc);
        CReceptionResponse res = internalReceptionService.createReception(userdocReception);
        receptionResponseWrapper.setReceptionResponse(res);
    }

    private CReception constructAdditionalMadridEfilingUserdocReceptionObject(CDocumentId parentDocumentId, CUserdoc additionalUserdoc) {
        CReception receptionForm = new CReception();
        receptionForm.setEntryDate(DateUtils.removeHoursFromDate(new Date()));
        receptionForm.setOriginalExpected(false);
        receptionForm.setNotes(additionalUserdoc.getNotes());
        receptionForm.setSubmissionType(SubmissionType.ELECTRONIC.code());
        receptionForm.setUserdoc(new CReceptionUserdoc());
        receptionForm.getUserdoc().setUserdocType(UserdocType.MARK_INTERNATIONAL_REGISTRATION_ADDITIONAL_REQUEST);
        receptionForm.getUserdoc().setWithoutCorrespondents(UserdocUtils.isUserdocWithoutCorrespondents(additionalUserdoc.getUserdocType(), userdocPanelService));
        receptionForm.getUserdoc().setDocumentId(parentDocumentId);
        receptionForm.setRegisterInDocflowSystem(true);

        COwnershipData ownershipData = new COwnershipData();
        ownershipData.setOwnerList(new ArrayList<>());
        CUserdocPerson userdocPerson = additionalUserdoc.getUserdocPersonData().getPersonList().get(0);
        if (userdocPerson.getRole().equals(UserdocPersonRole.APPLICANT)) {
            COwner owner = new COwner();
            owner.setPerson(userdocPerson.getPerson());
            ownershipData.getOwnerList().add(owner);
        }

        receptionForm.setOwnershipData(ownershipData);
        return receptionForm;
    }

    private void fillUserdocPersons(CUserdoc userdoc, List<CUserdocPerson> userdocPersons, COwnershipData ownershipData, CRepresentationData representationData) {
        ownershipData.setOwnerList(new ArrayList<>());
        representationData.setRepresentativeList(new ArrayList<>());
        //fill persons
        for (CUserdocPerson userdocPerson : userdoc.getUserdocPersonData().getPersonList()) {
            if (userdocPerson.getRole().equals(UserdocPersonRole.APPLICANT)) {
                COwner owner = new COwner();
                owner.setPerson(userdocPerson.getPerson());
                ownershipData.getOwnerList().add(owner);
            } else if (userdocPerson.getRole().equals(UserdocPersonRole.REPRESENTATIVE)) {
                CRepresentative representative = new CRepresentative(userdocPerson.getRepresentativeType(), userdocPerson.getPerson(),userdocPerson.getAttorneyPowerTerm(),userdocPerson.getReauthorizationRight(),userdocPerson.getPriorReprsRevocation(),userdocPerson.getAuthorizationCondition());
                representationData.getRepresentativeList().add(representative);
            } else {
                userdocPersons.add(userdocPerson);
            }
        }
    }

    private void uploadFilesToAbdocsOnAccept(Integer docFlowDocumentId, List<CAttachment> attachments) {
        attachments.stream().forEach(attachment -> {
            if (Objects.nonNull(attachment.getData()))
                abdocsService.uploadFileToExistingDocument(docFlowDocumentId, attachment.getData(), attachment.getFileName(), attachment.getDescription(), false, DocFileVisibility.PublicAttachedFile);
        });
    }

    private CReception constructUserdocReceptionObject(CUserdoc userdoc, CFileId affectedId, Boolean relateRequestToObject, CDocumentId parentDocumentId) {
        return constructUserdocReceptionObject(userdoc, affectedId, false, relateRequestToObject, parentDocumentId);
    }

    private CReception constructUserdocReceptionObject(CUserdoc userdoc, CFileId affectedId, boolean isInternationalImportUserdoc, Boolean relateRequestToObject, CDocumentId parentDocumentId) {
        // fill basic data related to userdoc
        CReception receptionForm = new CReception();
        receptionForm.setEntryDate(DateUtils.removeHoursFromDate(userdoc.getDocument().getFilingDate()));
        receptionForm.setNotes(userdoc.getNotes());
        receptionForm.setOriginalExpected(false);
        if (isInternationalImportUserdoc) {
            receptionForm.setSubmissionType(SubmissionType.IMPORT.code());
        } else {
            receptionForm.setSubmissionType(SubmissionType.ELECTRONIC.code());
        }
        receptionForm.setRegisterInDocflowSystem(true);
        receptionForm.setUserdoc(new CReceptionUserdoc());
        receptionForm.getUserdoc().setUserdocType(userdoc.getUserdocType().getUserdocType());
        receptionForm.getUserdoc().setWithoutCorrespondents(UserdocUtils.isUserdocWithoutCorrespondents(userdoc.getUserdocType(), userdocPanelService));
        receptionForm.getUserdoc().setExternalRegistrationNumber(userdoc.getDocument().getExternalSystemId());
        if (Objects.nonNull(relateRequestToObject) && relateRequestToObject) {
            receptionForm.getUserdoc().setFileId(affectedId);
        } else if (Objects.nonNull(parentDocumentId)) {
            receptionForm.getUserdoc().setDocumentId(parentDocumentId);
        } else {
            userdocTypeConfigService.defineUpperProc(receptionForm.getUserdoc(), affectedId);
        }
        //Init persons
        List<CUserdocPerson> userdocPersons = new ArrayList<>();
        COwnershipData ownershipData = new COwnershipData();
        CRepresentationData representationData = new CRepresentationData();
        fillUserdocPersons(userdoc, userdocPersons, ownershipData, representationData);
        // fill reception with person data
        receptionForm.setOwnershipData(ownershipData);
        receptionForm.setRepresentationData(representationData);
        receptionForm.getUserdoc().setUserdocPersons(userdocPersons);

        return receptionForm;
    }

    protected void mergeOrInsertPersons(IpUserdoc entity) {
        List<IpUserdocPerson> updateLast = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entity.getPersons())) {
            for (IpUserdocPerson userdocPerson : entity.getPersons()) {
                if (Objects.nonNull(userdocPerson.getIpPersonAddresses().getTempParentPersonNbr())) {
                    updateLast.add(userdocPerson);
                } else {
                    updateIpPersonAddresses(userdocPerson);
                }
            }
        }

        if (entity.getServicePerson() != null) {
            IpPersonAddresses updated = ipPersonAdressesRepository.mergeOrInsertPersonAddress(entity.getServicePerson());
            entity.setServicePerson(updated);
            entity.getServicePerson().getPk().setAddrNbr(updated.getPk().getAddrNbr());
            entity.getServicePerson().getPk().setPersonNbr(updated.getPk().getPersonNbr());
        }

        if (!CollectionUtils.isEmpty(updateLast)) {
            updateLast.forEach(this::updateIpPersonAddresses);
        }
    }

    protected void updateIpPersonAddresses(UserDocumentRelatedPerson person) {
        IpPersonAddresses p = ipPersonAdressesRepository.mergeOrInsertPersonAddress(person.getIpPersonAddresses());
        person.setIpPersonAddresses(p);
        person.getPk().setAddrNbr(p.getPk().getAddrNbr());
        person.getPk().setPersonNbr(p.getPk().getPersonNbr());
    }

    private boolean isSameProcess(CProcessId processId, CProcessId anotherProcessId) {
        String processType = processId.getProcessType();
        Integer processNbr = processId.getProcessNbr();
        String anotherProcessType = anotherProcessId.getProcessType();
        Integer anotherProcessNbr = anotherProcessId.getProcessNbr();
        return (processNbr.equals(anotherProcessNbr)) && (processType.equalsIgnoreCase(anotherProcessType));
    }

    public void deleteUserdoc(CDocumentId documentId, boolean deleteInDocflowSystem) {
        if (deleteInDocflowSystem) {
            String externalSystemId = ipDocRepository.selectExternalSystemId(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
            if (!StringUtils.isEmpty(externalSystemId)) {
                Integer abdocsDocumentId = abdocsServiceAdmin.selectDocumentIdByRegistrationNumber(externalSystemId);
                if (abdocsDocumentId != null) {
                    try {
                        log.debug("Trying to delete document in abdocs " + abdocsDocumentId);
                        abdocsServiceAdmin.deleteDocument(abdocsDocumentId);
                    } catch (RuntimeException e) {
                        log.debug("Cannot delete document in abdocs. Trying to unregister it!", e);
                        abdocsServiceAdmin.unregisterDocument(abdocsDocumentId);
                    }
                }

            }

        }
        ipUserdocRepository.deleteUserdoc(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());

    }

    @Override
    public List<CUserdocHierarchyNode> getUserdocUserdocHierarchy(CDocumentId documentId, boolean flatHierarchy) {
        return createHierarchyResult(callDocumentFunction(documentId, ipUserdocRepository::getUserdocUserdocHierarchy), flatHierarchy);
    }

    @Override
    public List<CUserdocHierarchyNode> getFileUserdocHierarchy(CFileId fileId, boolean flatHierarchy) {
        return createHierarchyResult(callFileFunction(fileId, ipUserdocRepository::getFileUserdocHierarchy), flatHierarchy);
    }

    private List<CUserdocHierarchyNode> createHierarchyResult(List<UserdocHierarchyChildNode> list, boolean flatHierarchy) {
        List<CUserdocHierarchyNode> res = userdocHierarchyMapper.toCoreList(list);
        if (flatHierarchy) {
            return res;
        }
        Map<CProcessId, CUserdocHierarchyNode> byProcessId = res.stream().collect(Collectors.toMap(r -> r.getProcessId(), Function.identity()));

        //grupira elementite pp upperProcessId, tezi koito upperProcessId ne sy6testvuva v byProcessId, sa elementite ot naj-gorno nivo, drugite se zakachat na syotvetniq element v byProcessId
        Map<CProcessId, List<CUserdocHierarchyNode>> byUpperProcess = res.stream().collect(Collectors.groupingBy(r -> r.getUpperProcessId()));
        List<CUserdocHierarchyNode> result = new ArrayList<>();
        for (Map.Entry<CProcessId, List<CUserdocHierarchyNode>> e : byUpperProcess.entrySet()) {
            if (!byProcessId.containsKey(e.getKey())) {//tova e naj-gornoto nivo - slaga se v list-a
                result.addAll(e.getValue());
            } else {//inache namirame v koi node trqbva da slojim tezi elementi kato children
                byProcessId.get(e.getKey()).setChildren(e.getValue());
            }
        }
        return result;
    }
}
