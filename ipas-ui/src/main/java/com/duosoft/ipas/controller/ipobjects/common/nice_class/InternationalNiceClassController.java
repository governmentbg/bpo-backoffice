package com.duosoft.ipas.controller.ipobjects.common.nice_class;

import bg.duosoft.ipas.core.model.mark.CInternationalNiceClass;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import com.duosoft.ipas.webmodel.NiceListType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/international-nice-class/{niceListType}")
public class InternationalNiceClassController {

    @GetMapping("/show-terms-text")
    public String editTermsAsText(HttpServletRequest request, Model model,
                                  @RequestParam Long niceClassCode,
                                  @RequestParam String sessionIdentifier,
                                  @PathVariable NiceListType niceListType){
        List<CInternationalNiceClass> niceClassList = getOriginalInternationalNiceClasses(request, sessionIdentifier, niceListType);
        CInternationalNiceClass existingClass = findNiceClassInList(niceClassCode, niceClassList);

        if(existingClass != null){
            model.addAttribute("niceClassDescription", existingClass.getNiceClassDescription());
            model.addAttribute("niceClassCode", existingClass.getNiceClassCode());
        } else {
            throw new RuntimeException("Cannot find nice class " + niceClassCode + " for session identifier " + sessionIdentifier);
        }

        return "ipobjects/common/nice_class/international_nice_classes_modal :: international-nice-class-terms-text";
    }

    private CInternationalNiceClass findNiceClassInList(Long niceClassCode, List<CInternationalNiceClass> niceClassList) {
        return niceClassList.stream()
                .filter(cInternationalNiceClass -> Objects.equals(cInternationalNiceClass.getNiceClassCode(), niceClassCode))
                .findFirst().orElse(null);
    }

    private List<CInternationalNiceClass> getOriginalInternationalNiceClasses(HttpServletRequest request, String sessionIdentifier, NiceListType niceListType) {
        switch (niceListType) {
            case OBJECT_LIST:
                return MarkSessionUtils.getSessionMark(request, sessionIdentifier).getInternationalNiceClasses();
            case USERDOC_REQUESTED:
                return UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier).getInternationalNiceClasses();
            default:
                throw new RuntimeException("Cannot find original nice classes for session identifier " + sessionIdentifier);
        }
    }
}
