package com.duosoft.ipas.controller.ipobjects.marklike.mark;

import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.model.mark.CViennaClass;
import bg.duosoft.ipas.core.service.nomenclature.ViennaClassService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.AttachmentType;
import bg.duosoft.ipas.util.mark.MarkSignDataAttachmentUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/mark")
public class MarkViennaController {
    public static final String VIENNA_CLASS_DELIMITER = ".";

    @Autowired
    private ViennaClassService viennaClassService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/open-vienna-modal")
    public String getViennaCategories(Model model, @RequestParam Integer attachmentIndex, @RequestParam String attachmentType) {
        List<CViennaClass> cViennaClasses = viennaClassService.selectAllCategories();
        model.addAttribute("categories", cViennaClasses);
        model.addAttribute("attachmentIndex", attachmentIndex);
        model.addAttribute("attachmentType", attachmentType);
        return "ipobjects/marklike/common/vienna/vienna_modal :: vienna-modal";
    }

    @PostMapping("/vienna-division")
    public String getViennaDivisions(Model model, @RequestParam Integer category) {
        List<CViennaClass> cViennaClasses = viennaClassService.selectAllDivisionsByCategory(category);
        model.addAttribute("divisions", cViennaClasses);
        return "ipobjects/marklike/common/vienna/vienna_modal :: vienna-division";
    }

    @PostMapping("/vienna-section")
    public String getViennaSections(@RequestParam Integer category, @RequestParam Integer division, Model model) {
        List<CViennaClass> cViennaClasses = viennaClassService.selectAllSectionsByCategoryAndDivision(category, division);
        model.addAttribute("sections", cViennaClasses);
        return "ipobjects/marklike/common/vienna/vienna_modal :: vienna-section";
    }

    @PostMapping("/save-vienna-class")
    public String saveViennaClass(HttpServletRequest request, Model model,
                                  @RequestParam String sessionIdentifier,
                                  @RequestParam Integer attachmentIndex,
                                  @RequestParam String attachmentType,
                                  @RequestParam Integer category,
                                  @RequestParam Integer division,
                                  @RequestParam Integer section) {
        List<CMarkAttachment> attachments = selectAttachmentsListByType(request, sessionIdentifier, attachmentType);
        CMarkAttachment markAttachment = attachments.get(attachmentIndex);
        List<CViennaClass> viennaClassList = markAttachment.getViennaClassList();
        CViennaClass existingViennaClass = findExistingViennaClass(category, division, section, viennaClassList);
        if (Objects.isNull(existingViennaClass)) {
            addViennaClassToSessionList(category, division, section, viennaClassList);
        } else
            log.warn("Vienna class: " + category + VIENNA_CLASS_DELIMITER + division + VIENNA_CLASS_DELIMITER + section + " already exists !");

        model.addAttribute("viennaClassList", viennaClassList);
        model.addAttribute("attachmentIndex", attachmentIndex);
        model.addAttribute("attachmentType", attachmentType);
        return "ipobjects/marklike/common/vienna/vienna_table :: vienna";
    }

    @PostMapping("/delete-vienna-class")
    public String deleteViennaClass(HttpServletRequest request, Model model,
                                    @RequestParam String sessionIdentifier,
                                    @RequestParam Integer attachmentIndex,
                                    @RequestParam String attachmentType,
                                    @RequestParam Integer category,
                                    @RequestParam Integer division,
                                    @RequestParam Integer section) {

        List<CMarkAttachment> attachments = selectAttachmentsListByType(request, sessionIdentifier, attachmentType);
        CMarkAttachment markAttachment = attachments.get(attachmentIndex);
        List<CViennaClass> viennaClassList = markAttachment.getViennaClassList();
        if (!CollectionUtils.isEmpty(viennaClassList)) {
            viennaClassList.removeIf(cViennaClass -> Objects.equals(cViennaClass.getViennaCategory(), Long.valueOf(category))
                    && Objects.equals(cViennaClass.getViennaDivision(), Long.valueOf(division))
                    && Objects.equals(cViennaClass.getViennaSection(), Long.valueOf(section)));
        }

        model.addAttribute("viennaClassList", viennaClassList);
        model.addAttribute("attachmentIndex", attachmentIndex);
        model.addAttribute("attachmentType", attachmentType);
        return "ipobjects/marklike/common/vienna/vienna_table :: vienna";
    }

