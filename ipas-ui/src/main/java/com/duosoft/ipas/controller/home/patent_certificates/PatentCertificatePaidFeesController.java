package com.duosoft.ipas.controller.home.patent_certificates;

import bg.duosoft.abdocs.model.Attachments;
import bg.duosoft.abdocs.model.DocCreation;
import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.CExtLiabilityDetail;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.service.payments.PaymentsService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.patent.PatentCertificatePaidFeesService;
import bg.duosoft.ipas.core.service.report.ReportService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.integration.abdocs.converter.DocCreationConverter;
import bg.duosoft.ipas.persistence.model.nonentity.PatentCertificatePaidFeesResult;
import bg.duosoft.ipas.services.core.IpasReportService;
import bg.duosoft.ipas.services.core.IpasServiceException;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.ProgressBarUtils;
import com.duosoft.ipas.webmodel.patent_certificates.PatentCertificatePaidFeesProgressBar;
import com.duosoft.ipas.webmodel.patent_certificates.PatentCertificatePaidFeesProgressResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/patent-certificates/paid-fees")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).CertificatesForPaidPatentFees.code())")
public class PatentCertificatePaidFeesController {

    private static final String PATENT_PAID_FEES_ABDOCS_DOCUMENT_SUBJECT = "Удостоверение за платени такси: {0}";
    private static final PatentCertificatePaidFeesProgressBar progressBar = new PatentCertificatePaidFeesProgressBar();

