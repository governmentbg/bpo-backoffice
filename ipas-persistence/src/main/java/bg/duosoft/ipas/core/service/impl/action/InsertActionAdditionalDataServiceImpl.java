package bg.duosoft.ipas.core.service.impl.action;

import bg.duosoft.abdocs.model.Attachments;
import bg.duosoft.abdocs.model.DocCreation;
import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.offidoc.*;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.process.*;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeTemplateService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.action.*;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.core.service.ext.OffidocAbdocsDocumentService;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.offidoc.OffidocService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.MissingAbdocsDocumentService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.integration.abdocs.converter.DocCreationConverter;
import bg.duosoft.ipas.properties.PropertyAccess;
import bg.duosoft.ipas.services.core.IpasServiceException;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import bg.duosoft.ipas.util.offidoc.OffidocUtils;
import bg.duosoft.ipas.util.process.ProcessActionUtils;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

@Slf4j
@Service
@Transactional
@LogExecutionTime
@RequiredArgsConstructor
public class InsertActionAdditionalDataServiceImpl implements InsertActionAdditionalDataService {

    private final AbdocsService abdocsService;
    private final StatusService statusService;
    private final MessageSource messageSource;
    private final OffidocService offidocService;
    private final ProcessService processService;
    private final UserdocService userdocService;
    private final PropertyAccess propertyAccess;
    private final ErrorLogService errorLogService;
    private final OffidocTypeService offidocTypeService;
    private final DocCreationConverter docCreationConverter;
    private final OffidocAbdocsDocumentService offidocAbdocsDocumentService;
    private final RecordalAuthorizationService recordalAuthorizationService;
    private final MissingAbdocsDocumentService missingAbdocsDocumentService;
    private final ReceptionService receptionService;
    private final OffidocTypeTemplateService offidocTypeTemplateService;

    @Override
    public void insertAdditionalData(CProcess process, CActionId actionId, CProcessInsertActionRequest insertActionRequest, CNextProcessAction nextProcessAction) throws RuntimeException, IpasServiceException {
        processOffidocData(process, nextProcessAction, insertActionRequest.getOffidocTemplates());
        CFileId dividedFileId = null;
        try {
            dividedFileId = processListCodeActions(process, nextProcessAction);
            processRecordalData(process, nextProcessAction, actionId, insertActionRequest);
        } catch (Exception e) {
            if (Objects.nonNull(dividedFileId)) {
                deleteDividedObjectFromAbdocs(dividedFileId);
            }
            throw e;
        }
    }

    private CFileId processListCodeActions(CProcess process, CNextProcessAction nextProcessAction) {
        if (ProcessActionUtils.doesActionContainSplitConfiguration(nextProcessAction)) {
            Pair<IpObjectSplitCode, CFileId> pair = ProcessActionUtils.selectIpObjectSplitType(nextProcessAction, process, processService);
            if (Objects.nonNull(pair)) {
                CFileId fileId = pair.getSecond();//Top process file id
                IpObjectSplitCode splitCode = pair.getFirst();
                switch (splitCode) {
                    case IP_OBJECT_MARK_SPLIT:
                        return receptionService.createDividedMark(fileId, null, null, false).getFile().getFileId();
                    case USERDOC_MARK_SPLIT: {
                        CUserdoc userdoc = userdocService.findUserdoc(process.getProcessOriginData().getDocumentId());
                        if (UserdocUtils.doesUserdocMeetSplitConditions(userdoc)) {
                            List<CNiceClass> niceClassList = Objects.isNull(userdoc.getProtectionData()) ? null : userdoc.getProtectionData().getNiceClassList();
                            CMark dividedMark = receptionService.createDividedMark(fileId, UserdocPersonUtils.selectNewOwnersAsCOwners(userdoc.getUserdocPersonData()), niceClassList, true);
                            return dividedMark.getFile().getFileId();
                        }
                        break;
                    }
                    case IP_OBJECT_DESIGN_SPLIT:
                        return receptionService.createDividedDesign(fileId, null, null, false).getFile().getFileId();
                    case USERDOC_DESIGN_SPLIT: {
                        CUserdoc userdoc = userdocService.findUserdoc(process.getProcessOriginData().getDocumentId());
                        if (UserdocUtils.doesUserdocMeetSplitConditions(userdoc)) {
                            CPatent dividedDesign = receptionService.createDividedDesign(fileId, UserdocUtils.selectSingleDesignsIdentifiers(userdoc.getSingleDesigns()), UserdocPersonUtils.selectNewOwnersAsCOwners(userdoc.getUserdocPersonData()), true);
                            return dividedDesign.getFile().getFileId();
                        }
                        break;
                    }
                }
            }
        }
        return null;
    }

