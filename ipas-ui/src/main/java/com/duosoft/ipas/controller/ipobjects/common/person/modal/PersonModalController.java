package com.duosoft.ipas.controller.ipobjects.common.person.modal;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.model.util.TempID;
import bg.duosoft.ipas.core.service.nomenclature.CountryService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.enums.GralPersonIdType;
import bg.duosoft.ipas.enums.PersonIdType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.person.OwnerUtils;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.json.PersonUiDto;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/person")
public class PersonModalController {

    @Autowired
    private PersonService personService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/open-person-modal")
    public String openPersonModal(HttpServletRequest request, Model model,
                                  @RequestParam String sessionIdentifier,
                                  @RequestParam Integer personKind,
                                  @RequestParam(required = false) boolean loadPersonFromSearch,
                                  @RequestParam(required = false) Integer tempParentPersonNbr,
                                  @RequestParam(required = false) String representativeType,
                                  @RequestParam(required = false) Integer personNbr,
                                  @RequestParam(required = false) Integer addressNbr) {

        if (loadPersonFromSearch) {
            CPerson person = personService.selectPersonByPersonNumberAndAddressNumber(personNbr, addressNbr);
            if (Objects.isNull(person))
                throw new RuntimeException("Cannot find person ! Person number: " + personNbr);

            CPerson clone = (CPerson) SerializationUtils.clone(person);
            clone.setTempParentPersonNbr(tempParentPersonNbr);
            clone.setPersonNbr(null);
            clone.setAddressNbr(null);
            model.addAttribute("person", clone);
        } else {
            PersonKind personKindEnum = PersonKind.selectByCode(personKind);
            switch (personKindEnum) {
                case Applicant:
                    addSelectedApplicantToModel(request, model, sessionIdentifier, personNbr, addressNbr);
                    break;
                case Representative:
                    addSelectedRepresentativeToModel(request, model, sessionIdentifier, personNbr, addressNbr);
                    break;
                case AcpRepresentative:
                    CRepresentationData representationData = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_REPRESENTATIVES, sessionIdentifier, request);
                    addSelectedRepresentativeFromCRepresentationDataToModel(request, model, sessionIdentifier, personNbr, addressNbr, representationData);
                    break;
                case Inventor:
                    addSelectedInventorToModel(request, model, sessionIdentifier, personNbr, addressNbr);
                    break;
                case CorrespondenceAddress:
                    addSelectedCAToModel(request, model, sessionIdentifier, personNbr, addressNbr, false);
                    break;
                case AcpCorrespondenceAddress:
                    addSelectedCAToModel(request, model, sessionIdentifier, personNbr, addressNbr, true);
                    break;
                case AcpInfringer:
                    addInfringerToModel(request, model, sessionIdentifier, personNbr, addressNbr);
                    break;
                case Grantor:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.GRANTOR);
                    break;
                case Grantee:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.GRANTEE);
                    break;
                case Payer:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.PAYER);
                    break;
                case Payee:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.PAYEE);
                    break;
                case Pledger:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.PLEDGER);
                    break;
                case Creditor:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.CREDITOR);
                    break;
                case NewOwner:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.NEW_OWNER);
                    break;
                case OldOwner:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.OLD_OWNER);
                    break;
                case NewCorrespondenceAddress:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.NEW_CORRESPONDENCE_ADDRESS);
                    break;
                case NewRepresentative:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.NEW_REPRESENTATIVE);
                    break;
                case RepresentativeOfTheOwner:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.REPRESENTATIVE_OF_THE_OWNER);
                    break;
                case AffectedInventor:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.AFFECTED_INVENTOR);
                    break;
            }
        }

        if (!model.containsAttribute("person")) {
            CPerson person = new CPerson();
            person.setResidenceCountryCode(DefaultValue.BULGARIA_CODE);
            person.setTempParentPersonNbr(tempParentPersonNbr);
            model.addAttribute("person", person);
        } else {
            // Edit old person version validation
            CPerson modelPerson = (CPerson) model.asMap().get("person");
            String gralPersonIdTyp = modelPerson.getGralPersonIdTyp();
            if (GralPersonIdType.OLD.name().equals(gralPersonIdTyp)) {
                String message = messageSource.getMessage("person.old.record.edit.error", null, LocaleContextHolder.getLocale());
                model.addAttribute("message", message);
                return "base/modal/error :: process-error-modal";
            }
        }

        model.addAttribute("countryMap", countryService.getCountryMap());
        model.addAttribute("personKind", personKind);
        model.addAttribute("representativeType", representativeType);
        return "ipobjects/common/person/modal/person_form_modal :: person-form";
    }

    @PostMapping("/update-city-input")
    public String updateCityInput(Model model, @RequestParam String residenceCountryCode) {
        model.addAttribute("residenceCountryCode", residenceCountryCode);
        model.addAttribute("cityName", null);
        model.addAttribute("validationErrors", null);
        return "ipobjects/common/person/modal/person_form_modal_elements :: city-input";
    }

    @PostMapping("/update-individual-id-type-radio")
    public String updateIndividualIdTypeRadio(Model model, @RequestParam String data) {
        PersonUiDto personUiDto = JsonUtil.readJson(data, PersonUiDto.class);
        if (Objects.isNull(personUiDto)) {
            throw new RuntimeException("Person data is empty !");
        }

        CPerson person = personUiDto.convertToCPerson();
        if (person.isCompany()) {
            if (person.isForeigner()) {
                person.setIndividualIdType(null);
                person.setIndividualIdTxt(null);
            } else {
                person.setIndividualIdType(PersonIdType.EIK.name());
            }
        } else {
            if (person.isForeigner()) {
                person.setIndividualIdType(PersonIdType.LNCH.name());
            } else {
                person.setIndividualIdType(PersonIdType.EGN.name());
            }
        }

        model.addAttribute("person", person);
        model.addAttribute("validationErrors", null);
        return "ipobjects/common/person/modal/person_form_modal_elements :: individualIdType-input";
    }


    private void addSelectedCAToModel(HttpServletRequest request, Model model, String sessionIdentifier, Integer personNbr, Integer addressNbr, boolean isAcpServicePerson) {
        if (Objects.nonNull(personNbr) && Objects.nonNull(addressNbr)) {
            CPerson servicePerson = null;
            if (isAcpServicePerson) {
                servicePerson = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_SERVICE_PERSON, sessionIdentifier, request);
            } else {
                servicePerson = PersonSessionUtils.getSessionServicePerson(request, sessionIdentifier);
            }
            if (Objects.nonNull(servicePerson))
                model.addAttribute("person", servicePerson);
        }
    }


    private void addInfringerToModel(HttpServletRequest request, Model model, String sessionIdentifier, Integer personNbr, Integer addressNbr) {
        if (Objects.nonNull(personNbr) && Objects.nonNull(addressNbr)) {
            model.addAttribute("person", HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_INFRINGER, sessionIdentifier, request));
        }
    }

    private void addSelectedInventorToModel(HttpServletRequest request, Model model, String sessionIdentifier, Integer personNbr, Integer addressNbr) {
        if (Objects.nonNull(personNbr) && Objects.nonNull(addressNbr)) {
            CAuthorshipData authorshipData = PersonSessionUtils.getSessionAuthorshipData(request, sessionIdentifier);
            if (Objects.nonNull(authorshipData)) {
                List<CAuthor> authorList = authorshipData.getAuthorList();
                CAuthor selectedAuthor = PersonUtils.selectAuthor(personNbr, addressNbr, authorList);
                if (Objects.nonNull(selectedAuthor))
                    model.addAttribute("person", selectedAuthor.getPerson());
            }

        }
    }

    private void addSelectedRepresentativeToModel(HttpServletRequest request, Model model, String sessionIdentifier, Integer personNbr, Integer addressNbr) {
        if (Objects.nonNull(personNbr) && Objects.nonNull(addressNbr)) {
            SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
            switch (sessionObjectType) {
                case RECEPTION:
                case MARK:
                case PATENT:
                    addSelectedRepresentativeFromCRepresentationDataToModel(request, model, sessionIdentifier, personNbr, addressNbr, null);
                    break;
                case USERDOC:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.REPRESENTATIVE);
                    break;
            }
        }
    }

    private void addSelectedRepresentativeFromCRepresentationDataToModel(HttpServletRequest request, Model model,
                                                                         String sessionIdentifier, Integer personNbr, Integer addressNbr, CRepresentationData representationDataParam) {

        CRepresentationData representationData = null;
        if (Objects.nonNull(representationDataParam)) {
            representationData = representationDataParam;
        } else {
            representationData = PersonSessionUtils.getSessionRepresentationData(request, sessionIdentifier);
        }

        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            CRepresentative selectedRepresentative = RepresentativeUtils.selectRepresentativeFromList(personNbr, addressNbr, representativeList);
            if (Objects.nonNull(selectedRepresentative))
                model.addAttribute("person", selectedRepresentative.getPerson());
        }
    }

    private void addSelectedApplicantToModel(HttpServletRequest request, Model model, String sessionIdentifier, Integer personNbr, Integer addressNbr) {
        if (Objects.nonNull(personNbr) && Objects.nonNull(addressNbr)) {
            SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
            switch (sessionObjectType) {
                case RECEPTION:
                case MARK:
                case PATENT:
                    addSelectedApplicantFromOwnershipDataToModel(request, model, sessionIdentifier, personNbr, addressNbr);
                    break;
                case USERDOC:
                    addSelectedUserdocPersonToModel(request, model, sessionIdentifier, personNbr, addressNbr, UserdocPersonRole.APPLICANT);
                    break;
            }
        }
    }


    private void addSelectedUserdocPersonToModel(HttpServletRequest request, Model model, String sessionIdentifier, Integer personNbr, Integer addressNbr, UserdocPersonRole role) {
        if (Objects.nonNull(personNbr) && Objects.nonNull(addressNbr)) {
            List<CUserdocPerson> sessionPersons = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
            if (!CollectionUtils.isEmpty(sessionPersons)) {
                CUserdocPerson person = PersonUtils.selectUserdocPerson(personNbr, addressNbr, sessionPersons, role);
                if (Objects.nonNull(person))
                    model.addAttribute("person", person.getPerson());
            }
        }
    }

    private void addSelectedApplicantFromOwnershipDataToModel(HttpServletRequest request, Model model, String sessionIdentifier, Integer personNbr, Integer addressNbr) {
        COwnershipData ownershipData = PersonSessionUtils.getSessionOwnershipData(request, sessionIdentifier);
        if (Objects.nonNull(ownershipData)) {
            List<COwner> ownerList = ownershipData.getOwnerList();
            COwner selectedOwner = OwnerUtils.selectOwnerFromList(personNbr, addressNbr, ownerList);
            if (Objects.nonNull(selectedOwner))
                model.addAttribute("person", selectedOwner.getPerson());
        }
    }

}
