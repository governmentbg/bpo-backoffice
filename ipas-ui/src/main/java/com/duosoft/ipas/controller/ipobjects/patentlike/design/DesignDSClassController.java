package com.duosoft.ipas.controller.ipobjects.patentlike.design;

import bg.duosoft.ipas.core.model.design.CProductTerm;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.util.LocalizationProperties;
import bg.duosoft.ipas.integration.dsclass.service.DSClassService;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Raya
 * 17.04.2020
 */
@Controller
@RequestMapping("/design/ds-class")
public class DesignDSClassController {

    @Autowired
    private DSClassService dsClassService;

    private LocalizationProperties defaultLocalizationProperties = new LocalizationProperties("BG", "bg");;


    private final String designProductVerifyView = "ipobjects/patentlike/design/product_verification :: product_verification";

    @PostMapping("/verify-single-product")
    public String verifySingleDesignProduct(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam String filingNumber, @RequestParam String index) {
        CFileId cFileId = CoreUtils.createCFileId(filingNumber);
        List<CPatent> singleDesigns = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, sessionIdentifier, request);
        CPatent singleDesign =  singleDesigns != null ?
            singleDesigns.stream().filter(r->r.getFile().getFileId().equals(cFileId)).findFirst().orElse(null) :
            null;

        if (singleDesign == null || singleDesign.getTechnicalData() == null){
            model.addAttribute("error", "design.not.found.for.validation");
        } else if (singleDesign.getTechnicalData().getTitle() == null ||
            singleDesign.getTechnicalData().getLocarnoClassList() == null ||
            singleDesign.getTechnicalData().getLocarnoClassList().size() == 0){
            model.addAttribute("error", "design.no.locarno.for.validation");
        } else {
            List<String> productClasses = singleDesign.getTechnicalData().getLocarnoClassList().stream().map(l -> l.getLocarnoClassCode()).collect(Collectors.toList());
            CProductTerm verified;
            try {
                verified = dsClassService.verifyDesignProductTerm(defaultLocalizationProperties,
                    singleDesign.getTechnicalData().getTitle(),
                    productClasses);
            } catch (Exception e){
                verified = singleDesign.getProductTerm();
                model.addAttribute("error", "design.verify.error");
            }
            singleDesign.setProductTerm(verified);
        }

        model.addAttribute("singleDesign", singleDesign);
        model.addAttribute("index", index);

        return designProductVerifyView;
    }

}