    private void processRecordalData(CProcess process, CNextProcessAction nextProcessAction, CActionId actionId, CProcessInsertActionRequest insertActionRequest) {
        String statusCode = nextProcessAction.getStatusCode();
        String processType = process.getProcessId().getProcessType();
        boolean isStatusTriggerActivity = UserdocUtils.isStatusTriggerActivity(statusCode, processType, statusService);
        if (isStatusTriggerActivity) {
            CDocumentId documentId = process.getProcessOriginData().getDocumentId();
            if (Objects.nonNull(documentId)) {
                recordalAuthorizationService.authorize(documentId, actionId, buildAuthorizationData(insertActionRequest));
            }
        }
    }

    private CProcessAuthorizationData buildAuthorizationData(CProcessInsertActionRequest insertActionRequest) {
        return CProcessAuthorizationData.builder()
                .effectiveDate(insertActionRequest.getRecordalDate())//Recordal date from action modal form
                .invalidationDate(insertActionRequest.getInvalidationDate())//Invalidation date from action modal form
                .transferCorrespondenceAddress(insertActionRequest.getTransferCorrespondenceAddress())
                .build();
    }

    private void processOffidocData(CProcess process, CNextProcessAction nextProcessAction, List<String> formDataOffidocTemplates) throws IpasServiceException {
        printOffidocs(process, nextProcessAction);
        String offidocType = nextProcessAction.getGeneratedOffidoc();
        if (!StringUtils.isEmpty(offidocType)) {
            updateOffidocResponsibleUser(process, offidocType);
            List<String> templates = new ArrayList<>();
            if (CollectionUtils.isEmpty(formDataOffidocTemplates)) {
                COffidocType offidocTypeObject = offidocTypeService.selectById(offidocType);
                templates.add(offidocTypeObject.getDefaultTemplate());
            } else
                templates.addAll(formDataOffidocTemplates);

            CProcessEvent processEvent = selectLastProcessEventWithSpecificOffidocType(process, offidocType);
            Map<String, byte[]> offidocFiles = offidocService.generateOffidocForProcessEvent(processEvent, templates);
            registerOffidocDocument(process, processEvent, offidocFiles);
        }
    }

    private void registerOffidocDocument(CProcess cProcess, CProcessEvent cProcessEvent, Map<String, byte[]> offidocFiles) {
        String filingNumber;
        boolean isManualSubProcess = ProcessTypeUtils.isManualSubProcess(cProcess);
        if (isManualSubProcess) {
            CProcessParentData cProcessParentData = processService.generateProcessParentHierarchy(cProcess.getProcessId());
            filingNumber = fillFilingNumberForParentProcess(cProcessParentData);
        } else {
            filingNumber = fillFilingNumberForCurrentProcess(cProcess);
        }

        log.debug("Registering offidoc document for " + filingNumber + " in ABDOCS...");
        if (Objects.nonNull(cProcessEvent) && Objects.nonNull(cProcessEvent.getEventAction())) {
            missingAbdocsDocumentService.insertMissingDocument(filingNumber);

            CActionProcessEvent eventAction = cProcessEvent.getEventAction();
            COffidoc generatedOffidoc = eventAction.getGeneratedOffidoc();
            CProcessParentData offidocParentData = generatedOffidoc.getOffidocParentData();

            COffidocType cOffidocType = generatedOffidoc.getOffidocType();
            String offidocName = cOffidocType.getOffidocName();
            String offidocType = cOffidocType.getOffidocType();

            String documentSubject = filingNumber + ": " + offidocName;
            String parentRegistrationNumber = docCreationConverter.getParentRegistrationNumber(filingNumber);
            if (!StringUtils.isEmpty(parentRegistrationNumber)) {
                documentSubject = parentRegistrationNumber + ": " + offidocName;
            }

            List<Attachments> attachments = new ArrayList<>();
            Set<String> keySet = offidocFiles.keySet();
            for (String key : keySet) {
                byte[] offidocContent = offidocFiles.get(key);
                String mimeType = AttachmentUtils.tikaMimeTypeConvertMap.get(AttachmentUtils.getContentType(offidocContent));
                Attachments attachment = new Attachments(offidocContent, mimeType, OffidocUtils.selectOffidocFileName(offidocTypeTemplateService, generatedOffidoc, key) + AttachmentUtils.contentTypeToExtensionMap.get(mimeType));
                attachments.add(attachment);
            }

            List<Attachments> staticAttachments = covertStaticTemplatesToAttachments(cOffidocType);//It is important to read the files before abdocs document registration

            DocCreation docCreation = docCreationConverter.convertOffidoc(filingNumber, documentSubject, offidocType, attachments, offidocParentData);
            if (Objects.nonNull(docCreation.getParentDocId())) {
                Document document = abdocsService.registerDocument(docCreation, null);
                if (Objects.nonNull(document)) {
                    uploadStaticFilesToDocument(document.getDocId(), staticAttachments);

                    log.debug("ABDOCS document for offidoc " + documentSubject + " is registered successfully !");
                    log.debug("Saving document id in EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT table...");
                    COffidocAbdocsDocument offidocAbdocsDocument = createOffidocAbdocsDocument(eventAction.getGeneratedOffidoc(), document);
                    COffidocAbdocsDocument save = offidocAbdocsDocumentService.save(offidocAbdocsDocument);
                    if (Objects.nonNull(save)) {
                        log.debug("ABDOCS Document id is saved successfully in EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT !");
                    } else {
                        log.error("ABDOCS Document id is not saved in EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT !");
                    }
                } else {
                    log.debug("ABDOCS document for offidoc " + documentSubject + " is empty !");
                }
            } else {
                String errorMessage = "ABDOCS document for offidoc " + documentSubject + " is not created because parent document id is missing !";
                log.error(errorMessage);
                String actionTitle = messageSource.getMessage("error.action.save.office.document.abdocs", null, LocaleContextHolder.getLocale());
                String customMessage = messageSource.getMessage("error.abdocs.save.office.document", new String[]{String.valueOf(filingNumber)}, LocaleContextHolder.getLocale());
                String instruction = messageSource.getMessage("instruction.abdocs.save.office.document", new String[]{String.valueOf(filingNumber)}, LocaleContextHolder.getLocale());
                errorLogService.createNewRecord(ErrorLogAbout.ABDOCS, actionTitle, errorMessage, customMessage, true, instruction, ErrorLogPriority.MEDIUM);
            }
        } else {
            log.debug("Offidoc document is not created because process event is empty !");
        }

    }