    @PostMapping("/vienna-fast-adding")
    public String fastAddingViennaClasses(HttpServletRequest request, Model model, @RequestParam String viennaClasses, @RequestParam Integer attachmentIndex, @RequestParam String attachmentType, @RequestParam String sessionIdentifier) {
        List<CMarkAttachment> attachments = selectAttachmentsListByType(request, sessionIdentifier, attachmentType);
        CMarkAttachment markAttachment = attachments.get(attachmentIndex);
        List<CViennaClass> viennaClassList = markAttachment.getViennaClassList();
        List<ValidationError> errors = new ArrayList<>();

        String attachmentIdentifier = attachmentType + attachmentIndex;
        String validationErrorPointer = "fast-adding-" + attachmentIdentifier;
        String viennaClassesInputFiledId = "vienna-fast-adding-" + attachmentIdentifier;

        String[] split = viennaClasses.split(";");
        String notMatching = Arrays.stream(split)
                .filter(viennaClass -> !viennaClass.trim().matches("^([0-9][0-9]" + VIENNA_CLASS_DELIMITER + "[0-9][0-9]" + VIENNA_CLASS_DELIMITER + "[0-9][0-9])"))
                .findAny()
                .orElse(null);

        List<String> missingViennaClasses = new ArrayList<>();
        if (StringUtils.isEmpty(viennaClasses)) {
            errors.add(new ValidationError(validationErrorPointer, "vienna.fast.adding.empty", "", null));
        } else {
            if (Objects.isNull(notMatching)) {
                Set.of(split).forEach(viennaClass -> {
                    viennaClass = viennaClass.trim();
                    String[] viennaParts = viennaClass.split("\\.");
                    Integer category = Integer.valueOf(viennaParts[0]);
                    Integer division = Integer.valueOf(viennaParts[1]);
                    Integer section = Integer.valueOf(viennaParts[2]);
                    CViennaClass existingViennaClass = findExistingViennaClass(category, division, section, viennaClassList);
                    if (Objects.isNull(existingViennaClass)) {
                        boolean isSuccessAdded = addViennaClassToSessionList(category, division, section, viennaClassList);
                        if (!isSuccessAdded)
                            missingViennaClasses.add(viennaClass);
                    }
                });
            } else
                errors.add(new ValidationError(validationErrorPointer, "vienna.fast.adding.format", "", null));
        }


        if (!CollectionUtils.isEmpty(missingViennaClasses)) {
            String missingViennaClassesString = String.join(", ", missingViennaClasses);
            String message = messageSource.getMessage("vienna.fast.adding.missing.classes", new String[]{missingViennaClassesString}, LocaleContextHolder.getLocale());
            errors.add(new ValidationError(validationErrorPointer, null, message, null));
        }

        if (CollectionUtils.isEmpty(errors)) {
            model.addAttribute("viennaClassList", viennaClassList);
            model.addAttribute("attachmentIndex", attachmentIndex);
            model.addAttribute("attachmentType", attachmentType);
            return "ipobjects/marklike/common/vienna/vienna_table :: vienna";
        } else {
            model.addAttribute("errors", errors);
            model.addAttribute("id", viennaClassesInputFiledId);
            return "base/validation :: validation-message";
        }

    }

    private CViennaClass findExistingViennaClass(Integer category, Integer division, Integer section, List<CViennaClass> viennaClassList) {
        return viennaClassList.stream()
                .filter(cViennaClass -> Objects.equals(cViennaClass.getViennaCategory(), Long.valueOf(category))
                        && Objects.equals(cViennaClass.getViennaDivision(), Long.valueOf(division))
                        && Objects.equals(cViennaClass.getViennaSection(), Long.valueOf(section)))
                .findAny()
                .orElse(null);
    }


    private boolean addViennaClassToSessionList(Integer category, Integer division, Integer section, List<CViennaClass> viennaClassList) {
        boolean isSuccessAdded = false;
        List<CViennaClass> cViennaClasses = viennaClassService.selectAllByCategoryDivisionAndSection(category, division, section);
        if (!CollectionUtils.isEmpty(cViennaClasses)) {
            if (cViennaClasses.size() > 1)
                throw new RuntimeException("Vienna classes with this PK are more than 1 ! You have to specify edition code to get unique result !");
            CViennaClass selectedViennaClass = cViennaClasses.get(0);
            viennaClassList.add(selectedViennaClass);
            isSuccessAdded = true;
        }
        return isSuccessAdded;
    }

    private List<CMarkAttachment> selectAttachmentsListByType(HttpServletRequest request, String sessionIdentifier, String attachmentType) {
        List<CMarkAttachment> sessionMarkAttachments = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ATTACHMENTS, sessionIdentifier, request);
        List<CMarkAttachment> attachments;
        AttachmentType attachmentTypeEnum = AttachmentType.valueOf(attachmentType);
        switch (attachmentTypeEnum) {
            case IMAGE:
                attachments = MarkSignDataAttachmentUtils.selectImagesFromAttachments(sessionMarkAttachments);
                break;
            case VIDEO:
                attachments = MarkSignDataAttachmentUtils.selectVideosFromAttachments(sessionMarkAttachments);
                break;
            default:
                throw new RuntimeException("Unknown attachment type ! AttachmentType: " + attachmentType);
        }
        return attachments;
    }
}
