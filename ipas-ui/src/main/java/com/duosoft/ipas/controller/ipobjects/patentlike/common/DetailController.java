package com.duosoft.ipas.controller.ipobjects.patentlike.common;

import bg.duosoft.ipas.core.model.patent.CAttachmentType;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentAttachment;
import bg.duosoft.ipas.core.model.patent.CPatentDetails;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.nomenclature.AttachmentTypeService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.patent.attachments.CPatentDetailsValidator;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.patent.PatentUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.json.PatentDetailsData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentInitPanelSessionObjectsUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/patent-like/detail")
public class DetailController {

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private AttachmentTypeService attachmentTypeService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private UserService userService;


    @PostMapping("/init-panel-main-data")
    @ResponseStatus(value = HttpStatus.OK)
    public void initMainDataPanel(HttpServletRequest request, @RequestParam String sessionIdentifier) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        PatentInitPanelSessionObjectsUtils.initMainDataPanelSessionObjects(request, sessionIdentifier, patent);
    }
    
    @PostMapping("/edit-panel-detail")
    public String editDetailPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {

        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        boolean hasSecretDataRights = SecurityUtils.hasSecretDataRights(patent.getFile().getProcessSimpleData().getResponsibleUser(),patent.getFile().getFileId(),actionService,userService);
        if (!isCancel) {
            PatentDetailsData patentDetailsData = JsonUtil.readJson(data, PatentDetailsData.class);
            List<CPatentAttachment> patentAttachments = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTACHMENTS, sessionIdentifier, request);
            fillPatentDetailData(patentDetailsData,patentAttachments, patent,hasSecretDataRights);
        }
        model.addAttribute("panelPrefix", CoreUtils.getUrlPrefixByFileType(patent.getFile().getFileId().getFileType()));
        model.addAttribute("patent", patent);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("attachmentTypes",attachmentTypeService.findAllByTypeAndOrder(patent.getFile().getFileId().getFileType()));
        model.addAttribute("hasSecretDataRights", hasSecretDataRights);
        PatentSessionUtils.removePatentDetailPanelSessionObjects(request, sessionIdentifier);
        return "ipobjects/patentlike/common/details/patent_details_panel :: patent-details";
    }


    @RequestMapping(value = "/validate-patent-attachment")
    public String validateUploadedAttachment(Model model, HttpServletRequest request, @RequestParam(value = "uploadAttachment") MultipartFile uploadFile,
                                             @RequestParam String sessionIdentifier,
                                             @RequestParam Integer attachmentType) {
        CPatent sessionPatent = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT, sessionIdentifier, request);
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, PatentSessionObject.SESSION_PATENT_NEW_ATTACHMENT);
        List<CPatentAttachment> patentAttachments = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTACHMENTS, sessionIdentifier, request);
        CPatentDetails cPatentDetails = new CPatentDetails();
        cPatentDetails.setPatentAttachments(new ArrayList<>());
        List<ValidationError> errors;

        setUploadedPatentAttachment(sessionPatent,patentAttachments,cPatentDetails, attachmentType, uploadFile);
        IpasValidator<CPatentDetails> validator = validatorCreator.create(true, CPatentDetailsValidator.class);
        errors = validator.validate(cPatentDetails);

        if (CollectionUtils.isEmpty(errors)) {
            HttpSessionUtils.setSessionAttribute(PatentSessionObject.SESSION_PATENT_NEW_ATTACHMENT, sessionIdentifier, cPatentDetails.getPatentAttachments().get(0), request);
        }
        model.addAttribute("errors", errors);
        model.addAttribute("id", "attachmentData");
        return "base/validation :: validation-message";

    }

    private void setUploadedPatentAttachment(CPatent sessionPatent,List<CPatentAttachment> sessionPatentAttachment,CPatentDetails cPatentDetails, Integer attachmentType, MultipartFile uploadFile) {
        try {
            CPatentAttachment newPatentAttachment = new CPatentAttachment();
            CAttachmentType attachmentTypeObject=  attachmentTypeService.findById(attachmentType);
            newPatentAttachment.setAttachmentType(attachmentTypeObject);
            newPatentAttachment.setContentLoaded(true);
            newPatentAttachment.setDateCreated(DateUtils.convertToDate(LocalDate.now()));
            newPatentAttachment.setAttachmentContent(uploadFile.getBytes());
            if ( !CollectionUtils.isEmpty(sessionPatentAttachment)){
                newPatentAttachment.setId(PatentUtils.generateAttachmentIdOnAdd(sessionPatentAttachment,attachmentType));
            }else{
                newPatentAttachment.setId(DefaultValue.INCREMENT_VALUE);
            }
            if (Objects.nonNull(attachmentTypeObject.getAttachmentNameSuffix())){
                String attachmentNameNumber=null;
                if (Objects.nonNull(sessionPatent.getFile().getRegistrationData()) && Objects.nonNull(sessionPatent.getFile().getRegistrationData().getRegistrationId())
                        && Objects.nonNull(sessionPatent.getFile().getRegistrationData().getRegistrationId().getRegistrationNbr())){
                    attachmentNameNumber=sessionPatent.getFile().getRegistrationData().getRegistrationId().getRegistrationNbr().toString();
                }else{
                    attachmentNameNumber = sessionPatent.getFile().getFileId().getFileNbr().toString();
                }
                newPatentAttachment.setAttachmentName("BG".concat(attachmentNameNumber).concat
                        (attachmentTypeObject.getAttachmentNameSuffix().concat("_").concat(newPatentAttachment.getId().toString()).concat(".").concat(attachmentTypeObject.getAttachmentExtension())));
            }else{
                 newPatentAttachment.setAttachmentName(uploadFile.getOriginalFilename());
            }
            cPatentDetails.getPatentAttachments().add(newPatentAttachment);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void fillPatentDetailData(PatentDetailsData patentDetailsData,List<CPatentAttachment> patentAttachments, CPatent cPatent,boolean hasSecretDataRights) {
        if (Objects.isNull(cPatent.getPatentDetails())){
            cPatent.setPatentDetails(new CPatentDetails());
        }
        cPatent.getPatentDetails().setPatentAttachments(patentAttachments);
        cPatent.getTechnicalData().setTitle(patentDetailsData.getTitle());
        cPatent.getTechnicalData().setEnglishTitle(patentDetailsData.getEnglishTitle());
        if (hasSecretDataRights){
            cPatent.getTechnicalData().setMainAbstract(patentDetailsData.getMainAbstract());
            cPatent.getTechnicalData().setEnglishAbstract(patentDetailsData.getEnglishAbstract());
        }
        cPatent.getPatentDetails().setDrawings(patentDetailsData.getDrawingsCnt());
        cPatent.getPatentDetails().setClaims(patentDetailsData.getClaimsCnt());
        cPatent.getPatentDetails().setDescriptionPages(patentDetailsData.getDescriptionPagesCnt());
        cPatent.getPatentDetails().setDrawingPublications(patentDetailsData.getDrawingPubl());
        cPatent.getPatentDetails().setInventionsGroup(patentDetailsData.getInventionsGroupCnt());
    }


}
