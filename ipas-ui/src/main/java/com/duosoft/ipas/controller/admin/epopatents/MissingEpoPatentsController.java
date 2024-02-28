package com.duosoft.ipas.controller.admin.epopatents;

import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.ApplicationSearchType;
import bg.duosoft.ipas.util.eupatent.EuPatentUtils;
import com.duosoft.ipas.util.CoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/admin/missing-epo-patents")
public class MissingEpoPatentsController {

    @Autowired
    private EbdPatentService ebdPatentService;

    @Autowired
    private PatentService patentService;

    @Autowired
    private MessageSource messageSource;


    @GetMapping
    public String openPage(Model model) {
        return "admin/epopatents/missing_epo_patents";
    }

    @PostMapping(value = "/update-table")
    public String updateTable(Model model, String searchText, @RequestParam String searchType) {

        List<ValidationError> validationErrors = validate(searchText);
        if (CollectionUtils.isEmpty(validationErrors)) {
            Integer searchNumber = Integer.valueOf(searchText.trim());
            ApplicationSearchType applicationSearchType = ApplicationSearchType.selectByCode(searchType);
            CEbdPatent patent;
            switch (applicationSearchType) {
                case FILE_NUMBER:
                    patent = ebdPatentService.selectByFileNumber(searchNumber);
                    break;
                case REGISTRATION_NUMBER:
                    patent = ebdPatentService.selectByRegistationNumber(searchNumber);
                    break;
                default:
                    patent = null;
            }

            List<CEbdPatent> cEbdPatents = Objects.nonNull(patent) ? Collections.singletonList(patent) : null;
            model.addAttribute("result", cEbdPatents);
        } else {
            model.addAttribute("validationErrors", validationErrors);
            model.addAttribute("searchText", searchText);
            return "admin/epopatents/missing_epo_patents :: input";
        }
        return "admin/epopatents/missing_epo_patents_table :: table";
    }

    @PostMapping(value = "/save")
    public String saveEpoPatent(Model model, @RequestParam Integer filingNumber, RedirectAttributes redirectAttributes) {
        CEbdPatent patent = ebdPatentService.selectByFileNumber(filingNumber);
        if (Objects.nonNull(patent)) {
            CFileId fileId = EuPatentUtils.generateEUPatentCFileId(patent);
            boolean isPatentExistsInIpas = patentService.isPatentExists(fileId);
            if (isPatentExistsInIpas) {
                redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("epopatent.exists.ipas", new String[]{fileId.createFilingNumber()}, LocaleContextHolder.getLocale()));
                return "redirect:/admin/missing-epo-patents";
            }

            boolean success = ebdPatentService.save(patent,false);
            if (!success){
                redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("missing.epo.patent.errors", null, LocaleContextHolder.getLocale()));
            }else{
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("missing.epo.patent.successful", new String[]{fileId.createFilingNumber()}, LocaleContextHolder.getLocale()));
            }

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("missing.epo.patent.errors", null, LocaleContextHolder.getLocale()));
        }
        return "redirect:/admin/missing-epo-patents";
    }

    private List<ValidationError> validate(String searchText) {
        List<ValidationError> errors = new ArrayList<>();
        if (StringUtils.isEmpty(searchText)) {
            errors.add(ValidationError.builder().pointer("epopatentInput").messageCode("required.field").build());
        } else {
            String trimmedText = searchText.trim();
            if (!CoreUtils.isNumber(trimmedText)) {
                errors.add(ValidationError.builder().pointer("epopatentInput").messageCode("required.number").build());
            }
        }

        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

}
