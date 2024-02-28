package com.duosoft.ipas.controller.ipobjects.marklike.mark;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.logging.annotation.LogExecutionTime;
import com.duosoft.ipas.controller.ipobjects.marklike.common.MarkLikeController;
import com.duosoft.ipas.util.CFileRelationshipUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/mark")
public class MarkController extends MarkLikeController {

    @Autowired
    private IpoSearchService searchService;;
    @Autowired
    private MarkService markService;
    @Autowired
    private MessageSource messageSource;

    @Override
    public void validateFileType(String fileType) {
        if (!FileType.MARK.code().equals(fileType) && !FileType.DIVISIONAL_MARK.code().equals(fileType))
            throw new RuntimeException("Invalid mark file type: " + fileType);
    }

    @Override
    public String getViewPage() {
        return "ipobjects/marklike/mark/view";
    }

    @Override
    public void setModelAttributes(Model model, CMark mark) {
        if (mark.getFile() != null) {
            CFileRelationshipUtils.supplyViewWithDivisionalData(searchService, model, mark.getFile());
        }
    }

//    @PostMapping("/create-divided-mark")
//    public String createDividedMark(HttpServletRequest request,
//                           RedirectAttributes redirectAttributes,
//                           @RequestParam String sessionIdentifier){
//        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
//        CMark dividedMark = markService.createDividedMark(mark);
//        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("create.divided.mark.success.message", new String[]{}, LocaleContextHolder.getLocale()));
//        return RedirectUtils.redirectToObjectViewPage(dividedMark.getFile().getFileId().createFilingNumber(), false);
//    }

}
