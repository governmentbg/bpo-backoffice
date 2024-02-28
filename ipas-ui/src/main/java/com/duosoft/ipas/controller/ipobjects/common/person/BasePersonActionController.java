package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.core.model.acp.CAcpPersonsData;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonData;
import bg.duosoft.ipas.util.DefaultValue;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.file.CFileSessionUtils;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import com.duosoft.ipas.util.session.reception.ReceptionSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public abstract class BasePersonActionController {

    public String personListPage(HttpServletRequest request, Model model, String sessionIdentifier, Integer personKind) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case RECEPTION:
                return selectReceptionPage(request, model, sessionIdentifier, personKind);
            case MARK:
                return selectMarkPage(request, model, sessionIdentifier);
            case PATENT:
                return selectPatentPage(request, model, sessionIdentifier);
            case USERDOC:
                return selectUserdocPage(request, model, sessionIdentifier);
            default:
                throw new RuntimeException("Cannot find person list page for session identifier: " + sessionIdentifier);
        }
    }


    protected void reorderAuthors(CAuthorshipData authorshipData) {
        if (Objects.nonNull(authorshipData) && !CollectionUtils.isEmpty(authorshipData.getAuthorList())) {
            authorshipData.getAuthorList().sort(Comparator.comparing(CAuthor::getAuthorSeq));
            for (int i = 0; i < authorshipData.getAuthorList().size(); i++) {
                authorshipData.getAuthorList().get(i).setAuthorSeq(i + Long.valueOf(DefaultValue.INCREMENT_VALUE));
            }
        }
    }

    private String selectUserdocPage(HttpServletRequest request, Model model, String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);

        CUserdoc userdoc = (CUserdoc) SerializationUtils.clone(sessionUserdoc);
        userdoc.setUserdocPersonData(new CUserdocPersonData());
        userdoc.getUserdocPersonData().setPersonList(new ArrayList<>());
        PersonSessionUtils.setSessionPersonsToUserdoc(request, sessionIdentifier, userdoc);
        model.addAttribute("userdoc", userdoc);
        return "ipobjects/userdoc/person/person_panel :: userdoc-persons";
    }

    private String selectPatentPage(HttpServletRequest request, Model model, String sessionIdentifier) {
        setCFileObjectToModal(request, model, sessionIdentifier);
        model.addAttribute("authorships", PersonSessionUtils.getSessionAuthorshipData(request, sessionIdentifier));
        return "ipobjects/patentlike/common/person/person_panel :: persons";
    }

    private String selectMarkPage(HttpServletRequest request, Model model, String sessionIdentifier) {
        setCFileObjectToModal(request, model, sessionIdentifier);
        return "ipobjects/marklike/common/person/person_panel :: persons";
    }

    private String selectReceptionPage(HttpServletRequest request, Model model, String sessionIdentifier, Integer personKind) {
        if (Objects.equals(PersonKind.Representative.code(), personKind)) {
            CRepresentationData representationData = ReceptionSessionUtils.getSessionReceptionRepresentationData(request, sessionIdentifier);
            model.addAttribute("representationData", representationData);
            return "ipobjects/common/reception/correspondent/correspondent_list :: agent-list";
        } else if (Objects.equals(PersonKind.Applicant.code(), personKind)) {
            COwnershipData ownershipData = ReceptionSessionUtils.getSessionReceptionOwnershipData(request, sessionIdentifier);
            model.addAttribute("ownershipData", ownershipData);
            model.addAttribute("isEditable", true);
            return "ipobjects/common/reception/correspondent/correspondent_list :: owner-list";
        } else
            throw new RuntimeException("Person kind for reception is limited to APPLICANT and REPRESENTATIVE !");
    }

    private void setCFileObjectToModal(HttpServletRequest request, Model model, String sessionIdentifier) {
        CFile sessionFile = CFileSessionUtils.getSessionFile(request, sessionIdentifier);
        CFile cFile = new CFile();
        cFile.setFileId(sessionFile.getFileId());
        PersonSessionUtils.setSessionPersonsToCFile(cFile, request, sessionIdentifier);
        model.addAttribute("file", cFile);
    }

}
