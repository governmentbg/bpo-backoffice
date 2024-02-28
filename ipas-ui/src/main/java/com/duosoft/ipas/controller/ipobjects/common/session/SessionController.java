package com.duosoft.ipas.controller.ipobjects.common.session;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionObjects;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import com.duosoft.ipas.webmodel.BreadcrumbRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping(value = "/session")
public class SessionController {

    @PostMapping(value = "/get-objects")
    public String getSessionObjects(Model model, HttpServletRequest request, @RequestParam(required = false) String activeSessionObject) {
        List<String> sessionObjects = HttpSessionUtils.getMainSessionObjects(request);
        model.addAttribute("sessionObjects", sessionObjects);
        model.addAttribute("activeSessionObject", activeSessionObject);
        return "ipobjects/common/session/session_object_list :: session-objects-list";
    }

    @PostMapping(value = "/remove-object")
    public String removeSessionObject(Model model, HttpServletRequest request, @RequestParam String sessionObject, @RequestParam(required = false) String activeSessionObject) {
        String identifier = HttpSessionUtils.getUniqueIdentifierFromSessionObject(sessionObject);
        HttpSession session = request.getSession();
        Enumeration<String> attributeNames = session.getAttributeNames();
        Iterator<String> stringIterator = attributeNames.asIterator();
        while (stringIterator.hasNext()) {
            String next = stringIterator.next();
            if (next.contains(identifier))
                HttpSessionUtils.removeSessionAttribute(request, next);
        }
        List<String> sessionObjects = HttpSessionUtils.getMainSessionObjects(request);
        model.addAttribute("sessionObjects", sessionObjects);
        model.addAttribute("activeSessionObject", activeSessionObject);
        return "ipobjects/common/session/session_object_list :: session-objects-list";
    }

    @RequestMapping(value = "/open-existing-object")
    public String openExistingSessionObject(HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam String sessionObject, @RequestParam Boolean openExistingObject) {
        String filingNumber = HttpSessionUtils.getFilingNumberFromSessionObject(sessionObject);
        String sessionIdentifierWithoutPrefix = HttpSessionUtils.getFilingNumberAndUniqueIdentifierFromSessionObject(sessionObject);
        if (Objects.isNull(openExistingObject))
            throw new RuntimeException("Open existing object information is empty !");

        boolean isReception = false;
        if (sessionObject.contains(MarkSessionObjects.SESSION_MARK)) {
            CMark sessionMark = MarkSessionUtils.getSessionMark(request, sessionIdentifierWithoutPrefix);
            if (Objects.isNull(sessionMark))
                throw new RuntimeException("Existing session mark cannot be empty !");

            if (sessionMark.isReception())
                isReception = true;

            processMark(request, redirectAttributes, openExistingObject, sessionIdentifierWithoutPrefix, sessionMark);
        } else if (sessionObject.contains(PatentSessionObject.SESSION_PATENT)) {
            CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifierWithoutPrefix);
            if (Objects.isNull(sessionPatent))
                throw new RuntimeException("Existing session patent cannot be empty !");

            if (sessionPatent.isReception())
                isReception = true;

            processPatent(request, redirectAttributes, openExistingObject, sessionIdentifierWithoutPrefix, sessionPatent);
        } else if (sessionObject.contains(UserdocSessionObjects.SESSION_USERDOC)) {
            CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifierWithoutPrefix);
            if (Objects.isNull(sessionUserdoc))
                throw new RuntimeException("Existing session userdoc cannot be empty !");

