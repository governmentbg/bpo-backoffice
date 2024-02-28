package com.duosoft.ipas.controller.ipobjects.userdoc.grounds;

import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.json.GroundNiceClasses;
import com.duosoft.ipas.webmodel.structure.UserdocRootGroundWebModel;
import de.danielbechler.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("ground/nice-class")
public class GroundNiceClassController {

    private final String niceClassesView = "ipobjects/userdoc/grounds/common_elements/subsections/ground_nice_classes_subsection :: ground-nice-classes-table";

    private List<ValidationError> validateNiceClasses(UserdocRootGroundWebModel rootGroundModalData, Long niceClassCode, String niceClassDescription, boolean isAdd){
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.isNull(niceClassCode)){
            errors.add(ValidationError.builder().pointer("nice.classes.table.errors").messageCode("root.ground.nice.classes.mandatory.class").build());
            return errors;
        }
        if ((Objects.isNull(niceClassDescription) || niceClassDescription.isEmpty()) && isAdd){
            errors.add(ValidationError.builder().pointer("nice.classes.table.errors").messageCode("root.ground.nice.classes.mandatory.description").build());
            return errors;
        }
        if (isAdd){
            for (GroundNiceClasses nice:rootGroundModalData.getMarkGroundData().getNiceClasses()) {
                if (nice.getNiceClassCode().equals(niceClassCode)){
                    errors.add(ValidationError.builder().pointer("nice.classes.table.errors").messageCode("root.ground.nice.classes.not.unique").build());
                    return errors;
                }
            }
        }

        return errors;
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request, Model model,
                                    @RequestParam String sessionIdentifier,@RequestParam Long niceClassCode,@RequestParam(required = false) String niceClassDescription, @RequestParam(required = false) String data) {

        UserdocRootGroundWebModel rootGroundModalData = JsonUtil.readJson(data, UserdocRootGroundWebModel.class);
        List<ValidationError> errors= validateNiceClasses(rootGroundModalData,niceClassCode,niceClassDescription,false);
        if (!Collections.isEmpty(errors)){
            model.addAttribute("validationErrors",errors);
            model.addAttribute("niceClasses",rootGroundModalData.getMarkGroundData().getNiceClasses());
            return niceClassesView;
        }
        rootGroundModalData.getMarkGroundData().getNiceClasses().removeIf(r->r.getNiceClassCode().equals(niceClassCode));
        model.addAttribute("niceClasses",rootGroundModalData.getMarkGroundData().getNiceClasses());
        return niceClassesView;
    }

    @PostMapping("/add")
    public String add(HttpServletRequest request, Model model,
                         @RequestParam String sessionIdentifier,@RequestParam Long niceClassCode,@RequestParam String niceClassDescription, @RequestParam(required = false) String data) {


        UserdocRootGroundWebModel rootGroundModalData = JsonUtil.readJson(data, UserdocRootGroundWebModel.class);
        List<ValidationError> errors= validateNiceClasses(rootGroundModalData,niceClassCode,niceClassDescription,true);
        if (!Collections.isEmpty(errors)){
            model.addAttribute("validationErrors",errors);
            model.addAttribute("niceClasses",rootGroundModalData.getMarkGroundData().getNiceClasses());
            return niceClassesView;
        }
        rootGroundModalData.getMarkGroundData().getNiceClasses().add(new GroundNiceClasses(niceClassCode,niceClassDescription));
        model.addAttribute("niceClasses",rootGroundModalData.getMarkGroundData().getNiceClasses());
        return niceClassesView;
    }

}
