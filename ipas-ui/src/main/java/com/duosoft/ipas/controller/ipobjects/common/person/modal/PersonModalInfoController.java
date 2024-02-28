package com.duosoft.ipas.controller.ipobjects.common.person.modal;

import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.service.nomenclature.CountryService;
import bg.duosoft.ipas.core.service.person.PersonService;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@RequestMapping("/person/info-modal")
public class PersonModalInfoController {

    @Autowired
    private PersonService personService;

    @Autowired
    private CountryService countryService;

    @PostMapping("/open")
    public String openPersonInfoModal(HttpServletRequest request, Model model,
                                      @RequestParam Integer personNbr,
                                      @RequestParam Integer addressNbr,
                                      @RequestParam(required = false) Boolean fromDatabase,
                                      @RequestParam String sessionIdentifier) {
        CPerson person;
        if (StringUtils.isEmpty(sessionIdentifier) || (Objects.nonNull(fromDatabase) && fromDatabase)) {
            person = personService.selectPersonByPersonNumberAndAddressNumber(personNbr, addressNbr);
        } else {
            person = PersonSessionUtils.getSessionPerson(request, sessionIdentifier, personNbr, addressNbr);
        }

        model.addAttribute("person", person);
        model.addAttribute("countryMap", countryService.getCountryMap());
        return "ipobjects/common/person/modal/person_info_modal :: person-info";
    }

}
