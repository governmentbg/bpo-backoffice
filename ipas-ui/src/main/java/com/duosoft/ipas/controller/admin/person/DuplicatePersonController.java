package com.duosoft.ipas.controller.admin.person;

import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult;
import bg.duosoft.ipas.util.DefaultValue;
import com.duosoft.ipas.util.PersonAdministrationUtils;
import com.duosoft.ipas.util.ProgressBarUtils;
import com.duosoft.ipas.webmodel.ProgressBar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin/person/duplicate")
public class DuplicatePersonController {

    private static final ProgressBar progressBar = new ProgressBar();

    @Autowired
    private PersonService personService;

    @GetMapping
    public String openPage(Model model) {
        model.addAttribute("progressBar",progressBar);
        return "admin/person/duplicate";
    }

    @PostMapping("/start-progressbar")
    public String start(RedirectAttributes redirectAttributes) {
        if (!progressBar.isInProgress()) {
            progressBar.start();
            redirectAttributes.addFlashAttribute("executeProcess", true);
        }
        return "redirect:/admin/person/duplicate";
    }

    @PostMapping("/stop-progressbar")
    public String stop() {
        progressBar.stop("Interrupted by user !");
        log.debug("Interrupted by user !");
        return "redirect:/admin/person/duplicate";
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
        List<SimplePersonAddressResult> persons = selectDatabasePersons();
        Map<String, List<SimplePersonAddressResult>> duplicatedPersonsMap = PersonAdministrationUtils.createDuplicatedPersonsMap(persons);

        Collection<List<SimplePersonAddressResult>> values = duplicatedPersonsMap.values();
        int total = values.size();
        int index = 0;
        for (List<SimplePersonAddressResult> value : values) {
            if (!progressBar.isInProgress()) {
                return;
            }

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            CPerson mainPerson = selectPersonWithHighestPriority(value);
            List<SimplePersonAddressResult> duplicates = value.stream()
                    .filter(result -> !(Objects.equals(result.getPersonNbr(), mainPerson.getPersonNbr()) && Objects.equals(result.getAddrNbr(), mainPerson.getAddressNbr())))
                    .collect(Collectors.toList());

            int totalReplaced = 0;
            for (SimplePersonAddressResult duplicate : duplicates) {
                boolean isReplaced = personService.replacePerson(duplicate.getPersonNbr(), duplicate.getAddrNbr(), mainPerson.getPersonNbr(), mainPerson.getAddressNbr());
                if (!isReplaced) {
                    log.error("Cannot replace persons! Old: " + duplicate.getPersonNbr() + "-" + duplicate.getAddrNbr() + " New: " + mainPerson.getPersonNbr() + "-" + mainPerson.getAddressNbr());
                    continue;
                }
                totalReplaced++;
            }
            stopWatch.stop();
            String logMessage = "Iteration " + index + " of " + total + ". Time: " + stopWatch.getTotalTimeMillis() + " ms. Main person: " + mainPerson.getPersonNbr() + "-" + mainPerson.getAddressNbr() + ", " +
                    "Total duplicates: " + duplicates.size() + ". Total replaced duplicates: " + totalReplaced;
            log.debug(logMessage);
            if(!progressBar.isInterrupt()) {
                progressBar.setMessage(logMessage);
                progressBar.setProgress(ProgressBarUtils.calculateProgress(total, index, true));
            }
            index++;
        }

        if (!progressBar.isInterrupt()) {
            progressBar.successful();
        }
    }

    private List<SimplePersonAddressResult> selectDatabasePersons() {
        List<SimplePersonAddressResult> persons = personService.selectAllSimple(PageRequest.of(0, Integer.MAX_VALUE));
        for (SimplePersonAddressResult person : persons) {
            String data = person.getPersonName() + person.getAddressStreet() + person.getCity();
            String replacedData = data.toLowerCase().replaceAll(PersonAdministrationUtils.DUPLICATE_PERSONS_REMOVE_CHARACTERS_REGEX, DefaultValue.EMPTY_STRING);
            person.setCheckText(replacedData);
        }
        return persons;
    }

    private CPerson selectPersonWithHighestPriority(List<SimplePersonAddressResult> persons) {
        return persons.stream()
                .map(person -> personService.selectPersonByPersonNumberAndAddressNumber(person.getPersonNbr(), person.getAddrNbr()))
                .max(Comparator.comparing(this::calculatePersonRecordPriority)).get();
    }

    private int calculatePersonRecordPriority(CPerson person) {
        final int HIGH = 100;
        final int MEDIUM = 10;
        final int LOW = 1;
        int total = 0;

        if (Objects.nonNull(person.getGralPersonIdNbr()))
            total += HIGH;
        if (!StringUtils.isEmpty(person.getPersonName()))
            total += HIGH;
        if (!StringUtils.isEmpty(person.getAddressStreet()))
            total += HIGH;
        if (!StringUtils.isEmpty(person.getCityName()))
            total += HIGH;
        if (!StringUtils.isEmpty(person.getAgentCode()))
            total += HIGH;
        if (!StringUtils.isEmpty(person.getNationalityCountryCode()))
            total += HIGH;
        if (!StringUtils.isEmpty(person.getResidenceCountryCode()))
            total += MEDIUM;
        if (!StringUtils.isEmpty(person.getAddressZone()))
            total += MEDIUM;
        if (!StringUtils.isEmpty(person.getEmail()))
            total += MEDIUM;
        if (!StringUtils.isEmpty(person.getTelephone()))
            total += MEDIUM;
        if (!StringUtils.isEmpty(person.getZipCode()))
            total += MEDIUM;
        if (!StringUtils.isEmpty(person.getStateName()))
            total += LOW;
        if (!StringUtils.isEmpty(person.getStateCode()))
            total += LOW;
        if (Objects.nonNull(person.getCityCode()))
            total += LOW;

        return total;
    }
}
