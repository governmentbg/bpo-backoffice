package com.duosoft.ipas.controller.admin.person;

import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.service.nomenclature.CountryService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.persistence.model.nonentity.PersonUsageData;
import bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.filter.MergePersonFilter;
import bg.duosoft.ipas.core.model.search.Pageable;
import com.duosoft.ipas.util.PersonAdministrationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin/person/merge")
public class MergePersonController {

    private static Map<String, List<SimplePersonAddressResult>> duplicatedPersonsMap;

    @Autowired
    private PersonService personService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String openPage(Model model, HttpSession session) {
        session.removeAttribute("mergePersonFilter");
        MergePersonFilter filter = new MergePersonFilter();
        model.addAttribute("mergePersonFilter", filter);
        return "admin/person/merge/merge";
    }

    @PostMapping(value = "/load-merge-persons")
    public String loadMergePersonsList(Model model) {
        MergePersonFilter filter = new MergePersonFilter();

        List<SimplePersonAddressResult> persons = selectDatabasePersons(filter);
        duplicatedPersonsMap = PersonAdministrationUtils.createDuplicatedPersonsMap(persons);
        List<SimplePersonAddressResult> results = duplicatedPersonsMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
        List<SimplePersonAddressResult> pageResult = getMergePersonPage(Pageable.DEFAULT_PAGE, Pageable.DEFAULT_PAGE_SIZE, results);
        fillModelAttributes(model, pageResult, filter, Pageable.DEFAULT_PAGE, Pageable.DEFAULT_PAGE_SIZE, results.size());
        return "admin/person/merge/merge_table :: table";
    }

    @PostMapping(value = "/update-map")
    public String updateDuplicatedPersonsMap() {
        return "redirect:/admin/person/merge";
    }

    @PostMapping(value = "/search")
    public String searchMergePersons(Model model, HttpSession session, MergePersonFilter filter) {
        List<SimplePersonAddressResult> results;

        if (!StringUtils.isEmpty(filter.getPersonName()) || !StringUtils.isEmpty(filter.getPersonAddress())) {
            session.setAttribute("mergePersonFilter", filter);
            List<SimplePersonAddressResult> persons = selectDatabasePersons(filter);
            Map<String, List<SimplePersonAddressResult>> duplicates = PersonAdministrationUtils.createDuplicatedPersonsMap(persons);
            results = duplicates.values().stream().flatMap(List::stream).collect(Collectors.toList());
        } else {
            session.removeAttribute("mergePersonFilter");
            results = duplicatedPersonsMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
        }

        List<SimplePersonAddressResult> pageResult = getMergePersonPage(Pageable.DEFAULT_PAGE, Pageable.DEFAULT_PAGE_SIZE, results);

        fillModelAttributes(model, pageResult, filter, Pageable.DEFAULT_PAGE, Pageable.DEFAULT_PAGE_SIZE, results.size());
        return "admin/person/merge/merge_table :: table";
    }

    @PostMapping(value = "/update-table")
    public String updateMergePersonsTable(Model model, HttpSession session,
                                          @RequestParam(required = false) Integer pageSize,
                                          @RequestParam(required = false) Integer page) {
        MergePersonFilter filter = (MergePersonFilter) session.getAttribute("mergePersonFilter");
        List<SimplePersonAddressResult> results;

        if (Objects.isNull(filter)) {
            filter = new MergePersonFilter();
            results = duplicatedPersonsMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
        } else {
            Map<String, List<SimplePersonAddressResult>> duplicates = PersonAdministrationUtils.createDuplicatedPersonsMap(selectDatabasePersons(filter));
            results = duplicates.values().stream().flatMap(List::stream).collect(Collectors.toList());
        }

        List<SimplePersonAddressResult> pageResults = getMergePersonPage(page, pageSize, results);

        fillModelAttributes(model, pageResults, filter, page, pageSize, results.size());
        return "admin/person/merge/merge_table :: table";
    }

