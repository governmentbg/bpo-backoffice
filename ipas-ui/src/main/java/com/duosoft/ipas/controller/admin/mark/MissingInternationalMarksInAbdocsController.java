package com.duosoft.ipas.controller.admin.mark;

import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.reception.MissingAbdocsDocumentService;
import com.duosoft.ipas.util.ProgressBarUtils;
import com.duosoft.ipas.webmodel.ProgressBar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/admin/missing-international-marks-in-abdocs")
public class MissingInternationalMarksInAbdocsController {

    private static final ProgressBar progressBar = new ProgressBar();

    @Autowired
    private MarkService markService;

    @Autowired
    private MissingAbdocsDocumentService missingAbdocsDocumentService;

    @GetMapping
    public String openPage(Model model) {
        model.addAttribute("progressBar", progressBar);
        return "admin/mark/missing_international_marks_in_abdocs/missing_international_marks";
    }

    @PostMapping("/start-progressbar")
    public String start(RedirectAttributes redirectAttributes) {
        if (!progressBar.isInProgress()) {
            progressBar.start();
            redirectAttributes.addFlashAttribute("executeProcess", true);
        }
        return "redirect:/admin/missing-international-marks-in-abdocs";
    }

    @PostMapping("/stop-progressbar")
    public String stop() {
        progressBar.stop("Interrupted by user !");
        log.debug("Interrupted by user !");
        return "redirect:/admin/missing-international-marks-in-abdocs";
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
        List<String> internationalMarks = markService.selectInternationalMarkIds();

        if (!CollectionUtils.isEmpty(internationalMarks)) {
            int total = internationalMarks.size();
            for (int i = 0; i < total; i++) {
                if (!progressBar.isInProgress()) {
                    return;
                }

                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                String markFilingNumber = internationalMarks.get(i);
                missingAbdocsDocumentService.insertMissingDocument(markFilingNumber);
                stopWatch.stop();

                String logMessage = "Iteration " + i + " of " + total + ". Time: " + stopWatch.getTotalTimeMillis() + " ms. Mark filing number: " + markFilingNumber;
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
}
