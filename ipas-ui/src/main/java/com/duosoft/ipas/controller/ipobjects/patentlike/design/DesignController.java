package com.duosoft.ipas.controller.ipobjects.patentlike.design;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.nomenclature.ImageViewTypeService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.ProcessType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.default_value.DefaultValueUtils;
import com.duosoft.ipas.controller.ipobjects.patentlike.common.PatentLikeController;
import com.duosoft.ipas.util.CFileRelationshipUtils;
import com.duosoft.ipas.util.PatentDrawingUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignController extends PatentLikeController {

    @Autowired
    private DefaultValueUtils defaultValueUtils;

    @Autowired
    private PatentService patentService;

    @Autowired
    private DesignService designService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private ImageViewTypeService imageViewTypeService;

    @Override
    public boolean isValidFileType(String fileType) {
        return FileType.DESIGN.code().equals(fileType) || FileType.INTERNATIONAL_DESIGN.code().equals(fileType);
    }

    @Override
    public String getViewPage() {
        return "ipobjects/patentlike/design/view";
    }

    @Override
    public void setModelAttributes(Model model, CPatent patent) {
        CFileRelationshipUtils.supplyViewWithDivisionalData(searchService, model, patent.getFile());
        model.addAttribute("defaultValues", defaultValueUtils.createPatentLikeDefaultValuesObject(patent));
    }

    @Override
    public String savePatent(HttpServletRequest request,
                             RedirectAttributes redirectAttributes,
                             @RequestParam String sessionIdentifier,
                             @RequestParam(required = false) List<String> editedPanels) {
        CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        List<CPatent> sessionSingleDesigns = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, sessionIdentifier, request);
        log.debug("Read session single designs on save design - key:".concat(PatentSessionObject.SESSION_SINGLE_DESIGNS.concat(DefaultValue.DASH).concat(sessionIdentifier)).concat(" and size:").concat(CollectionUtils.isEmpty(sessionSingleDesigns) ? "no elements" : String.valueOf(sessionSingleDesigns.size())));
        CFileId fileId = sessionPatent.getFile().getFileId();

        CPatent cloneSessionPatent = (CPatent) SerializationUtils.clone(sessionPatent);
        List<CPatent> cloneSingleDesigns = new ArrayList<>();
        if (!Objects.isNull(sessionSingleDesigns)) {
            sessionSingleDesigns.stream().forEach(singleDesign -> {
                cloneSingleDesigns.add((CPatent) SerializationUtils.clone(singleDesign));
            });
        }
        loadDesignDrawings(cloneSessionPatent, cloneSingleDesigns);
        try {
            if (cloneSessionPatent.isReception()) {
                designService.insertDesigns(cloneSessionPatent, cloneSingleDesigns);
            } else {
                designService.updateDesigns(cloneSessionPatent, cloneSingleDesigns);
            }
            PatentSessionUtils.removeAllPatentSessionObjects(request, sessionIdentifier);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("edit.success." + FileType.selectByCode(fileId.getFileType()).name(), null, LocaleContextHolder.getLocale()));
        } catch (IpasValidationException e) {
            redirectAttributes.addFlashAttribute("validationErrors", e.getErrors());
            redirectAttributes.addFlashAttribute("patent", sessionPatent);
            redirectAttributes.addFlashAttribute("singleDesigns", sessionSingleDesigns);
            redirectAttributes.addFlashAttribute("sessionObjectIdentifier", sessionIdentifier);
            redirectAttributes.addFlashAttribute("imageViewTypeList", imageViewTypeService.getAllImageViewTypes());
            redirectAttributes.addFlashAttribute("singleDesignStatuses", statusService.getInitialOrFinalStatusesByProcType(sessionPatent.getFile().getFileId().getFileType().equals(FileType.DESIGN.code()) ? ProcessType.NATIONAL_SINGLE_DESIGN_PROCESS_TYPE.code() : ProcessType.INTERNATIONAL_SINGLE_DESIGN_PROCESS_TYPE.code()));
            redirectAttributes.addFlashAttribute("singleDesignAppSubTypes", applicationTypeService.findAllSingleDesignApplicationSubTypesByMasterApplTyp(sessionPatent.getFile().getFilingData().getApplicationType()));
        }
        return RedirectUtils.redirectToObjectViewPage(fileId, false);
    }

    private void loadDesignDrawings(CPatent design, List<CPatent> singleDesigns) {
        PatentDrawingUtils.loadCPatentEmptyDrawings(design, patentService);
        if (!Objects.isNull(singleDesigns)) {
            singleDesigns.stream().forEach(cloneSingleDesign -> {
                PatentDrawingUtils.loadCPatentEmptyDrawings(cloneSingleDesign, patentService);
            });
        }

    }
}
