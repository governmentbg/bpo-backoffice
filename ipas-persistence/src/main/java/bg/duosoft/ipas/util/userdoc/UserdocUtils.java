package bg.duosoft.ipas.util.userdoc;

import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CUserdocSingleDesign;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.*;
import bg.duosoft.ipas.core.model.userdoc.config.CInternationalUserdocTypeConfig;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPanelService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.service.userdoc.config.InternationalUserdocTypeConfigService;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.file.FileTypeUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static bg.duosoft.ipas.core.service.impl.action.InsertActionServiceImpl.TRIGGER_ACTIVITY_CODE_USERDOC;
import static bg.duosoft.ipas.core.service.nomenclature.ConfigParamService.*;

public class UserdocUtils {

    public static boolean isRecordal(List<CUserdocPanel> userdocPanels) {
        CUserdocPanel recordalPanel = selectRecordalPanel(userdocPanels);
        return Objects.nonNull(recordalPanel);
    }

    public static boolean isRecordalInvalidation(List<String> invalidatedUserdocTypes) {
        return !CollectionUtils.isEmpty(invalidatedUserdocTypes);
    }

    public static CUserdocPanel selectRecordalPanel(List<CUserdocPanel> userdocPanels) {
        if (CollectionUtils.isEmpty(userdocPanels))
            return null;

        List<CUserdocPanel> recordalPanels = userdocPanels.stream()
                .filter(CUserdocPanel::getIndRecordal)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(recordalPanels))
            return null;

        if (recordalPanels.size() > 1)
            throw new RuntimeException("Recordal panel for userdoc type must be only one !");

        if (recordalPanels.size() == 1)
            return recordalPanels.get(0);

