package com.duosoft.ipas.controller.ipobjects.userdoc.reviewers;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.reviewers.CUserdocReviewer;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.userdoc.reviewers.UniqueReviewersValidator;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/userdoc-reviewer-squad")
public class UserdocReviewerSquadController {

    @Autowired
    private UserService userService;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @PostMapping("/save-userdoc-reviewer")
    public String save(HttpServletRequest request, Model model, @RequestParam Boolean isCancel,
                       @RequestParam String sessionIdentifier) {
        CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        if (!isCancel) {
            List<CUserdocReviewer> reviewers = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_REVIEWER_SQUAD, sessionIdentifier, request);
            userdoc.setReviewers(reviewers);
        }
        HttpSessionUtils.removeSessionAttributes(request,sessionIdentifier,UserdocSessionObjects.SESSION_USERDOC_REVIEWER_SQUAD);
        model.addAttribute("reviewers",userdoc.getReviewers());
        return "ipobjects/userdoc/reviewers/reviewer_squad :: reviewer_squad";
    }

    @PostMapping("/add-reviewer")
    public String addReviewer(HttpServletRequest request, Model model,@RequestParam String sessionIdentifier, @RequestParam(required = false) Integer userId) {
        List<CUserdocReviewer> reviewers = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_REVIEWER_SQUAD, sessionIdentifier, request);
        List<ValidationError> errors = new ArrayList<>();
        if(Objects.isNull(userId)){
            errors.add(ValidationError.builder().pointer("reviewers.errors").messageCode("userdoc.empty.reviewer").build());
        }else{
            CUser user = new CUser();
            user.setUserId(userId);
            CUserdocReviewer cUserdocReviewer= new CUserdocReviewer(user,false);
            IpasTwoArgsValidator<CUserdocReviewer, List<CUserdocReviewer>> validator = validatorCreator.createTwoArgsValidator(true, UniqueReviewersValidator.class);
            errors = validator.validate(cUserdocReviewer, reviewers);
        }

        if (!CollectionUtils.isEmpty(errors)){
            model.addAttribute("validationErrors",errors);
        }else{
            reviewers.add(new CUserdocReviewer(userService.getUser(userId),false));
        }
        model.addAttribute("reviewers",reviewers);
        return "ipobjects/userdoc/reviewers/reviewer_squad_content :: reviewer_squad_content";
    }

    @PostMapping("/delete-reviewer")
    public String deleteReviewer(HttpServletRequest request, Model model,@RequestParam String sessionIdentifier, @RequestParam Integer userId) {
        List<CUserdocReviewer> reviewers = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_REVIEWER_SQUAD, sessionIdentifier, request);
        reviewers.removeIf(reviewer->reviewer.getUser().getUserId().equals(userId));
        model.addAttribute("reviewers",reviewers);
        return "ipobjects/userdoc/reviewers/reviewer_squad_content :: reviewer_squad_content";
    }

    @PostMapping("/main-reviewer-check")
    public String mainReviewerCheck(HttpServletRequest request, Model model,@RequestParam String sessionIdentifier, @RequestParam Integer userId,@RequestParam Boolean mainSelected) {
        List<CUserdocReviewer> reviewers = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_REVIEWER_SQUAD, sessionIdentifier, request);
        reviewers.stream().forEach(r->{
            if (r.getUser().getUserId().equals(userId)){
                r.setMain(mainSelected);
            }else{
                r.setMain(false);
            }
        });
        model.addAttribute("reviewers",reviewers);
        return "ipobjects/userdoc/reviewers/reviewer_squad_content :: reviewer_squad_content";
    }

}
