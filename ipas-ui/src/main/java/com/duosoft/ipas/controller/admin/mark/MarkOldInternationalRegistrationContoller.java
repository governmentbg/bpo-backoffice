package com.duosoft.ipas.controller.admin.mark;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.mark.CMarkOldInternationalRegistration;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.mark.MarkOldInternationalRegistrationService;
import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.DefaultValue;
import com.duosoft.ipas.util.ProgressBarUtils;
import com.duosoft.ipas.webmodel.ProgressBar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/mark-old-international-registration")
public class MarkOldInternationalRegistrationContoller {
    private static final ProgressBar progressBar = new ProgressBar();

    private final MarkOldInternationalRegistrationService markOldInternationalRegistrationService;
    private final FileService fileService;
    private final ErrorLogService errorLogService;
    private final MessageSource messageSource;

    @GetMapping
    public String openPage(Model model) {
        model.addAttribute("progressBar", progressBar);
        return "admin/mark/old_international_registration/old_international_registration";
    }

    @PostMapping("/start-progressbar")
    public String start(RedirectAttributes redirectAttributes) {
        if (!progressBar.isInProgress()) {
            progressBar.start();
            redirectAttributes.addFlashAttribute("executeProcess", true);
        }
        return "redirect:/admin/mark-old-international-registration";
    }

    @PostMapping("/stop-progressbar")
    public String stop() {
        progressBar.stop("Interrupted by user !");
        log.debug("Interrupted by user !");
        return "redirect:/admin/mark-old-international-registration";
    }

    @PostMapping("/select-progressbar-info")
    @ResponseBody
    public ProgressBar selectProgressBarInfo() {
        return progressBar;
    }

    @PostMapping("/execute-process")
    @ResponseStatus(value = HttpStatus.OK)
    public void run() {
        executeProcess();
    }

    private void executeProcess() {
        List<CMarkOldInternationalRegistration> oldRegistrations = markOldInternationalRegistrationService.selectUnprocessed();

        if (!CollectionUtils.isEmpty(oldRegistrations)) {
            int total = oldRegistrations.size();
            for (int i = 0; i < total; i++) {
                if (!progressBar.isInProgress()) {
                    return;
                }

                CMarkOldInternationalRegistration oldRegistration = oldRegistrations.get(i);
                String logMessage = null;
                try {
                    CFile mainMark = findMainMark(oldRegistration);

                    if (Objects.nonNull(mainMark)) {
                        CReceptionResponse receptionResponse = markOldInternationalRegistrationService.insertOldInternationalRegistration(oldRegistration, mainMark);
                        logMessage = constructSuccessLogMessage(total, i, receptionResponse, oldRegistration.getBgReference());
                    }
                    updateProcessedDate(oldRegistration);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    logMessage = e.getMessage();
                }

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

    private void updateProcessedDate(CMarkOldInternationalRegistration oldRegistration) {
        oldRegistration.setProcessedDate(new Date());
        markOldInternationalRegistrationService.save(oldRegistration);
    }

    private String constructSuccessLogMessage(int total, int index, CReceptionResponse response, String bgReference) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(index).append(" of ").append(total).append(".")
                .append(" BG reference: ").append(bgReference).append(".")
                .append(" Added document: ").append(response.getDocId())
                .append(" Docflow system id: ").append(response.getDocflowDocumentId())
                .append(" External system id: ").append(response.getExternalSystemId());
        return stringBuilder.toString();
    }

    private CFile findMainMark(CMarkOldInternationalRegistration oldRegistration) {
        List<CFile> marks = new ArrayList<>();

        if (Objects.nonNull(oldRegistration.getRegistrationNbr())) {
            marks = fileService.findAllByRegistrationNbrAndDupAndFileType(oldRegistration.getRegistrationNbr(), oldRegistration.getRegistrationDup(), FileType.getNationalMarkFileTypes());
        } else if (Objects.nonNull(oldRegistration.getFileNbr())) {
            marks = fileService.findAllByFileNbrAndFileType(oldRegistration.getFileNbr(), FileType.getNationalMarkFileTypes());
        }

        if (!CollectionUtils.isEmpty(marks) && marks.size() == DefaultValue.ONE_RESULT_SIZE) {
            return marks.get(0);
        } else {
            String errorMessage = "Main mark for old international registration could not be found !";
            String actionTitle = messageSource.getMessage("error.action.find.zmr.mark", null, LocaleContextHolder.getLocale());
            String customMessage = messageSource.getMessage("error.find.zmr.mark", null, LocaleContextHolder.getLocale());
            String instruction = messageSource.getMessage("instruction.find.zmr.mark", new String[]{String.valueOf(oldRegistration.getFileNbr()), String.valueOf(oldRegistration.getRegistrationNbr()), String.valueOf(oldRegistration.getRegistrationDup()), String.valueOf(oldRegistration.getId())}, LocaleContextHolder.getLocale());
            errorLogService.createNewRecord(ErrorLogAbout.IPAS, actionTitle, errorMessage, customMessage, true, instruction, ErrorLogPriority.MEDIUM);
        }

        return null;
    }
}