    private void uploadStaticFilesToDocument(Integer documentId, List<Attachments> staticAttachments) {
        if (!CollectionUtils.isEmpty(staticAttachments)) {
            for (Attachments staticAttachment : staticAttachments) {
                try {
                    abdocsService.uploadFileToExistingDocument(documentId, staticAttachment.getContent(), staticAttachment.getName(), false, DocFileVisibility.PublicAttachedFile);
                } catch (Exception e) {
                    log.error("Cannot upload static file to offidoc! File: {}, Document ID: {}", staticAttachment.getName(), documentId);
                }
            }
        }
    }

    private List<Attachments> covertStaticTemplatesToAttachments(COffidocType cOffidocType) {
        List<Attachments> list = new ArrayList<>();

        List<COffidocTypeStaticTemplate> staticTemplates = cOffidocType.getStaticTemplates();
        if (!CollectionUtils.isEmpty(staticTemplates)) {
            String offidocType = cOffidocType.getOffidocType();

            String templatesDir = propertyAccess.getOffidocStaticTemplatesDirectory();
            if (StringUtils.isEmpty(templatesDir)) {
                throw new RuntimeException("Offidoc static templates directory is empty ! Offidoc type: " + offidocType);
            }

            for (COffidocTypeStaticTemplate staticTemplate : staticTemplates) {
                String fileSystemPath = templatesDir + File.separator + staticTemplate.getStaticFileName();
                byte[] fileContent = AttachmentUtils.readFileSystemResource(fileSystemPath);
                String mimeType = AttachmentUtils.tikaMimeTypeConvertMap.get(AttachmentUtils.getContentType(fileContent));
                Attachments attachment = new Attachments(fileContent, mimeType, staticTemplate.getStaticFileName());
                list.add(attachment);
            }
        }
        return list;
    }

    private COffidocAbdocsDocument createOffidocAbdocsDocument(COffidoc cOffidoc, Document document) {
        CProcessParentData offidocParentData = cOffidoc.getOffidocParentData();
        COffidocAbdocsDocument offidocAbdocsDocument = new COffidocAbdocsDocument();
        offidocAbdocsDocument.setOffidocId(cOffidoc.getOffidocId());
        offidocAbdocsDocument.setAbdocsDocumentId(document.getDocId());
        offidocAbdocsDocument.setRegistrationNumber(document.getRegUri());
        fillClosestMainParentObjectData(offidocAbdocsDocument, offidocParentData);
        fillDirectParentObjectData(offidocAbdocsDocument, offidocParentData);
        return offidocAbdocsDocument;
    }

    private void fillDirectParentObjectData(COffidocAbdocsDocument offidocAbdocsDocument, CProcessParentData offidocParentData) {
        String directParentObjectId = null;
        String directParentObjectType = null;
        if (offidocParentData.isFileProcess()) {
            directParentObjectId = offidocParentData.getFileId().createFilingNumber();
            directParentObjectType = OffidocParentObjectType.IPOBJECT.name();
        } else if (offidocParentData.isUserdocProcess()) {
            directParentObjectId = offidocParentData.getUserdocId().createFilingNumber();
            directParentObjectType = OffidocParentObjectType.USERDOC.name();
        } else if (offidocParentData.isOffidocProcess()) {
            directParentObjectId = offidocParentData.getOffidocId().createFilingNumber();
            directParentObjectType = OffidocParentObjectType.OFFIDOC.name();
        } else if (offidocParentData.getIsManualSubProcess()) {
            directParentObjectType = OffidocParentObjectType.MANUAL_SUB_PROCESS.name();
        }
        offidocAbdocsDocument.setDirectParentObjectId(directParentObjectId);
        offidocAbdocsDocument.setDirectParentObjectType(directParentObjectType);
    }

