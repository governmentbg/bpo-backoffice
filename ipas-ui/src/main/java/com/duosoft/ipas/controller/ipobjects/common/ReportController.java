package com.duosoft.ipas.controller.ipobjects.common;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.report.ReportService;
import bg.duosoft.ipas.services.core.IpasReportService;
import bg.duosoft.ipas.services.core.IpasServiceException;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import com.duosoft.ipas.controller.ipobjects.common.attachment.FileController;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.file.CFileSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ReportService reportService;

    private final String reportModalView = "ipobjects/common/identity/reports_modal :: reports-modal";

    @PostMapping("/open-reports-modal")
    public String openReportsModal(HttpServletRequest request, Model model) {
        model.addAttribute("fileNames",reportService.getReportTemplateFileNames());
        return reportModalView;
    }

    @RequestMapping(value = "/generate-report")
    public void generateReport(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestParam String sessionIdentifier,
                               @RequestParam String fileName, RedirectAttributes redirectAttributes) throws IpasServiceException {
        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        List<CFileId> objectIds =new ArrayList<>();
        List<CDocumentId> documentIds =new ArrayList<>();
        if (SessionObjectUtils.isSessionObjectUserdoc(fullSessionIdentifier)){
            documentIds.add(UserdocSessionUtils.getSessionUserdoc(request,sessionIdentifier).getDocumentId());
        }else{
            objectIds.add(CFileSessionUtils.getSessionFile(request, sessionIdentifier).getFileId());
        }
        byte[] content = reportService.generateDocument(fileName, objectIds, documentIds, null, null, IpasReportService.CONTENT_TYPE_DOCX);
        AttachmentUtils.writeData(response,content,AttachmentUtils.getContentType(content),fileName);
    }

}
