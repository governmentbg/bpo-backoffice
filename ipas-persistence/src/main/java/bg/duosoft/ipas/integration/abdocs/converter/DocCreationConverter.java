package bg.duosoft.ipas.integration.abdocs.converter;

import bg.duosoft.abdocs.model.*;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonData;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.model.userdoc.config.CUserdocTypeConfig;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.efiling.IpObjectEfilingDataService;
import bg.duosoft.ipas.core.service.efiling.UserdocEfilingDataService;
import bg.duosoft.ipas.core.service.ext.OffidocAbdocsDocumentService;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.offidoc.OffidocService;
import bg.duosoft.ipas.core.service.reception.AbdocsDocumentTypeService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.service.userdoc.config.UserdocTypeConfigService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.OffidocParentObjectType;
import bg.duosoft.ipas.enums.SubmissionType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.offidoc.OffidocUtils;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DocCreationConverter {

    private static final Integer NOTIFICATION_ABDOCS_DOCUMENT_TYPE = 69;
    private static final Integer CERTIFICATE_ABDOCS_DOCUMENT_TYPE = 70;
    private static final String OFFIDOC_ABDOCS_DOCUMENT_TYPE_OUT = "ИК";
    private static final String OFFIDOC_ABDOCS_DOCUMENT_TYPE_IN = "ВД";
    private static final String DEFAULT_ADDRESS_NUMBER = "1";

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private AbdocsDocumentTypeService abdocsDocumentTypeService;

    @Autowired
    private DocService docService;

    @Autowired
    private OffidocTypeService offidocTypeService;

    @Autowired
    private CorrespondentConverter correspondentConverter;

    @Autowired
    private OffidocAbdocsDocumentService offidocAbdocsDocumentService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private SearchApplicationService searchApplicationService;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private OffidocService offidocService;

    @Autowired
    private ConfigParamService configParamService;

    @Autowired
    private UserdocTypeConfigService userdocTypeConfigService;

    @Autowired
    private UserdocEfilingDataService userdocEfilingDataService;

    @Autowired
    private IpObjectEfilingDataService ipObjectEfilingDataService;

    public DocCreation createUserdocDocCreationWhenObjectIsMissing(CUserdoc userdoc, Integer submissionType, String relatedObject, boolean originalExpected, CRepresentationData representationData, COwnershipData ownershipData) {
        DocCreation docCreation = new DocCreation();
        docCreation.setRegistration(new RegistrationDto());
        docCreation.setDocSourceTypeId(submissionType);
        docCreation.getRegistration().setDocRegistrationType(DocRegistrationType.ByDocType);
        String docSubject = messageSource.getMessage("docflow.userdoc.docsubject", new Object[]{userdoc.getUserdocType().getUserdocType(), relatedObject}, LocaleContextHolder.getLocale());
        docCreation.setDocSubject(docSubject);
        docCreation.setDocTypeId(abdocsDocumentTypeService.selectAbdocsDocTypeIdByType(userdoc.getUserdocType().getUserdocType().concat(DefaultValue.IMARK_ABDOCS_DOCUMENT_TYPE_SUFFIX)));
        docCreation.setDocCaseLink(new DocCaseLinkDto());
        setReceivedOriginal(originalExpected, docCreation);
        docCreation.setCorrespondents(getDocumentCorrespondents(representationData, ownershipData));
        docCreation.getCorrespondents().stream().forEach(cor -> {
            cor.setCorrespondentContactId(null);
        });
        return docCreation;
    }


    public DocCreation createDocCreationForReception(CReception receptionForm) {
        DocCreation docCreation = new DocCreation();
        docCreation.setRegistration(new RegistrationDto());
        docCreation.setDocSourceTypeId(receptionForm.getSubmissionType());


        if (Objects.nonNull(receptionForm.getFile()) && Objects.nonNull(receptionForm.getFile().getFileId()) && Objects.nonNull(receptionForm.getFile().getFileId().getFileNbr())) {
            docCreation.getRegistration().setDocRegistrationType(DocRegistrationType.ExternalRegistrationNumber);
        } else {
            DocRegistrationType docRegistrationType = getDocumentRegistrationType(receptionForm);
            docCreation.getRegistration().setDocRegistrationType(docRegistrationType);
        }

        if (receptionForm.isUserdocRequest()) {
            docCreation.setParentDocId(getParentDocumentId(receptionForm.getUserdoc().getFileNumberOrDocumentNumber()));

            if (!StringUtils.isEmpty(receptionForm.getUserdoc().getExternalRegistrationNumber())) {
                docCreation.getRegistration().setDocRegistrationType(DocRegistrationType.ExternalRegistrationNumber);
                docCreation.getRegistration().setRegistrationNumber(receptionForm.getUserdoc().getExternalRegistrationNumber());
            }

        } else if (receptionForm.isFileRequest()) {
            CFileId fileId = receptionForm.getFile().getFileId();
            if (fileId == null && docCreation.getRegistration().getDocRegistrationType() == DocRegistrationType.ExternalRegistrationNumber) {
                throw new RuntimeException("fileId should not be empty!");
            }
            if (fileId != null) {
                docCreation.getRegistration().setRegistrationNumber(fileId.createFilingNumber());
            }
        } else {
            throw new RuntimeException("Unknown request type. It should be either file request or userdoc request!");
        }
        docCreation.setDocSubject(getDocSubject(receptionForm));

        docCreation.setDocTypeId(getDocumentType(receptionForm));
        docCreation.setCorrespondents(getDocumentCorrespondents(receptionForm));
        docCreation.setDocCaseLink(new DocCaseLinkDto());
        setReceivedOriginal(receptionForm.getOriginalExpected(), docCreation);
        setAdditionalDocUnitsUserNamesOnDocCreation(receptionForm, docCreation);

        Integer docflowEmailId = receptionForm.getDocflowEmailId();
        if (Objects.nonNull(docflowEmailId)) {
            docCreation.setIncomingEmailId(docflowEmailId);
        }
        return docCreation;
    }

    private void setAdditionalDocUnitsUserNamesOnDocCreation(CReception receptionForm, DocCreation docCreation) {
        if (Objects.nonNull(receptionForm.getSubmissionType()) && (SubmissionType.ELECTRONIC.code() != receptionForm.getSubmissionType())) {

            String userName = null;
            if (receptionForm.isUserdocRequest()) {
                CUserdocTypeConfig userdocTypeConfig = userdocTypeConfigService.findById(receptionForm.getUserdoc().getUserdocType());
                userName = userdocTypeConfig.getAbdocsUserTargetingOnRegistration();
            } else {
                CConfigParam userConfigParam = configParamService.selectExtConfigByCode(ConfigParamService.ABODCS_USER_TO_WHOM_DOCUMENT_IS_SEND_ON_REGISTRATION);
                if (Objects.nonNull(userConfigParam)) {
                    userName = userConfigParam.getValue();
                }
            }
            if (!StringUtils.isEmpty(userName)) {
                docCreation.setAdditionalDocUnitsUsernames(Collections.singletonList(userName));
            }

        }
    }

    public DocCreation convertOffidoc(String objectNumber, String subject, String offidocType, List<Attachments> attachments, CProcessParentData offidocParentData) {
        DocCreation docCreation = new DocCreation();
        docCreation.setDocSourceTypeId(SubmissionType.COUNTER.code());
        docCreation.setParentDocId(getParentDocumentId(objectNumber));
        docCreation.setReceivedOriginalState(ReceivedOriginalState.ReceivedOriginal);
        docCreation.setDocSubject(subject);
        docCreation.setDocCaseLink(new DocCaseLinkDto());
        docCreation.setDocStatusProcessed(false);
        docCreation.setCreateFromDocUnit(true);
        setOffidocDirection(offidocType, docCreation, objectNumber);
        if (DocDirection.Internal != docCreation.getDocDirection()) {
            docCreation.setCorrespondents(selectOffidocCorrespondents(objectNumber));
            fillDestionationType(offidocParentData, docCreation);
        }

        if (!CollectionUtils.isEmpty(attachments)) {
            docCreation.setIncomingEmailAttachments(attachments);
            docCreation.setDocFileVisibility(DocFileVisibility.PrivateAttachedFile);
        }

        return docCreation;
    }

    private void fillDestionationType(CProcessParentData offidocParentData, DocCreation docCreation) {
        CEFilingData efilingData = selectParentEfilingData(offidocParentData);
        if (Objects.nonNull(efilingData)) {
            String logUserName = efilingData.getLogUserName();
            if (StringUtils.hasText(logUserName) && !logUserName.trim().equals(DefaultValue.UNDEFINED_PORTAL_USER)) {
                List<DocCorrespondents> correspondents = docCreation.getCorrespondents();
                if (!CollectionUtils.isEmpty(correspondents)) {
                    for (DocCorrespondents correspondent : correspondents) {
                        correspondent.setDocDestinationTypeId(DestinationType.INTERNET_PORTAL.value());
                    }
                }
            }
        }
    }

    public DocCreation convertExpiringMarkDocument(String markFilingNumber, String subject, List<Attachments> attachments) {
        DocCreation docCreation = new DocCreation();
        docCreation.setDocSourceTypeId(SubmissionType.COUNTER.code());
        docCreation.setParentDocId(getParentDocumentId(markFilingNumber));
        docCreation.setReceivedOriginalState(ReceivedOriginalState.ReceivedOriginal);
        docCreation.setDocSubject(subject);
        docCreation.setDocCaseLink(new DocCaseLinkDto());
        docCreation.setCreateFromDocUnit(true);
        docCreation.setDocTypeId(NOTIFICATION_ABDOCS_DOCUMENT_TYPE);
        docCreation.setDocDirection(DocDirection.Outgoing);
        docCreation.setCorrespondents(selectOffidocCorrespondents(markFilingNumber));
        if (!CollectionUtils.isEmpty(attachments)) {
            docCreation.setIncomingEmailAttachments(attachments);
            docCreation.setDocFileVisibility(DocFileVisibility.PublicAttachedFile);
        }

        CConfigParam userConfigParam = configParamService.selectExtConfigByCode(ConfigParamService.EXPIRING_MARK_NOTIFICATION_ABDOCS_DOCUMENT_TO);
        if (Objects.nonNull(userConfigParam)) {
            String username = userConfigParam.getValue();
            if (!StringUtils.isEmpty(username)) {
                docCreation.setAdditionalDocUnitsUsernames(Collections.singletonList(username));
                docCreation.setExpectedResultAlias("Sign");
            }
        }
        return docCreation;
    }

    public DocCreation convertPatentCertificate(Document parentDoc, String subject, List<Attachments> attachments) {
        if (Objects.isNull(parentDoc))
            throw new RuntimeException("Parent document is empty ! " + subject);

        DocCreation docCreation = new DocCreation();
        docCreation.setDocSourceTypeId(SubmissionType.COUNTER.code());
        docCreation.setParentDocId(parentDoc.getDocId());
        docCreation.setReceivedOriginalState(ReceivedOriginalState.ReceivedOriginal);
        docCreation.setDocSubject(subject);
        docCreation.setDocCaseLink(new DocCaseLinkDto());
        docCreation.setCreateFromDocUnit(true);
        docCreation.setDocTypeId(CERTIFICATE_ABDOCS_DOCUMENT_TYPE);
        docCreation.setDocDirection(DocDirection.Outgoing);
        docCreation.setCorrespondents(parentDoc.getDocCorrespondents());
        if (!CollectionUtils.isEmpty(attachments))
            docCreation.setIncomingEmailAttachments(attachments);

        return docCreation;
    }

    private List<DocCorrespondents> selectOffidocCorrespondents(String objectNumber) {
        if (StringUtils.isEmpty(objectNumber))
            return null;

        if (OffidocUtils.isOffidoc(objectNumber)) {
            COffidoc offidoc = offidocService.findOffidoc(OffidocUtils.convertStringToOffidocId(objectNumber));
            if (Objects.nonNull(offidoc)) {
                CProcessParentData offidocParentData = offidoc.getOffidocParentData();
                if (Objects.nonNull(offidocParentData)) {
                    return selectOffidocCorrespondents(offidocParentData.selectConcatenatedIdentifier());
                }
            }
        } else if (UserdocUtils.isUserdoc(objectNumber)) {
            CDocumentId documentId = UserdocUtils.convertStringToDocumentId(objectNumber);
            if (Objects.nonNull(documentId)) {
                CUserdoc userdoc = userdocService.findUserdoc(documentId);
                if (Objects.nonNull(userdoc)) {
                    List<CPerson> correspondentPersons = fillOffidocCorrespondentsFromUserdoc(userdoc);
                    if (!CollectionUtils.isEmpty(correspondentPersons)) {
                        return convertPersonsToDocCorrespondents(correspondentPersons);
                    }
                }
            }
        } else {
            CFileId fileId = BasicUtils.createCFileId(objectNumber);
            if (Objects.nonNull(fileId)) {
                IpasApplicationSearchResult iapsApplication = searchApplicationService.selectApplication(BasicUtils.createCFileId(objectNumber));
                if (Objects.nonNull(iapsApplication)) {
                    List<CPerson> correspondentPersons = fillOffidocCorrespondentsFromIpObject(iapsApplication);
                    if (!CollectionUtils.isEmpty(correspondentPersons)) {
                        return convertPersonsToDocCorrespondents(correspondentPersons);
                    }
                }
            }
        }
        return null;
    }

    private List<DocCorrespondents> convertPersonsToDocCorrespondents(List<CPerson> correspondentPersons) {
        List<DocCorrespondents> result = new ArrayList<>();
        for (CPerson correspondentsPerson : correspondentPersons) {
            DocCorrespondents correspondent = correspondentConverter.convertPersonToDocCorrespondent(correspondentsPerson, DocCorrespondentType.WithoutType);
            if (Objects.nonNull(correspondent)) {
                result.add(correspondent);
            }
        }
        return CollectionUtils.isEmpty(result) ? null : result;
    }

    private List<CPerson> fillOffidocCorrespondentsFromUserdoc(CUserdoc userdoc) {
        CPerson servicePerson = userdoc.getServicePerson();
        if (Objects.nonNull(servicePerson)) {// Use service person as correspondent
            return Collections.singletonList(servicePerson);
        } else {
            CUserdocPersonData userdocPersonData = userdoc.getUserdocPersonData();
            if (Objects.nonNull(userdocPersonData)) {
                List<CUserdocPerson> representatives = UserdocPersonUtils.selectRepresentatives(userdocPersonData);
                if (!CollectionUtils.isEmpty(representatives)) {// Use representatives as correspondents
                    return representatives.stream()
                            .map(CUserdocPerson::getPerson)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
                List<CUserdocPerson> applicants = UserdocPersonUtils.selectApplicants(userdocPersonData);
                if (!CollectionUtils.isEmpty(applicants)) { // Use applicants as correspondents
                    return applicants.stream()
                            .map(CUserdocPerson::getPerson)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            }
        }
        return null;
    }

    private List<CPerson> fillOffidocCorrespondentsFromIpObject(IpasApplicationSearchResult ipobject) {
        CPerson servicePerson = ipobject.getServicePerson();
        if (Objects.nonNull(servicePerson)) {// Use service person as correspondent
            return Collections.singletonList(servicePerson);
        }
        List<CRepresentative> representatives = ipobject.getRepresentatives();
        if (!CollectionUtils.isEmpty(representatives)) {// Use representatives as correspondents
            return ipobject.getRepresentativePersons();
        }
        List<CPerson> owners = ipobject.getOwners();
        if (!CollectionUtils.isEmpty(owners)) {
            return owners;// Use applicants as correspondents
        }
        return null;
    }

    public DocCreation convertMissingDocumentInAbdocs(CReception receptionForm, String filingNumber) {
        DocCreation docCreation = new DocCreation();
        docCreation.setDocSourceTypeId(receptionForm.getSubmissionType());
        docCreation.setDocSubject(receptionForm.getFile().getTitle());
        docCreation.setDocCaseLink(new DocCaseLinkDto());

        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setDocRegistrationType(DocRegistrationType.ExternalRegistrationNumber);
        registrationDto.setRegistrationNumber(filingNumber);
        docCreation.setRegistration(registrationDto);

        CFileId cFileId = BasicUtils.createCFileId(filingNumber);
        if (Objects.isNull(cFileId))
            throw new RuntimeException("File id is empty ! " + filingNumber);

        if (cFileId.getFileType().equals(FileType.DIVISIONAL_MARK.code())) {
            Integer documentTypeId = abdocsDocumentTypeService.selectAbdocsDocTypeIdByType(FileType.MARK.code());// TODO This must be DIVISONAL_MARK but now this type doesn't exist in ABDOCS !
            if (Objects.isNull(documentTypeId))
                throw new RuntimeException("Cannot find abdocs document type !");

            docCreation.setDocTypeId(documentTypeId);
        } else {
            docCreation.setDocTypeId(getDocumentType(receptionForm));
        }

        List<DocCorrespondents> documentCorrespondents = getDocumentCorrespondents(receptionForm);
        docCreation.setCorrespondents(documentCorrespondents);

        setReceivedOriginal(receptionForm.getOriginalExpected(), docCreation);
        return docCreation;
    }

    public DocCreation convertMissingUserdocDocumentInAbdocs(CReception receptionForm) {
        DocCreation docCreation = new DocCreation();
        docCreation.setRegistration(new RegistrationDto());
        docCreation.setDocSourceTypeId(SubmissionType.COUNTER.code());
        if (StringUtils.isEmpty(receptionForm.getExternalSystemId())) {
            DocRegistrationType docRegistrationType = getDocumentRegistrationType(receptionForm);
            docCreation.getRegistration().setDocRegistrationType(docRegistrationType);
        } else {
            docCreation.getRegistration().setDocRegistrationType(DocRegistrationType.ExternalRegistrationNumber);
            docCreation.getRegistration().setRegistrationNumber(receptionForm.getExternalSystemId());
        }
        docCreation.setParentDocId(getParentDocumentId(receptionForm.getUserdoc().getFileNumberOrDocumentNumber()));
        docCreation.setDocTypeId(getDocumentType(receptionForm));
        docCreation.setDocSubject(receptionForm.getNotes());
        docCreation.setCorrespondents(getDocumentCorrespondents(receptionForm));
        docCreation.setDocCaseLink(new DocCaseLinkDto());
        setReceivedOriginal(receptionForm.getOriginalExpected(), docCreation);
        return docCreation;
    }

    private String getDocSubject(CReception receptionForm) {
        if (receptionForm.isFileRequest()) {
            if (receptionForm.getFile().isEmptyTitle()) {
                return (messageSource.getMessage("reception.without.verbal.element.const", null, LocaleContextHolder.getLocale()));
            } else {
                return (receptionForm.getFile().getTitle());
            }

        } else if (receptionForm.isUserdocRequest()) {
            String userdocType = userdocTypesService.selectUserdocTypeById(receptionForm.getUserdoc().getUserdocType()).getUserdocName();
            CReceptionUserdoc udr = receptionForm.getUserdoc();
            String relatedToObject = udr.isRelatedToFile() ? receptionForm.getUserdoc().getFileNumberOrDocumentNumber() : docService.selectExternalSystemId(udr.getDocumentId());

            //if related to IP Object  - userdocType related to BG/N/2000/123456
            //if related to userdoc - userdocType related to externalSystemId
            String docSubject = messageSource.getMessage("docflow.userdoc.docsubject", new Object[]{userdocType, relatedToObject}, LocaleContextHolder.getLocale());
            return docSubject;
        } else {
            throw new RuntimeException("Unknown request type. It should be either file request or userdoc request!");
        }
    }

    //Default Direction for all documents is DocDirection.Incoming
    private void setOffidocDirection(String offidocType, DocCreation docCreation, String objectNumber) {
        COffidocType cOffidocType = offidocTypeService.selectById(offidocType);
        if (Objects.nonNull(cOffidocType)) {
            String direction = cOffidocType.getDirection();
            if (StringUtils.isEmpty(direction) || OffidocService.OFFIDOC_DIRECTION_IN.equals(direction)) {
                if (StringUtils.isEmpty(direction)) {
                    log.error("Missing direction for offidoc type " + offidocType + " . Internal document is created in ABDOCS for object " + objectNumber);
                }
                docCreation.setDocTypeId(selectAbdocsDocumentType(OFFIDOC_ABDOCS_DOCUMENT_TYPE_IN));
                docCreation.setDocDirection(DocDirection.Internal);
            } else if (OffidocService.OFFIDOC_DIRECTION_OUT.equals(direction)) {
                docCreation.setDocTypeId(selectAbdocsDocumentType(OFFIDOC_ABDOCS_DOCUMENT_TYPE_OUT));
                docCreation.setDocDirection(DocDirection.Outgoing);
            } else
                throw new RuntimeException("Wrong office document direction: " + direction);
        } else
            throw new RuntimeException("Cannot find office document type in IPASPROD.CF_OFFIDOC_TYPE. Office document type: " + offidocType);
    }

    private Integer selectAbdocsDocumentType(String offidocAbdocsDocumentTypeOut) {
        Integer documentTypeId = abdocsDocumentTypeService.selectAbdocsDocTypeIdByType(offidocAbdocsDocumentTypeOut);
        if (Objects.isNull(documentTypeId)) {
            throw new RuntimeException("Cannot find abdocs document type " + offidocAbdocsDocumentTypeOut);
        }
        return documentTypeId;
    }

    private DocRegistrationType getDocumentRegistrationType(CReception receptionForm) {
        Integer docRegistrationTypeId = abdocsDocumentTypeService.getDocRegistrationType(_getType(receptionForm));
        if (docRegistrationTypeId == null) {
            throw new RuntimeException("Cannot find abdocs document registration type !");
        }
        Optional<DocRegistrationType> res = Arrays.stream(DocRegistrationType.values()).filter(r -> r.value() == docRegistrationTypeId).findFirst();
        if (res.isEmpty()) {
            throw new RuntimeException("Cannot find abdocs document registration type !");
        }
        return res.get();
    }

    private Integer getDocumentType(CReception receptionForm) {
        Integer documentTypeId = abdocsDocumentTypeService.selectAbdocsDocTypeIdByType(_getType(receptionForm));
        if (Objects.isNull(documentTypeId))
            throw new RuntimeException("Cannot find abdocs document type !");

        return documentTypeId;
    }

    private String _getType(CReception receptionForm) {
        String type;

        if (receptionForm.isUserdocRequest()) {
            type = receptionForm.getUserdoc().getUserdocType();
        } else if (receptionForm.isFileRequest()) {
            type = applicationTypeService.getFileTypeByApplicationType(receptionForm.getFile().getApplicationType());
        } else {
            throw new RuntimeException("Unknwon receptinForm type...");
        }
        return type;
    }

    private void setReceivedOriginal(boolean originalExpected, DocCreation docCreation) {
        if (originalExpected) {
            docCreation.setReceivedOriginalState(ReceivedOriginalState.WaitingForOriginal);
        } else {
            docCreation.setReceivedOriginalState(ReceivedOriginalState.ReceivedOriginal);
        }

    }

    public Integer getParentDocumentId(String objectNumber) {
        String abdocsRegistrationNumber;
        if (OffidocUtils.isOffidoc(objectNumber)) {
            COffidocId cOffidocId = OffidocUtils.convertStringToOffidocId(objectNumber);
            if (Objects.isNull(cOffidocId))
                throw new RuntimeException("Office document id is empty !");

            COffidocAbdocsDocument offidocAbdocsDocument = offidocAbdocsDocumentService.selectById(cOffidocId);
            if (Objects.isNull(offidocAbdocsDocument))
                throw new RuntimeException("Cannot fidn parent document id for office document: " + objectNumber);

            Integer abdocsDocId = offidocAbdocsDocument.getAbdocsDocumentId();
            if (Objects.nonNull(abdocsDocId)) {
                Document document = abdocsService.selectDocumentById(abdocsDocId);
                if (Objects.isNull(document)) {
                    throw new RuntimeException("Cannot find abdocs document with id = " + abdocsDocId);
                }
                return abdocsDocId;
            }

            abdocsRegistrationNumber = offidocAbdocsDocument.getRegistrationNumber();
        } else {
            String[] objectNumberParts = objectNumber.split(DefaultValue.IPAS_OBJECT_ID_SEPARATOR);
            String type = objectNumberParts[1];
            if (type.equals(FileType.USERDOC.code())) {
                CDocumentId cDocumentId = UserdocUtils.convertStringToDocumentId(objectNumber);
                if (Objects.isNull(cDocumentId))
                    throw new RuntimeException("Document id is empty !");

                abdocsRegistrationNumber = docService.selectExternalSystemId(cDocumentId.getDocOrigin(), cDocumentId.getDocLog(), cDocumentId.getDocSeries(), cDocumentId.getDocNbr());
            } else {
                CFileId cFileId = BasicUtils.createCFileId(objectNumber);
                abdocsRegistrationNumber = Objects.isNull(cFileId) ? null : cFileId.createFilingNumber();
            }
        }

        Integer parentDocId = abdocsService.selectDocumentIdByRegistrationNumber(abdocsRegistrationNumber);
        if (Objects.isNull(parentDocId))
            log.error("Cannot find abdocs document id for " + objectNumber + ". Registration number: " + abdocsRegistrationNumber);

        return parentDocId;
    }

    public String getParentRegistrationNumber(String objectNumber) {
        if (Objects.isNull(objectNumber)) {
            return null;
        }

        if (OffidocUtils.isOffidoc(objectNumber)) {
            COffidocId cOffidocId = OffidocUtils.convertStringToOffidocId(objectNumber);
            if (Objects.isNull(cOffidocId)) {
                log.error("Office document id is empty ! " + objectNumber);
                return null;
            }
            COffidocAbdocsDocument offidocAbdocsDocument = offidocAbdocsDocumentService.selectById(cOffidocId);
            if (Objects.isNull(offidocAbdocsDocument)) {
                log.error("Cannot find parent document id for office document: " + objectNumber);
                return null;
            }
            return offidocAbdocsDocument.getRegistrationNumber();
        } else if (UserdocUtils.isUserdoc(objectNumber)) {
            CDocumentId cDocumentId = UserdocUtils.convertStringToDocumentId(objectNumber);
            if (Objects.isNull(cDocumentId)) {
                return null;
            }
            return docService.selectExternalSystemId(cDocumentId.getDocOrigin(), cDocumentId.getDocLog(), cDocumentId.getDocSeries(), cDocumentId.getDocNbr());
        } else {
            CFileId cFileId = BasicUtils.createCFileId(objectNumber);
            return Objects.isNull(cFileId) ? null : cFileId.createFilingNumber();
        }
    }

    private List<DocCorrespondents> getDocumentCorrespondents(CReception receptionForm) {
        return getDocumentCorrespondents(receptionForm.getRepresentationData(), receptionForm.getOwnershipData());
    }

    private List<DocCorrespondents> getDocumentCorrespondents(CRepresentationData representationData, COwnershipData ownershipData) {
        List<DocCorrespondents> correspondents = new ArrayList<>();
        if (representationData != null) {
            correspondents.addAll(correspondentConverter.convertRepresentationDataToDocCorrespondent(representationData));
        }
        if (ownershipData != null) {
            correspondents.addAll(correspondentConverter.convertOwnershipDataToDocCorrespondent(ownershipData));
        }
        return correspondents;
    }

    public DocCreation createDocCreationObjectForUserdocChangePosition(Document document, Integer parentDocumentId, CUserdocType userdocType) {
        DocCreation docCreation = new DocCreation();
        docCreation.setRegistration(new RegistrationDto());
        docCreation.setDocSourceTypeId(document.getDocSourceTypeId());

        Integer docRegistrationTypeId = abdocsDocumentTypeService.getDocRegistrationType(userdocType.getUserdocType());
        Optional<DocRegistrationType> docRegistrationType = Arrays.stream(DocRegistrationType.values()).filter(r -> r.value() == docRegistrationTypeId).findFirst();
        if (docRegistrationType.isEmpty()) {
            throw new RuntimeException("Cannot find abdocs document registration type !");
        }

        docCreation.getRegistration().setDocRegistrationType(docRegistrationType.get());
        docCreation.setParentDocId(parentDocumentId);
        docCreation.setDocTypeId(document.getDocTypeId());
        docCreation.setDocSubject(document.getDocSubject());
        docCreation.setCorrespondents(document.getDocCorrespondents());
        docCreation.setDocCaseLink(new DocCaseLinkDto());
        docCreation.setReceivedOriginalState(document.getReceivedOriginalState());

        List<DocCorrespondents> correspondents = docCreation.getCorrespondents();
        if (!CollectionUtils.isEmpty(correspondents)) {
            for (DocCorrespondents correspondent : correspondents) {
                correspondent.setDocId(null);
                correspondent.setId(null);
                correspondent.setCorrespondentId(null);
                correspondent.getCorrespondent().setId(null);
                correspondent.getCorrespondent().setVersion(null);
                correspondent.setCorrespondentContactId(DEFAULT_ADDRESS_NUMBER);
                List<CorrespondentContact> correspondentContacts = correspondent.getCorrespondent().getCorrespondentContacts();
                if (!CollectionUtils.isEmpty(correspondentContacts)) {
                    for (CorrespondentContact correspondentContact : correspondentContacts) {
                        correspondentContact.setId(null);
                        correspondentContact.setCorrespondentId(null);
                    }
                }
            }
        }
        return docCreation;
    }

    private CEFilingData selectParentEfilingData(CProcessParentData offidocParentData) {
        Pair<String, String> pair = OffidocUtils.selectClosestMainParentObjectData(offidocParentData);
        if (Objects.nonNull(pair)) {
            String parentObjectId = pair.getFirst();
            OffidocParentObjectType parentObjectType = OffidocParentObjectType.valueOf(pair.getSecond());
            switch (parentObjectType) {
                case IPOBJECT:
                    return ipObjectEfilingDataService.selectById(BasicUtils.createCFileId(parentObjectId));
                case USERDOC:
                    return userdocEfilingDataService.selectById(BasicUtils.createCDocumentId(parentObjectId));
            }
        }
        return null;
    }

}