    private void fillClosestMainParentObjectData(COffidocAbdocsDocument offidocAbdocsDocument, CProcessParentData offidocParentData) {
        Pair<String, String> pairResult = OffidocUtils.selectClosestMainParentObjectData(offidocParentData);
        if (Objects.nonNull(pairResult)) {
            offidocAbdocsDocument.setClosestMainParentObjectId(pairResult.getFirst());
            offidocAbdocsDocument.setClosestMainParentObjectType(pairResult.getSecond());
        }
    }

    private String fillFilingNumberForCurrentProcess(CProcess cProcess) {
        String filingNumber = null;
        CFileId fileId = cProcess.getProcessOriginData().getFileId();
        CDocumentId documentId = cProcess.getProcessOriginData().getDocumentId();
        COffidocId offidocId = cProcess.getProcessOriginData().getOffidocId();
        if (Objects.nonNull(fileId))
            filingNumber = fileId.createFilingNumber();
        else if (Objects.nonNull(documentId))
            filingNumber = UserdocUtils.convertDocumentIdToString(documentId);
        else if (Objects.nonNull(offidocId))
            filingNumber = OffidocUtils.covertOffidocIdToString(offidocId);

        return filingNumber;
    }

    private String fillFilingNumberForParentProcess(CProcessParentData processParentData) {
        String filingNumber = null;
        CFileId fileId = processParentData.getFileId();
        CDocumentId documentId = processParentData.getUserdocId();
        COffidocId offidocId = processParentData.getOffidocId();
        if (Objects.nonNull(fileId))
            filingNumber = fileId.createFilingNumber();
        else if (Objects.nonNull(documentId))
            filingNumber = UserdocUtils.convertDocumentIdToString(documentId);
        else if (Objects.nonNull(offidocId))
            filingNumber = OffidocUtils.covertOffidocIdToString(offidocId);
        return filingNumber;
    }

    private void printOffidocs(CProcess cProcess, CNextProcessAction nextProcessAction) {
        try {
            if (Objects.nonNull(nextProcessAction.getGeneratedOffidoc()))
                OffidocUtils.printAllNotPrintedOffidocs(cProcess.getProcessId(), processService, offidocService);
        } catch (IpasServiceException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateOffidocResponsibleUser(CProcess cProcess, String offidocType) {
        CProcessEvent processEvent = selectLastProcessEventWithSpecificOffidocType(cProcess, offidocType);
        CActionProcessEvent eventAction = processEvent.getEventAction();
        COffidoc generatedOffidoc = eventAction.getGeneratedOffidoc();
        if (Objects.nonNull(generatedOffidoc)) {
            CProcessId offidocProcess = generatedOffidoc.getProcessId();
            if (Objects.nonNull(offidocProcess) && Objects.nonNull(cProcess.getResponsibleUser())) {
                processService.updateResponsibleUser(cProcess.getResponsibleUser().getUserId(), offidocProcess.getProcessType(), offidocProcess.getProcessNbr());
            }
        }
    }

    private CProcessEvent selectLastProcessEventWithSpecificOffidocType(CProcess cProcess, String offidocType) {
        List<CProcessEvent> processEventList = cProcess.getProcessEventList();
        return processEventList.stream()
                .filter(cProcessEvent -> Objects.nonNull(cProcessEvent.getEventAction()))
                .filter(cProcessEvent -> Objects.nonNull(cProcessEvent.getEventAction().getGeneratedOffidoc()))
                .filter(cProcessEvent -> !StringUtils.isEmpty(cProcessEvent.getEventAction().getGeneratedOffidoc().getOffidocType().getOffidocType()))
                .filter(cProcessEvent -> cProcessEvent.getEventAction().getGeneratedOffidoc().getOffidocType().getOffidocType().equals(offidocType))
                .max(Comparator.comparing(cProcessEvent -> cProcessEvent.getEventAction().getCaptureDate()))
                .orElse(null);
    }

    private void deleteDividedObjectFromAbdocs(CFileId dividedFileId) {
        Integer dividedObjectAbdocsId = abdocsService.selectDocumentIdByRegistrationNumber(dividedFileId.createFilingNumber());
        if (Objects.nonNull(dividedObjectAbdocsId)) {
            abdocsService.deleteDocument(dividedObjectAbdocsId);
        }
    }

}