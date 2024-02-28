package bg.duosoft.ipas.core.service.impl.process;

import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.mapper.process.*;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFileRestrictData;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.process.*;
import bg.duosoft.ipas.core.model.userdoc.CUserdocDocData;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.process.ProcessValidator;
import bg.duosoft.ipas.enums.NextProcessActionType;
import bg.duosoft.ipas.enums.ProcessResultType;
import bg.duosoft.ipas.enums.UserdocGroup;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpOffidocAbdocsDocument;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfProcessType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatusPK;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcResponsibleUserChanges;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.model.entity.vw.VwSelectNextProcessActions;
import bg.duosoft.ipas.persistence.model.nonentity.TopProcessFileData;
import bg.duosoft.ipas.persistence.repository.entity.custom.SequenceRepository;
import bg.duosoft.ipas.persistence.repository.entity.ext.IpOffidocAbdocsDocumentRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfProcessTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfStatusRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcResponsibleUserChangesRepository;
import bg.duosoft.ipas.persistence.repository.entity.vw.VwNextProcessActionRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProcessServiceImpl implements ProcessService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IpProcRepository ipProcRepository;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private FileIdMapper fileIdMapper;

    @Autowired
    private ProcessIdMapper processIdMapper;

    @Autowired
    private TopProcessFileDataMapper topProcessFileDataMapper;

    @Autowired
    private NextProcessActionMapper nextProcessActionMapper;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private VwNextProcessActionRepository vwNextProcessActionRepository;

    @Autowired
    private IpOffidocAbdocsDocumentRepository ipOffidocAbdocsDocumentRepository;

    @Autowired
    private CfProcessTypeRepository cfProcessTypeRepository;

    @Autowired
    private SequenceRepository sequenceRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DocService docService;

    @Autowired
    private FileService fileService;

    @Autowired
    private IpProcResponsibleUserChangesRepository ipProcResponsibleUserChangesRepository;

    @Autowired
    private ProcessResponsibleUserChangeMapper processResponsibleUserChangeMapper;

    @Autowired
    private CfStatusRepository cfStatusRepository;

    @Override
    public List<CProcess> selectNotFinishedUserdocProcessesRelatedToIpObjectProcess(CProcessId cProcessId,boolean addProcessEvents) {
        // get all userdoc processes related to the affected object
        List<CProcess> affectedObjectUserdocProcesses = selectSubUserdocProcessesRelatedToIpObjectProcess(cProcessId,addProcessEvents);
        List<CProcess> notFinishedUserdocProcesses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(affectedObjectUserdocProcesses)){
            // get not finished userdoc processes
            affectedObjectUserdocProcesses.stream().forEach(process->{
                CfStatus procStatus = cfStatusRepository.findById(new CfStatusPK(process.getProcessId().getProcessType(), process.getStatus().getStatusId().getStatusCode())).orElse(null);
                CUserdocType cUserdocType = null;
                if (!StringUtils.isEmpty(process.getProcessOriginData().getUserdocType())){
                    cUserdocType = userdocTypesService.selectUserdocTypeById(process.getProcessOriginData().getUserdocType());
                }
                if (Objects.nonNull(procStatus) && procStatus.getProcessResultTyp().equals(ProcessResultType.PENDING.code()) && Objects.nonNull(cUserdocType) && cUserdocType.getUserdocGroup() == UserdocGroup.MAIN){
                    notFinishedUserdocProcesses.add(process);
                }
            });
        }
        return CollectionUtils.isEmpty(notFinishedUserdocProcesses)? null :notFinishedUserdocProcesses;
    }

    @Override
    public List<CProcess> selectSubUserdocProcessesRelatedToIpObjectProcess(CProcessId cProcessId,boolean addProcessEvents) {
        List<IpProc> ipProcs = ipProcRepository.selectSubUserdocProcessesRelatedToIpObjectProcess(cProcessId.getProcessType(), cProcessId.getProcessNbr());
        return processMapper.toCoreList(ipProcs,addProcessEvents);
    }

    @Override
    public void setResponsibleUserChangeAsRead(String processType, Integer processNbr, Integer responsibleUser) {
        if (Objects.nonNull(responsibleUser) && Objects.equals(responsibleUser,SecurityUtils.getLoggedUserId())){
            Integer unreadChanges = ipProcResponsibleUserChangesRepository.getUnreadResponsibleUserChangesCount(processType,processNbr);
            if (Objects.nonNull(unreadChanges) && unreadChanges>0){
                ipProcResponsibleUserChangesRepository.setResponsibleUserChangeAsRead(processType,processNbr,responsibleUser);
            }
        }
    }

    @Override
    public CProcess selectProcess(CProcessId cProcessId, boolean addProcessEvents) {
        if (Objects.isNull(cProcessId))
            return null;

        IpProcPK ipProcPK = new IpProcPK(cProcessId.getProcessType(), cProcessId.getProcessNbr());
        IpProc ipProc = ipProcRepository.findById(ipProcPK).orElse(null);
        if (Objects.isNull(ipProc))
            return null;

        return processMapper.toCore(ipProc, addProcessEvents);
    }

    @Override
    public CProcess selectUserdocProcess(CDocumentId documentId, boolean addProcessEvents) {
        if (Objects.isNull(documentId))
            return null;

        IpProc ipProc = ipProcRepository.findByUserdocIpDoc_Pk_DocOriAndUserdocIpDoc_Pk_DocLogAndUserdocIpDoc_Pk_DocSerAndUserdocIpDoc_Pk_DocNbr(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
        if (Objects.isNull(ipProc))
            return null;

        return processMapper.toCore(ipProc, addProcessEvents);
    }

    @Override
    public CFileId selectTopProcessFileId(CProcessId cProcessId) {
        IpFilePK ipFilePK = ipProcRepository.selectTopProcessFileId(cProcessId.getProcessType(), cProcessId.getProcessNbr());
        if (Objects.isNull(ipFilePK))
            return null;

        return fileIdMapper.toCore(ipFilePK);
    }

    public CProcessId selectFileProcessId(CFileId fileId) {
        IpProcPK ipProcPK = ipProcRepository.selectFileProcessId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
        return ipProcPK == null ? null : processIdMapper.toCore(ipProcPK);
    }

    @Override
    public CProcessId selectUserdocProcessId(CDocumentId documentId) {
        IpProcPK ipProcPK = ipProcRepository.selectUserdocProcessId(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
        if (Objects.isNull(ipProcPK))
            return null;

        return processIdMapper.toCore(ipProcPK);
    }

    @Override
    public CProcessParentData generateProcessParentHierarchy(CProcessId cProcessId) {
        CProcess mainProcess = selectProcess(cProcessId, false);
        if (Objects.isNull(mainProcess))
            return null;

        CTopProcessFileData topProcessFileData = selectTopProcessFileData(mainProcess.getProcessOriginData().getTopProcessId());
        return fillProcessParentData(mainProcess, topProcessFileData);
    }

    @Override
    public CProcessParentData generateProcessHierarchy(CProcessId cProcessId) {
        CProcess mainProcess = selectProcess(cProcessId, false);
        if (Objects.isNull(mainProcess))
            return null;

        CProcessParentData mainProcessAsParent = createMainProcessAsParent(mainProcess);
        mainProcessAsParent.setParent(generateProcessParentHierarchy(cProcessId));
        return mainProcessAsParent;
    }

    private CProcessParentData createMainProcessAsParent(CProcess process) {
        CProcessParentData cProcessParentData = new CProcessParentData();
        cProcessParentData.setTopProcessId(process.getProcessOriginData().getTopProcessId());
        CDocumentId documentId = process.getProcessOriginData().getDocumentId();
        COffidocId offidocId = process.getProcessOriginData().getOffidocId();
        if (Objects.nonNull(documentId)) {
            String userdocType = process.getProcessOriginData().getUserdocType();
            fillUserdocData(cProcessParentData, documentId, userdocType);
        } else if (Objects.nonNull(offidocId)) {
            fillOffidocData(cProcessParentData, offidocId);
        } else {
            cProcessParentData.setFileId(process.getProcessOriginData().getFileId());
        }

        cProcessParentData.setProcessId(process.getProcessId());
        cProcessParentData.setIsManualSubProcess(ProcessTypeUtils.isManualSubProcess(process));

        CTopProcessFileData topProcessFileData = selectTopProcessFileData(process.getProcessOriginData().getTopProcessId());
        cProcessParentData.setTopProcessFileData(topProcessFileData);
        return cProcessParentData;
    }

    private CProcessParentData fillProcessParentData(CProcess mainProcess, CTopProcessFileData topProcessFileData) {
        if (Objects.nonNull(mainProcess)) {
            CProcessParentData cProcessParentData = new CProcessParentData();
            cProcessParentData.setTopProcessId(mainProcess.getProcessOriginData().getTopProcessId());
            CProcessId upperProcessId = mainProcess.getProcessOriginData().getUpperProcessId();
            if (Objects.nonNull(upperProcessId)) {
                CProcess upperProcess = selectProcess(upperProcessId, false);
                CDocumentId documentId = upperProcess.getProcessOriginData().getDocumentId();
                COffidocId offidocId = upperProcess.getProcessOriginData().getOffidocId();
                CFileId fileId = upperProcess.getProcessOriginData().getFileId();
                if (Objects.nonNull(documentId)) {
                    String userdocType = upperProcess.getProcessOriginData().getUserdocType();
                    fillUserdocData(cProcessParentData, documentId, userdocType);
                    cProcessParentData.setParent(fillProcessParentData(upperProcess, topProcessFileData));
                } else if (Objects.nonNull(offidocId)) {
                    fillOffidocData(cProcessParentData, offidocId);
                    cProcessParentData.setParent(fillProcessParentData(upperProcess, topProcessFileData));
                } else if (Objects.nonNull(fileId)) {
                    cProcessParentData.setFileId(fileId);
                } else {
                    cProcessParentData.setParent(fillProcessParentData(upperProcess, topProcessFileData));
                }

                cProcessParentData.setProcessId(upperProcess.getProcessId());
                cProcessParentData.setIsManualSubProcess(ProcessTypeUtils.isManualSubProcess(upperProcess));
                cProcessParentData.setTopProcessFileData(topProcessFileData);
            }
            return cProcessParentData;
        } else
            return null;
    }

    private void fillUserdocData(CProcessParentData cProcessParentData, CDocumentId documentId, String userdocType) {
        cProcessParentData.setUserdocId(documentId);
        cProcessParentData.setUserdocType(userdocType);
        String externalSystemId = docService.selectExternalSystemId(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
        if (StringUtils.isEmpty(externalSystemId)) {
            cProcessParentData.setUserdocRegistrationNumber(messageSource.getMessage("not.registered", null, LocaleContextHolder.getLocale()));
        } else
            cProcessParentData.setUserdocRegistrationNumber(externalSystemId);
    }

    private void fillOffidocData(CProcessParentData cProcessParentData, COffidocId cOffidocId) {
        cProcessParentData.setOffidocId(cOffidocId);
        String noNumber = messageSource.getMessage("not.registered", null, LocaleContextHolder.getLocale());
        IpOffidocAbdocsDocument ipOffidocAbdocsDocument = ipOffidocAbdocsDocumentRepository.findById(new IpOffidocPK(cOffidocId.getOffidocOrigin(), cOffidocId.getOffidocSeries(), cOffidocId.getOffidocNbr())).orElse(null);
        if (Objects.isNull(ipOffidocAbdocsDocument)) {
            cProcessParentData.setOffidocRegistrationNumber(noNumber);
        } else {
            String registrationNumber = ipOffidocAbdocsDocument.getRegistrationNumber();
            if (StringUtils.isEmpty(registrationNumber))
                cProcessParentData.setOffidocRegistrationNumber(noNumber);
            else
                cProcessParentData.setOffidocRegistrationNumber(registrationNumber);
        }
    }

    @Override
    public List<CNextProcessAction> selectNextProcessActions(CProcess process, boolean hasRightsForExecuteAutomaticActions) {
        if (Objects.isNull(process))
            return null;

        String procType = process.getProcessId().getProcessType();
        String initialStatusCode = process.getStatus().getStatusId().getStatusCode();
        if (StringUtils.isEmpty(procType) || StringUtils.isEmpty(initialStatusCode))
            return null;

        List<VwSelectNextProcessActions> result = vwNextProcessActionRepository.selectNextProcessActions(procType, initialStatusCode);
        if (CollectionUtils.isEmpty(result))
            return null;

        if (!hasRightsForExecuteAutomaticActions)//Remove automatic action if user hasn't rights
            result.removeIf(nextProcessActions -> Objects.nonNull(nextProcessActions.getAutomaticActionWcode()));

        result.removeIf(vwSelectNextProcessActions -> {
            boolean isSameStatus = vwSelectNextProcessActions.getStatusCode().equalsIgnoreCase(process.getStatus().getStatusId().getStatusCode());
            NextProcessActionType nextProcessActionType = NextProcessActionType.valueOf(vwSelectNextProcessActions.getProcessActionType());
            return isSameStatus && (NextProcessActionType.SPECIAL_RANDOM_TO_SPECIFIC == nextProcessActionType);
        }); //Remove

        checkForRestricts(process, result);
        return nextProcessActionMapper.toCoreList(result);
    }

    private void checkForRestricts(CProcess process, List<VwSelectNextProcessActions> nextProcessActions) {
        CFileId fileId = selectTopProcessFileId(process.getProcessOriginData().getTopProcessId());
        CFileRestrictData restrictData = fileService.selectRestrictData(fileId);
        Iterator<VwSelectNextProcessActions> iterator = nextProcessActions.iterator();
        while (iterator.hasNext()) {
            VwSelectNextProcessActions nextProcessAction = iterator.next();
            boolean isExistRestrict = false;
            boolean isElementRemoved = false;
            String restrictApplicationSubType = nextProcessAction.getRestrictApplicationSubType();
            if (!StringUtils.isEmpty(restrictApplicationSubType)) {
                isExistRestrict = true;
                if (!restrictApplicationSubType.equalsIgnoreCase(restrictData.getApplicationSubType())) {
                    iterator.remove();
                    isElementRemoved = true;
                }
            }
            if (!isExistRestrict) {
                String restrictApplicationType = nextProcessAction.getRestrictApplicationType();
                if (!StringUtils.isEmpty(restrictApplicationType)) {
                    isExistRestrict = true;
                    if (!restrictApplicationType.equalsIgnoreCase(restrictData.getApplicationType())) {
                        iterator.remove();
                        isElementRemoved = true;
                    }
                }
            }
            if (!isExistRestrict) {
                String restrictFileTyp = nextProcessAction.getRestrictFileTyp();
                if (!StringUtils.isEmpty(restrictFileTyp)) {
                    isExistRestrict = true;
                    if (!restrictFileTyp.equalsIgnoreCase(restrictData.getFileType())) {
                        iterator.remove();
                        isElementRemoved = true;
                    }
                }
            }
            if (!isExistRestrict) {
                Integer restrictLawCode = nextProcessAction.getRestrictLawCode();
                if (Objects.nonNull(restrictLawCode)) {
                    if (!Objects.equals(restrictLawCode, restrictData.getLawCode())) {
                        iterator.remove();
                        isElementRemoved = true;
                    }
                }
            }

            if (!isElementRemoved) {
                String userdocListCodeInclude = nextProcessAction.getUserdocListCodeInclude();
                if (!StringUtils.isEmpty(userdocListCodeInclude)) {
                    String userdocType = process.getProcessOriginData().getUserdocType();
                    if (ProcessTypeUtils.isUserdocProcess(process) && !StringUtils.isEmpty(userdocType)) {
                        List<String> userdocTypesInclude = userdocTypesService.selectUserdocTypesByListCode(userdocListCodeInclude);
                        if (!CollectionUtils.isEmpty(userdocTypesInclude)) {
                            boolean deleteAction = true;
                            for (String s : userdocTypesInclude) {
                                if (s.equalsIgnoreCase(userdocType)) {
                                    deleteAction = false;
                                    break;
                                }
                            }
                            if (deleteAction) {
                                iterator.remove();
                                isElementRemoved = true;
                            }
                        }
                    }
                }
            }
            if (!isElementRemoved) {
                String userdocListCodeExclude = nextProcessAction.getUserdocListCodeExclude();
                if (!StringUtils.isEmpty(userdocListCodeExclude)) {
                    String userdocType = process.getProcessOriginData().getUserdocType();
                    if (ProcessTypeUtils.isUserdocProcess(process) && !StringUtils.isEmpty(userdocType)) {
                        List<String> userdocTypesExclude = userdocTypesService.selectUserdocTypesByListCode(userdocListCodeExclude);
                        if (!CollectionUtils.isEmpty(userdocTypesExclude)) {
                            boolean deleteAction = false;
                            for (String s : userdocTypesExclude) {
                                if (s.equalsIgnoreCase(userdocType)) {
                                    deleteAction = true;
                                    break;
                                }
                            }
                            if (deleteAction) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }
    }

    public CNextProcessAction selectNextProcessActionBySelectedActionType(CProcess cProcess, String actionTyp) {
        List<CNextProcessAction> nextProcessActions = selectNextProcessActions(cProcess, SecurityUtils.hasRights(SecurityRole.ProcessAutomaticActionExecute));
        if (Objects.isNull(nextProcessActions))
            throw new RuntimeException("Missing next process actions for process " + cProcess.getProcessId().getProcessType() + "-" + cProcess.getProcessId().getProcessNbr());

        CNextProcessAction nextProcessAction = nextProcessActions.stream()
                .filter(result -> result.getActionType() != null)
                .filter(result -> result.getActionType().equals(actionTyp))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(nextProcessAction))
            throw new RuntimeException("Cannot find next action type with for process " + cProcess.getProcessId().getProcessType() + "-" + cProcess.getProcessId().getProcessNbr() + " and action = " + actionTyp);

        return nextProcessAction;
    }

    @Override
    @IpasValidatorDefinition({ProcessValidator.class})
    public void updateProcess(CProcess cProcess) throws IpasValidationException {
        log.debug("Trying to update process with pk = " + cProcess.getProcessId());
        IpProc ipProc = processMapper.toEntity(cProcess);

        IpProc originalProcess = ipProcRepository.findById(ipProc.getPk()).orElse(null);
        if (Objects.isNull(originalProcess))
            throw new RuntimeException("Process does not exist...");
        Integer originalProcessResponsibleUser = originalProcess.getResponsibleUser() == null ? null : originalProcess.getResponsibleUser().getUserId();
        entityManager.detach(originalProcess);
        entityManager.merge(ipProc);
        Integer newResponsibleUser = ipProc.getResponsibleUser() == null ? null : ipProc.getResponsibleUser().getUserId();
        if (!Objects.equals(originalProcessResponsibleUser, newResponsibleUser)) {
            ipProcResponsibleUserChangesRepository.addResponsibleUserChanges(SecurityUtils.getLoggedUserId(), ipProc.getPk().getProcTyp(), ipProc.getPk().getProcNbr(), originalProcessResponsibleUser, newResponsibleUser);
        }
    }

    @Override
    public CNextProcessAction selectNextActionAfterExpirationDate(CProcessId processId, String statusCode) {
        if (Objects.isNull(processId) || StringUtils.isEmpty(statusCode))
            return null;

        List<VwSelectNextProcessActions> nextProcessActions = vwNextProcessActionRepository.selectNextProcessActionsAfterExpirationDate(processId.getProcessType(), statusCode);
        if (CollectionUtils.isEmpty(nextProcessActions))
            return null;

        if (nextProcessActions.size() > 1) {
            Optional<Integer> minActionType = nextProcessActions.stream().map(vwSelectNextProcessActions -> Integer.parseInt(vwSelectNextProcessActions.getId().getActionTyp())).min(Comparator.comparingInt(Integer::intValue));
            VwSelectNextProcessActions nextProcessActionsWithMinActionType = nextProcessActions.stream().filter(vwSelectNextProcessActions -> vwSelectNextProcessActions.getId().getActionTyp().equalsIgnoreCase(String.valueOf(minActionType.get()))).findFirst().orElse(null);
            return nextProcessActionMapper.toCore(nextProcessActionsWithMinActionType);
        }

        return nextProcessActionMapper.toCore(nextProcessActions.get(0));
    }

    @Override
    public List<CNextProcessAction> selectActionsWithExpirationDate(CProcess cProcess) {
        if (Objects.isNull(cProcess))
            return null;

        List<VwSelectNextProcessActions> nextProcessActions = vwNextProcessActionRepository.selectActionsWithExpirationDate(cProcess.getProcessId().getProcessType(), cProcess.getStatus().getStatusId().getStatusCode());
        if (CollectionUtils.isEmpty(nextProcessActions))
            return null;

        return nextProcessActionMapper.toCoreList(nextProcessActions);
    }

    @Override
    public boolean isUpperProcessUserdoc(CDocumentId id) {
        Boolean result = ipProcRepository.isUpperProcessUserdoc(id.getDocOrigin(), id.getDocLog(), id.getDocSeries(), id.getDocNbr());
        if (Objects.isNull(result))
            return false;

        return result;
    }

    @Override
    public void updateResponsibleUser(Integer userId, String processType, Integer processNbr) {
        updateResponsibleUserAndLogChange(userId, processType, processNbr);
    }

    @Override
    public void updateResponsibleUsers(Integer userId, List<CProcessId> cProcessIds) {
        if (CollectionUtils.isEmpty(cProcessIds)) {
            throw new RuntimeException("Empty process ids !");
        }
        cProcessIds.stream().forEach(r -> {
            updateResponsibleUserAndLogChange(userId, r.getProcessType(), r.getProcessNbr());
        });
    }

    private void updateResponsibleUserAndLogChange(Integer userId, String processType, Integer processNbr) {
        Integer oldResponsibleUser = ipProcRepository.selectResponsibleUser(processType, processNbr);
        ipProcRepository.updateResponsibleUser(userId, processType, processNbr);
        if (!Objects.equals(oldResponsibleUser, userId)) {
            ipProcResponsibleUserChangesRepository.addResponsibleUserChanges(SecurityUtils.getLoggedUserId(), processType, processNbr, oldResponsibleUser, userId);
        }
    }

    @Override
    public Integer selectResponsibleUserOfUserdocParentProcess(String docOri, String docLog, Integer docSer, Integer docNbr) {
        return ipProcRepository.selectResponsibleUserOfUserdocParentProcess(docOri, docLog, docSer, docNbr);
    }

    @Override
    public Integer selectResponsibleUser(CProcessId processId) {
        return ipProcRepository.selectResponsibleUser(processId.getProcessType(), processId.getProcessNbr());
    }

    @Override
    public Integer selectIpObjectResponsibleUser(CFileId fileId) {
        return ipProcRepository.selectIpObjectResponsibleUser(fileId);
    }

    @Override
    public Integer selectUserdocResponsibleUser(CDocumentId documentId) {
        return ipProcRepository.selectUserdocResponsibleUser(documentId);
    }

    @Override
    public Integer selectOffidocResponsibleUser(COffidocId offidocId) {
        return ipProcRepository.selectOffidocResponsibleUser(offidocId);
    }

    @Override
    public List<String> selectProcessIdsOfUserdocAndOffidocChildren(CProcessId processId) {
        if (Objects.isNull(processId))
            return null;

        return ipProcRepository.selectProcessIdsOfUserdocAndOffidocChildren(processId.getProcessType(), processId.getProcessNbr());
    }

    @Override
    public long count() {
        return ipProcRepository.count();
    }

    @Override
    public int selectUpperProcessesCount(String procTyp, Integer procNbr) {
        return ipProcRepository.selectUpperProcessesCount(procTyp, procNbr);
    }

    @Override
    public int selectUserdocUpperProcessesCount(String procTyp, Integer procNbr) {
        return ipProcRepository.selectUserdocUpperProcessesCount(procTyp, procNbr);
    }

    @Override
    public void deleteProcess(CProcessId processId) {
        ipProcRepository.deleteById(new IpProcPK(processId.getProcessType(), processId.getProcessNbr()));
    }

    @Override
    public CProcess createManualSubProcess(CProcess parentProcess, String manualSubProcessType) {
        if (Objects.isNull(parentProcess) || Objects.isNull(parentProcess.getProcessId())) {
            throw new RuntimeException("Cannot find parent process !");
        }

        CfProcessType cfProcessType = cfProcessTypeRepository.findById(manualSubProcessType).orElse(null);
        if (Objects.isNull(cfProcessType)) {
            throw new RuntimeException("Cannot find manual sub process type: " + manualSubProcessType);
        }

        Integer relatedToWcode = cfProcessType.getRelatedToWcode();
        if (!ProcessTypeUtils.MANUAL_SUB_PROCESS_TYPE_WCODE.equals(relatedToWcode)) {
            throw new RuntimeException("Selected process type is not for manual sub process ! Process type: " + manualSubProcessType);
        }

        String primaryIniStatusCode = cfProcessType.getPrimaryIniStatusCode();
        if (StringUtils.isEmpty(primaryIniStatusCode)) {
            throw new RuntimeException("Primary ini status code is empty for process type " + manualSubProcessType);
        }

        IpProc process = convertToManualSubProcessEntity(parentProcess, manualSubProcessType, cfProcessType);
        IpProc save = ipProcRepository.save(process);
        return processMapper.toCore(save, false);
    }

    @Override
    public CTopProcessFileData selectTopProcessFileData(CProcessId topProcessId) {
        if (Objects.isNull(topProcessId))
            return null;

        TopProcessFileData topProcessFileData = ipProcRepository.selectTopProcessFileData(topProcessId);
        if (Objects.isNull(topProcessFileData))
            return null;

        return topProcessFileDataMapper.toCore(topProcessFileData);
    }

    @Override
    public boolean isUpperProcessOfUserdocIsInStatusRegistered(CDocumentId documentId) {
        Integer count = ipProcRepository.isUpperProcessOfUserdocIsInStatusRegistered(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
        if (Objects.isNull(count))
            return false;

        return !(count == 0);
    }

    @Override
    public List<Integer> selectSubProcessesResponsibleUserIds(String procTyp, Integer procNbr) {
        List<BigDecimal> userIds = ipProcRepository.selectSubProcessesResponsibleUserIds(procTyp, procNbr);
        return CollectionUtils.isEmpty(userIds)?null:userIds.stream().map(BigDecimal::intValue).collect(Collectors.toList());
    }

    @Override
    public List<CProcessResponsibleUserChange> selectNotProcessedAbdocsUserTargeting() {
        List<IpProcResponsibleUserChanges> ipProcResponsibleUserChanges = ipProcResponsibleUserChangesRepository.selectNotProcessedAbdocsUserTargeting();
        if (CollectionUtils.isEmpty(ipProcResponsibleUserChanges)) {
            return null;
        }

        return processResponsibleUserChangeMapper.toCoreList(ipProcResponsibleUserChanges);
    }

    @Override
    public void updateAbdocsUserTargetingAsProcessed(String processType, Integer processNbr, Integer changeNbr) {
        ipProcResponsibleUserChangesRepository.updateAbdocsUserTargetingAsProcessed(processType, processNbr, changeNbr);
    }

    @Override
    public void updateUserdocProcessCreationDate(Date creationDate, String docOri, String docLog, Integer docSer, Integer docNbr) {
        ipProcRepository.updateUserdocProcessCreationDate(creationDate, docOri, docLog, docSer, docNbr);
    }

    @Override
    public String selectUserdocTypeByProcessId(CProcessId processId) {
        if (Objects.isNull(processId) || Objects.isNull(processId.getProcessType()) || Objects.isNull(processId.getProcessNbr())) {
            return null;
        }

        return ipProcRepository.selectUserdocTypeByProcessId(processId.getProcessType(), processId.getProcessNbr());
    }

    private IpProc convertToManualSubProcessEntity(CProcess parentProcess, String manualSubProcessType, CfProcessType cfProcessType) {
        CProcessId parentProcessId = parentProcess.getProcessId();
        CProcessId topProcessId = parentProcess.getProcessOriginData().getTopProcessId();
        CUser responsibleUser = parentProcess.getResponsibleUser();

        IpProc process = new IpProc();
        process.setRowVersion(DefaultValue.ROW_VERSION);
        process.setPk(new IpProcPK(manualSubProcessType, sequenceRepository.getNextSequenceValue(SequenceRepository.SEQUENCE_NAME.SEQUENCE_NAME_PROC_NBR)));
        process.setCreationDate(new Date());
        process.setStatusCode(new CfStatus(new CfStatusPK(cfProcessType.getProcTyp(), cfProcessType.getPrimaryIniStatusCode())));
        process.setStatusDate(new Date());

        IpProc upperProc = new IpProc();
        upperProc.setPk(new IpProcPK(parentProcessId.getProcessType(), parentProcessId.getProcessNbr()));
        process.setUpperProc(upperProc);

        if (Objects.nonNull(responsibleUser)) {
            process.setResponsibleUser(new IpUser(responsibleUser.getUserId()));
        }

        process.setFileProcTyp(topProcessId.getProcessType());
        process.setFileProcNbr(topProcessId.getProcessNbr());
        return process;
    }

    public void updateStatusCodeAndDateById(String statusCode,CProcessId processId) {
        if (Objects.isNull(processId) || Objects.isNull(processId.getProcessType()) || Objects.isNull(processId.getProcessNbr()) || Objects.isNull(statusCode)) {
            return;
        }

        ipProcRepository.updateStatusCodeAndDateById(statusCode, processId.getProcessType(), processId.getProcessNbr());
    }

    @Override
    public List<CUserdocDocData> selectSubUserdocPartialDataByUserdocTyp(CProcessId processId, String userdocType) {
        List<Object[]> result = ipProcRepository.selectSubUserdocPartialDataByUserdocTyp(processId.getProcessType(), processId.getProcessNbr(), userdocType);
        return convertSubUserdocPartialDataToMap(result);
    }

    private List<CUserdocDocData> convertSubUserdocPartialDataToMap(List<Object[]> result) {
        List<CUserdocDocData> docDataList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                docDataList.add(CUserdocDocData.builder().externalSystemId((String) obj[0]).filingDate((Date) obj[1]).statusName((String) obj[2]).build());
            }
        }
        return docDataList;
    }
}