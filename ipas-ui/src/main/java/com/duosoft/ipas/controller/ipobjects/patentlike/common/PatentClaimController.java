package com.duosoft.ipas.controller.ipobjects.patentlike.common;


import bg.duosoft.ipas.core.model.patent.CClaim;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.patent.claims.CClaimMandatoryDataValidator;
import bg.duosoft.ipas.core.validation.patent.claims.CClaimUniqueNumberValidator;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/patent-like/claim")
public class PatentClaimController {

    private final String patentClaimsView = "ipobjects/patentlike/common/claims/claims :: patent-claims";
    private final String patentClaimsModalView = "ipobjects/patentlike/common/claims/claim_form_modal :: patent-claim-form-modal";

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @PostMapping("/edit-claims")
    public String editClaims(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {

        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        if (!isCancel) {
            List<CClaim> claims = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CLAIMS, sessionIdentifier, request);
            if (claims != null && !claims.isEmpty()) {
                patent.getTechnicalData().setClaimList(claims);
            } else {
                patent.getTechnicalData().setClaimList(new ArrayList<>());
            }
        }
        model.addAttribute("claimsList", patent.getTechnicalData().getClaimList());
        PatentSessionUtils.removePatentClaimsSessionObjects(request, sessionIdentifier);
        return patentClaimsView;
    }


        @PostMapping("/edit-list")
        public String editClaimsList(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,
                                 @RequestParam Long claimNbr, @RequestParam String descrEn, @RequestParam String descrBg,@RequestParam Long oldClaimNbr) {

        boolean deleteChangedClaimNbr = false;
        List<ValidationError> errors = new ArrayList<>();
        List<CClaim> claims = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CLAIMS, sessionIdentifier, request);

        if (Objects.isNull(oldClaimNbr)){
            IpasTwoArgsValidator<CClaim, List<CClaim>> cClaimValidator = validatorCreator.createTwoArgsValidator(false, CClaimMandatoryDataValidator.class, CClaimUniqueNumberValidator.class);
            errors = cClaimValidator.validate(new CClaim(claimNbr, descrBg, descrEn),claims);
        }
        if (!Objects.isNull(oldClaimNbr) && oldClaimNbr.equals(claimNbr)){
            IpasTwoArgsValidator<CClaim, List<CClaim>> cClaimValidator = validatorCreator.createTwoArgsValidator(false, CClaimMandatoryDataValidator.class);
            errors = cClaimValidator.validate(new CClaim(claimNbr, descrBg, descrEn),null);
        }
        if (!Objects.isNull(oldClaimNbr) && !oldClaimNbr.equals(claimNbr)){
            IpasTwoArgsValidator<CClaim, List<CClaim>> cClaimValidator = validatorCreator.createTwoArgsValidator(false, CClaimMandatoryDataValidator.class, CClaimUniqueNumberValidator.class);
            errors = cClaimValidator.validate(new CClaim(claimNbr, descrBg, descrEn),claims);

            if (CollectionUtils.isEmpty(errors)){
                deleteChangedClaimNbr=true;
            }

        }

        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("validationErrors", errors);
            model.addAttribute("claim",new CClaim(claimNbr,descrBg,descrEn));
            model.addAttribute("oldClaimNbr",oldClaimNbr);
            return patentClaimsModalView;
        }

        if (deleteChangedClaimNbr){
            claims.removeIf(cClaim -> cClaim.getClaimNbr().equals(oldClaimNbr));
            claims.add(new CClaim(claimNbr, descrBg, descrEn));
        }
        else{
            CClaim cEditedClaim = claims.stream().filter(cClaim -> Objects.equals(cClaim.getClaimNbr(), claimNbr)).findFirst().orElse(null);
            if (Objects.isNull(cEditedClaim)) {
                claims.add(new CClaim(claimNbr, descrBg, descrEn));
            } else{
                cEditedClaim.setClaimDescription(descrBg);
                cEditedClaim.setClaimEnglishDescription(descrEn);
            }
        }

        model.addAttribute("claimsList", claims);
        return patentClaimsView;

    }


    @PostMapping("/delete")
    public String deleteSpecificClaim(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam Long claimNbr) {

        List<CClaim> claims = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CLAIMS, sessionIdentifier, request);
        CClaim cDeletedClaim = claims.stream().filter(cClaim -> Objects.equals(cClaim.getClaimNbr(), claimNbr)).findFirst().orElse(null);
        if (!Objects.isNull(cDeletedClaim) && !CollectionUtils.isEmpty(claims)){
            claims.remove(cDeletedClaim);
        }
        model.addAttribute("claimsList", claims);
        return patentClaimsView;
    }

    @PostMapping("/open-modal")
    public String openClaimModal(HttpServletRequest request, Model model,
                                 @RequestParam String sessionIdentifier,
                                 @RequestParam(required = false) Long claimNbr) {

        List<CClaim> claims = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CLAIMS, sessionIdentifier, request);
        if (claimNbr != null) {
            CClaim cClaimEdited = claims.stream().filter(cClaim -> Objects.equals(cClaim.getClaimNbr(), claimNbr)).findFirst().orElse(null);
            model.addAttribute("oldClaimNbr",claimNbr);
            model.addAttribute("claim", cClaimEdited);
            model.addAttribute("isNewClaim", false);
            return patentClaimsModalView;
        } else {
            model.addAttribute("oldClaimNbr",claimNbr);
            model.addAttribute("claim", new CClaim());
            model.addAttribute("isNewClaim", true);
            return patentClaimsModalView;
        }

    }


}
