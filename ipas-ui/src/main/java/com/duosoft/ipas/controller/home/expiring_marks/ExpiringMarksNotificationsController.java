package com.duosoft.ipas.controller.home.expiring_marks;

import bg.duosoft.abdocs.model.Attachments;
import bg.duosoft.abdocs.model.DocCreation;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.service.mark.ExpiringMarksService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.reception.MissingAbdocsDocumentService;
import bg.duosoft.ipas.core.service.report.ReportService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.integration.abdocs.converter.DocCreationConverter;
import bg.duosoft.ipas.persistence.model.nonentity.ExpiringMarkResult;
import bg.duosoft.ipas.services.core.IpasReportService;
import bg.duosoft.ipas.services.core.IpasServiceException;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import com.duosoft.ipas.util.ProgressBarUtils;
import com.duosoft.ipas.webmodel.ExpiringMarkNotificationDocument;
import com.duosoft.ipas.webmodel.ExpiringMarkNotificationProgressBar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/expiring-marks/notifications")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ExpiringMarksNotifications.code())")
public class ExpiringMarksNotificationsController {

    private final ReportService reportService;
    private final AbdocsService abdocsService;
    private final ConfigParamService configParamService;
    private final ExpiringMarksService expiringMarksService;
    private final DocCreationConverter docCreationConverter;
    private final MissingAbdocsDocumentService missingAbdocsDocumentService;

