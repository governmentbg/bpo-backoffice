package bg.duosoft.ipas.core.service.impl.abdocs.notification;

import bg.duosoft.abdocs.model.*;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.abdocs.notification.CDocumentNotificationProcessResult;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.miscellaneous.CAbdocsDocumentType;
import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.reception.CReceptionCorrespondent;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.core.service.ext.OffidocAbdocsDocumentService;
import bg.duosoft.ipas.core.service.reception.ReceptionUserdocRequestService;
import bg.duosoft.ipas.core.service.abdocs.notification.AbdocsNotificationService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.reception.OriginalExpectedService;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import bg.duosoft.ipas.enums.IpasObjectType;
import bg.duosoft.ipas.integration.abdocs.converter.CorrespondentConverter;
import bg.duosoft.ipas.util.general.BasicUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//TODO Maybe we should write in IpErrorLog if notification is not processed correctly
@Slf4j
@Component
public class AbdocsNotificationServiceImpl implements AbdocsNotificationService {

    @Autowired
    private OffidocAbdocsDocumentService offidocAbdocsDocumentService;

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @Autowired
    private ReceptionUserdocRequestService receptionUserdocRequestService;

    @Autowired
    private MarkService markService;

    @Autowired
    private PatentService patentService;

    @Autowired
    private FileService fileService;

    @Autowired
    private OriginalExpectedService originalExpectedService;

    @Autowired
    private CorrespondentConverter correspondentConverter;

    @Autowired
    private PersonService personService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private DocService docService;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public CDocumentNotificationProcessResult notifyRegistration(Document document, IpasObjectType ipasObject) {
        Integer documentId = document.getDocId();
        switch (ipasObject) {
            case OFFIDOC:
                COffidocAbdocsDocument cOffidocAbdocsDocument = offidocAbdocsDocumentService.selectByAbdocsId(documentId);
                if (Objects.isNull(cOffidocAbdocsDocument)) {
                    throw new RuntimeException("Cannot find record with document id = " + documentId + " in EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT table");
                }
                String newRegistrationNumber = document.getRegUri();
                if (Objects.nonNull(newRegistrationNumber)) {
                    offidocAbdocsDocumentService.updateRegistrationNumber(newRegistrationNumber, documentId);
                }

                break;
        }

        return new CDocumentNotificationProcessResult(true, null);
    }