    private final FileService fileService;
    private final ReportService reportService;
    private final AbdocsService abdocsService;
    private final ConfigParamService configParamService;
    private final DocCreationConverter docCreationConverter;
    private final PaymentsService paymentsService;
    private final PatentCertificatePaidFeesService patentPaidFeesService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        CustomDateEditor customDateEditor = new CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DOT), true, 10);
        binder.registerCustomEditor(Date.class, customDateEditor);
    }

    @GetMapping
    public String view(Model model) {
        model.addAttribute("progressBar", progressBar);
        return "home/patent_certificates/paid_fees/view";
    }

    @PostMapping("search")
    public String search(Model model,
                         @RequestParam(required = false) String abdocsDoc,
                         @RequestParam(required = false) String payer,
                         @RequestParam(required = false) Date paymentDateFrom,
                         @RequestParam(required = false) Date paymentDateTo) {
        List<ValidationError> validationErrors = validateSearchForm(paymentDateFrom, paymentDateTo, abdocsDoc, payer);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            model.addAttribute("validationErrors", validationErrors);
            model.addAttribute("paymentDateFrom", paymentDateFrom);
            model.addAttribute("paymentDateTo", paymentDateTo);
            model.addAttribute("abdocsDoc", abdocsDoc);
            model.addAttribute("payer", payer);
            return "home/patent_certificates/paid_fees/filters :: filters";
        } else {
            List<PatentCertificatePaidFeesResult> paidFees = patentPaidFeesService.selectPaidFees(paymentDateFrom, paymentDateTo, payer);
            model.addAttribute("paidFees", paidFees);
            model.addAttribute("document", abdocsService.selectDocumentByRegistrationNumber(abdocsDoc));
            return "home/patent_certificates/paid_fees/table :: table";
        }
    }


    @PostMapping("/update-generation-result")
    public String updateGenerationResult(Model model) {
        model.addAttribute("progressBar", progressBar);
        List<PatentCertificatePaidFeesProgressResult> results = progressBar.getResults();
        model.addAttribute("generatedDocuments", CollectionUtils.isEmpty(results) ? null : new ArrayList<>(results));
        return "home/patent_certificates/paid_fees/result :: result";
    }

    @PostMapping("/start-progressbar")
    public String startProgressBar(RedirectAttributes redirectAttributes, @RequestParam List<Integer> paidFees, @RequestParam String abdocsDoc) {
        if (!progressBar.isInProgress()) {
            progressBar.start(paidFees, abdocsDoc);
            Collections.sort(progressBar.getPaidFeesForGeneration());
            redirectAttributes.addFlashAttribute("executeProcess", true);
        }
        return "redirect:/patent-certificates/paid-fees";
    }

    @PostMapping("/stop-progressbar")
    public String stop() {
        progressBar.stop("Interrupted by user !");
        return "redirect:/patent-certificates/paid-fees";
    }

    @PostMapping("/select-progressbar-info")
    @ResponseBody
    public PatentCertificatePaidFeesProgressBar selectProgressBarInfo() {
        return progressBar;
    }

    @PostMapping("/execute-process")
    @ResponseStatus(value = HttpStatus.OK)
    public void run() {
        try {
            executeProcess();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            progressBar.stop(e.getMessage());
        }
    }

    @RequestMapping(value = "/download-file")
    public void getAbdocsFile(HttpServletResponse response, @RequestParam Integer id) throws IpasServiceException {
        CExtLiabilityDetail liabilityDetail = paymentsService.getLiabilityDetailById(id);
        if (Objects.nonNull(liabilityDetail)) {
            Attachments attachment = generateAttachment(liabilityDetail.getFileId().createFilingNumber(), id);
            AttachmentUtils.writeData(response, attachment.getContent(), attachment.getMimeType(), attachment.getName());
        }
    }

    private List<ValidationError> validateSearchForm(Date paymentDateFrom, Date paymentDateTo, String abdocsDoc, String payer) {
        List<ValidationError> errors = new ArrayList<>();
        if (StringUtils.isEmpty(abdocsDoc)) {
            errors.add(ValidationError.builder().pointer("paidFees.abdocsDoc").messageCode("required.field").build());
        } else {
            Document document = abdocsService.selectDocumentByRegistrationNumber(abdocsDoc);
            if (Objects.isNull(document)) {
                errors.add(ValidationError.builder().pointer("paidFees.abdocsDoc").messageCode("patentCertificate.paidFees.abdocsDoc.not.found").build());
            }
        }
        if (Objects.isNull(paymentDateFrom)) {
            errors.add(ValidationError.builder().pointer("paidFees.dateFrom.filter.input").messageCode("required.field").build());
        }
        if (Objects.isNull(paymentDateTo)) {
            errors.add(ValidationError.builder().pointer("paidFees.dateTo.filter.input").messageCode("required.field").build());
        }
        if (StringUtils.isEmpty(payer)) {
            errors.add(ValidationError.builder().pointer("paidFees.payer.filter.input").messageCode("required.field").build());
        }
        return errors;
    }


    private Attachments generateAttachment(String filingNumber, Integer liabilityDetailId) throws IpasServiceException {
        CConfigParam templatePath = configParamService.selectConfigByCode(ConfigParamService.OFFICE_DOCUMENT_TEMPLATES_DIRECTORY);
        if (Objects.isNull(templatePath))
            throw new RuntimeException("Cannot find config record for code: " + ConfigParamService.OFFICE_DOCUMENT_TEMPLATES_DIRECTORY);

        CConfigParam templateName = configParamService.selectExtConfigByCode(ConfigParamService.PATENT_CERTIFICATE_PAID_FEES_TEMPLATE);
        if (Objects.isNull(templateName))
            throw new RuntimeException("Cannot find ext config record for code: " + ConfigParamService.PATENT_CERTIFICATE_PAID_FEES_TEMPLATE);

        String template = templatePath.getValue() + File.separator + templateName.getValue();
        byte[] attachmentContent = reportService.generateDocument(template, null, Collections.singletonList(new CDocumentId(null, null, null, liabilityDetailId)), null, null, IpasReportService.CONTENT_TYPE_DOCX);
        return new Attachments(attachmentContent, AttachmentUtils.DOCX_MIME_TYPE, generateAttachmentName(filingNumber));
    }

    private String generateAttachmentName(String filingNumber) {
        StringBuilder fileNameSb = new StringBuilder("Удостоверение за плащане " + CoreUtils.replaceForwardSlashWithUnderscore(filingNumber));
        Integer regNumber = fileService.selectRegistrationNumberById(BasicUtils.createCFileId(filingNumber));
        if (Objects.nonNull(regNumber)) {
            fileNameSb.append("-").append(regNumber);
        }
        fileNameSb.append(".docx");
        return fileNameSb.toString();
    }

    private void executeProcess() {
        List<Integer> paidLiabilities = progressBar.getPaidFeesForGeneration();
        if (CollectionUtils.isEmpty(paidLiabilities)) {
            progressBar.stop("Empty paid fees list !");
        } else {
            String abdocsDocRegNumber = progressBar.getPatentCertificateMainObject();
            Document parentDoc = abdocsService.selectDocumentByRegistrationNumber(abdocsDocRegNumber);
            DocCreation docCreation = docCreationConverter.convertPatentCertificate(parentDoc, PATENT_PAID_FEES_ABDOCS_DOCUMENT_SUBJECT.replace("{0}", abdocsDocRegNumber), null);
            Document document = abdocsService.registerDocument(docCreation, null);
            if (Objects.isNull(document)) {
                throw new RuntimeException("Abdocs certificate document is empty ! " + abdocsDocRegNumber);
            }

            int total = paidLiabilities.size();
            for (int i = 0; i < total; i++) {
                if (!progressBar.isInProgress()) {
                    return;
                }
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                Integer liabilityId = paidLiabilities.get(i);

                PatentCertificatePaidFeesProgressResult result = new PatentCertificatePaidFeesProgressResult();
                try {
                    CExtLiabilityDetail extLiabilityDetail = paymentsService.getLiabilityDetailById(liabilityId);
                    if (Objects.isNull(extLiabilityDetail)) {
                        throw new RuntimeException("Ext liability detail record is empty ! Liability id: " + liabilityId);
                    }
                    String filingNumber = extLiabilityDetail.getFileId().createFilingNumber();
                    result.setExtLiabilityDetail(extLiabilityDetail);
                    result.setDocumentId(document.getDocId());

                    Attachments attachment = generateAttachment(filingNumber, liabilityId);
                    abdocsService.uploadFileToExistingDocument(document.getDocId(), attachment.getContent(), attachment.getName(), false, DocFileVisibility.PrivateAttachedFile);
                    result.setGeneratedSuccessfully(true);
                } catch (Exception e) {
                    log.error("Exception during generation of certificate for paid patent fees! Liability id: " + liabilityId);
                    log.error(e.getMessage(), e);
                    result.setErrorMessage(e.getMessage());
                    result.setGeneratedSuccessfully(false);
                }
                progressBar.getResults().add(result);
                stopWatch.stop();
                String logMessage = "Iteration " + i + " of " + total + ". Time: " + stopWatch.getTotalTimeMillis() + " ms. ";
                log.debug(logMessage);
                progressBar.setMessage(logMessage);
                progressBar.setProgress(ProgressBarUtils.calculateProgress(total, i, true));
            }
        }

        if (!progressBar.isInterrupt()) {
            progressBar.successful();
        }
    }


}
