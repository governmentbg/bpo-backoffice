package com.duosoft.ipas.controller.admin.person;

import bg.duosoft.ipas.core.model.person.CPersonId;
import bg.duosoft.ipas.core.service.person.PersonService;
import com.duosoft.ipas.util.ProgressBarUtils;
import com.duosoft.ipas.webmodel.ProgressBar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/person/split")
public class SplitPersonController {

    private static final ProgressBar progressBar = new ProgressBar();

    @Autowired
    private PersonService personService;

    @GetMapping
    public String openPage(Model model) {
        model.addAttribute("progressBar", progressBar);
        return "admin/person/split";
    }

    @PostMapping("/start-progressbar")
    public String start(RedirectAttributes redirectAttributes) {
        if (!progressBar.isInProgress()) {
            progressBar.start();
            redirectAttributes.addFlashAttribute("executeProcess", true);
        }
        return "redirect:/admin/person/split";
    }

    @PostMapping("/stop-progressbar")
    public String stop() {
        progressBar.stop("Interrupted by user !");
        return "redirect:/admin/person/split";
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

    public void executeProcess() {
        List<CPersonId> personAddresses = personService.selectIdentifiersOfAllNotMainPersonAddresses();
        if (!CollectionUtils.isEmpty(personAddresses)) {
            int total = personAddresses.size();
            for (int i = 0; i < total; i++) {
                if (!progressBar.isInProgress()) {
                    return;
                }

                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                CPersonId id = personAddresses.get(i);
                CPersonId newIpPersonPk = personService.splitIpPerson(id.getPersonNbr(), id.getAddressNbr());
                stopWatch.stop();

                String logMessage = "Iteration " + i + " of " + total + ". Time: " + stopWatch.getTotalTimeMillis() + " ms. " +
                        "Old person: " + id.getPersonNbr() + "-" + id.getAddressNbr() + ", " +
                        "New person: " + newIpPersonPk.getPersonNbr() + "-" + newIpPersonPk.getAddressNbr() + ". ";
                log.debug(logMessage);
                progressBar.setMessage(logMessage);
                progressBar.setProgress(ProgressBarUtils.calculateProgress(total, i, true));
            }
        }

        if (!progressBar.isInterrupt()) {
            progressBar.successful();
        }
    }

}
