package com.duosoft.ipas.util.session.reception;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeGroupService;
import bg.duosoft.ipas.core.service.reception.ReceptionTypeService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.ReceptionType;
import bg.duosoft.ipas.exception.SessionObjectNotFoundException;
import bg.duosoft.ipas.util.person.OwnerUtils;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import com.duosoft.ipas.util.json.CreateReceptionData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.webmodel.ReceptionEuPatentForm;
import com.duosoft.ipas.webmodel.ReceptionForm;
import com.duosoft.ipas.webmodel.ReceptionMarkForm;
import com.duosoft.ipas.webmodel.ReceptionUserdocForm;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class ReceptionSessionUtils {
    public static final String SESSION_RECEPTION_FORM = "sessionReceptionForm";

    public static COwnershipData getSessionReceptionOwnershipData(HttpServletRequest request, String receptionSessionKey) {
        ReceptionForm receptionForm = HttpSessionUtils.getSessionAttribute(receptionSessionKey, request);
        return receptionForm.getOwnershipData();
    }

    public static CRepresentationData getSessionReceptionRepresentationData(HttpServletRequest request, String receptionSessionKey) {
        ReceptionForm receptionForm = HttpSessionUtils.getSessionAttribute(receptionSessionKey, request);
        return receptionForm.getRepresentationData();
    }

    public static List<CPerson> getSessionReceptionPersons(HttpServletRequest request, String receptionSessionKey) {
        List<CPerson> list = new ArrayList<>();

        List<CPerson> applicants = OwnerUtils.convertToCPersonList(getSessionReceptionOwnershipData(request, receptionSessionKey));
        if (!CollectionUtils.isEmpty(applicants)) {
            list.addAll(applicants);
        }

        List<CPerson> representatives = RepresentativeUtils.convertToCPersonList(getSessionReceptionRepresentationData(request, receptionSessionKey));
        if (!CollectionUtils.isEmpty(representatives)) {
            list.addAll(representatives);
        }

        return CollectionUtils.isEmpty(list) ? null : list;
    }

    public static void removeSessionReceptions(HttpServletRequest request) {
        Enumeration<String> attributeNames = request.getSession().getAttributeNames();
        Iterator<String> stringIterator = attributeNames.asIterator();
        while (stringIterator.hasNext()) {
            String sessionObjectName = stringIterator.next();
            if (sessionObjectName.startsWith(SESSION_RECEPTION_FORM))
                HttpSessionUtils.removeSessionAttribute(request, sessionObjectName);
        }
    }

    public static boolean isSessionReceptionExist(HttpServletRequest request, String sessionObjectId) {
        ReceptionForm sessionAttribute = HttpSessionUtils.getSessionAttribute(sessionObjectId, request);
        return Objects.nonNull(sessionAttribute);
    }


    public static void checkSessionObjectExisting(String sessionObjectId, HttpServletRequest request) {
        boolean sessionReceptionExist = isSessionReceptionExist(request, sessionObjectId);
        if (!sessionReceptionExist)
            throw new SessionObjectNotFoundException("Session object not found ! ID: " + sessionObjectId);
    }

    public static ReceptionForm setFormDataToSessionReception(HttpServletRequest request, String sessionIdentifier, CreateReceptionData formData, ReceptionTypeService receptionTypeService) {
        ReceptionForm receptionForm = HttpSessionUtils.getSessionAttribute(sessionIdentifier, request);
            receptionForm.setOriginalExpected(formData.getOriginalExpected());
        receptionForm.setSubmissionType(formData.getSubmissionType());
        receptionForm.setReceptionType(formData.getReceptionType());
        receptionForm.setName(formData.getReceptionName());
        receptionForm.setEntryDate(formData.getEntryDate());
        receptionForm.setNotes(formData.getReceptionNotes());
        if (Objects.nonNull(receptionForm.getInitialDocument())){
            String shortTitle = receptionTypeService.selectById(receptionForm.getReceptionType()).getShortTitle();
            receptionForm.getInitialDocument().setDescription(shortTitle+ " от " + receptionForm.getInitialDocument().getRegNumber());
        }
        if (formData.getReceptionType().equals(ReceptionType.USERDOC.code())) {
            if (Objects.isNull(receptionForm.getUserdoc()))
                receptionForm.setUserdoc(new ReceptionUserdocForm());

            receptionForm.getUserdoc().setUserdocType(formData.getReceptionUserdocType());
            receptionForm.getUserdoc().setFileTypeGroup(formData.getUserdocRelatedObjectGroupType());
            receptionForm.getUserdoc().setObjectNumber(formData.getUserdocRelatedObjectNumber());
        } else
            receptionForm.setUserdoc(null);

        if (formData.getReceptionType().equals(ReceptionType.EU_PATENT.code())) {
            if (Objects.isNull(receptionForm.getEuPatent()))
                receptionForm.setEuPatent(new ReceptionEuPatentForm());

            receptionForm.getEuPatent().setType(formData.getEuPatentType());
            receptionForm.getEuPatent().setObjectNumber(formData.getEuPatentNumber());
        } else
            receptionForm.setEuPatent(null);

        if (formData.getReceptionType().equals(ReceptionType.MARK.code())) {
            if (Objects.isNull(receptionForm.getMark()))
                receptionForm.setMark(new ReceptionMarkForm());

            receptionForm.getMark().setFigurativeMark(formData.getFigurativeMark());
        } else
            receptionForm.setMark(null);

        return receptionForm;
    }

}
