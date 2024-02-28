package bg.duosoft.ipas.core.service.impl.action;

import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessInsertActionRequest;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.action.InsertActionService;
import bg.duosoft.ipas.core.service.action.InternationalMarkActionService;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.ActionTypeService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.service.userdoc.config.InternationalUserdocTypeConfigService;
import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InternationalMarkActionServiceImpl implements InternationalMarkActionService {

    @Autowired
    private ConfigParamService configParamService;

    @Autowired
    private InsertActionService insertActionService;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private InternationalUserdocTypeConfigService internationalUserdocTypeConfigService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ActionTypeService actionTypeService;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private FileService fileService;

    @Override
    public void insertMarkFirstNormalAction(CMark insertedMark, boolean isFullyDivided) {
        CProcessSimpleData cProcessSimpleData = insertedMark.getFile().getProcessSimpleData();
        CFileId fileId = insertedMark.getFile().getFileId();
        boolean isWipoDivided = FileType.INTERNATIONAL_MARK_B.code().equals(fileId.getFileType());
        boolean isDivided = isWipoDivided && StringUtils.hasText(insertedMark.getFile().getRegistrationData().getRegistrationId().getRegistrationDup());
        CFile previousFile = isDivided ? findPreviousIntlFile(insertedMark) : null;
        boolean isPreviousAcceptedRegistrationStatus = Objects.nonNull(previousFile) && isAcceptedRegistrationStatus(previousFile);
        String configActionType = findActionType(isWipoDivided, isFullyDivided, previousFile, isPreviousAcceptedRegistrationStatus);
        CUser responsibleUser = findResponsibleUser(cProcessSimpleData, previousFile, isPreviousAcceptedRegistrationStatus);

        try {
            CProcessInsertActionRequest insertActionRequest = createInsertNormalAction(cProcessSimpleData.getProcessId(), responsibleUser, configActionType);
            insertActionService.insertAction(insertActionRequest);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            String actionTitle = messageSource.getMessage("error.action.insert.normal.action", null, LocaleContextHolder.getLocale());
            String customMessage = messageSource.getMessage("error.ipas.insert.normal.action", new String[]{String.valueOf(fileId)}, LocaleContextHolder.getLocale());
            String instruction = messageSource.getMessage("instruction.ipas.insert.normal.action", new String[]{configActionType}, LocaleContextHolder.getLocale());
            errorLogService.createNewRecord(ErrorLogAbout.IPAS, actionTitle, e.getMessage(), customMessage, true, instruction, ErrorLogPriority.MEDIUM);
        }

        if (Objects.isNull(responsibleUser)) {
            processService.updateResponsibleUser(null, cProcessSimpleData.getProcessId().getProcessType(), cProcessSimpleData.getProcessId().getProcessNbr());
        }
    }

    private CUser findResponsibleUser(CProcessSimpleData cProcessSimpleData, CFile previousFile, boolean isPreviousAcceptedRegistrationStatus) {
        if (Objects.nonNull(previousFile) && !isPreviousAcceptedRegistrationStatus) {
            if (Objects.nonNull(previousFile.getProcessSimpleData().getResponsibleUser())) {
                boolean active = Objects.nonNull(previousFile.getProcessSimpleData().getResponsibleUser().getIndInactive()) && !previousFile.getProcessSimpleData().getResponsibleUser().getIndInactive();
                if (active) {
                    Integer userId = previousFile.getProcessSimpleData().getResponsibleUser().getUserId();
                    boolean isResponsibleUserExluded = isResponsibleUserExcluded(userId);
                    if (!isResponsibleUserExluded) {
                        return previousFile.getProcessSimpleData().getResponsibleUser();
                    }
                }
            }
        }
        return cProcessSimpleData.getResponsibleUser();
    }

    private String findActionType(boolean isWipoDivided, boolean isFullyDivided, CFile previousFile, boolean isPreviousAcceptedRegistrationStatus) {
        String wipoDividedActionType = ConfigParamService.EXT_CONFIG_PARAM_INTL_MARK_WIPO_DIVIDED_ACTION_TYPE;
        String fullyDividedActionType = ConfigParamService.EXT_CONFIG_PARAM_INTL_MARK_WIPO_FULLY_DIVIDED_ACTION_TYPE;
        String formalExpertiseActionType = ConfigParamService.EXT_CONFIG_PARAM_INTL_MARK_FORMAL_EXPERTISE_ACTION_TYPE;
        if (Objects.nonNull(previousFile) && !isPreviousAcceptedRegistrationStatus) {
            return formalExpertiseActionType;
        } else {
            return isWipoDivided ? isFullyDivided ? fullyDividedActionType : wipoDividedActionType : formalExpertiseActionType;
        }
    }

    private boolean isAcceptedRegistrationStatus(CFile file) {
        CConfigParam acceptedRegistrationStatusCodeParam =  configParamService.selectExtConfigByCode(ConfigParamService.EXT_CONFIG_PARAM_INTL_MARK_ACCEPTED_REGISTRATION_STATUS_CODE);
        if (Objects.isNull(acceptedRegistrationStatusCodeParam)) {
            throw new RuntimeException("Cannot find accepted international registration status code config !");
        }

        String acceptedRegistrationStatus = acceptedRegistrationStatusCodeParam.getValue();
        return acceptedRegistrationStatus.equals(file.getProcessSimpleData().getStatusCode());
    }

    private boolean isResponsibleUserExcluded(Integer userId) {
        CConfigParam excludedResponsibleUsersParam =  configParamService.selectExtConfigByCode(ConfigParamService.EXCLUDED_RESPONSIBLE_USERS);
        if (Objects.isNull(excludedResponsibleUsersParam)) {
            throw new RuntimeException("Cannot find excluded responsible users config!");
        }
        String excludedResponsibleUsers = excludedResponsibleUsersParam.getValue();
        return excludedResponsibleUsers.contains(String.valueOf(userId));
    }

    private CFile findPreviousIntlFile(CMark insertedMark) {
        Long registrationNbr = insertedMark.getFile().getRegistrationData().getRegistrationId().getRegistrationNbr();
        List<CFile> allIntlByRegistrationNbr = fileService.findAllByRegistrationNbrAndFileType(registrationNbr.intValue(), FileType.getInternationalMarkFileTypes());
        if (CollectionUtils.isEmpty(allIntlByRegistrationNbr)) {
            return null;
        }

        return allIntlByRegistrationNbr
                .stream()
                .filter(file -> !file.getFileId().equals(insertedMark.getFile().getFileId()))
                .sorted(Comparator.comparing((CFile file) -> file.getRegistrationData().getRegistrationId().getRegistrationDup(), Comparator.nullsLast(Comparator.reverseOrder())))
                .findFirst().orElse(null);
    }

    @Override
    public void insertSpecialAction(CDocumentId documentId) {
        CUserdoc cUserdoc = userdocService.findUserdoc(documentId);
        CProcess topProcess = processService.selectProcess(cUserdoc.getProcessSimpleData().getFileProcessId(), false);

        Pair<String, String> actionData = UserdocUtils.selectSpecialActionData(cUserdoc.getUserdocType(), internationalUserdocTypeConfigService, configParamService);
        if (Objects.nonNull(actionData)) {
            try {
                CProcessInsertActionRequest insertActionRequest = createInsertSpecialActionObject(topProcess, actionData);
                insertActionService.insertAction(insertActionRequest);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                String actionTitle = messageSource.getMessage("error.action.insert.special.action", null, LocaleContextHolder.getLocale());
                String customMessage = messageSource.getMessage("error.ipas.insert.special.action", new String[]{String.valueOf(cUserdoc.getUserdocMainObjectData().getFileId()), String.valueOf(cUserdoc.getDocumentId())}, LocaleContextHolder.getLocale());
                CActionType actionType = actionTypeService.findById(actionData.getFirst());
                String instruction;
                if (Objects.nonNull(actionType)) {
                    instruction = messageSource.getMessage("instruction.ipas.insert.special.action", new String[]{actionType.getActionName()}, LocaleContextHolder.getLocale());
                } else {
                    instruction = messageSource.getMessage("instruction.ipas.insert.special.action.config", new String[]{actionData.getFirst()}, LocaleContextHolder.getLocale());
                }
                errorLogService.createNewRecord(ErrorLogAbout.IPAS, actionTitle, e.getMessage(), customMessage, true, instruction, ErrorLogPriority.MEDIUM);
            }

            if (Objects.isNull(topProcess.getResponsibleUser())) {
                processService.updateResponsibleUser(null, topProcess.getProcessId().getProcessType(), topProcess.getProcessId().getProcessNbr());
            }
        }
    }

    @Override
    public void insertUserdocNormalAction(CUserdoc userdoc, String extParamCode) {
        CProcessSimpleData cProcessSimpleData = userdoc.getProcessSimpleData();
        try {
            CProcessInsertActionRequest insertActionRequest = createInsertNormalAction(cProcessSimpleData.getProcessId(), cProcessSimpleData.getResponsibleUser(), extParamCode);
            insertActionService.insertAction(insertActionRequest);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            String actionTitle = messageSource.getMessage("error.action.insert.normal.action", null, LocaleContextHolder.getLocale());
            String customMessage = messageSource.getMessage("error.ipas.insert.normal.action.userdoc", new String[]{String.valueOf(userdoc.getDocumentId())}, LocaleContextHolder.getLocale());
            String instruction = messageSource.getMessage("instruction.ipas.insert.normal.action",  new String[]{extParamCode} , LocaleContextHolder.getLocale());
            errorLogService.createNewRecord(ErrorLogAbout.IPAS, actionTitle, e.getMessage(), customMessage, true, instruction, ErrorLogPriority.MEDIUM);
        }

        if (Objects.isNull(cProcessSimpleData.getResponsibleUser())) {
            processService.updateResponsibleUser(null, cProcessSimpleData.getProcessId().getProcessType(), cProcessSimpleData.getProcessId().getProcessNbr());
        }
    }

    private CProcessInsertActionRequest createInsertSpecialActionObject(CProcess process, Pair<String, String> actionData) {
        CProcessInsertActionRequest insertActionRequest = new CProcessInsertActionRequest();
        insertActionRequest.setProcessId(process.getProcessId());
        insertActionRequest.setActionType(actionData.getFirst());
        insertActionRequest.setActionDate(new Date());
        insertActionRequest.setSpecialFinalStatus(actionData.getSecond());
        insertActionRequest.setResponsibleUser(Objects.isNull(process.getResponsibleUser()) ? DefaultValue.DEFAULT_USER_ID : process.getResponsibleUser().getUserId());
        insertActionRequest.setCaptureUser(DefaultValue.DEFAULT_USER_ID); //IPASPROD
        return insertActionRequest;
    }

    private CProcessInsertActionRequest createInsertNormalAction(CProcessId processId, CUser responsibleUser, String extConfigCode) {
        CProcessInsertActionRequest insertActionRequest = new CProcessInsertActionRequest();
        insertActionRequest.setProcessId(processId);
        insertActionRequest.setActionDate(new Date());
        insertActionRequest.setResponsibleUser(Objects.isNull(responsibleUser) ? DefaultValue.DEFAULT_USER_ID : responsibleUser.getUserId());
        insertActionRequest.setCaptureUser(DefaultValue.DEFAULT_USER_ID);
        CConfigParam actionTypeConfig =  configParamService.selectExtConfigByCode(extConfigCode);
        if (Objects.isNull(actionTypeConfig)) {
            throw new RuntimeException("Cannot find normal action type config !");
        }
        insertActionRequest.setActionType(actionTypeConfig.getValue());
        return insertActionRequest;
    }
}
