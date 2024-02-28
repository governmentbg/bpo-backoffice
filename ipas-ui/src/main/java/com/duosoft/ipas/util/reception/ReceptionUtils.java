package com.duosoft.ipas.util.reception;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.reception.*;
import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeGroupService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.reception.*;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.ReceptionType;
import bg.duosoft.ipas.enums.ReceptionTypeConfig;
import bg.duosoft.ipas.enums.SubmissionType;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.eupatent.EuPatentUtils;
import bg.duosoft.ipas.util.reception.ReceptionCorrespondentUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.FigurativeMarkUtils;
import com.duosoft.ipas.util.ReceptionTypeUtils;
import com.duosoft.ipas.util.json.OriginalReceptionResult;
import com.duosoft.ipas.webmodel.ReceptionEuPatentForm;
import com.duosoft.ipas.webmodel.ReceptionForm;
import com.duosoft.ipas.webmodel.ReceptionUserdocForm;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReceptionUtils {

    public static CMark getReceptionMark(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr,
                                         MarkService markService, FileService fileService, ReceptionRequestService receptionRequestService,
                                         ReceptionInitializationService receptionInitializationService) {
        CMark receptionMark = markService.findMark(fileSeq, fileTyp, fileSer, fileNbr, false);
        if (Objects.nonNull(receptionMark))
            throw new RuntimeException("Invalid reception !");

        CFile receptionFile = selectReceptionFile(fileSeq, fileTyp, fileSer, fileNbr, fileService);
        CReceptionRequest cReceptionRequest = selectReceptionRequestRecord(fileSeq, fileTyp, fileSer, fileNbr, receptionRequestService);
        return receptionInitializationService.initMark(receptionFile, cReceptionRequest);
    }

    public static CPatent getReceptionPatent(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, PatentService patentService, FileService fileService,
                                             ReceptionRequestService receptionRequestService, ReceptionInitializationService receptionInitializationService) {
        CPatent receptionPatent = patentService.findPatent(fileSeq, fileTyp, fileSer, fileNbr, false);
        if (Objects.nonNull(receptionPatent))
            throw new RuntimeException("Invalid reception !");

        CFile receptionFile = selectReceptionFile(fileSeq, fileTyp, fileSer, fileNbr, fileService);
        CReceptionRequest receptionRequest = selectReceptionRequestRecord(fileSeq, fileTyp, fileSer, fileNbr, receptionRequestService);
        return receptionInitializationService.initPatent(receptionFile, receptionRequest);
    }

    public static OriginalReceptionResult searchOriginalExpectedReceptionRequest(ReceptionForm receptionForm, EbdPatentService ebdPatentService,
                                                                                 FileService fileService, ReceptionTypeService receptionTypeService,
                                                                                 ReceptionRequestService receptionRequestService,
                                                                                 ReceptionUserdocRequestService receptionUserdocRequestService,
                                                                                 MessageSource messageSource,
                                                                                 DocService docService,
                                                                                 HttpServletRequest request

    ) {

        List<CPerson> applicants = new ArrayList<>();
        COwnershipData ownershipData = receptionForm.getOwnershipData();
        if (Objects.nonNull(ownershipData) && !CollectionUtils.isEmpty(ownershipData.getOwnerList())) {
            applicants = ownershipData.getOwnerList()
                    .stream()
                    .map(COwner::getPerson)
                    .collect(Collectors.toList());
        }

        String name = receptionForm.getName();
        if (FigurativeMarkUtils.isFigurativeMark(receptionForm)) {
            name = DefaultValue.EMPTY_OBJECT_NAME;
        }
        if (ReceptionTypeUtils.isUserdoc(receptionForm)) {
            List<CPerson> representatives = selectCPersonRepresentatives(receptionForm);
            ReceptionUserdocForm userdoc = receptionForm.getUserdoc();
            OriginalReceptionResult originalReception = getOriginalExpectedUserdocReceptionRequest(applicants, representatives, userdoc.getObjectNumber(), userdoc.getUserdocType(), receptionUserdocRequestService, messageSource, receptionForm.getSubmissionType(), request, docService);
            if (Objects.nonNull(originalReception))
                return originalReception;

        } else if (ReceptionTypeUtils.isEuPatent(receptionForm)) {
            ReceptionEuPatentForm euPatent = receptionForm.getEuPatent();
            CEbdPatent cEbdPatent = ebdPatentService.selectByFileNumber(euPatent.getObjectNumber());
            CFileId euPatentFileId = EuPatentUtils.generateEUPatentCFileId(cEbdPatent);
            boolean isEuPatentExistInIpas = fileService.isFileExist(euPatentFileId.getFileSeq(), euPatentFileId.getFileType(), euPatentFileId.getFileSeries(), euPatentFileId.getFileNbr());
            if (isEuPatentExistInIpas) {
                List<CPerson> representatives = selectCPersonRepresentatives(receptionForm);
                OriginalReceptionResult originalReception = getOriginalExpectedUserdocReceptionRequest(applicants, representatives, euPatentFileId.createFilingNumber(), euPatent.getType(), receptionUserdocRequestService, messageSource, receptionForm.getSubmissionType(), request, docService);
                if (Objects.nonNull(originalReception))
                    return originalReception;
            }
        } else {
            CReceptionType cReceptionType = receptionTypeService.selectById(receptionForm.getReceptionType());
            List<CReceptionRequest> cReceptionRequests = receptionRequestService.selectOriginalExpectedByNameAndFileType(name, cReceptionType.getFileType());
            if (!CollectionUtils.isEmpty(cReceptionRequests)) {
                CReceptionRequest match = ReceptionCorrespondentUtils.selectReceptionRequestWithMatchedApplicants(applicants, cReceptionRequests);
                if (Objects.nonNull(match)) {
                    CFileId cFileId = new CFileId(match.getFileSeq(), match.getFileType(), match.getFileSer(), match.getFileNbr());
                    String filingDate = DateUtils.formatDate(match.getFilingDate());
                    return generateOriginalReceptionResult(receptionForm.getSubmissionType(), messageSource, match.getId(), cFileId.createFilingNumber(), filingDate, request, docService);
                }
            }
        }

        return OriginalReceptionResult.notOriginalReceptionRequest();
    }

    private static List<CPerson> selectCPersonRepresentatives(ReceptionForm receptionForm) {
        List<CPerson> representatives = new ArrayList<>();
        CRepresentationData representationData = receptionForm.getRepresentationData();
        if (Objects.nonNull(representationData) && !CollectionUtils.isEmpty(representationData.getRepresentativeList())) {
            representatives = representationData.getRepresentativeList()
                    .stream()
                    .map(CRepresentative::getPerson)
                    .collect(Collectors.toList());
        }
        return representatives;
    }


    private static OriginalReceptionResult getOriginalExpectedUserdocReceptionRequest(List<CPerson> applicants, List<CPerson> representatives, String relatedObjectNumber, String userdocType, ReceptionUserdocRequestService receptionUserdocRequestService, MessageSource messageSource, Integer submissionType, HttpServletRequest request, DocService docService) {
        List<CReceptionUserdocRequest> receptionUserdocRequests = receptionUserdocRequestService.selectOriginalExpectedUserdoc(relatedObjectNumber, userdocType);
        if (!CollectionUtils.isEmpty(receptionUserdocRequests)) {
            List<CReceptionUserdocRequest> matchList = ReceptionCorrespondentUtils.selectUserdocReceptionRequestWithMatchedPersons(applicants, representatives, receptionUserdocRequests);
            if (!CollectionUtils.isEmpty(matchList)) {
                CReceptionUserdocRequest match = matchList.get(DefaultValue.FIRST_RESULT);
                String filingDate = DateUtils.formatDate(match.getFilingDate());
                CDocumentId cDocumentId = new CDocumentId(match.getDocOri(), match.getDocLog(), match.getDocSer(), match.getDocNbr());
                return generateOriginalReceptionResult(submissionType, messageSource, match.getId(), UserdocUtils.convertDocumentIdToString(cDocumentId), filingDate, request, docService);
            }
        }
        return null;
    }

    public static void addReceptionPanelBasicModelAttributes(Model model, ReceptionForm receptionForm, String receptionSessionKey,
                                                             SubmissionTypeService submissionTypeService, ReceptionTypeService receptionTypeService, DailyLogService dailyLogService,
                                                             FileTypeGroupService fileTypeGroupService) {
        model.addAttribute("submissionTypes", getReceptionSubmissionTypes(submissionTypeService));
        model.addAttribute("receptionTypes", receptionTypeService.selectAllDependingOnConfiguration(getConfigsOnAddReceptionPanelBasicModelAttributes(receptionForm)));
        model.addAttribute("workingDate", dailyLogService.getWorkingDate());
        model.addAttribute("receptionForm", receptionForm);
        model.addAttribute("receptionSessionKey", receptionSessionKey);
        model.addAttribute("userdocFileTypeGroups", fileTypeGroupService.getUserdocFileTypesGroupNamesMap());
    }

    private static List<ReceptionTypeConfig> getConfigsOnAddReceptionPanelBasicModelAttributes(ReceptionForm receptionForm) {
        List<ReceptionTypeConfig> configList = new ArrayList<>();
        if (Objects.isNull(receptionForm) || Objects.isNull(receptionForm.getReceptionType())) {
            configList.add(ReceptionTypeConfig.RECEPTION_ON_COUNTER);
            return configList;
        }

        if (Objects.nonNull(receptionForm.getInitialDocument())) {
            configList.add(ReceptionTypeConfig.RECEPTION_FROM_EXISTING_DOCUMENT);
            return configList;
        }

        configList.add(ReceptionTypeConfig.RECEPTION_ON_COUNTER);
        return configList;
    }


    private static List<CSubmissionType> getReceptionSubmissionTypes(SubmissionTypeService submissionTypeService) {
        List<CSubmissionType> cSubmissionTypes = submissionTypeService.selectAll();
        cSubmissionTypes.removeIf(cSubmissionType -> cSubmissionType.getId() == SubmissionType.ELECTRONIC.code());
        cSubmissionTypes.removeIf(cSubmissionType -> cSubmissionType.getId() == SubmissionType.BACKOFFICE_SYSTEM.code());
        cSubmissionTypes.removeIf(cSubmissionType -> cSubmissionType.getId() == SubmissionType.COMMUNICATOR.code());
        cSubmissionTypes.removeIf(cSubmissionType -> cSubmissionType.getId() == SubmissionType.IMPORT.code());
        cSubmissionTypes.removeIf(cSubmissionType -> cSubmissionType.getId() == SubmissionType.DIVIDED.code());
        cSubmissionTypes.sort(Comparator.comparing(CSubmissionType::getId));
        return cSubmissionTypes;
    }

    public static boolean getOriginalExpectedDefaultValue(Integer submissionType) {
        if (Objects.isNull(submissionType))
            return true;

        SubmissionType submissionTypeEnum = SubmissionType.selectByCode(submissionType);
        switch (submissionTypeEnum) {
            case COUNTER:
            case MAIL:
            case COURIER:
                return false;
            default:
                return true;
        }
    }

    private static CReceptionRequest selectReceptionRequestRecord(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, ReceptionRequestService receptionRequestService) {
        CReceptionRequest cReceptionRequest = receptionRequestService.selectReceptionByFileId(fileSeq, fileTyp, fileSer, fileNbr);
        if (Objects.isNull(cReceptionRequest))
            throw new RuntimeException("Missing record in EXT_RECEPTION.RECEPTION_REQUEST ! ID: " + CoreUtils.createFilingNumber(fileSeq, fileTyp, fileSer, fileNbr, false));
        return cReceptionRequest;
    }

    private static CFile selectReceptionFile(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, FileService fileService) {
        CFile receptionFile = fileService.findById(fileSeq, fileTyp, fileSer, fileNbr);
        if (Objects.isNull(receptionFile))
            throw new RuntimeException("IP_FILE record is missing for reception !");
        return receptionFile;
    }

    private static OriginalReceptionResult generateOriginalReceptionResult(Integer submissionTypeInt, MessageSource messageSource, Integer receptionRequestId, String filingNumber, String filingDate, HttpServletRequest request, DocService docService) {
        boolean isUserdoc = UserdocUtils.isUserdoc(filingNumber);
        String regNumber = filingNumber;
        if (isUserdoc) {
            CDocumentId cDocumentId = UserdocUtils.convertStringToDocumentId(filingNumber);
            if (Objects.nonNull(cDocumentId)) {
                regNumber = docService.selectExternalSystemId(cDocumentId);
            }
        }

        SubmissionType submissionType = SubmissionType.selectByCode(submissionTypeInt);
        switch (submissionType) {
            case FAX:
            case EMAIL: {
                String message = messageSource.getMessage("reception.found.original.fax.mail", new String[]{filingNumber, filingDate, request.getContextPath(), regNumber}, LocaleContextHolder.getLocale());
                return new OriginalReceptionResult(true, receptionRequestId, message, true);
            }
            default: {
                String message = messageSource.getMessage("reception.found.original", new String[]{filingNumber, filingDate, request.getContextPath(), regNumber}, LocaleContextHolder.getLocale());
                return new OriginalReceptionResult(true, receptionRequestId, message, false);
            }
        }
    }

}
