package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.abdocs.model.DocCreation;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.document.CDocument;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionFile;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.MissingAbdocsDocumentService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.integration.abdocs.converter.DocCreationConverter;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.mark.InternationalMarkUtils;
import bg.duosoft.ipas.util.offidoc.OffidocUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MissingAbdocsDocumentServiceImpl implements MissingAbdocsDocumentService {

    private static final LocalDate ACSTRE_STARTING_IMPLEMENTATION_DATE = LocalDate.of(2014, Month.JUNE, 15);
    private static final LocalDate ACSTRE_DATE_FROM = LocalDate.of(2016, Month.APRIL, 1);
    private static final LocalDate ACSTRE_DATE_TO = LocalDate.of(2020, Month.OCTOBER, 9);

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private SearchApplicationService searchApplicationService;

    @Autowired
    private DocCreationConverter docCreationConverter;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private DocService docService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProcessService processService;

    private static final String UNKNOWN_DOCUMENT_SUBJECT = "...";

    public Map<String, String> insertMissingDocument(String objectNumber) {
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isEmpty(objectNumber)) {
            if (OffidocUtils.isOffidoc(objectNumber)) {
                return null;
            } else if (UserdocUtils.isUserdoc(objectNumber)) {
                processMissingUserdoc(UserdocUtils.convertStringToDocumentId(objectNumber), map);
            } else {
                processMissingMainObject(Objects.requireNonNull(BasicUtils.createCFileId(objectNumber)), map);
            }
        }
        return map.isEmpty() ? null : map;
    }

    private void processMissingMainObject(CFileId cFileId, Map<String, String> resultMap) {
        String filingNumber = cFileId.createFilingNumber();
        Integer abdocsId = abdocsServiceAdmin.selectDocumentIdByRegistrationNumber(cFileId.createFilingNumber());
        if (Objects.isNull(abdocsId)) {
            IpasApplicationSearchResult ipasApplicationSearchResult = searchApplicationService.selectApplication(cFileId);
            if (Objects.nonNull(ipasApplicationSearchResult)) {
                Document document = saveMissingObjectInAbdocs(filingNumber, ipasApplicationSearchResult);
                if (Objects.nonNull(document)) {
                    resultMap.put(ipasApplicationSearchResult.getFileId().createFilingNumber(), document.getRegUri());
                }
            }
        }
    }

    private void processMissingUserdoc(CDocumentId cDocumentId, Map<String, String> resultMap) {
        if (Objects.nonNull(cDocumentId)) {
            Integer abdocsId = selectAbdocsId(cDocumentId);
            if (Objects.isNull(abdocsId)) {
                boolean isUpperProcessUserdoc = processService.isUpperProcessUserdoc(cDocumentId);
                if (isUpperProcessUserdoc) {
                    String errorMessage = "Cannot insert missing documents for document with id = " + cDocumentId.toString() + " because hierarchy deep level is higher than 1 !";
                    log.error(errorMessage);
                    String actionTitle = messageSource.getMessage("error.action.add.missing.doc.abdocs", null, LocaleContextHolder.getLocale());
                    String customMessage = messageSource.getMessage("error.abdocs.missing.userdoc.hierarchy", new String[]{String.valueOf(cDocumentId)}, LocaleContextHolder.getLocale());
                    String instruction = messageSource.getMessage("instruction.abdocs.missing.userdoc.hierarchy", null, LocaleContextHolder.getLocale());
                    errorLogService.createNewRecord(ErrorLogAbout.ABDOCS, actionTitle, errorMessage, customMessage, true, instruction, ErrorLogPriority.MEDIUM);
                } else {
                    CFileId mainObjectOfUserdoc = userdocService.selectMainObjectIdOfUserdoc(cDocumentId);
                    if (Objects.isNull(mainObjectOfUserdoc))
                        log.error("Cannot find main object of selected user document " + cDocumentId.toString());
                    else {
                        IpasApplicationSearchResult mainIpasObject = searchApplicationService.selectApplication(mainObjectOfUserdoc);
                        if (Objects.nonNull(mainIpasObject)) {
                            String mainObjectFilingNumber = mainObjectOfUserdoc.createFilingNumber();
                            Integer mainObjectAbdocsId = abdocsServiceAdmin.selectDocumentIdByRegistrationNumber(mainObjectFilingNumber);
                            if (Objects.isNull(mainObjectAbdocsId)) {
                                Document mainObjectDocument = saveMissingObjectInAbdocs(mainObjectFilingNumber, mainIpasObject);
                                if (Objects.nonNull(mainObjectDocument)) {
                                    resultMap.put(mainIpasObject.getFileId().createFilingNumber(), mainObjectDocument.getRegUri());
                                }
                            }

                            Document document = saveMissingUserdocInAbdocs(cDocumentId, mainIpasObject, mainObjectOfUserdoc);
                            if (Objects.nonNull(document)) {
                                resultMap.put(cDocumentId.createFilingNumber(), document.getRegUri());
                            }
                        }
                    }
                }
            }
        }
    }

    private Document saveMissingUserdocInAbdocs(CDocumentId cDocumentId, IpasApplicationSearchResult mainIpasObject, CFileId mainObjectFileId) {
        Document document = null;
        CDocument cDocument = docService.selectDocument(cDocumentId);
        if (Objects.nonNull(cDocument)) {
            CReception missingUserdocForm = new CReception();
            missingUserdocForm.setUserdoc(new CReceptionUserdoc());

            Date filingDate = cDocument.getFilingDate();
            LocalDate filingDateAsLocalDate = DateUtils.dateToLocalDate(filingDate);

            String externalSystemId = cDocument.getExternalSystemId();
            if (!StringUtils.isEmpty(externalSystemId)) {
                if (isInAcstrePeriod(filingDateAsLocalDate) || isInPeriodBeforeAcstre(filingDateAsLocalDate)) {
                    missingUserdocForm.setExternalSystemId(externalSystemId);
                }
            } else if (isInAcstreImplementationPeriod(filingDateAsLocalDate)) {
                CDocumentSeqId documentSeqId = cDocument.getDocumentSeqId();
                if (Objects.nonNull(documentSeqId)) {
                    Integer docSeqNbr = documentSeqId.getDocSeqNbr();
                    Integer docSeqSeries = documentSeqId.getDocSeqSeries();
                    if (Objects.nonNull(docSeqNbr) && Objects.nonNull(docSeqSeries)) {
                        String regNumber = BasicUtils.generateRegistrationNumberBasedOnDocSeqId(cDocumentId, documentSeqId);
                        Integer integer = abdocsServiceAdmin.selectDocumentIdByRegistrationNumber(regNumber);
                        if (Objects.isNull(integer)) {
                            missingUserdocForm.setExternalSystemId(regNumber);
                        } else {
                            log.error("Cannot insert document with registration number {}. There is document with the same number! ABDOCS ID: {}", docSeqNbr, integer);
                            return null;
                        }
                    }
                }
            }

            missingUserdocForm.setNotes(cDocument.getCUserdocType().getUserdocName());
            missingUserdocForm.setSubmissionType(SubmissionType.COUNTER.code());
            missingUserdocForm.setOriginalExpected(false);
            missingUserdocForm.setEntryDate(filingDate);

            fillOwnersOfMissionDocument(mainIpasObject, missingUserdocForm);
            fillRepresentativesOfMissionDocument(mainIpasObject, missingUserdocForm);

            missingUserdocForm.getUserdoc().setUserdocType(cDocument.getCUserdocType().getUserdocType());
            missingUserdocForm.getUserdoc().setFileId(mainObjectFileId);

            DocCreation docCreation = docCreationConverter.convertMissingUserdocDocumentInAbdocs(missingUserdocForm);
            document = abdocsServiceAdmin.registerDocument(docCreation, missingUserdocForm.getEntryDate());
            if (Objects.nonNull(document)) {
                String existingExternalSystemId = docService.selectExternalSystemId(cDocument.getDocumentId().getDocOrigin(), cDocument.getDocumentId().getDocLog(), cDocument.getDocumentId().getDocSeries(), cDocument.getDocumentId().getDocNbr());
                if (!StringUtils.isEmpty(existingExternalSystemId)) {
                    log.error("Existing external id is overridden ! Old ID = " + existingExternalSystemId + ", New ID = " + document.getRegUri());
                    //TODO Save in new exception table
                }

                docService.updateExternalSystemId(document.getRegUri(), cDocument.getDocumentId().getDocOrigin(), cDocument.getDocumentId().getDocLog(), cDocument.getDocumentId().getDocSeries(), cDocument.getDocumentId().getDocNbr());
            }
        }
        return document;
    }

    private boolean isInAcstrePeriod(LocalDate localDate) {
        return localDate.isAfter(ACSTRE_DATE_FROM) && localDate.isBefore(ACSTRE_DATE_TO);
    }

    private boolean isInPeriodBeforeAcstre(LocalDate localDate) {
        return localDate.isBefore(ACSTRE_DATE_FROM);
    }

    private boolean isInAcstreImplementationPeriod(LocalDate localDate) {
        return localDate.isAfter(ACSTRE_STARTING_IMPLEMENTATION_DATE) && localDate.isBefore(ACSTRE_DATE_FROM);
    }

    private Document saveMissingObjectInAbdocs(String filingNumber, IpasApplicationSearchResult ipasApplicationSearchResult) {
        CReception missingDocumentReception = new CReception();

        if (InternationalMarkUtils.isInternationalMark(ipasApplicationSearchResult.getFileId())) {
            missingDocumentReception.setSubmissionType(SubmissionType.IMPORT.code());
        } else {
            missingDocumentReception.setSubmissionType(SubmissionType.COUNTER.code());
        }

        FileType fileType = FileType.selectByCode(ipasApplicationSearchResult.getFileId().getFileType());
        if (fileType != FileType.USERDOC) {
            missingDocumentReception.setFile(new CReceptionFile());
            if (Objects.nonNull(ipasApplicationSearchResult.getFileId())) {
                String applicationType = fileService.selectApplicationTypeByFileId(ipasApplicationSearchResult.getFileId());
                missingDocumentReception.getFile().setApplicationType(applicationType);
            }

            String description = ipasApplicationSearchResult.getDescription();
            if (Objects.nonNull(description)) {
                missingDocumentReception.getFile().setTitle(description);
            } else if (ipasApplicationSearchResult.isFigurative()) {
                missingDocumentReception.getFile().setTitle(messageSource.getMessage("reception.figurative.mark.const", null, LocaleContextHolder.getLocale()));
            } else {
                missingDocumentReception.getFile().setTitle(UNKNOWN_DOCUMENT_SUBJECT);
            }
        } else {
            throw new RuntimeException("Should not come here!!!!");
        }

        missingDocumentReception.setOriginalExpected(false);
        missingDocumentReception.setEntryDate(ipasApplicationSearchResult.getFilingDate());

        fillOwnersOfMissionDocument(ipasApplicationSearchResult, missingDocumentReception);
        fillRepresentativesOfMissionDocument(ipasApplicationSearchResult, missingDocumentReception);

        DocCreation docCreation = docCreationConverter.convertMissingDocumentInAbdocs(missingDocumentReception, filingNumber);
        Document document = abdocsServiceAdmin.registerDocument(docCreation, missingDocumentReception.getEntryDate());
        if (Objects.isNull(document))
            throw new RuntimeException("Cannot register missing abdocs document! Registration number: " + filingNumber);

        return document;
    }

    private void fillOwnersOfMissionDocument(IpasApplicationSearchResult ipasApplicationSearchResult, CReception missingDocumentReception) {
        COwnershipData ownershipData = new COwnershipData();
        List<CPerson> ipasOwners = ipasApplicationSearchResult.getOwners();
        if (!CollectionUtils.isEmpty(ipasOwners)) {
            List<COwner> ownerList = ipasOwners.stream()
                    .map(cPerson -> {
                        COwner cOwner = new COwner();
                        cOwner.setPerson(cPerson);
                        return cOwner;
                    })
                    .collect(Collectors.toList());
            ownershipData.setOwnerList(ownerList);
        }
        missingDocumentReception.setOwnershipData(ownershipData);
    }

    private void fillRepresentativesOfMissionDocument(IpasApplicationSearchResult ipasApplicationSearchResult, CReception missingDocumentReception) {
        CRepresentationData cRepresentationData = new CRepresentationData();
        List<CRepresentative> representatives = ipasApplicationSearchResult.getRepresentatives();
        if (!CollectionUtils.isEmpty(representatives)) {
            cRepresentationData.setRepresentativeList(representatives);
        }
        missingDocumentReception.setRepresentationData(cRepresentationData);
    }

    private Integer selectAbdocsId(CDocumentId cDocumentId) {
        Integer abdocsId = null;
        String externalSystemId = docService.selectExternalSystemId(cDocumentId.getDocOrigin(), cDocumentId.getDocLog(), cDocumentId.getDocSeries(), cDocumentId.getDocNbr());
        if (!StringUtils.isEmpty(externalSystemId)) {
            abdocsId = abdocsServiceAdmin.selectDocumentIdByRegistrationNumber(externalSystemId);
            log.debug("Selected parent user document exist in abdocs. Abdocs id =  " + abdocsId + ". Ipas id = " + cDocumentId.toString());
        }
        return abdocsId;
    }
}
