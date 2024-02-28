package com.duosoft.ipas.controller.ipobjects.marklike.mark;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMarkSeniority;
import bg.duosoft.ipas.integration.tmseniority.service.TMSeniorityService;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/mark/seniority")
public class MarkSeniorityController {

    @Autowired
    private TMSeniorityService tmSeniorityService;

    @PostMapping("/load-data")
    public String editClaimsPanel(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        String filingNumber = HttpSessionUtils.getFilingNumberFromSessionObject(fullSessionIdentifier);
        CFileId id = CoreUtils.createCFileId(filingNumber);
        if (Objects.nonNull(id)) {
            List<CMarkSeniority> seniorities = null;
            boolean tmSeniorityError = true;

            try {
                seniorities = tmSeniorityService.getMarkSeniorities(id.getFileType(), id.getFileSeries(), id.getFileNbr());
                tmSeniorityError = false;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            model.addAttribute("seniorities", seniorities);
            model.addAttribute("tmSeniorityError", tmSeniorityError);
        }
        return "ipobjects/marklike/common/seniority/seniority_panel :: seniorities";
    }

}
