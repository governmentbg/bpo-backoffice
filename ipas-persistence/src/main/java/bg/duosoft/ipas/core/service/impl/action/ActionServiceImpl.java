package bg.duosoft.ipas.core.service.impl.action;

import bg.duosoft.abdocs.model.response.DeleteDocumentResponse;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.mapper.action.ActionMapper;
import bg.duosoft.ipas.core.mapper.action.ActiontIdMapper;
import bg.duosoft.ipas.core.model.action.CAction;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.error.CErrorLog;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.core.validation.action.ActionDateValidator;
import bg.duosoft.ipas.core.validation.action.ActionValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpOffidocAbdocsDocument;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidoc;
import bg.duosoft.ipas.persistence.repository.entity.action.IpActionRepository;
import bg.duosoft.ipas.persistence.repository.entity.ext.IpOffidocAbdocsDocumentRepository;
import bg.duosoft.ipas.util.date.DateUtils;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@LogExecutionTime
public class ActionServiceImpl implements ActionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IpActionRepository ipActionRepository;

    @Autowired
    private ActiontIdMapper actiontIdMapper;

    @Autowired
    private ActionMapper actionMapper;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private IpOffidocAbdocsDocumentRepository ipOffidocAbdocsDocumentRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public CAction selectAction(CActionId cActionId) {
        if (Objects.isNull(cActionId))
            return null;

        IpActionPK ipActionPK = actiontIdMapper.toEntity(cActionId);
        IpAction ipAction = ipActionRepository.findById(ipActionPK).orElse(null);
        if (Objects.isNull(ipAction))
            return null;

        return actionMapper.toCore(ipAction);
    }

    @Override
    public boolean isUserdocAuthorizationActionsExists(CProcessId processId) {
        Integer count = ipActionRepository.isUserdocAuthorizationActionsExists(processId.getProcessType(), processId.getProcessNbr());
        return !(Objects.isNull(count) || 0 == count);
    }

    @Override
    @IpasValidatorDefinition({ActionValidator.class, ActionDateValidator.class})
    public void updateAction(CAction cAction) {
        log.debug("Trying to update action with pk = " + cAction.getActionId());

        IpAction ipAction = actionMapper.toEntity(cAction);
        if (Objects.isNull(ipAction))
            throw new RuntimeException("Converted action is empty !");

        IpAction originalAction = ipActionRepository.findById(ipAction.getPk()).orElse(null);
        if (Objects.isNull(originalAction))
            throw new RuntimeException("Original action does not exist... ");

        entityManager.detach(originalAction);
        actionMapper.fillActionFields(cAction, originalAction);
        entityManager.merge(originalAction);

        log.debug(String.format("Updated action with pk = %s ", cAction.getActionId()));
    }

    @Override
    public CAction selectLastInsertedAction(String procTyp, Integer procNumber) {
        if (Objects.isNull(procNumber) || StringUtils.isEmpty(procTyp))
            return null;

        IpAction ipAction = ipActionRepository.selectLastInsertedAction(procTyp, procNumber);
        if (Objects.isNull(ipAction))
            return null;

        return actionMapper.toCore(ipAction);
    }

    @Override
    public boolean deleteAction(CActionId cActionId, Integer userId, String reason) {
        IpActionPK ipActionPk = actiontIdMapper.toEntity(cActionId);
        IpAction ipAction = ipActionRepository.findById(ipActionPk).orElse(null);
        if (Objects.isNull(ipAction)) {
            return false;
        }

        Integer abdocsId = null;
        IpOffidoc ipOffidoc = ipAction.getIpOffidoc();
        if (Objects.nonNull(ipOffidoc)){
            abdocsId = selectAbdocsId(ipOffidoc);
        }

        boolean isDeleted = ipActionRepository.deleteProcessAction(cActionId.getProcessId().getProcessType(), String.valueOf(cActionId.getProcessId().getProcessNbr()), cActionId.getActionNbr(), userId, reason);
        if (!isDeleted) {
            return false;
        }

        deleteOffidocInAbdocs(ipOffidoc, abdocsId);
        return true;
    }

    @Override
    public boolean isActionExistsForProcessId(CProcessId processId) {
        Integer count = ipActionRepository.countByProcessId(processId.getProcessType(), processId.getProcessNbr());
        return !(Objects.isNull(count) || 0 == count);
    }

    @Override
    public long count() {
        return ipActionRepository.count();
    }

    @Override
    public Date selectMaxActionDateByDate(CProcessId processId, Date date) {
        if (Objects.isNull(processId) || Objects.isNull(date))
            return null;

        LocalDate localDate = DateUtils.convertToLocalDate(date);
        Date from = DateUtils.convertToDate(LocalDateTime.of(localDate, LocalTime.MIN));
        Date to = DateUtils.convertToDate(LocalDateTime.of(localDate, LocalTime.MAX));
        return ipActionRepository.selectMaxActionDateByDate(processId.getProcessType(), processId.getProcessNbr(), from, to);
    }

    @Override
    public boolean hasPublications(String fileSeq, String fileType, Integer fileSer, Integer fileNbr) {
        return !CollectionUtils.isEmpty(ipActionRepository.getPublications(fileSeq,fileType,fileSer,fileNbr));
    }

    private void deleteOffidocInAbdocs(IpOffidoc ipOffidoc, Integer abdocsDocId) {
        if (Objects.nonNull(abdocsDocId)) {
            try {
                DeleteDocumentResponse response = abdocsServiceAdmin.deleteDocument(abdocsDocId);
                if (Objects.isNull(response.getParentId()))
                    throw new RuntimeException("Office document is not deleted in abdocs !");

                log.debug("Office document is deleted successfully ! Office document: " + ipOffidoc.getPk().toString());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                cancelOffidocInAbdocs(ipOffidoc, abdocsDocId);
            }
        }
    }

    private boolean cancelOffidocInAbdocs(IpOffidoc ipOffidoc, Integer abdocsDocId) {
        boolean isCancelled = false;
        try {
            abdocsServiceAdmin.cancelDocument(abdocsDocId);
            log.debug("Office document cannot be deleted so it is cancelled ! Office document: " + ipOffidoc.getPk().toString());
            isCancelled = true;
        } catch (Exception e) {
            log.debug("Office docuemnt cannot be cancelled ! Office document: " + ipOffidoc.getPk().toString());
            log.error(e.getMessage(), e);

            String actionTitle = messageSource.getMessage("error.action.delete.process.action", null, LocaleContextHolder.getLocale());
            String customMessage = messageSource.getMessage("error.abdocs.cancel.offidoc", new String[]{String.valueOf(abdocsDocId), ipOffidoc.getPk().toString(), ipOffidoc.getProcTyp() + " " + ipOffidoc.getOffidocProcNbr()}, LocaleContextHolder.getLocale());
            String instruction = messageSource.getMessage("instruction.abdocs.cancel.offidoc", new String[]{String.valueOf(abdocsDocId)}, LocaleContextHolder.getLocale());
            errorLogService.createNewRecord(ErrorLogAbout.ABDOCS, actionTitle, e.getMessage(), customMessage, true, instruction, ErrorLogPriority.MEDIUM);
        }

        return isCancelled;
    }

    private Integer selectAbdocsId(IpOffidoc ipOffidoc) {
        try {
            IpOffidocAbdocsDocument ipOffidocAbdocsDocument = ipOffidocAbdocsDocumentRepository.findById(ipOffidoc.getPk()).orElse(null);
            if (Objects.isNull(ipOffidocAbdocsDocument)){
                log.warn("Missing record in EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT for office document = " + ipOffidoc.getPk().toString());
                return null;
            }

            Integer abdocsDocId = ipOffidocAbdocsDocument.getAbdocsDocumentId();
            if (Objects.isNull(abdocsDocId))
                throw new RuntimeException("Cannot find abdocs id for office document " + ipOffidoc.getPk().toString());

            return abdocsDocId;
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            String process = ipOffidoc.getProcTyp() + " " + ipOffidoc.getOffidocProcNbr();
            String actionTitle = messageSource.getMessage("error.action.delete.process.action", null, LocaleContextHolder.getLocale());
            String customMessage = messageSource.getMessage("error.dms.doc.map.missing.offidoc.id", new String[]{ipOffidoc.getPk().toString(), process}, LocaleContextHolder.getLocale());
            String instruction = messageSource.getMessage("instruction.dms.doc.map.missing.offidoc.id", new String[]{ipOffidoc.getPk().toString(), process}, LocaleContextHolder.getLocale());
            errorLogService.save(CErrorLog.create(ErrorLogAbout.ABDOCS, actionTitle, e.getMessage(), customMessage, true, instruction, ErrorLogPriority.MEDIUM, SecurityUtils.getLoggedUsername()));
            return null;
        }
    }

}