    private static final String EXPIRING_MARKS_NOTIFICATION_ABDOCS_DOCUMENT_SUBJECT = "Уведомление за изтичащ срок на действие на марка {0}";
    private static final ExpiringMarkNotificationProgressBar progressBar = new ExpiringMarkNotificationProgressBar();

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        CustomDateEditor customDateEditor = new CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DOT), true, 10);
        binder.registerCustomEditor(Date.class, customDateEditor);
    }

    @GetMapping
    public String generateNotifications(Model model) {
        model.addAttribute("progressBar", progressBar);
        return "home/expiring_marks/expiring_marks";
    }

    @PostMapping("search")
    public String searchMarks(Model model, @RequestParam(required = false) Date expirationDateFrom, @RequestParam(required = false) Date expirationDateTo) {
        List<ValidationError> validationErrors = validateDates(expirationDateFrom, expirationDateTo);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            model.addAttribute("validationErrors", validationErrors);
            model.addAttribute("expirationDateFrom", expirationDateFrom);
            model.addAttribute("expirationDateTo", expirationDateTo);
            return "home/expiring_marks/expiring_marks_filters :: expiring-marks-search-filters";
        } else {
            List<ExpiringMarkResult> expiringMarks = expiringMarksService.selectExpiringMarks(expirationDateFrom, expirationDateTo);
            model.addAttribute("expiringMarks", expiringMarks);
            return "home/expiring_marks/expiring_marks_table :: table";
        }
    }

    @PostMapping("/start-progressbar")
    public String startProgressBar(RedirectAttributes redirectAttributes, @RequestParam List<String> marks) {
        if (!progressBar.isInProgress()) {
            progressBar.start(marks);
            Collections.sort(progressBar.getMarksForGeneration());
            redirectAttributes.addFlashAttribute("executeProcess", true);
        }
        return "redirect:/expiring-marks/notifications";
    }

    @PostMapping("/stop-progressbar")
    public String stop() {
        progressBar.stop("Interrupted by user !");
        return "redirect:/expiring-marks/notifications";
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

    @PostMapping("/select-progressbar-info")
    @ResponseBody
    public ExpiringMarkNotificationProgressBar selectProgressBarInfo() {
        return progressBar;
    }

    @PostMapping("/update-generation-result")
    public String updateGenerationResult(Model model) {
        model.addAttribute("progressBar", progressBar);
        return "home/expiring_marks/generation_result :: result";
    }

    public void executeProcess() {
        List<String> marksForGeneration = progressBar.getMarksForGeneration();
        if (CollectionUtils.isEmpty(marksForGeneration)) {
            progressBar.stop("Empty mark list !");
        } else {
            int total = marksForGeneration.size();
            for (int i = 0; i < total; i++) {
                if (!progressBar.isInProgress()) {
                    return;
                }

                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                String mark = marksForGeneration.get(i);

                ExpiringMarkNotificationDocument result = new ExpiringMarkNotificationDocument();
                result.setFilingNumber(mark);
                try {
                    boolean alreadyExist = false;
                    missingAbdocsDocumentService.insertMissingDocument(mark);
                    DocCreation docCreation = docCreationConverter.convertExpiringMarkDocument(mark, EXPIRING_MARKS_NOTIFICATION_ABDOCS_DOCUMENT_SUBJECT.replace("{0}", mark), Collections.singletonList(generateAttachment(mark)));
                    Integer parentDocId = docCreation.getParentDocId();
                    if (Objects.nonNull(parentDocId)) {
                        List<Document> documents = abdocsService.selectDocumentHierarchy(parentDocId);
                        if (!CollectionUtils.isEmpty(documents)) {
                            Document existingDocument = documents.stream()
                                    .filter(document -> Objects.equals(document.getParentId(), parentDocId))
                                    .filter(document -> Objects.equals(document.getDocTypeId(), docCreation.getDocTypeId()))
                                    .filter(document -> Objects.nonNull(document.getRegDate()))
                                    .filter(document -> DateUtils.isDateEqualsToCurrentDate(document.getRegDate()))
                                    .findFirst()
                                    .orElse(null);

                            if (Objects.nonNull(existingDocument)) {
                                result.setStatus(ExpiringMarkNotificationDocument.Status.ALREADY_GENERATED);
                                result.setDocumentId(existingDocument.getDocId());
                                alreadyExist = true;
                            }
                        }
                    }
                    if (!alreadyExist) {
                        Document document = abdocsService.registerDocument(docCreation, null);
                        result.setDocumentId(document.getDocId());
                        result.setStatus(ExpiringMarkNotificationDocument.Status.SUCCESS);
                    }
                } catch (Exception e) {
                    log.error("Exception during generation of expiring mark notification! Mark: " + mark);
                    log.error(e.getMessage(), e);
                    result.setErrorMessage(e.getMessage());
                    result.setStatus(ExpiringMarkNotificationDocument.Status.ERROR);
                }
                progressBar.getExpiringMarkNotificationDocuments().add(result);
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

    private Attachments generateAttachment(String mark) throws IpasServiceException {
        CConfigParam templatePath = configParamService.selectConfigByCode(ConfigParamService.OFFICE_DOCUMENT_TEMPLATES_DIRECTORY);
        if (Objects.isNull(templatePath))
            throw new RuntimeException("Cannot find template path config !");

        CConfigParam templateName = configParamService.selectExtConfigByCode(ConfigParamService.EXPIRING_MARK_NOTIFICATION_TEMPLATE);
        if (Objects.isNull(templateName))
            throw new RuntimeException("Cannot find expiring marks template name config !");

        String template = templatePath.getValue() + File.separator + templateName.getValue();
        byte[] attachmentContent = reportService.generateDocument(template, Collections.singletonList(BasicUtils.createCFileId(mark)), null, null, null, IpasReportService.CONTENT_TYPE_DOCX);
        return new Attachments(attachmentContent, null, "ТМ Уедомление изтичане.docx");
    }

    private List<ValidationError> validateDates(Date expirationDateFrom, Date expirationDateTo) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.isNull(expirationDateFrom)) {
            errors.add(ValidationError.builder().pointer("marks.dateFrom.filter.input").messageCode("required.field").build());
        }
        if (Objects.isNull(expirationDateTo)) {
            errors.add(ValidationError.builder().pointer("marks.dateTo.filter.input").messageCode("required.field").build());
        }
        if (CollectionUtils.isEmpty(errors)) {
            LocalDate dateFromAsLocalDate = DateUtils.dateToLocalDate(expirationDateFrom);
            LocalDate dateToAsLocalDate = DateUtils.dateToLocalDate(expirationDateTo);
            long days = ChronoUnit.DAYS.between(dateFromAsLocalDate, dateToAsLocalDate);
            if (days > 40 || days < 0) {
                errors.add(ValidationError.builder().pointer("marks.dateFrom.filter.input").messageCode("expiring.marks.expiration.date.period").build());
            }
        }
        return errors;
    }

}
