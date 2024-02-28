package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.model.miscellaneous.CSettlement;
import bg.duosoft.ipas.core.model.util.PersonAddressResult;
import bg.duosoft.ipas.core.service.nomenclature.SettlementService;
import bg.duosoft.ipas.core.service.search.PersonAddressSearchService;
import bg.duosoft.ipas.core.model.search.Pageable;
import com.duosoft.ipas.util.PersonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/person")
public class PersonAutocompleteController {

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private PersonAddressSearchService personSearchService;

    @GetMapping(value = "/agent-autocomplete", produces = "application/json")
    @ResponseBody
    public List<PersonAddressResult> agentAutocomplete(@RequestParam String nameOrCode, @RequestParam(required = false) String representativeType, @RequestParam boolean onlyActive) {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setOnlyAgent(true);
        cCriteriaPerson.setOnlyActiveAgent(onlyActive);
        cCriteriaPerson.setAgentCodeOrNameContainsWords(nameOrCode);
        cCriteriaPerson.setPage(0);
        cCriteriaPerson.setPageSize(Pageable.PAGE_SIZE_100);
        Page<PersonAddressResult> search = personSearchService.search(cCriteriaPerson);
        List<PersonAddressResult> persons = new ArrayList<>(search.getContent());// Important for sorting
        if (!CollectionUtils.isEmpty(persons)) {
            if (!StringUtils.isEmpty(representativeType)) {
                RepresentativeType representativeTypeEnum = Enum.valueOf(RepresentativeType.class, representativeType);
                switch (representativeTypeEnum) {
                    case NATURAL_PERSON:
                        persons.removeIf(r -> r.getAgentCode().startsWith(DefaultValue.PARTNERSHIP_PREFIX));
                        break;
                    case PARTNERSHIP:
                        persons.removeIf(r -> !r.getAgentCode().startsWith(DefaultValue.PARTNERSHIP_PREFIX));
                        break;
                }
            }
            persons.sort(PersonUtils.agentComparator().reversed());
        }
        return persons;
    }

    @GetMapping(value = "/person-name-autocomplete", produces = "application/json")
    @ResponseBody
    public Set<String> personNameAutocomplete(@RequestParam String nameLike) {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords(nameLike);
        cCriteriaPerson.setPage(0);
        cCriteriaPerson.setPageSize(Pageable.PAGE_SIZE_100);
        Page<PersonAddressResult> search = personSearchService.search(cCriteriaPerson);
        List<PersonAddressResult> persons = search.getContent();
        if (CollectionUtils.isEmpty(persons))
            return null;

        return persons.stream()
                .map(PersonAddressResult::getPersonName)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping(value = "/foreign-settlement-autocomplete", produces = "application/json")
    @ResponseBody
    public Set<String> foreignSettlementAutocomplete(@RequestParam String nameLike, @RequestParam String country) {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setCityContainsWords(nameLike);
        cCriteriaPerson.setResidenceCountryCode(country);
        cCriteriaPerson.setPage(0);
        cCriteriaPerson.setPageSize(Pageable.PAGE_SIZE_100);
        Page<PersonAddressResult> search = personSearchService.search(cCriteriaPerson);
        List<PersonAddressResult> persons = search.getContent();
        if (CollectionUtils.isEmpty(persons))
            return null;

        return persons.stream()
                .map(PersonAddressResult::getCityName)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping(value = "/settlement-autocomplete", produces = "application/json")
    @ResponseBody
    public List<CSettlement> settlementAutocomplete(@RequestParam String nameLike) {
        List<CSettlement> cSettlements = settlementService.selectByNameLikeOrSettlementNameLike(nameLike);
        if (!CollectionUtils.isEmpty(cSettlements)) {
            cSettlements.sort(Comparator.comparing(CSettlement::getName));
        }
        return cSettlements;
    }

}
