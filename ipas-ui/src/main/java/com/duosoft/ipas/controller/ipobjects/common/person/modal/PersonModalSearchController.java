package com.duosoft.ipas.controller.ipobjects.common.person.modal;

import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.model.util.PersonAddressResult;
import bg.duosoft.ipas.core.service.search.PersonAddressSearchService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.enums.PersonIdType;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.enums.search.PersonNameSearchType;
import com.duosoft.ipas.webmodel.PersonModalSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/person/search-modal")
public class PersonModalSearchController {

    @Autowired
    private PersonAddressSearchService personSearchService;

    @PostMapping("/open")
    public String openSearchPersonModal(HttpServletRequest request, Model model,
                                        @RequestParam Integer personKind,
                                        @RequestParam(required = false) Integer tempParentPersonNbr,
                                        @RequestParam String sessionIdentifier) {

        model.addAttribute("personKind", personKind);
        model.addAttribute("tempParentPersonNbr", tempParentPersonNbr);
        return "ipobjects/common/person/modal/person_search_modal :: person-search";
    }

    @PostMapping("/search")
    public String searchPerson(HttpServletRequest request, Model model,
                               @RequestParam Integer personKind,
                               @RequestParam(required = false) Integer tempParentPersonNbr,
                               @RequestParam(required = false) String representativeType,
                               @RequestParam String data) {
        PersonModalSearchForm searchForm = JsonUtil.readJson(data, PersonModalSearchForm.class);

        List<ValidationError> errors = validateSearchForm(searchForm);
        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("validationErrors", errors);
            model.addAttribute("searchForm", searchForm);
            return "ipobjects/common/person/modal/person_search_modal :: person-search-modal-body";
        }

        CCriteriaPerson cCriteriaPerson = (CCriteriaPerson) creteSearchCriteria(searchForm).page(0).pageSize(Pageable.PAGE_SIZE_100);
        Page<PersonAddressResult> search = personSearchService.search(cCriteriaPerson);
        model.addAttribute("resultList", search.getContent());
        model.addAttribute("personKind", personKind);
        model.addAttribute("tempParentPersonNbr", tempParentPersonNbr);
        model.addAttribute("representativeType", representativeType);
        return "ipobjects/common/person/modal/person_search_modal :: person-search-modal-result";
    }

    private List<ValidationError> validateSearchForm(PersonModalSearchForm searchForm) {
        List<ValidationError> errors = new ArrayList<>();
        int individualTypeCount = getFilledIndividualTypeInputsCount(searchForm);
        if (individualTypeCount == 0 && StringUtils.isEmpty(searchForm.getPersonName())) {
            errors.add(ValidationError.builder().pointer("searchModal.personName").messageCode("required.field").build());
        }
        if (individualTypeCount > 1) {
            errors.add(ValidationError.builder().pointer("searchModal.egn").messageCode("person.search.individual.id.multiple").build());
        }
        return errors;
    }

    private int getFilledIndividualTypeInputsCount(PersonModalSearchForm searchForm) {
        int individualTypeCount = 0;
        if (StringUtils.hasText(searchForm.getEgn()))
            individualTypeCount++;
        if (StringUtils.hasText(searchForm.getLnch()))
            individualTypeCount++;
        if (StringUtils.hasText(searchForm.getEik()))
            individualTypeCount++;
        return individualTypeCount;
    }

    private CCriteriaPerson creteSearchCriteria(PersonModalSearchForm searchForm) {
        CCriteriaPerson criteria = new CCriteriaPerson();
        switch (PersonNameSearchType.valueOf(searchForm.getPersonNameSearchType())) {
            case CONTAINS_WORDS:
                criteria.setPersonNameContainsWords(searchForm.getPersonName());
                break;
            case WHOLE_WORDS:
                criteria.setPersonNameWholeWords(searchForm.getPersonName());
                break;
            case EXACTLY:
                criteria.setPersonNameExactly(searchForm.getPersonName());
                break;
            default:
                throw new RuntimeException("Unknown person name search type !");
        }

        if (!StringUtils.isEmpty(searchForm.getStreet()))
            criteria.setStreetContainsWords(searchForm.getStreet());
        if (!StringUtils.isEmpty(searchForm.getCity()))
            criteria.setCityContainsWords(searchForm.getCity());
        if (!StringUtils.isEmpty(searchForm.getZipCode()))
            criteria.setZipCode(searchForm.getZipCode());
        if (!StringUtils.isEmpty(searchForm.getEmail()))
            criteria.setEmailContainsWords(searchForm.getEmail());
        if (!StringUtils.isEmpty(searchForm.getTelephone()))
            criteria.setTelephoneContainsWords(searchForm.getTelephone());
        if (Objects.nonNull(searchForm.getIndCompany())) {
            criteria.setIndCompany(searchForm.getIndCompany());
        }
        if (!StringUtils.isEmpty(searchForm.getEgn())) {
            criteria.setIndividualIdType(PersonIdType.EGN.name());
            criteria.setIndividualIdText(searchForm.getEgn());
        }
        if (!StringUtils.isEmpty(searchForm.getLnch())) {
            criteria.setIndividualIdType(PersonIdType.LNCH.name());
            criteria.setIndividualIdText(searchForm.getLnch());
        }
        if (!StringUtils.isEmpty(searchForm.getEik())) {
            criteria.setIndividualIdType(PersonIdType.EIK.name());
            criteria.setIndividualIdText(searchForm.getEik());
        }

        criteria.setExcludeOldVersions(true);
        return criteria;
    }


}
