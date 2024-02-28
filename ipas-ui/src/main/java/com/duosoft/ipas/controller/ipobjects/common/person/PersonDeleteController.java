package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.DefaultValue;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/person")
public class PersonDeleteController extends BasePersonActionController {

    @PostMapping("/delete-person")
    public String deletePerson(HttpServletRequest request, Model model,
                               @RequestParam String sessionIdentifier,
                               @RequestParam Integer personKind,
                               @RequestParam Integer personNbr,
                               @RequestParam Integer addressNbr) {
        PersonKind personKindEnum = PersonKind.selectByCode(personKind);
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case RECEPTION:
                deleteReceptionPerson(request, sessionIdentifier, personNbr, addressNbr, personKindEnum);
                break;
            case MARK:
                deleteMarkPerson(request, sessionIdentifier, personNbr, addressNbr, personKindEnum);
                break;
            case PATENT:
                deletePatentPerson(request, sessionIdentifier, personNbr, addressNbr, personKindEnum);
                break;
            case USERDOC:
                deleteUserDocumentPerson(request, sessionIdentifier, personNbr, addressNbr, personKindEnum);
                break;
        }
        return personListPage(request, model, sessionIdentifier, personKind);
    }

    private void deleteReceptionPerson(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr, PersonKind personKind) {
        switch (personKind) {
            case Applicant:
                removeApplicantFromOwnershipData(request, sessionIdentifier, personNbr, addressNbr);
                break;
            case Representative:
                removeRepresentativeFromRepresentationData(request, sessionIdentifier, personNbr, addressNbr);
                break;
        }
    }

    private void deleteMarkPerson(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr, PersonKind personKind) {
        switch (personKind) {
            case Applicant:
                removeApplicantFromOwnershipData(request, sessionIdentifier, personNbr, addressNbr);
                break;
            case Representative:
                removeRepresentativeFromRepresentationData(request, sessionIdentifier, personNbr, addressNbr);
                break;
            case AcpRepresentative:
                removeAcpRepresentativeFromRepresentationData(request, sessionIdentifier, personNbr, addressNbr);
                break;
        }
    }

    private void deletePatentPerson(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr, PersonKind personKind) {
        switch (personKind) {
            case Applicant:
                removeApplicantFromOwnershipData(request, sessionIdentifier, personNbr, addressNbr);
                break;
            case Representative:
                removeRepresentativeFromRepresentationData(request, sessionIdentifier, personNbr, addressNbr);
                break;
            case Inventor:
                removeInventorFromAuthorshipData(request, sessionIdentifier, personNbr, addressNbr);
                break;
        }
    }

    private void deleteUserDocumentPerson(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr, PersonKind personKind) {
        UserdocPersonRole role = PersonUtils.selectUserdocPersonRoleByPersonKind(personKind);
        List<CUserdocPerson> sessionPersons = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
        if (!CollectionUtils.isEmpty(sessionPersons))
            sessionPersons.removeIf(userdocPerson -> Objects.equals(userdocPerson.getPerson().getPersonNbr(), personNbr) && Objects.equals(userdocPerson.getPerson().getAddressNbr(), addressNbr));
    }

    private void removeRepresentativeFromRepresentationData(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr) {
        CRepresentationData representationData = PersonSessionUtils.getSessionRepresentationData(request, sessionIdentifier);
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            representativeList.removeIf(representative -> Objects.equals(representative.getPerson().getPersonNbr(), personNbr) && Objects.equals(representative.getPerson().getAddressNbr(), addressNbr));
        }
    }

    private void removeAcpRepresentativeFromRepresentationData(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr) {
        CRepresentationData representationData = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_ACP_REPRESENTATIVES, sessionIdentifier, request);
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            representativeList.removeIf(representative -> Objects.equals(representative.getPerson().getPersonNbr(), personNbr) && Objects.equals(representative.getPerson().getAddressNbr(), addressNbr));
        }
    }

    private void removeApplicantFromOwnershipData(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr) {
        COwnershipData ownershipData = PersonSessionUtils.getSessionOwnershipData(request, sessionIdentifier);
        if (Objects.nonNull(ownershipData)) {
            List<COwner> ownerList = ownershipData.getOwnerList();
            ownerList.removeIf(cOwner -> Objects.equals(cOwner.getPerson().getPersonNbr(), personNbr) && Objects.equals(cOwner.getPerson().getAddressNbr(), addressNbr));
        }
    }

    private void removeInventorFromAuthorshipData(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr) {
        CAuthorshipData authorshipData = PersonSessionUtils.getSessionAuthorshipData(request, sessionIdentifier);
        if (Objects.nonNull(authorshipData)) {
            List<CAuthor> cAuthorsList = authorshipData.getAuthorList();
            cAuthorsList.removeIf(author -> Objects.equals(author.getPerson().getPersonNbr(), personNbr) && Objects.equals(author.getPerson().getAddressNbr(), addressNbr));
            reorderAuthors(authorshipData);
        }
    }

}
