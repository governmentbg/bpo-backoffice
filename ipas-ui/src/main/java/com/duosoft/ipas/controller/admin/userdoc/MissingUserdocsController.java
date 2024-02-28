package com.duosoft.ipas.controller.admin.userdoc;

import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocOldDocument;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.core.service.reception.MissingAbdocsDocumentService;
import bg.duosoft.ipas.core.service.userdoc.UserdocOldDocumentService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import bg.duosoft.ipas.util.abdocs.AbdocsUtils;
import com.duosoft.ipas.util.ProgressBarUtils;
import com.duosoft.ipas.webmodel.ProgressBar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/admin/missing-userdocs")
public class MissingUserdocsController {
    private static final ProgressBar progressBar = new ProgressBar();

    @Autowired
    private UserdocOldDocumentService userdocOldDocumentService;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private MissingAbdocsDocumentService missingAbdocsDocumentService;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String openPage(Model model) {
        model.addAttribute("progressBar", progressBar);
        return "admin/userdoc/missing_userdocs/missing_userdocs";
    }

    @PostMapping("/start-progressbar")
    public String start(RedirectAttributes redirectAttributes) {
        if (!progressBar.isInProgress()) {
            progressBar.start();
            redirectAttributes.addFlashAttribute("executeProcess", true);
        }
        return "redirect:/admin/missing-userdocs";
    }

    @PostMapping("/stop-progressbar")
    public String stop() {
        progressBar.stop("Interrupted by user !");
        log.debug("Interrupted by user !");
        return "redirect:/admin/missing-userdocs";
    }

    @PostMapping("/execute-process")
    @ResponseStatus(value = HttpStatus.OK)
    public void run() {
        try {
            executeProcess();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            progressBar.stop(e.getMessage());
        }
    }

    @PostMapping("/select-progressbar-info")
    @ResponseBody
    public ProgressBar selectProgressBarInfo() {
        return progressBar;
    }

    private void executeProcess() {
        List<CUserdocOldDocument> oldDocuments = userdocOldDocumentService.selectAll();

        if (!CollectionUtils.isEmpty(oldDocuments)) {
            int total = oldDocuments.size();
            for (int i = 0; i < total; i++) {
                if (!progressBar.isInProgress()) {
                    return;
                }

                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                CUserdocOldDocument oldDocument = oldDocuments.get(i);
                Integer abdocsId = abdocsService.selectDocumentIdByRegistrationNumber(oldDocument.getExternalSystemId());
                Document abdocsDocument = Objects.nonNull(abdocsId) ? abdocsService.selectDocumentById(abdocsId) : null;
                CUserdoc userdoc = userdocService.selectByExternalSystemId(oldDocument.getExternalSystemId());
                CReceptionResponse receptionResponse = null;

                if (Objects.isNull(userdoc) && (Objects.nonNull(abdocsDocument) || oldDocument.getRegisterInAbdocs())) {
                    CFileId fileId = new CFileId(oldDocument.getFileSeq(), oldDocument.getFileType(), oldDocument.getFileSeries(), oldDocument.getFileNbr());
                    receptionResponse = userdocOldDocumentService.insertOldUserdoc(oldDocument, abdocsDocument, fileId, oldDocument.getRegisterInAbdocs());

                    try {
                        AbdocsUtils.updateDocumentParent(receptionResponse.getDocflowDocumentId(), fileId, missingAbdocsDocumentService, abdocsService);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        String actionTitle = messageSource.getMessage("error.action.abdocs.change.parent", null, LocaleContextHolder.getLocale());
                        String customMessage = messageSource.getMessage("error.abdocs.change.parent", new String[]{oldDocument.getExternalSystemId(), fileId.createFilingNumber()}, LocaleContextHolder.getLocale());
                        String instruction = messageSource.getMessage("instruction.abdocs.change.parent", new String[]{oldDocument.getExternalSystemId()}, LocaleContextHolder.getLocale());
                        errorLogService.createNewRecord(ErrorLogAbout.ABDOCS, actionTitle, e.getMessage(), customMessage, true, instruction, ErrorLogPriority.MEDIUM);
                    }
                }
                stopWatch.stop();

                String logMessage = constructLogMessage(total, i, stopWatch.getTotalTimeMillis(), receptionResponse);
                log.debug(logMessage);
                if (!progressBar.isInterrupt()) {
                    progressBar.setMessage(logMessage);
                    progressBar.setProgress(ProgressBarUtils.calculateProgress(total, i, true));
                }
            }
        }

        if (!progressBar.isInterrupt()) {
            progressBar.successful();
        }
    }

    private String constructLogMessage(int total, int i, long time, CReceptionResponse receptionResponse) {
        StringBuilder builder = new StringBuilder();
        builder.append("Iteration ").append(i).append(" of ").append(total).append(". Time: ").append(time).append(" ms.");

        if (Objects.nonNull(receptionResponse)) {
            builder.append("Created userdoc: ").append(receptionResponse.getDocId().createFilingNumber()).append(" for abdocs document with external system id: ").append(receptionResponse.getExternalSystemId());
        }
        return builder.toString();
    }

}
