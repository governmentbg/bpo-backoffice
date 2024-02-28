package com.duosoft.ipas.controller.ipobjects.offidoc;

import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.service.ext.OffidocAbdocsDocumentService;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.offidoc.OffidocService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.config.exception.ForbiddenException;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionObjects;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/offidoc")
public class OffidocController {

    @Autowired
    private OffidocService offidocService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private OffidocAbdocsDocumentService offidocAbdocsDocumentService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ProcessService processService;

    @RequestMapping(value = {"/detail/{offidocOri}/{offidocSer}/{offidocNbr}"}, method = RequestMethod.GET)
    public String getUserdocDetails(Model model, HttpServletRequest request,
                                    @PathVariable String offidocOri,
                                    @PathVariable Integer offidocSer,
                                    @PathVariable Integer offidocNbr) {
        checkPermissions(offidocOri, offidocSer, offidocNbr);
        COffidoc offidoc = selectOffidocFromModel(model);
        if (Objects.isNull(offidoc)) {
            offidoc = selectOffidocFromDatabase(offidocOri, offidocSer, offidocNbr);
            String sessionIdentifier = HttpSessionUtils.findExistingMainSessionObjectIdentifier(request, new COffidocId(offidocOri, offidocSer, offidocNbr).createFilingNumber());
            if (StringUtils.isEmpty(sessionIdentifier)) {
                Integer maxSessionObjects = yamlConfig.getMaxSessionObjects();
                if (SessionObjectUtils.hasFreeSpaceForSessionObject(request, maxSessionObjects))
                    setSessionOffidoc(model, request, offidoc);
                else
                    return removeSessionObjectsPage(model, request, maxSessionObjects);
            } else {
                String sessionIdentifierWithoutPrefix = HttpSessionUtils.getFilingNumberAndUniqueIdentifierFromSessionObject(sessionIdentifier);
                if (isOffidocEdited(request, offidoc, sessionIdentifierWithoutPrefix))
                    return openExistingObjectPage(model, sessionIdentifier);
                else {
                    OffidocSessionUtils.removeAllOffidocSessionObjects(request, sessionIdentifierWithoutPrefix);
                    setSessionOffidoc(model, request, offidoc);
                }
            }
        }
        setBaseModelAttributes(model, offidoc);
        return "ipobjects/offidoc/view";
    }


    private COffidoc selectOffidocFromModel(Model model) {
        return (COffidoc) model.asMap().get("offidoc");
    }

    private COffidoc selectOffidocFromDatabase(String offidocOri, Integer offidocSer, Integer offidocNbr) {
        COffidoc offidoc = offidocService.findOffidoc(offidocOri, offidocSer, offidocNbr);
        if (Objects.isNull(offidoc))
            throw new RuntimeException("Cannot find offidoc !");

        return offidoc;
    }

    private void setSessionOffidoc(Model model, HttpServletRequest request, COffidoc offidoc) {
        String sessionIdentifier = HttpSessionUtils.generateIdentifier(offidoc.getOffidocId().createFilingNumber());
        HttpSessionUtils.setSessionAttribute(OffidocSessionObjects.SESSION_OFFIDOC, sessionIdentifier, offidoc, request);
        model.addAttribute("offidoc", offidoc);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
    }

    private String removeSessionObjectsPage(Model model, HttpServletRequest request, Integer maxSessionObjects) {
        List<String> sessionObjects = HttpSessionUtils.getMainSessionObjects(request);
        model.addAttribute("sessionObjects", sessionObjects);
        model.addAttribute("maxSessionObjects", maxSessionObjects);
        return "ipobjects/common/session/max_session_objects";
    }

    private boolean isOffidocEdited(HttpServletRequest request, COffidoc offidoc, String sessionIdentifierWithoutPrefix) {
        COffidoc sessionUserdoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifierWithoutPrefix);
        return DiffGenerator.create(offidoc, sessionUserdoc).isChanged();
    }

    private String openExistingObjectPage(Model model, String sessionIdentifier) {
        model.addAttribute("existingSessionIdentifier", sessionIdentifier);
        return "ipobjects/common/session/open_object";
    }

    private void setBaseModelAttributes(Model model, COffidoc offidoc) {
        COffidocAbdocsDocument offidocAbdocsDocument = offidocAbdocsDocumentService.selectById(offidoc.getOffidocId());
        if (Objects.nonNull(offidocAbdocsDocument)) {
            Integer abdocsDocId = offidocAbdocsDocument.getAbdocsDocumentId();
            if (Objects.nonNull(abdocsDocId)) {
                Document document = abdocsService.selectDocumentById(abdocsDocId);
                model.addAttribute("document", document);
            }
        }
        model.addAttribute("statusMap", statusService.getStatusMap());
    }

    private void checkPermissions(String offidocOri, Integer offidocSer, Integer offidocNbr) {
        if (SecurityUtils.hasRights(SecurityRole.OffidocViewAll))
            return;

        COffidocId offidocId = new COffidocId(offidocOri, offidocSer, offidocNbr);
        if (SecurityUtils.hasRights(SecurityRole.OffidocViewOwn) && SecurityUtils.isLoggedUserResponsibleUser(offidocId, processService))
            return;

        throw new ForbiddenException();
    }

}
