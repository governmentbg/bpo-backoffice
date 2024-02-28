package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.structure.UserService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/generic-responsible-user-change")
public class GenericResponsibleUserChangeController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private ProcessService processService;

    @PostMapping("/change")
    public String changeResponsibleUser(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model, @RequestParam String redirectUrl,
                                        @RequestParam(required = false) Integer userId,@RequestParam List<String> checkedProcTypes) {

        if (Objects.isNull(userId)){
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("generic.responsible.user.change.empty.user", new String[]{""}, LocaleContextHolder.getLocale()));
        }else{
            if (!CollectionUtils.isEmpty(checkedProcTypes)){
                User user = userService.getUser(userId);
                List<CProcessId> cProcessIds =new ArrayList<>();
                checkedProcTypes.stream().forEach(r->{
                    String[] procIdSplit = r.split("-");
                    cProcessIds.add(new CProcessId(procIdSplit[0],Integer.valueOf(procIdSplit[1])));
                });
                processService.updateResponsibleUsers(user.getUserId(),cProcessIds);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("generic.responsible.user.change.success", new String[]{user.getUserName()}, LocaleContextHolder.getLocale()));
            }else{
                redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("generic.responsible.user.change.empty.procs.list", new String[]{""}, LocaleContextHolder.getLocale()));
            }
        }
        return "redirect:"+redirectUrl;
    }

}
