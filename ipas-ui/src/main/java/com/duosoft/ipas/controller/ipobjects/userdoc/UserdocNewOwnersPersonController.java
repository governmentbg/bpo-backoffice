package com.duosoft.ipas.controller.ipobjects.userdoc;

import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocMainObjectData;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonData;
import bg.duosoft.ipas.core.service.nomenclature.CountryService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/userdoc/person")
public class UserdocNewOwnersPersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private CountryService countryService;

    @PostMapping("/view-connected-old-owner")
    public String viewConnectedOldOwner(HttpServletRequest request, Model model,
                                        @RequestParam Integer newOwnerPersonNumber,
                                        @RequestParam String sessionIdentifier) {
        List<CPerson> owners = selectMainObjectOwners(request, sessionIdentifier);

        Integer connectedOldOwner = null;
        for (CPerson owner : owners) {
            Integer oldOwnerPersonNumber = owner.getPersonNbr();
            Integer oldOwnerLastVersion = owner.getPersonLastVersion();
            if (newOwnerPersonNumber.equals(oldOwnerLastVersion)) {
                connectedOldOwner = oldOwnerPersonNumber;
                break;
            }
        }

        CPerson person = personService.selectPersonByPersonNumberAndAddressNumber(connectedOldOwner, DefaultValue.FIRST_PERSON_ADDRESS_NUMBER);
        model.addAttribute("person", person);
        model.addAttribute("countryMap", countryService.getCountryMap());
        return "ipobjects/common/person/modal/person_info_modal :: person-info";
    }

    @PostMapping("/open-connect-new-owner-modal")
    public String openConnectNewOwnerModal(HttpServletRequest request, Model model,
                                           @RequestParam Integer newOwnerPersonNumber,
                                           @RequestParam String sessionIdentifier) {
        List<CPerson> owners = selectMainObjectOwners(request, sessionIdentifier);
        model.addAttribute("owners", owners);
        model.addAttribute("newOwnerPersonNumber", newOwnerPersonNumber);
        return "ipobjects/userdoc/person/modal/connect_new_owner_modal :: modal";
    }

    @PostMapping("/connect-new-owner")
    public String connectNewOwner(HttpServletRequest request, Model model,
                                  @RequestParam Integer newOwnerPersonNumber,
                                  @RequestParam(required = false) Integer connectedPersonNumber,
                                  @RequestParam String sessionIdentifier) {
        List<CPerson> owners = selectMainObjectOwners(request, sessionIdentifier);
        List<CUserdocPerson> newOwners = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, UserdocPersonRole.NEW_OWNER);

        List<ValidationError> validationErrors = validateConnectNewOwner(owners, newOwners, connectedPersonNumber);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            model.addAttribute("owners", owners);
            model.addAttribute("validationErrors", validationErrors);
            model.addAttribute("newOwnerPersonNumber", newOwnerPersonNumber);
            model.addAttribute("connectedPersonNumber", connectedPersonNumber);
            return "ipobjects/userdoc/person/modal/connect_new_owner_modal :: modal";
        } else {
            CUserdocPerson selectedNewOwner = newOwners.stream().filter(cUserdocPerson -> cUserdocPerson.getPerson().getPersonNbr().equals(newOwnerPersonNumber)).findFirst().orElse(null);
            if (Objects.nonNull(selectedNewOwner)) {
                Integer lastVersionOfConnectedPerson = personService.selectPersonNumberOfLastVersionOfPerson(connectedPersonNumber);
                selectedNewOwner.getPerson().setTempParentPersonNbr(lastVersionOfConnectedPerson);
            }
            CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
            CUserdoc cloneUserdoc = (CUserdoc) SerializationUtils.clone(sessionUserdoc);
            cloneUserdoc.setUserdocPersonData(new CUserdocPersonData());
            cloneUserdoc.getUserdocPersonData().setPersonList(new ArrayList<>());
            PersonSessionUtils.setSessionPersonsToUserdoc(request, sessionIdentifier, cloneUserdoc);
            model.addAttribute("userdoc", cloneUserdoc);
            return "ipobjects/userdoc/person/person_panel :: userdoc-persons";
        }
    }

    @PostMapping("/import-new-owner-representatives")
    public String importNewOwnerRepresentatives(HttpServletRequest request, Model model,
                                                @RequestParam String sessionIdentifier,
                                                @RequestParam Integer personKind) {
        PersonKind personKindEnum = PersonKind.selectByCode(personKind);
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case USERDOC:
                CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
                UserdocPersonRole role = PersonUtils.selectUserdocPersonRoleByPersonKind(personKindEnum);
                if (role.equals(UserdocPersonRole.NEW_OWNER)) {
                    List<CUserdocPerson> sessionNewOwners = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
                    List<CPerson> mainObjectOwners = sessionUserdoc.getUserdocMainObjectData().getMainObjectOwners();
                    if (!CollectionUtils.isEmpty(mainObjectOwners)) {
                        List<CPerson> representatives = mainObjectOwners.stream()
                                .filter(CPerson::hasAgentCode)
                                .collect(Collectors.toList());

                        if (!CollectionUtils.isEmpty(representatives)) {
                            List<CUserdocPerson> newUserdocPersons = UserdocPersonUtils.convertToUserdocPerson(representatives, UserdocPersonRole.NEW_OWNER, null);
                            if (!CollectionUtils.isEmpty(newUserdocPersons)) {
                                for (CUserdocPerson newUserdocPerson : newUserdocPersons) {
                                    Integer personNbr = newUserdocPerson.getPerson().getPersonNbr();
                                    sessionNewOwners.removeIf(cUserdocPerson -> cUserdocPerson.getPerson().getPersonNbr().equals(personNbr));
                                    sessionNewOwners.add(newUserdocPerson);
                                }
                            }
                        }
                    }
                }
                CUserdoc cloneUserdoc = (CUserdoc) SerializationUtils.clone(sessionUserdoc);
                cloneUserdoc.setUserdocPersonData(new CUserdocPersonData());
                cloneUserdoc.getUserdocPersonData().setPersonList(new ArrayList<>());
                PersonSessionUtils.setSessionPersonsToUserdoc(request, sessionIdentifier, cloneUserdoc);
                model.addAttribute("userdoc", cloneUserdoc);
                return "ipobjects/userdoc/person/person_panel :: userdoc-persons";
        }
        return null;
    }

    private List<ValidationError> validateConnectNewOwner(List<CPerson> owners, List<CUserdocPerson> newOwners, Integer connectedPersonNumber) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.isNull(connectedPersonNumber)) {
            errors.add(ValidationError.builder().pointer("newOwner.connection.errors").messageCode("required.field").build());
        }
        if (CollectionUtils.isEmpty(errors)) {
            CUserdocPerson existingOwnerWithTheSameConnection = newOwners.stream()
                    .filter(cUserdocPerson -> connectedPersonNumber.equals(cUserdocPerson.getPerson().getTempParentPersonNbr()))
                    .findFirst()
                    .orElse(null);

            if (Objects.nonNull(existingOwnerWithTheSameConnection)) {
                errors.add(ValidationError.builder().pointer("newOwner.connection.errors").messageCode("newOwner.connection.exist").build());
            }
        }
        return errors;
    }

    private List<CPerson> selectMainObjectOwners(HttpServletRequest request, String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        CUserdocMainObjectData userdocMainObjectData = sessionUserdoc.getUserdocMainObjectData();
        if (Objects.isNull(userdocMainObjectData)) {
            throw new RuntimeException("Userdoc main object data is empty !");
        }

        List<CPerson> owners = userdocMainObjectData.getMainObjectOwners();
        if (CollectionUtils.isEmpty(owners))
            throw new RuntimeException("Cannot find owners for userdoc main object " + userdocMainObjectData.getFileId());

        owners.removeIf(cPerson -> Objects.nonNull(cPerson.getAgentCode()));// Remove representatives
        return owners;
    }

}
