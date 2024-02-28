package com.duosoft.ipas.controller.ipobjects.marklike.common;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.mark.CProtectionData;
import com.duosoft.ipas.controller.ipobjects.common.nice_class.NiceClassController;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mark-like/nice-class")
public class MarkLikeNiceClassController extends NiceClassController {

    @PostMapping("/edit-panel-nice")
    public String editNiceClassesPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam String sessionIdentifier) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);

        if (!isCancel)
            setSessionNiceClassesToMark(mark, request,sessionIdentifier);

        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, MarkSessionObjects.SESSION_MARK_NICE_CLASSES);
        model.addAttribute("mark", mark);
        return "ipobjects/marklike/common/niceclasses/nice_classes_panel :: nice-classes";
    }

    private void setSessionNiceClassesToMark(CMark cMark, HttpServletRequest request, String sessionIdentifier) {
        List<CNiceClass> niceClassList = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_NICE_CLASSES, sessionIdentifier, request);
        if (cMark.getProtectionData() == null) {
            cMark.setProtectionData(new CProtectionData());
        }

        cMark.getProtectionData().setNiceClassList(niceClassList);
    }
}
