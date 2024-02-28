package com.duosoft.ipas.controller.admin.nice_class;

import bg.duosoft.ipas.core.model.mark.CNiceClassList;
import bg.duosoft.ipas.core.service.mark.NiceClassListService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.mark.MarkNiceClassListValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/nice-class")
public class NiceClassAdministrationController {

    @Autowired
    private NiceClassListService niceClassListService;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/list")
    public String getNiceClasses(Model model) {
        model.addAttribute("niceClasses", niceClassListService.getNiceClassesCodes());
        return "admin/nice_class/nice_class";
    }

    @GetMapping(value = "/edit/{id}")
    public String editUserdocType(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("niceClass", niceClassListService.selectByNiceClassCode(id));
        return "admin/nice_class/nice_class_edit";
    }

    @PostMapping(value = "/save-nice-class")
    public String editNiceClass(@RequestParam("id") Integer id, @RequestParam("heading") String heading, @RequestParam("alphaList") String alphaList, Model model) {
        CNiceClassList cNiceClassList = setNiceClassChanges(id, heading, alphaList);
        IpasValidator<CNiceClassList> validator = validatorCreator.create(false, MarkNiceClassListValidator.class);
        List<ValidationError> errors = validator.validate(cNiceClassList);

        if (CollectionUtils.isEmpty(errors)) {
            CNiceClassList updatedCNiceClassList = niceClassListService.saveNiceClassList(cNiceClassList);
            model.addAttribute("successMessage", messageSource.getMessage("edit.success", null, LocaleContextHolder.getLocale()));
            model.addAttribute("niceClass", updatedCNiceClassList);
            return "admin/nice_class/nice_class_edit_panels :: edit-nice-class-panel";
        } else {
            model.addAttribute("validationErrors", errors);
            return "base/modal/validation_errors::errors";
        }
    }

    private CNiceClassList setNiceClassChanges(Integer niceClassCode, String heading, String alphaList) {
        CNiceClassList cNiceClassList = niceClassListService.selectByNiceClassCode(niceClassCode);

        if(Objects.nonNull(cNiceClassList)) {
            cNiceClassList.setHeading(heading);
            cNiceClassList.setAlphaList(alphaList);
        }

        return cNiceClassList;
    }

}