    @GetMapping(value = "/edit/{checkText}")
    public String personMergeDetailsPage(@PathVariable("checkText") String checkText, Model model) {
        List<SimplePersonAddressResult> duplicatePersonsSimple = duplicatedPersonsMap.get(checkText);
        List<CPerson> duplicatePersons = new ArrayList<>();

        if (Objects.nonNull(duplicatePersonsSimple)) {
            for (SimplePersonAddressResult duplicate : duplicatePersonsSimple) {
                CPerson person = personService.selectPersonByPersonNumberAndAddressNumber(duplicate.getPersonNbr(), duplicate.getAddrNbr());
                duplicatePersons.add(person);
            }
        }

        model.addAttribute("mergePersonList", duplicatePersons);
        model.addAttribute("checkText", checkText);
        return "admin/person/merge/merge_edit";
    }

    @PostMapping(value = "/merge-info")
    public String personMergeDetails(@RequestParam Integer personNbr, @RequestParam Integer addressNbr, Model model) {
        PersonUsageData usageData = personService.selectPersonUsageData(personNbr, addressNbr);
        CPerson person = personService.selectPersonByPersonNumberAndAddressNumber(personNbr,addressNbr);
        model.addAttribute("personUsageMap", usageData.getTablesInfo());
        model.addAttribute("person", person);
        model.addAttribute("countryMap", countryService.getCountryMap());
        model.addAttribute("totalUses", usageData.getTotal());
        return "admin/person/merge/person_merge_info_modal :: info-modal";
    }

    @PostMapping(value = "/execute-merge")
    public String executePersonsMerge(@RequestParam Integer mainPersonNbr, @RequestParam Integer mainAddressNbr, @RequestParam String checkText, RedirectAttributes redirectAttributes) {
        List<SimplePersonAddressResult> duplicates = duplicatedPersonsMap.get(checkText);
        CPerson mainPerson = personService.selectPersonByPersonNumberAndAddressNumber(mainPersonNbr, mainAddressNbr);

        if (Objects.nonNull(duplicates) && Objects.nonNull(mainPerson)) {
            duplicates = duplicates.stream()
                    .filter(result -> !(Objects.equals(result.getPersonNbr(), mainPerson.getPersonNbr()) && Objects.equals(result.getAddrNbr(), mainPerson.getAddressNbr())))
                    .collect(Collectors.toList());

            int totalReplaced = 0;
            for (SimplePersonAddressResult duplicate : duplicates) {
                boolean isReplaced = personService.replacePerson(duplicate.getPersonNbr(), duplicate.getAddrNbr(), mainPerson.getPersonNbr(), mainPerson.getAddressNbr());
                if (!isReplaced) {
                    log.error("Cannot replace merged persons! Old: " + duplicate.getPersonNbr() + "-" + duplicate.getAddrNbr() + " New: " + mainPerson.getPersonNbr() + "-" + mainPerson.getAddressNbr());
                    continue;
                }
                totalReplaced++;
            }

            if (totalReplaced == duplicates.size()) {
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("merge.persons.success", null, LocaleContextHolder.getLocale()));
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("merge.persons.error", null, LocaleContextHolder.getLocale()));
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("merge.persons.error", null, LocaleContextHolder.getLocale()));
        }
        return "redirect:/admin/person/merge";
    }

    private List<SimplePersonAddressResult> selectDatabasePersons(MergePersonFilter filter) {
        List<SimplePersonAddressResult> persons = personService.selectMergeSimple(filter);

        for (SimplePersonAddressResult person : persons) {
            String data = person.getPersonName() + person.getAddressStreet();
            String replacedData = data.toLowerCase().replaceAll(PersonAdministrationUtils.DUPLICATE_PERSONS_REMOVE_CHARACTERS_REGEX, DefaultValue.EMPTY_STRING);
            person.setCheckText(replacedData);
        }

        return persons;
    }

    private void fillModelAttributes(Model model, List<SimplePersonAddressResult> results, MergePersonFilter filter, Integer page, Integer pageSize, Integer total) {
        model.addAttribute("mergePersonList", results);
        model.addAttribute("mergePersonCount", total);
        model.addAttribute("mergePersonFilter", filter);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
    }

    private List<SimplePersonAddressResult> getMergePersonPage(Integer page, Integer pageSize, List<SimplePersonAddressResult> results) {
        int firstPageResult = ((page - 1) * pageSize);
        int lastPageResult = firstPageResult + pageSize;
        int totalResults = results.size();

        if (totalResults > lastPageResult) {
            return results.subList(firstPageResult, lastPageResult);
        } else if (totalResults >= firstPageResult && totalResults <= lastPageResult) {
            return results.subList(firstPageResult, totalResults);
        } else {
            return null;
        }
    }

}
