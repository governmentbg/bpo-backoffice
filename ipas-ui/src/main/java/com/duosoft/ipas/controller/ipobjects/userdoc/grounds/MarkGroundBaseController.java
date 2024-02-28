package com.duosoft.ipas.controller.ipobjects.userdoc.grounds;

import bg.duosoft.ipas.core.service.nomenclature.SignTypeService;
import bg.duosoft.ipas.enums.GroundMarkTypes;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.webmodel.structure.UserdocRootGroundWebModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("userdoc-mark-ground")
public class MarkGroundBaseController {

    private final String nonNationalMarkView = "ipobjects/userdoc/grounds/common_elements/subsections/mark_earlier_right_type_subsection :: not-national-mark-earlier-right-type-subsection";
    private final String nationalMarkView = "ipobjects/userdoc/grounds/common_elements/subsections/mark_earlier_right_type_subsection :: national-mark-earlier-right-type-subsection";

    @Autowired
    private SignTypeService signTypeService;

    private void setModelAttributes(Model model, String sessionIdentifier, UserdocRootGroundWebModel rootGroundModalData){
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("panel", rootGroundModalData.getPanel());
        model.addAttribute("rootGround", rootGroundModalData);
        model.addAttribute("signTypesMap", signTypeService.getSignTypesMap());
    }

    @PostMapping("/import-national-mark-check-action")
    public String importNationalMarkCheckAction(HttpServletRequest request, Model model,
                                       @RequestParam String sessionIdentifier, @RequestParam(required = false) String data) {
        HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE, sessionIdentifier, null, request);
        UserdocRootGroundWebModel rootGroundModalData = JsonUtil.readJson(data, UserdocRootGroundWebModel.class);
        setModelAttributes(model,sessionIdentifier,rootGroundModalData);
        return nationalMarkView;
    }

    @PostMapping("/change-mark-ground-type")
    public String changeMarkGroundType(HttpServletRequest request, Model model,
                                    @RequestParam String sessionIdentifier, @RequestParam(required = false) String data) {
        HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE, sessionIdentifier, null, request);
        UserdocRootGroundWebModel rootGroundModalData = JsonUtil.readJson(data, UserdocRootGroundWebModel.class);
        setModelAttributes(model,sessionIdentifier,rootGroundModalData);
        if (Objects.isNull(rootGroundModalData.getMarkGroundData().getMarkGroundType())){
            return "";
        }
        if (rootGroundModalData.getMarkGroundData().getMarkGroundType().equals(GroundMarkTypes.NATIONAL_MARK.code())){
            rootGroundModalData.getMarkGroundData().setMarkImportedInd(true);
            return nationalMarkView;
        }else if(rootGroundModalData.getMarkGroundData().getMarkGroundType().equals(GroundMarkTypes.PUBLIC_MARK.code()) ||
                rootGroundModalData.getMarkGroundData().getMarkGroundType().equals(GroundMarkTypes.INTERNATIONAL_MARK.code())){
            rootGroundModalData.getMarkGroundData().setMarkImportedInd(false);
            return nonNationalMarkView;
        }else{
            return "";
        }
    }
}
