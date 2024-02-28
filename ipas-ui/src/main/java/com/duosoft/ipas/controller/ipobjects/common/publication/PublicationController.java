package com.duosoft.ipas.controller.ipobjects.common.publication;

import bg.duosoft.ipas.core.model.CPublicationInfo;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.PublicationService;
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

@Slf4j
@Controller
@RequestMapping("/publications")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    @PostMapping("/load-panel")
    public String loadPublicationPanel(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        String filingNumber = HttpSessionUtils.getFilingNumberFromSessionObject(fullSessionIdentifier);
        model.addAttribute("filingNumber", filingNumber);
        addLiabilityDetailsToModel(model, filingNumber);
        return "ipobjects/common/publication/publication_panel :: publication";
    }

    private void addLiabilityDetailsToModel(Model model, String filingNumber) {
        try {
            CFileId fileId = CoreUtils.createCFileId(filingNumber);
            List<CPublicationInfo> publications = publicationService.selectPublications(CoreUtils.createFilingNumber(fileId, true));
            model.addAttribute("publications", publications);
            model.addAttribute("hasError", false);
        } catch (Exception e) {
            model.addAttribute("hasError", true);
            log.error(e.getMessage(), e);
        }
    }

}
