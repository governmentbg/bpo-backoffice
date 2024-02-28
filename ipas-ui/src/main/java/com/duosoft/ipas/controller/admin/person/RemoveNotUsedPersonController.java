package com.duosoft.ipas.controller.admin.person;

import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.persistence.model.nonentity.PersonUsageData;
import bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult;
import com.duosoft.ipas.util.ProgressBarUtils;
import com.duosoft.ipas.webmodel.ProgressBar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/person/remove-not-used")
public class RemoveNotUsedPersonController {

    private static final ProgressBar progressBar = new ProgressBar();

    @Autowired
    private PersonService personService;

    @GetMapping
    public String openPage(Model model) {
        model.addAttribute("progressBar", progressBar);
        return "admin/person/remove_non_used";
    }

    @PostMapping("/start-progressbar")
    public String start(RedirectAttributes redirectAttributes) {
        if (!progressBar.isInProgress()) {
            progressBar.start();
            redirectAttributes.addFlashAttribute("executeProcess", true);
        }
        return "redirect:/admin/person/remove-not-used";
    }

    @PostMapping("/stop-progressbar")
    public String stop() {
        progressBar.stop("Interrupted by user !");
        log.debug("Interrupted by user !");
        return "redirect:/admin/person/remove-not-used";
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
        List<SimplePersonAddressResult> persons = personService.selectAllSimple(PageRequest.of(0, Integer.MAX_VALUE));
        int total = persons.size();
        int totalDeleted = 0;
        for (int i = 0; i < total; i++) {
            if (!progressBar.isInProgress()) {
                return;
            }

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            SimplePersonAddressResult person = persons.get(i);
            PersonUsageData personInfo = personService.selectPersonUsageData(person.getPersonNbr(), person.getAddrNbr());
            if (personInfo.getTotal() == 0) {
                boolean isDeleted = personService.deletePerson(person.getPersonNbr(), person.getAddrNbr(), true);
                if (!isDeleted) {
                    throw new RuntimeException("Person with ID: " + person.getPersonNbr() + "-" + person.getAddressStreet() + " cannot be deleted !");
                }
                totalDeleted++;
            }
            stopWatch.stop();

            String logMessage = "Iteration " + i + " of " + total + ". Time: " + stopWatch.getTotalTimeMillis() + " ms. Person: " + person.getPersonNbr() + "-" + person.getAddrNbr() + ", " +
                    "Total uses: " + personInfo.getTotal() + ". Total deleted persons: " + totalDeleted;
            log.debug(logMessage);
            if (!progressBar.isInterrupt()) {
                progressBar.setMessage(logMessage);
                progressBar.setProgress(ProgressBarUtils.calculateProgress(total, i, true));
            }
        }

        if (!progressBar.isInterrupt()) {
            progressBar.successful();
        }
    }


}
