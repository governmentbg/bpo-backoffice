package com.duosoft.ipas.controller.admin.userdoc;

import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.service.reception.MissingAbdocsDocumentService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocIpDocSimpleResult;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin/missing-userdocs-in-abdocs")
public class MissingUserdocsInAbdocsController {

    private static final ProgressBar progressBar = new ProgressBar();
    private static Map<String, String> insertedUserdocs = new LinkedHashMap<>();

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private MissingAbdocsDocumentService missingAbdocsDocumentService;

    @GetMapping
    public String openPage(Model model) {
        model.addAttribute("progressBar", progressBar);
        model.addAttribute("insertedUserdocs", insertedUserdocs);
        return "admin/userdoc/missing_userdocs_in_abdocs/missing_userdocs";
    }

    @PostMapping("/start-progressbar")
    public String start(RedirectAttributes redirectAttributes) {
        if (!progressBar.isInProgress()) {
            progressBar.start();
            redirectAttributes.addFlashAttribute("executeProcess", true);
        }
        return "redirect:/admin/missing-userdocs-in-abdocs";
    }

    @PostMapping("/stop-progressbar")
    public String stop() {
        progressBar.stop("Interrupted by user !");
        log.debug("Interrupted by user !");
        return "redirect:/admin/missing-userdocs-in-abdocs";
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

    @PostMapping("/select-inserted-userdocs")
    public String selectInsertedUserdocs(Model model) {
        model.addAttribute("inProgress", progressBar.isInProgress());
        model.addAttribute("startDate", progressBar.getStartAt());
        model.addAttribute("result", insertedUserdocs);
        return "admin/userdoc/missing_userdocs_in_abdocs/missing_userdocs_table :: table";
    }

    private void executeProcess() {
        List<UserdocIpDocSimpleResult> userdocs = userdocService.selectUserdocsFromAcstre();

        if (!CollectionUtils.isEmpty(userdocs)) {
            int total = userdocs.size();
            for (int i = 0; i < total; i++) {
                if (!progressBar.isInProgress()) {
                    return;
                }

                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                UserdocIpDocSimpleResult userdocSimpleResult = userdocs.get(i);
                String userdocFilingNumber = userdocSimpleResult.getDocumentId().createFilingNumber();
                Integer documentId = abdocsServiceAdmin.selectDocumentIdByRegistrationNumber(userdocSimpleResult.getRegistrationNumber());

                if (documentId == null) {
                    Map<String, String> newRegistrationNumbers = missingAbdocsDocumentService.insertMissingDocument(userdocFilingNumber);
                    if (!CollectionUtils.isEmpty(newRegistrationNumbers)) {
                        insertedUserdocs.putAll(newRegistrationNumbers);
                    }
                }
                stopWatch.stop();

                String logMessage = "Iteration " + i + " of " + total + ". Time: " + stopWatch.getTotalTimeMillis() + " ms. Userdoc filing number: " + userdocFilingNumber;
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