            processUserdoc(request, redirectAttributes, openExistingObject, sessionIdentifierWithoutPrefix, sessionUserdoc);
        } else if (sessionObject.contains(OffidocSessionObjects.SESSION_OFFIDOC)) {
            COffidoc sessionOffidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifierWithoutPrefix);
            if (Objects.isNull(sessionOffidoc))
                throw new RuntimeException("Existing session offidoc cannot be empty !");

            processOffidoc(request, redirectAttributes, openExistingObject, sessionIdentifierWithoutPrefix, sessionOffidoc);
            addFlashAttributes(request, redirectAttributes);
            return RedirectUtils.redirectToOffidocViewPage(sessionOffidoc.getOffidocId());
        }

        addFlashAttributes(request, redirectAttributes);
        return RedirectUtils.redirectToObjectViewPage(filingNumber, isReception);
    }

    @PostMapping(value = "/remove-breadcrumb-objects")
    @ResponseStatus(value = HttpStatus.OK)
    public void removeBreadcrumbObjects(HttpServletRequest request, @RequestParam String data, @RequestParam Boolean excludeLastElement) {
        if (!StringUtils.isEmpty(data)) {
            BreadcrumbRecord[] breadcrumbArray = JsonUtil.readJson(data, BreadcrumbRecord[].class);
            if (Objects.nonNull(breadcrumbArray) && breadcrumbArray.length > 0) {
                List<BreadcrumbRecord> breadcrumbRecords = new ArrayList<>(Arrays.asList(breadcrumbArray));
                if (excludeLastElement) {
                    BreadcrumbRecord lastElement = breadcrumbRecords.get(breadcrumbRecords.size() - 1);
                    breadcrumbRecords.removeIf(breadcrumbRecord -> breadcrumbRecord.getText().equalsIgnoreCase(lastElement.getText()) && breadcrumbRecord.getLink().equalsIgnoreCase(lastElement.getLink()));
                }
                if (!CollectionUtils.isEmpty(breadcrumbRecords)) {
                    for (BreadcrumbRecord breadcrumbRecord : breadcrumbRecords) {
                        String ipObjectFilingNumber = breadcrumbRecord.getIpObjectFilingNumber();
                        if (!StringUtils.isEmpty(ipObjectFilingNumber)) {
                            SessionObjectUtils.removeAllSessionObjectsRelatedToIpObject(request, ipObjectFilingNumber);
                        }
                    }
                }
            }
        }
    }

    private void addFlashAttributes(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (Objects.nonNull(inputFlashMap)) {
            String successMessage = (String) inputFlashMap.get("successMessage");
            if (Objects.nonNull(successMessage))
                redirectAttributes.addFlashAttribute("successMessage", successMessage);

            String errorMessage = (String) inputFlashMap.get("errorMessage");
            if (Objects.nonNull(errorMessage))
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

            List<ValidationError> validationErrors = (List<ValidationError>) inputFlashMap.get("validationErrors");
            if (Objects.nonNull(validationErrors))
                redirectAttributes.addFlashAttribute("validationErrors", validationErrors);

            String scrollToPanel = (String) inputFlashMap.get("scrollToPanel");
            if (Objects.nonNull(scrollToPanel))
                redirectAttributes.addFlashAttribute("scrollToPanel", scrollToPanel);

        }
    }

    private void processMark(HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam Boolean openExistingObject, String sessionIdentifierWithoutPrefix, CMark sessionMark) {
        if (openExistingObject) {
            MarkSessionUtils.removeAllMarkSessionObjects(request, sessionIdentifierWithoutPrefix);
            String identifier = HttpSessionUtils.generateIdentifier(sessionMark.getFile().getFileId());
            HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_MARK, identifier, sessionMark, request);
            redirectAttributes.addFlashAttribute("mark", sessionMark);
            redirectAttributes.addFlashAttribute("sessionObjectIdentifier", identifier);
        } else
            MarkSessionUtils.removeAllMarkSessionObjects(request, sessionIdentifierWithoutPrefix);
    }

    private void processPatent(HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam Boolean openExistingObject, String sessionIdentifierWithoutPrefix, CPatent sessionPatent) {
        if (openExistingObject) {
            List<CPatent> singleDesigns = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, sessionIdentifierWithoutPrefix, request);
            int sessionPatentHash = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_HASH, sessionIdentifierWithoutPrefix, request);
            PatentSessionUtils.removeAllPatentSessionObjects(request, sessionIdentifierWithoutPrefix);
            String identifier = HttpSessionUtils.generateIdentifier(sessionPatent.getFile().getFileId());
            HttpSessionUtils.setSessionAttribute(PatentSessionObject.SESSION_PATENT, identifier, sessionPatent, request);
            if (!Objects.isNull(singleDesigns)) {
                HttpSessionUtils.setSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, identifier, singleDesigns, request);
            }
            HttpSessionUtils.setSessionAttribute(PatentSessionObject.SESSION_PATENT_HASH, identifier, sessionPatentHash, request);
            redirectAttributes.addFlashAttribute("patent", sessionPatent);
            redirectAttributes.addFlashAttribute("sessionObjectIdentifier", identifier);
        } else
            PatentSessionUtils.removeAllPatentSessionObjects(request, sessionIdentifierWithoutPrefix);
    }

    private void processUserdoc(HttpServletRequest request, RedirectAttributes redirectAttributes, Boolean openExistingObject, String sessionIdentifierWithoutPrefix, CUserdoc sessionUserdoc) {
        if (openExistingObject) {
            UserdocSessionUtils.removeAllUserdocSessionObjects(request, sessionIdentifierWithoutPrefix);
            CDocumentId userdocId = sessionUserdoc.getDocumentId();
            String identifier = HttpSessionUtils.generateIdentifier(new CFileId(userdocId.getDocOrigin(), userdocId.getDocLog(), userdocId.getDocSeries(), userdocId.getDocNbr()));
            HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC, identifier, sessionUserdoc, request);
            redirectAttributes.addFlashAttribute("userdoc", sessionUserdoc);
            redirectAttributes.addFlashAttribute("sessionObjectIdentifier", identifier);
        } else
            UserdocSessionUtils.removeAllUserdocSessionObjects(request, sessionIdentifierWithoutPrefix);
    }

    private void processOffidoc(HttpServletRequest request, RedirectAttributes redirectAttributes, Boolean openExistingObject, String sessionIdentifierWithoutPrefix, COffidoc sessionOffidoc) {
        if (openExistingObject) {
            OffidocSessionUtils.removeAllOffidocSessionObjects(request, sessionIdentifierWithoutPrefix);
            COffidocId offidocId = sessionOffidoc.getOffidocId();
            String identifier = HttpSessionUtils.generateIdentifier(offidocId.createFilingNumber());
            HttpSessionUtils.setSessionAttribute(OffidocSessionObjects.SESSION_OFFIDOC, identifier, sessionOffidoc, request);
            redirectAttributes.addFlashAttribute("offidoc", sessionOffidoc);
            redirectAttributes.addFlashAttribute("sessionObjectIdentifier", identifier);
        } else
            OffidocSessionUtils.removeAllOffidocSessionObjects(request, sessionIdentifierWithoutPrefix);
    }


}