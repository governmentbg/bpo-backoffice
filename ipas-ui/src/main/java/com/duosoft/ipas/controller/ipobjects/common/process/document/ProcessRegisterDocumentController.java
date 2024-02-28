package com.duosoft.ipas.controller.ipobjects.common.process.document;

import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.model.reception.CUserdocReceptionRelation;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.reception.UserdocReceptionRelationService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.reception.ReceptionValidator;
import bg.duosoft.ipas.enums.SubmissionType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.file.FileTypeUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.enums.GeneralPanel;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.json.ProcessRegisterDocument;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/process/document/register")
public class ProcessRegisterDocumentController {
    private final MarkService markService;
    private final MessageSource messageSource;
    private final PatentService patentService;
    private final ProcessService processService;
    private final AbdocsService abdocsServiceAdmin;
    private final ReceptionService receptionService;
    private final ReceptionValidator receptionValidator;
    private final UserdocReceptionRelationService userdocReceptionRelationService;

    @PostMapping("/open-modal")
    public String openModal(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        CFileId fileId = selectFileIdFromProcess(sessionIdentifier, request);
        List<CUserdocReceptionRelation> userdocReceptionRelations = userdocReceptionRelationService.selectUserdocsByMainType(fileId.getFileType(), true);
        model.addAttribute("userdocReceptionRelations", userdocReceptionRelations);
        return "ipobjects/common/process/document/register_document_modal :: modal";
    }


    @PostMapping("/validate")
    public String validate(HttpServletRequest request, Model model, @RequestParam String data, @RequestParam String sessionIdentifier) {
        ProcessRegisterDocument registerDocumentForm = JsonUtil.readJson(data, ProcessRegisterDocument.class);

        CFileId fileId = selectFileIdFromProcess(sessionIdentifier, request);
        CReception reception = buildReceptionObject(registerDocumentForm, fileId);

        List<ValidationError> errors = new ArrayList<>();

        String registrationNumber = registerDocumentForm.getRegistrationNumber();
        if (StringUtils.isEmpty(registrationNumber)) {
            errors.add(ValidationError.builder().pointer("registerUserdoc.registrationNumber").messageCode("required.field").build());
        } else {
            Integer existingAbdocsDocument = abdocsServiceAdmin.selectDocumentIdByRegistrationNumber(registrationNumber);
            if (Objects.nonNull(existingAbdocsDocument)) {
                errors.add(ValidationError.builder().pointer("registerUserdoc.registrationNumber").messageCode("registerUserdoc.registrationNumber.abdocsDucumentExists").build());
            }
        }
        if (Objects.isNull(registerDocumentForm.getFilingDate())) {
            errors.add(ValidationError.builder().pointer("registerUserdoc.filingDate").messageCode("required.field").build());
        }
        if (Objects.isNull(registerDocumentForm.getUserdocType())) {
            errors.add(ValidationError.builder().pointer("registerUserdoc.userdocType").messageCode("required.field").build());
        }
        if (CollectionUtils.isEmpty(errors)) {
            List<ValidationError> receptionErrors = receptionValidator.validate(reception);
            if (!CollectionUtils.isEmpty(receptionErrors)) {
                receptionErrors.forEach(validationError -> validationError.setPointer("registerUserdoc.errors"));
                errors.addAll(receptionErrors);
            }
        }

        List<CUserdocReceptionRelation> userdocReceptionRelations = userdocReceptionRelationService.selectUserdocsByMainType(fileId.getFileType(), true);
        model.addAttribute("userdocReceptionRelations", userdocReceptionRelations);
        model.addAttribute("validationErrors", errors);
        model.addAttribute("registerDocumentForm", registerDocumentForm);
        return "ipobjects/common/process/document/register_document_modal :: modal";
    }

    @PostMapping("/submit")
    public String register(HttpServletRequest request,
                           RedirectAttributes redirectAttributes,
                           @RequestParam String data,
                           @RequestParam String sessionIdentifier) {
        String sessionObjectIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        ProcessRegisterDocument registerDocumentForm = JsonUtil.readJson(data, ProcessRegisterDocument.class);
        CFileId fileId = selectFileIdFromProcess(sessionIdentifier, request);
        CReception cReception = buildReceptionObject(registerDocumentForm, fileId);
        try {
            CReceptionResponse response = receptionService.createReception(cReception);
            CDocumentId docId = response.getDocId();
            if (Objects.nonNull(docId) && Objects.nonNull(docId.getDocNbr())) {
                try {
                    processService.updateUserdocProcessCreationDate(cReception.getEntryDate(), docId.getDocOrigin(), docId.getDocLog(), docId.getDocSeries(), docId.getDocNbr());
                } catch (Exception e) {
                    log.error("Process creation date is not update for document {}", docId);
                    log.error(e.getMessage(), e);
                }
            }
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("process.document.register.success", null, LocaleContextHolder.getLocale()));
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, GeneralPanel.Process.code());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("process.document.register.error", null, LocaleContextHolder.getLocale()));
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, null);
        }
    }

    private CReception buildReceptionObject(ProcessRegisterDocument registerDocumentForm, CFileId fileId) {
        CReception reception = new CReception();
        reception.setRegisterReceptionRequest(false);
        reception.setRegisterInDocflowSystem(true);
        reception.setSubmissionType(SubmissionType.COUNTER.code());
        reception.setOriginalExpected(false);
        reception.setRegisterAsAdministrator(true);
        reception.setEntryDate(registerDocumentForm.getFilingDate());
        reception.setUserdoc(new CReceptionUserdoc());
        reception.getUserdoc().setFileId(fileId);
        reception.getUserdoc().setUserdocType(registerDocumentForm.getUserdocType());
        reception.getUserdoc().setExternalRegistrationNumber(registerDocumentForm.getRegistrationNumber());

        String ipObjectIndication = FileTypeUtils.selectIpObjectTypeByFileType(fileId.getFileType());
        switch (ipObjectIndication) {
            case DefaultValue.PATENT_OBJECT_INDICATION:
                CPatent patent = patentService.findPatent(fileId, false);
                reception.setOwnershipData(patent.getFile().getOwnershipData());
                break;
            case DefaultValue.MARK_OBJECT_INDICATION:
                CMark mark = markService.findMark(fileId, false);
                reception.setOwnershipData(mark.getFile().getOwnershipData());
                break;
            default:
                throw new RuntimeException("Invalid object indication !");
        }
        return reception;
    }

    private CFileId selectFileIdFromProcess(String sessionIdentifier, HttpServletRequest request) {
        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        String filingNumber = HttpSessionUtils.getFilingNumberFromSessionObject(fullSessionIdentifier);

        CFileId fileId = BasicUtils.createCFileId(filingNumber);
        if (Objects.isNull(fileId)) {
            throw new RuntimeException("Cannot create file id from filingnumber " + filingNumber);
        }

        if (!(FileTypeUtils.isMarkFileType(fileId.getFileType()) || FileTypeUtils.isPatentFileType(fileId.getFileType()))) {
            throw new RuntimeException("Wrong file id !" + fileId);
        }

        return BasicUtils.createCFileId(filingNumber);
    }

}