    @Override
    public CDocumentNotificationProcessResult notifyProcessed(Document document, IpasObjectType ipasObject) {
        CDocumentNotificationProcessResult result = new CDocumentNotificationProcessResult();
        String regUri = document.getRegUri();

        if (ipasObject == IpasObjectType.MARK || ipasObject == IpasObjectType.PATENT) {
            CFileId fileId = BasicUtils.createCFileId(regUri);
            if (Objects.isNull(fileId)) {
                throw new RuntimeException("Cannot create file id from regUri = " + regUri + " from document with id = " + document.getDocId());
            }

            boolean isFileExists = fileService.isFileExist(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
            if (isFileExists) {
                List<String> messages = result.getMessages();
                updateIpObjectOriginalExpected(document, fileId, messages);
                updateIpObjectSubmissionType(document, fileId, messages);
                if (isIpObjectReception(fileId, ipasObject)) {
                    updateIpObjectCorrespondents(document, fileId, messages);
                }
            }
        } else if (ipasObject == IpasObjectType.USERDOC) {
            CDocumentId userdocId = docService.selectDocumentIdByExternalSystemId(regUri);
            if (Objects.isNull(userdocId) || Objects.isNull(userdocId.getDocNbr())) {
                String errorMessage = "Document with external system id = " + regUri + " doesn't exist in IPAS !";
                log.error(errorMessage);
                String actionTitle = messageSource.getMessage("error.action.abdocs.notification.userdoc.not.found", null, LocaleContextHolder.getLocale());
                String customMessage = messageSource.getMessage("error.abdocs.notification.userdoc.not.found", new String[]{String.valueOf(document.getDocId())}, LocaleContextHolder.getLocale());
                String instruction = messageSource.getMessage("instruction.abdocs.notification.userdoc.not.found", new String[]{String.valueOf(document.getDocId())}, LocaleContextHolder.getLocale());
                errorLogService.createNewRecord(ErrorLogAbout.ABDOCS, actionTitle, errorMessage, customMessage, true, instruction, ErrorLogPriority.HIGH);
                return new CDocumentNotificationProcessResult(true, null);
            }

            boolean isUserdocExists = docService.isDocumentExist(userdocId.getDocOrigin(), userdocId.getDocLog(), userdocId.getDocSeries(), userdocId.getDocNbr());
            if (isUserdocExists) {
                List<String> messages = result.getMessages();
                updateUserdocOriginalExpected(document, userdocId, messages);
                updateUserdocSubmissionType(document, userdocId, messages);
            }
        }

        if (CollectionUtils.isEmpty(result.getMessages())) {
            result.setSuccessful(true);
        }
        return result;
    }

    @Override
    public CDocumentNotificationProcessResult notifyEditDocCorrespondents(Document document, IpasObjectType ipasObject) {
        CDocumentNotificationProcessResult result = new CDocumentNotificationProcessResult();
        Integer documentId = document.getDocId();
        String regUri = document.getRegUri();

        if (ipasObject == IpasObjectType.MARK || ipasObject == IpasObjectType.PATENT) {
            CFileId fileId = BasicUtils.createCFileId(regUri);
            if (Objects.isNull(fileId)) {
                throw new RuntimeException("Cannot create file id from regUri = " + regUri + " from document with id = " + documentId);
            }
            CReceptionRequest receptionRequest = receptionRequestService.selectReceptionByFileId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
            if (Objects.nonNull(receptionRequest) && isIpObjectReception(fileId, ipasObject)) {
                updateIpObjectCorrespondents(document, fileId, result.getMessages());
            }
        }

        if (CollectionUtils.isEmpty(result.getMessages())) {
            result.setSuccessful(true);
        }
        return result;
    }

    @Override
    public CDocumentNotificationProcessResult notifyFinished(Document document, IpasObjectType ipasObject) {
        Integer documentId = document.getDocId();
        switch (ipasObject) {
            case OFFIDOC:
                COffidocAbdocsDocument offidocAbdocsDocument = offidocAbdocsDocumentService.selectByAbdocsId(documentId);
                if (Objects.isNull(offidocAbdocsDocument)) {
                    throw new RuntimeException("Cannot find record with document id = " + documentId + " in EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT table");
                }

                if (document.getDocStatus().equals(DocStatus.Finished)) {
                    Date finishedDate = new Date();
                    offidocAbdocsDocumentService.updateNotificationFinishedDate(finishedDate, documentId);
                }
                break;
        }

        return new CDocumentNotificationProcessResult(true, null);
    }

    @Override
    public CDocumentNotificationProcessResult notifyDeRegistration(Document document, IpasObjectType ipasObject) {
        Integer documentId = document.getDocId();
        switch (ipasObject) {
            case OFFIDOC:
                COffidocAbdocsDocument offidocAbdocsDocument = offidocAbdocsDocumentService.selectByAbdocsId(documentId);
                if (Objects.isNull(offidocAbdocsDocument)) {
                    throw new RuntimeException("Cannot find record with document id = " + documentId + " in EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT table");
                }

                String regUri = document.getRegUri();
                if(StringUtils.isEmpty(regUri)){
                    offidocAbdocsDocumentService.updateRegistrationNumber(null, documentId);
                }

                break;
        }

        return new CDocumentNotificationProcessResult(true, null);
    }

    private void updateIpObjectSubmissionType(Document document, CFileId fileId, List<String> messages) {
        try {
            receptionRequestService.updateIpObjectSubmissionType(document.getDocSourceTypeId(), fileId);// Sync submission type
        } catch (Exception e) {
            log.error(e.getMessage());
            messages.add("Submission type is not updated !");
        }
    }

    private void updateUserdocSubmissionType(Document document, CDocumentId userdocId, List<String> messages) {
        try {
            receptionUserdocRequestService.updateUserdocSubmissionType(document.getDocSourceTypeId(), userdocId);// Sync submission type
        } catch (Exception e) {
            log.error(e.getMessage());
            messages.add("Submission type is not updated !");
        }
    }

    private void updateIpObjectOriginalExpected(Document document, CFileId fileId, List<String> messages) {
        try {// Sync original expected
            originalExpectedService.updateIpObjectOriginalExpectedOnAbdocsNotification(fileId, document);
        } catch (Exception e) {
            log.error(e.getMessage());
            messages.add("Original expected is not updated !");
        }
    }

    private void updateUserdocOriginalExpected(Document document, CDocumentId userdocId, List<String> messages) {
        try {// Sync original expected
            originalExpectedService.updateUserdocOriginalExpectedOnAbdocsNotification(userdocId, document);
        } catch (Exception e) {
            log.error(e.getMessage());
            messages.add("Original expected is not updated !");
        }
    }

    private void updateIpObjectCorrespondents(Document document, CFileId fileId, List<String> messages) {
        try {
            syncCorrespondents(document, fileId);// Sync correspondents
        } catch (Exception e) {
            log.error(e.getMessage());
            messages.add("Correspondents are not updated !");
        }
    }

    private void syncCorrespondents(Document document, CFileId fileId) {
        CReceptionRequest receptionRequest = receptionRequestService.selectReceptionByFileId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
        List<DocCorrespondents> docCorrespondents = document.getDocCorrespondents();
        if (!CollectionUtils.isEmpty(docCorrespondents)) {
            List<CReceptionCorrespondent> newCorrespondents = new ArrayList<>();
            for (DocCorrespondents docCorrespondent : docCorrespondents) {
                CReceptionCorrespondent convertedCorrespondent = correspondentConverter.convertToReceptionCorrespondent(docCorrespondent, receptionRequest.getId(), null);
                if (Objects.isNull(convertedCorrespondent.getPersonNbr()) && Objects.isNull(convertedCorrespondent.getAddressNbr())) {
                    CPerson cPerson = correspondentConverter.convertToCPerson(docCorrespondent);
                    CPerson result = personService.mergeOrInsertPersonAddress(cPerson);
                    if (Objects.nonNull(result)) {
                        Integer newPersonNbr = result.getPersonNbr();
                        Integer newAddressNbr = result.getAddressNbr();
                        if (Objects.nonNull(newPersonNbr) && Objects.nonNull(newAddressNbr)) {
                            convertedCorrespondent.setPersonNbr(newPersonNbr);
                            convertedCorrespondent.setAddressNbr(newAddressNbr);
                            newCorrespondents.add(convertedCorrespondent);

                            Correspondent correspondent = docCorrespondent.getCorrespondent();
                            correspondent.setExternalId(newPersonNbr);
                            CorrespondentContact correspondentContact = correspondent.getCorrespondentContacts().get(0);
                            correspondentContact.setExternalId(newAddressNbr);
                            abdocsServiceAdmin.updateCorrespondent(correspondent);
                        }
                    }
                } else {
                    newCorrespondents.add(convertedCorrespondent);
                }
            }
            receptionRequest.setCorrespondents(newCorrespondents);
            receptionRequestService.update(receptionRequest);
        }
    }

    private boolean isIpObjectReception(CFileId fileId, IpasObjectType ipasObject) {
        boolean isFileExists = fileService.isFileExist(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
        return isFileExists && !isMarkOrPatentExists(ipasObject, fileId);
    }

    private boolean isMarkOrPatentExists(IpasObjectType ipasObject, CFileId fileId) {
        switch (ipasObject) {
            case MARK:
                return markService.isMarkExists(fileId);
            case PATENT:
                return patentService.isPatentExists(fileId);
            default:
                throw new RuntimeException("Wrong ipas object type " + ipasObject.name());
        }
    }
}