        return null;
    }

    public static CDocumentId convertStringToDocumentId(String concatenatedId) {
        if (StringUtils.isEmpty(concatenatedId))
            return null;

        String[] split = concatenatedId.split("/");
        if (split.length != 4)
            throw new RuntimeException("Wrong document id !");

        CDocumentId cDocumentId = new CDocumentId();
        cDocumentId.setDocOrigin(split[0]);
        cDocumentId.setDocLog(split[1]);
        cDocumentId.setDocSeries(Integer.valueOf(split[2]));
        cDocumentId.setDocNbr(Integer.valueOf(split[3]));
        return cDocumentId;
    }

    public static String convertDocumentIdToString(CDocumentId id) {
        return id.getDocOrigin() + "/" + id.getDocLog() + "/" + id.getDocSeries() + "/" + id.getDocNbr();
    }

    public static CFileId selectUserdocMainObject(CProcessParentData userdocParentData) {
        return Objects.nonNull(userdocParentData.getFileId()) ? userdocParentData.getFileId() : selectUserdocMainObject(userdocParentData.getParent());
    }

    public static boolean isUserdocMainObjectMarkOrDesign(CProcessParentData userdocParentData) {
        return isUserdocMainObjectMark(userdocParentData) || isUserdocMainObjectDesign(userdocParentData);
    }

    public static boolean isUserdocMainObjectDesign(CProcessParentData userdocParentData) {
        return checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.DESIGN);
    }

    public static boolean isUserdocMainObjectMark(CProcessParentData userdocParentData) {
        return checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.MARK) || checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.DIVISIONAL_MARK)
                || checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.INTERNATIONAL_MARK_I) || checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.INTERNATIONAL_MARK_R)
                || checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.INTERNATIONAL_MARK_B);
    }

    public static boolean isUserdocMainObjectInternationalMark(CProcessParentData userdocParentData) {
        return checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.INTERNATIONAL_MARK_I)
                || checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.INTERNATIONAL_MARK_R)
                || checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.INTERNATIONAL_MARK_B) ;
    }

    public static boolean isUserdocMainObjectPatent(CProcessParentData userdocParentData) {
        return checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.PATENT);
    }

    public static boolean isUserdocMainObjectPlant(CProcessParentData userdocParentData) {
        return checkIfUserdocMainObjectIsFromSelectedType(userdocParentData, FileType.PLANTS_AND_BREEDS);
    }

    private static boolean checkIfUserdocMainObjectIsFromSelectedType(CProcessParentData userdocParentData, FileType fileType) {
        CFileId cFileId = selectUserdocMainObject(userdocParentData);
        return FileType.selectByCode(cFileId.getFileType()) == fileType;
    }

    public static CFile selectUserdocMainObjectFile(CUserdoc userdoc, MarkService markService, PatentService patentService) {
        if (Objects.isNull(userdoc))
            return null;

        CFileId fileId = selectUserdocMainObject(userdoc.getUserdocParentData());
        if (Objects.isNull(fileId))
            return null;

        CFile cFile = null;
        if (FileTypeUtils.isMarkFileType(fileId.getFileType())) {
            CMark mark = markService.findMark(fileId, false);
            if (Objects.isNull(mark))
                return null;

            cFile = mark.getFile();
        } else if (FileTypeUtils.isPatentFileType(fileId.getFileType())) {
            CPatent patent = patentService.findPatent(fileId, false);
            if (Objects.isNull(patent))
                return null;

            cFile = patent.getFile();
        }
        return cFile;
    }

    public static boolean isStatusTriggerActivity(String statusCode, String processType, StatusService statusService) {
        if (!StringUtils.isEmpty(statusCode)) {
            CStatus cStatus = statusService.findById(processType, statusCode);
            if (Objects.isNull(cStatus))
                throw new RuntimeException("Cannot find status with code " + statusCode);

            return isStatusTriggerActivity(cStatus);
        }
        return false;
    }

    public static boolean isStatusTriggerActivity(CStatus cStatus) {
        if (Objects.isNull(cStatus))
            throw new RuntimeException("CStatus object is empty !");

        Integer triggerActivityWcode = cStatus.getTriggerActivityWcode();
        return Objects.nonNull(triggerActivityWcode) && TRIGGER_ACTIVITY_CODE_USERDOC.equals(triggerActivityWcode);
    }

    public static boolean isStatusTriggerActivityForRecordalRegistration(String statusCode, CProcess process, StatusService statusService, UserdocTypesService userdocTypesService) {
        CDocumentId documentId = process.getProcessOriginData().getDocumentId();
        if (Objects.nonNull(documentId) && Objects.nonNull(documentId.getDocNbr())) {
            String processType = process.getProcessId().getProcessType();
            if (isStatusTriggerActivity(statusCode, processType, statusService)) {
                CUserdocType cUserdocType = selectUserdocTypeFromDatabase(process, userdocTypesService);
                return UserdocUtils.isRecordal(cUserdocType.getPanels());
            }
        }

        return false;
    }

    public static boolean isStatusTriggerActivityForRecordalInvalidation(String statusCode, CProcess process, StatusService statusService, UserdocTypesService userdocTypesService) {
        CDocumentId documentId = process.getProcessOriginData().getDocumentId();
        if (Objects.nonNull(documentId) && Objects.nonNull(documentId.getDocNbr())) {
            String processType = process.getProcessId().getProcessType();
            if (isStatusTriggerActivity(statusCode, processType, statusService)) {
                CUserdocType cUserdocType = selectUserdocTypeFromDatabase(process, userdocTypesService);
                return isRecordalInvalidation(cUserdocType.getInvalidatedUserdocTypes());
            }
        }

        return false;
    }

    public static boolean isInvalidationUserdocRelatedToCurrectUserdoc(CProcess process, ProcessService processService, UserdocTypesService userdocTypesService) {
        CDocumentId documentId = process.getProcessOriginData().getDocumentId();
        if (Objects.nonNull(documentId) && Objects.nonNull(documentId.getDocNbr())) {
            CUserdocType userdocType = selectUserdocTypeFromDatabase(process, userdocTypesService);
            List<String> invalidatedUserdocTypes = userdocType.getInvalidatedUserdocTypes();
            if (!CollectionUtils.isEmpty(invalidatedUserdocTypes)) {
                CProcessId upperProcessId = process.getProcessOriginData().getUpperProcessId();
                String upperProcessUserdocType = processService.selectUserdocTypeByProcessId(upperProcessId);
                if (StringUtils.hasText(upperProcessUserdocType)) {
                    String invalidatedUserdocType = invalidatedUserdocTypes.stream().filter(s -> s.equalsIgnoreCase(upperProcessUserdocType)).findFirst().orElse(null);
                    return Objects.nonNull(invalidatedUserdocType);
                }
            }
        }
        return false;
    }

    public static CUserdocType selectUserdocTypeFromDatabase(CProcess process, UserdocTypesService userdocTypesService) {
        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(process.getProcessOriginData().getUserdocType());
        if (Objects.isNull(cUserdocType)) {
            throw new RuntimeException("Userdoc type is empty ! " + process.getProcessOriginData().getUserdocType());
        }
        return cUserdocType;
    }

    public static CDocumentId selectRecordalUserdocIdByInvalidationUserdoc(CUserdoc invalidationUserdoc) {
        return selectRecordalUserdocIdByInvalidationUserdocParentData(invalidationUserdoc.getUserdocParentData());
    }

    public static CDocumentId selectRecordalUserdocIdByInvalidationUserdocParentData(CProcessParentData invalidationUserdocParentData) {
        CDocumentId recordalUserdocId = invalidationUserdocParentData.getUserdocId();
        if (Objects.isNull(recordalUserdocId))
            throw new RuntimeException("Parent userdoc is empty for invalidation recordal !");

        return recordalUserdocId;
    }

    public static CUserdoc selectUserdocFromDatabase(CDocumentId documentId, UserdocService userdocService) {
        CUserdoc userdoc = userdocService.findUserdoc(documentId);
        if (Objects.isNull(userdoc)) {
            throw new RuntimeException("Cannot find userdoc ! ID: " + documentId);
        }
        return userdoc;
    }

    public static String selectParentUserdocType(CProcessParentData processParentData) {
        CFileId fileId = processParentData.getFileId();
        if (Objects.nonNull(fileId)) {
            return fileId.getFileType();
        } else if (Objects.nonNull(processParentData.getUserdocId())) {
            return processParentData.getUserdocType();
        } else {
            throw new RuntimeException("Cannot find userdoc parent document type !");
        }
    }

    public static boolean isRecordalForChangeNameOrAddress(CUserdoc userdoc) {
        CUserdocType userdocType = userdoc.getUserdocType();
        CUserdocPanel recordalPanel = selectRecordalPanel(userdocType.getPanels());
        if (Objects.isNull(recordalPanel)) {
            return false;
        }

        RecordalType recordalType = RecordalType.valueOf(recordalPanel.getPanel());
        return recordalType == RecordalType.Change;
    }

    public static void removePersonsWithRolesWhichIsNotInConfiguration(CUserdoc userdoc, CUserdocType cUserdocType) {
        List<CUserdocPerson> personList = userdoc.getUserdocPersonData().getPersonList();
        if (!CollectionUtils.isEmpty(personList)) {
            List<UserdocPersonRole> roles = cUserdocType.getRoles().stream().map(CUserdocPersonRole::getRole).collect(Collectors.toList());
            personList.removeIf(cUserdocPerson -> !(roles.contains(cUserdocPerson.getRole())));
        }
    }

    public static boolean isUserdocTypeChanged(CUserdoc originalUserdoc, CUserdoc newUserdoc) {
        String originalUserdocType = originalUserdoc.getUserdocType().getUserdocType();
        String userdocType = newUserdoc.getUserdocType().getUserdocType();
        return !originalUserdocType.equalsIgnoreCase(userdocType);
    }

    public static boolean isUserdoc(String filingNumber) {
        if (filingNumber.contains(FileType.USERDOC.code())) {
            CDocumentId documentId = BasicUtils.createCDocumentId(filingNumber);
            return FileType.USERDOC.code().equals(documentId.getDocLog());
        }
        return false;
    }

    public static boolean isFilingDateThirtyDaysEarlier(Date filingDate) {
        if (Objects.isNull(filingDate))
            return false;

        Integer days = calculateDaysAfterFilingDate(filingDate);
        if (Objects.isNull(days))
            return false;

        return days <= 30;
    }


    private static Integer calculateDaysAfterFilingDate(Date filingDate) {
        if (Objects.isNull(filingDate))
            return null;

        LocalDate filingDateAslocalDate = DateUtils.dateToLocalDate(filingDate);
        LocalDate today = LocalDate.now();

        if (today.isBefore(filingDateAslocalDate)) {
            return null;
        } else {
            long days = ChronoUnit.DAYS.between(filingDateAslocalDate, today);
            return Math.abs(Math.toIntExact(days));
        }
    }

    public static List<CFileId> selectSingleDesignsIdentifiers(List<CUserdocSingleDesign> singleDesigns) {
        if (CollectionUtils.isEmpty(singleDesigns))
            return null;

        return singleDesigns.stream()
                .filter(design -> design.getFileId() != null)
                .map(CUserdocSingleDesign::getFileId)
                .collect(Collectors.toList());
    }

    public static boolean isUserdocRecordalTransfer(CUserdoc userdoc){
        if (Objects.isNull(userdoc))
            throw new RuntimeException("Userdoc object is empty !");

        CUserdocPanel panel = UserdocUtils.selectRecordalPanel(userdoc.getUserdocType().getPanels());
        if (Objects.nonNull(panel)) {
            RecordalType recordalType = RecordalType.valueOf(panel.getPanel());
            return RecordalType.Transfer == recordalType;
        }

        return false;
    }

    public static boolean doesUserdocMeetSplitConditions(CUserdoc userdoc){
        if (UserdocUtils.isUserdocRecordalTransfer(userdoc)) {
            Boolean areAllServiceScopeSelected = UserdocExtraDataUtils.selectBoolean(UserdocExtraDataTypeCode.SERVICE_SCOPE.name(), userdoc.getUserdocExtraData());
            if (Objects.nonNull(areAllServiceScopeSelected) && !areAllServiceScopeSelected) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUserdocWithoutCorrespondents(CUserdocType userdocType, UserdocPanelService userdocPanelService) {
       return  Objects.isNull(userdocPanelService.findUserdocPanelByPanelAndUserdocType(DefaultValue.PERSONS_PANEL, userdocType.getUserdocType()));
    }

    public static CUserdoc findCorrectUserdocFromWipoRequest(String requestExternalSystemId, UserdocService userdocService) {
        CUserdoc userdoc = userdocService.selectByExternalSystemId(requestExternalSystemId);

        if (Objects.isNull(userdoc) && StringUtils.hasText(requestExternalSystemId) && requestExternalSystemId.startsWith("BG/N/") && requestExternalSystemId.contains(DefaultValue.DASH)) {
            String userdocFile = requestExternalSystemId.substring(0, requestExternalSystemId.lastIndexOf(DefaultValue.DASH) + 1);
            String userdocNumber = requestExternalSystemId.substring(requestExternalSystemId.lastIndexOf(DefaultValue.DASH) + 1);
            if (userdocNumber.matches("\\d+")) {
                userdocNumber = "[" + userdocNumber + "]";
                userdoc = userdocService.selectByExternalSystemId(userdocFile + userdocNumber);
            }
        }

        return userdoc;
    }

    public static Pair<String, String> selectSpecialActionData(CUserdocType userdocType, InternationalUserdocTypeConfigService intlUserdocConfigService, ConfigParamService configParamService) {
        CInternationalUserdocTypeConfig userdocTypeConfig = intlUserdocConfigService.findById(userdocType.getUserdocType());
        if (userdocTypeConfig == null) {
            return null;
        }

        if (userdocTypeConfig.getInsertMarkActionProtectionSurrender()) {
            return getActionDataPair(EXT_CONFIG_PARAM_INTL_MARK_SURRENDER_PROTECTION_ACTION_TYPE, EXT_CONFIG_PARAM_INTL_MARK_SURRENDER_PROTECTION_STATUS_CODE, configParamService);
        } else if (userdocTypeConfig.getInsertMarkActionCancellation()) {
            return getActionDataPair(EXT_CONFIG_PARAM_INTL_MARK_CANCELLATION_ACTION_TYPE, EXT_CONFIG_PARAM_INTL_MARK_CANCELLATION_STATUS_CODE, configParamService);
        } else if (userdocTypeConfig.getInsertMarkActionNonRenewalOfTrademark()) {
            return getActionDataPair(EXT_CONFIG_PARAM_INTL_MARK_NON_RENEWAL_OF_TRADEMARK_ACTION_TYPE, EXT_CONFIG_PARAM_INTL_MARK_NON_RENEWAL_OF_TRADEMARK_STATUS_CODE, configParamService);
        } else if (userdocTypeConfig.getInsertMarkActionNonRenewalOfContractingParty()) {
            return getActionDataPair(EXT_CONFIG_PARAM_INTL_MARK_NON_RENEWAL_OF_CONTRACTING_PARTY_ACTION_TYPE, EXT_CONFIG_PARAM_INTL_MARK_NON_RENEWAL_OF_CONTRACTING_PARTY_STATUS_CODE, configParamService);
        }

        return null;
    }

    private static Pair<String, String> getActionDataPair(String actionTypeConfigCode, String statusCodeConfigCode, ConfigParamService configParamService) {
        CConfigParam actionTypeParam = configParamService.selectExtConfigByCode(actionTypeConfigCode);
        if (Objects.isNull(actionTypeParam))
            throw new RuntimeException("Cannot find config param " + actionTypeConfigCode);

        CConfigParam statusCodeParam = configParamService.selectExtConfigByCode(statusCodeConfigCode);
        if (Objects.isNull(statusCodeParam))
            throw new RuntimeException("Cannot find config param " + statusCodeConfigCode);

        return Pair.of(actionTypeParam.getValue(), statusCodeParam.getValue());
    }

}
