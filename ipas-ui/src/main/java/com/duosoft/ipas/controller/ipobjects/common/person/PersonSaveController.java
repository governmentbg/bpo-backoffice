package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.person.*;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.model.util.TempID;
import bg.duosoft.ipas.core.service.nomenclature.CountryService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.person.PersonValidator;
import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import bg.duosoft.ipas.util.validation.PersonValidationUtils;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.json.PersonUiDto;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.xpath.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/person")
public class PersonSaveController extends BasePersonActionController {

    @Autowired
    private TempID tempID;

    @Autowired
    private CountryService countryService;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private PersonService personService;

    @PostMapping("/save-person")
    public String savePerson(@RequestParam String data, @RequestParam String sessionIdentifier, HttpServletRequest request, Model model) {
        PersonUiDto personUiDto = JsonUtil.readJson(data, PersonUiDto.class);
        if (Objects.nonNull(personUiDto)) {
            CPerson cPerson = personUiDto.convertToCPerson();

            PersonKind personKindEnum = PersonKind.selectByCode(personUiDto.getPersonKind());
            SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);

            IpasValidator<CPerson> validator = validatorCreator.create(true, PersonValidator.class);
            List<ValidationError> errors;
            if (sessionObjectType == SessionObjectType.RECEPTION) {
                errors = validator.validate(cPerson, PersonValidationUtils.excludeFieldsOnReception());
            } else {
                errors = validator.validate(cPerson);
            }

            if (CollectionUtils.isEmpty(errors)) {
                switch (sessionObjectType) {
                    case RECEPTION:
                        saveReceptionPerson(request, sessionIdentifier, cPerson, personKindEnum, personUiDto);
                        break;
                    case MARK:
                        saveMarkPerson(request, sessionIdentifier, cPerson, personKindEnum, personUiDto);
                        break;
                    case PATENT:
                        savePatentPerson(request, sessionIdentifier, cPerson, personKindEnum, personUiDto);
                        break;
                    case USERDOC:
                        saveUserDocumentPerson(request, sessionIdentifier, cPerson, personKindEnum, personUiDto);
                        break;
                }
            } else {
                return updatePersonFormModal(model, cPerson, personKindEnum.code(), personUiDto.getRepresentativeType(), errors);
            }

            return personListPage(request, model, sessionIdentifier, personKindEnum.code());
        } else
            throw new RuntimeException("Person Data is empty !");
    }

    @PostMapping(value = "/save-agent")
    public String saveImportedAgent(HttpServletRequest request, Model model,
                                    @RequestParam(required = false) String agentCodeAndName,
                                    @RequestParam String sessionIdentifier,
                                    @RequestParam(required = false) String representativeType,
                                    @RequestParam(required = false) Boolean onlyActive,
                                    @RequestParam Integer personKind) {
        if (StringUtils.isEmpty(agentCodeAndName)) {
            ValidationError error = new ValidationError("agentImport", "agent.autocomplete.selectFromList", null, null);
            model.addAttribute("validationErrors", Collections.singletonList(error));
            model.addAttribute("personKind", personKind);
            model.addAttribute("onlyActive", onlyActive);
            model.addAttribute("representativeType", representativeType);
            return "ipobjects/common/person/modal/agent_modal :: agent-form-modal";
        }

        RepresentativeType representativeTypeEnum = Enum.valueOf(RepresentativeType.class, representativeType);
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);

        CReprsPowerOfAttorney sessionAttorneyData = PersonSessionUtils.getSessionAttorneyData(request, sessionIdentifier);
        if (Objects.nonNull(personKind) && personKind.equals(PersonKind.AcpRepresentative.code())) {
            CRepresentationData representationData = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_REPRESENTATIVES, sessionIdentifier, request);
            PersonUtils.addRepresentativeToRepresentationData(agentCodeAndName, representationData, personService, representativeTypeEnum, sessionAttorneyData);
            return personListPage(request, model, sessionIdentifier, PersonKind.AcpRepresentative.code());
        } else {
            switch (sessionObjectType) {
                case RECEPTION:
                case MARK:
                case PATENT:
                    CRepresentationData representationData = PersonSessionUtils.getSessionRepresentationData(request, sessionIdentifier);
                    PersonUtils.addRepresentativeToRepresentationData(agentCodeAndName, representationData, personService, representativeTypeEnum, sessionAttorneyData);
                    break;
                case USERDOC:
                    PersonKind personKindEnum = PersonKind.selectByCode(personKind);
                    UserdocPersonRole role = PersonUtils.selectUserdocPersonRoleByPersonKind(personKindEnum);
                    List<CUserdocPerson> sessionRepresentatives = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
                    PersonUtils.addRepresentativeToUserdocPersons(agentCodeAndName, sessionRepresentatives, personService, representativeTypeEnum, role, sessionAttorneyData);
                    break;
            }
            PersonSessionUtils.removeSessionAttorneyData(request, sessionIdentifier);
            return personListPage(request, model, sessionIdentifier, PersonKind.Representative.code());
        }

    }

    private String updatePersonFormModal(Model model, CPerson cPerson, Integer personKind, String representativeType, List<ValidationError> errors) {
        model.addAttribute("person", cPerson);
        model.addAttribute("personKind", personKind);
        model.addAttribute("representativeType", representativeType);
        model.addAttribute("validationErrors", errors);
        model.addAttribute("countryMap", countryService.getCountryMap());
        return "ipobjects/common/person/modal/person_form_modal :: person-form";
    }

    private void saveReceptionPerson(HttpServletRequest request, String sessionIdentifier, CPerson person, PersonKind personKind, PersonUiDto personUiDto) {
        switch (personKind) {
            case Applicant:
                PersonUtils.saveApplicant(request, person, sessionIdentifier, personService, tempID, false);
                break;
            case Representative:
                RepresentativeType representativeType = RepresentativeType.valueOf(personUiDto.getRepresentativeType());
                PersonUtils.saveRepresentative(request, representativeType, person, sessionIdentifier, personService, tempID, false, null);
                break;
        }
    }

    private void saveMarkPerson(HttpServletRequest request, String sessionIdentifier, CPerson person, PersonKind personKind, PersonUiDto personUiDto) {
        switch (personKind) {
            case Applicant:
                PersonUtils.saveApplicant(request, person, sessionIdentifier, personService, tempID, false);
                break;
            case Representative:
                RepresentativeType representativeType = RepresentativeType.valueOf(personUiDto.getRepresentativeType());
                PersonUtils.saveRepresentative(request, representativeType, person, sessionIdentifier, personService, tempID, false, null);
                break;
            case CorrespondenceAddress:
                saveCorrespondenceAddress(request, person, sessionIdentifier, tempID);
                break;
            case AcpCorrespondenceAddress:
                saveAcpCorrespondenceAddress(request, person, sessionIdentifier, tempID);
                break;
            case AcpInfringer:
                saveAcpInfringer(request, person, sessionIdentifier, tempID);
                break;
            case AcpRepresentative:
                CRepresentationData representationData = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_REPRESENTATIVES, sessionIdentifier, request);
                RepresentativeType acpRepresentativeType = RepresentativeType.valueOf(personUiDto.getRepresentativeType());
                PersonUtils.saveRepresentative(request, acpRepresentativeType, person, sessionIdentifier, personService, tempID, false, representationData);
                break;
        }
        synchronizeAllMarkPersonsWithSameId(request, sessionIdentifier, person);
    }

    private void savePatentPerson(HttpServletRequest request, String sessionIdentifier, CPerson person, PersonKind personKind, PersonUiDto personUiDto) {
        switch (personKind) {
            case Applicant:
                PersonUtils.saveApplicant(request, person, sessionIdentifier, personService, tempID, false);
                break;
            case Representative:
                RepresentativeType representativeType = RepresentativeType.valueOf(personUiDto.getRepresentativeType());
                PersonUtils.saveRepresentative(request, representativeType, person, sessionIdentifier, personService, tempID, false, null);
                break;
            case CorrespondenceAddress:
                saveCorrespondenceAddress(request, person, sessionIdentifier, tempID);
                break;
            case Inventor:
                saveInventor(request, person, sessionIdentifier, tempID);
                break;
        }
        synchronizeAllPatentPersonsWithSameId(request, sessionIdentifier, person);
    }

    private void saveUserDocumentPerson(HttpServletRequest request, String sessionIdentifier, CPerson person, PersonKind personKind, PersonUiDto personUiDto) {
        switch (personKind) {
            case CorrespondenceAddress:
                saveCorrespondenceAddress(request, person, sessionIdentifier, tempID);
                break;
            default:
                UserdocPersonRole userdocPersonRole = PersonUtils.selectUserdocPersonRoleByPersonKind(personKind);
                saveUserdocPerson(request, sessionIdentifier, person, personUiDto, userdocPersonRole);
                PersonUtils.synchronizeAllUserdocPersonsWithSameId(request, sessionIdentifier, person, userdocPersonRole);
                break;
        }
    }

    private void saveUserdocPerson(HttpServletRequest request, String sessionIdentifier, CPerson person, PersonUiDto personUiDto, UserdocPersonRole userdocPersonRole) {
        switch (userdocPersonRole) {
            case REPRESENTATIVE:
            case REPRESENTATIVE_OF_THE_OWNER:
            case NEW_REPRESENTATIVE:
            case OLD_REPRESENTATIVE: {
                RepresentativeType representativeType = Enum.valueOf(RepresentativeType.class, personUiDto.getRepresentativeType());
                saveUserdocPerson(request, sessionIdentifier, person, userdocPersonRole, representativeType);
                break;
            }
            default:
                saveUserdocPerson(request, sessionIdentifier, person, userdocPersonRole, null);
                break;
        }
    }

    private void saveUserdocPerson(HttpServletRequest request, String sessionIdentifier, CPerson person, UserdocPersonRole role, RepresentativeType representativeType) {
        List<CUserdocPerson> sessionPersonsByRole = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
        if (Objects.nonNull(person.getPersonNbr()) && Objects.nonNull(person.getAddressNbr())) {
            if (CollectionUtils.isEmpty(sessionPersonsByRole))
                throw new RuntimeException("How is possible to update userdoc person when person list is empty ?");

            CUserdocPerson cUserdocPerson = PersonUtils.selectUserdocPerson(person.getPersonNbr(), person.getAddressNbr(), sessionPersonsByRole, role);
            if (Objects.isNull(cUserdocPerson))
                throw new RuntimeException("Cannot find userdoc person for update !");

            cUserdocPerson.setPerson(person);
        } else {
            if (role == UserdocPersonRole.NEW_CORRESPONDENCE_ADDRESS) {
                sessionPersonsByRole.clear();
            }
            if (role == UserdocPersonRole.NEW_OWNER) {
                Integer tempParentPersonNbr = person.getTempParentPersonNbr();
                if (Objects.nonNull(tempParentPersonNbr)) {
                    sessionPersonsByRole.removeIf(cUserdocPerson -> tempParentPersonNbr.equals(cUserdocPerson.getPerson().getTempParentPersonNbr()));
                }
            }
            PersonUtils.fillPersonID(person, personService, tempID, false);
            PersonUtils.addPersonToUserdocPersonListByRole(person, sessionPersonsByRole, role, representativeType, request, sessionIdentifier);
        }
    }

    private static void saveInventor(HttpServletRequest request, CPerson cPerson, String sessionIdentifier, TempID tempID) {
        CAuthorshipData authorshipData = PersonSessionUtils.getSessionAuthorshipData(request, sessionIdentifier);
        if (Objects.nonNull(cPerson.getPersonNbr()) && Objects.nonNull(cPerson.getAddressNbr())) {
            List<CAuthor> authorList = authorshipData.getAuthorList();
            if (CollectionUtils.isEmpty(authorList))
                throw new RuntimeException("How is possible to update inventor when inventor list is empty ?");

            CAuthor selectedAuthor = PersonUtils.selectAuthor(cPerson.getPersonNbr(), cPerson.getAddressNbr(), authorList);
            if (Objects.isNull(selectedAuthor))
                throw new RuntimeException("Cannot find inventor for update !");

            selectedAuthor.setPerson(cPerson);
            PersonSessionUtils.updateSessionServicePerson(request, cPerson, sessionIdentifier);
            PersonSessionUtils.updateSessionApplicantPersonOnMatch(request, cPerson, sessionIdentifier);
        } else {
            PersonUtils.setTemporaryPersonId(cPerson, tempID);
            PersonUtils.addInventorToList(cPerson, authorshipData);
        }
    }

    private static void saveCorrespondenceAddress(HttpServletRequest request, CPerson cPerson, String sessionIdentifier, TempID tempID) {
        //Set temporary person and address number! Negative values !
        if (!(Objects.nonNull(cPerson.getPersonNbr()) && Objects.nonNull(cPerson.getAddressNbr())))
            PersonUtils.setTemporaryPersonId(cPerson, tempID);

        PersonSessionUtils.setSessionServicePerson(request, cPerson, sessionIdentifier);
    }

    private static void saveAcpCorrespondenceAddress(HttpServletRequest request, CPerson cPerson, String sessionIdentifier, TempID tempID) {
        //Set temporary person and address number! Negative values !
        if (!(Objects.nonNull(cPerson.getPersonNbr()) && Objects.nonNull(cPerson.getAddressNbr())))
            PersonUtils.setTemporaryPersonId(cPerson, tempID);

        HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_ACP_SERVICE_PERSON, sessionIdentifier, cPerson, request);
    }


    private static void saveAcpInfringer(HttpServletRequest request, CPerson cPerson, String sessionIdentifier, TempID tempID) {
        //Set temporary person and address number! Negative values !
        if (!(Objects.nonNull(cPerson.getPersonNbr()) && Objects.nonNull(cPerson.getAddressNbr())))
            PersonUtils.setTemporaryPersonId(cPerson, tempID);

        HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_ACP_INFRINGER, sessionIdentifier, cPerson, request);
    }

    private void synchronizeAllMarkPersonsWithSameId(HttpServletRequest request, String sessionIdentifier, CPerson newPerson) {
        Integer newPersonNbr = newPerson.getPersonNbr();
        Integer newAddressNumber = newPerson.getAddressNbr();
        if (Objects.nonNull(newPersonNbr) && Objects.nonNull(newAddressNumber)) {
            updateOwnershipData(request, sessionIdentifier, newPerson, newPersonNbr, newAddressNumber);
            updateRepresentationData(request, sessionIdentifier, newPerson, newPersonNbr, newAddressNumber, null);
            PersonSessionUtils.updateSessionServicePerson(request, newPerson, sessionIdentifier);
            updateSessionAcpServicePerson(request, newPerson, sessionIdentifier);
            updateSessionAcpInfringer(request, newPerson, sessionIdentifier);
            CRepresentationData acpRepresentationData = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_REPRESENTATIVES, sessionIdentifier, request);
            updateRepresentationData(request, sessionIdentifier, newPerson, newPersonNbr, newAddressNumber, acpRepresentationData);
        }
    }

    private void synchronizeAllPatentPersonsWithSameId(HttpServletRequest request, String sessionIdentifier, CPerson newPerson) {
        Integer newPersonNbr = newPerson.getPersonNbr();
        Integer newAddressNumber = newPerson.getAddressNbr();
        if (Objects.nonNull(newPersonNbr) && Objects.nonNull(newAddressNumber)) {
            updateOwnershipData(request, sessionIdentifier, newPerson, newPersonNbr, newAddressNumber);
            updateAuthorshipData(request, sessionIdentifier, newPerson, newPersonNbr, newAddressNumber);
            PersonSessionUtils.updateSessionServicePerson(request, newPerson, sessionIdentifier);
        }
    }


    private void updateAuthorshipData(HttpServletRequest request, String sessionIdentifier, CPerson newPerson, Integer newPersonNbr, Integer newAddressNumber) {
        CAuthorshipData authorshipData = PersonSessionUtils.getSessionAuthorshipData(request, sessionIdentifier);
        if (Objects.nonNull(authorshipData) && !CollectionUtils.isEmpty(authorshipData.getAuthorList())) {
            List<CAuthor> authorList = authorshipData.getAuthorList();
            for (CAuthor author : authorList) {
                CPerson person = author.getPerson();
                if (Objects.nonNull(person)) {
                    if (Objects.equals(newPersonNbr, person.getPersonNbr()) && Objects.equals(newAddressNumber, person.getAddressNbr())) {
                        author.setPerson(newPerson);
                    }
                }
            }
        }
    }

    private void updateSessionAcpServicePerson(HttpServletRequest request, CPerson cPerson, String sessionIdentifier) {
        CPerson acpServicePerson = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_SERVICE_PERSON, sessionIdentifier, request);
        if (Objects.nonNull(acpServicePerson)) {
            if (Objects.equals(acpServicePerson.getAddressNbr(), cPerson.getAddressNbr()) && acpServicePerson.getPersonNbr().equals(cPerson.getPersonNbr())) {
                HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_ACP_SERVICE_PERSON, sessionIdentifier, cPerson, request);
            }
        }
    }


    private void updateSessionAcpInfringer(HttpServletRequest request, CPerson cPerson, String sessionIdentifier) {
        CPerson infringer = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_INFRINGER, sessionIdentifier, request);
        if (Objects.nonNull(infringer)) {
            if (Objects.equals(infringer.getAddressNbr(), cPerson.getAddressNbr()) && infringer.getPersonNbr().equals(cPerson.getPersonNbr())) {
                HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_ACP_INFRINGER, sessionIdentifier, cPerson, request);
            }
        }
    }


    private void updateRepresentationData(HttpServletRequest request, String sessionIdentifier,
                                          CPerson newPerson, Integer newPersonNbr, Integer newAddressNumber, CRepresentationData representationDataParam) {
        CRepresentationData representationData = null;
        if (Objects.nonNull(representationDataParam)) {
            representationData = representationDataParam;
        } else {
            representationData = PersonSessionUtils.getSessionRepresentationData(request, sessionIdentifier);
        }
        List<CRepresentative> representatives = representationData.getRepresentativeList();
        if (!CollectionUtils.isEmpty(representatives)) {
            for (CRepresentative representative : representatives) {
                CPerson person = representative.getPerson();
                if (Objects.nonNull(person)) {
                    if (Objects.equals(newPersonNbr, person.getPersonNbr()) && Objects.equals(newAddressNumber, person.getAddressNbr())) {
                        representative.setPerson(newPerson);
                    }
                }
            }
        }
    }

    private void updateOwnershipData(HttpServletRequest request, String sessionIdentifier, CPerson newPerson, Integer newPersonNbr, Integer newAddressNumber) {
        COwnershipData ownershipData = PersonSessionUtils.getSessionOwnershipData(request, sessionIdentifier);
        List<COwner> ownerList = ownershipData.getOwnerList();
        if (!CollectionUtils.isEmpty(ownerList)) {
            for (COwner owner : ownerList) {
                CPerson person = owner.getPerson();
                if (Objects.nonNull(person)) {
                    if (Objects.equals(newPersonNbr, person.getPersonNbr()) && Objects.equals(newAddressNumber, person.getAddressNbr())) {
                        owner.setPerson(newPerson);
                    }
                }
            }
        }
    }

}
