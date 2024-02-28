package com.duosoft.ipas.controller.ipobjects.common.reception.create;

import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.util.TempID;
import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeGroupService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.reception.ReceptionTypeService;
import bg.duosoft.ipas.core.service.reception.SubmissionTypeService;
import bg.duosoft.ipas.enums.ApplicationSearchType;
import bg.duosoft.ipas.enums.EuPatentReceptionType;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.reception.ReceptionUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.reception.ReceptionSessionUtils;
import com.duosoft.ipas.webmodel.ReceptionForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/reception/create/eupatent")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
public class EbdRecetpionController {

    @Autowired
    private EbdPatentService ebdPatentService;

    @Autowired
    private ReceptionTypeService receptionTypeService;

    @Autowired
    private SubmissionTypeService submissionTypeService;

    @Autowired
    private DailyLogService dailyLogService;

    @Autowired
    private PersonService personService;

    @Autowired
    private TempID tempID;
    @Autowired
    private FileTypeGroupService fileTypeGroupService;

    @PostMapping("/open-search-modal")
    public String openEuPatentSearchModal(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        return "ipobjects/common/reception/eupatent/eupatent_modal :: search";
    }

    @PostMapping("/search")
    public String searchEuPatent(Model model, HttpServletRequest request,
                                 @RequestParam String sessionIdentifier,
                                 @RequestParam String searchText,
                                 @RequestParam String searchType) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);

        String trimmedText = searchText.trim();
        boolean isSearchTextNumber = CoreUtils.isNumber(trimmedText);
        if (isSearchTextNumber) {
            Integer searchNumber = Integer.valueOf(trimmedText);
            ApplicationSearchType applicationSearchType = ApplicationSearchType.selectByCode(searchType);

            CEbdPatent cEbdPatent;
            switch (applicationSearchType) {
                case FILE_NUMBER:
                    cEbdPatent = ebdPatentService.selectByFileNumber(searchNumber);
                    break;
                case REGISTRATION_NUMBER:
                    cEbdPatent = ebdPatentService.selectByRegistationNumber(searchNumber);
                    break;
                default:
                    cEbdPatent = null;
            }

            if (Objects.nonNull(cEbdPatent))
                model.addAttribute("ebdPatent", cEbdPatent);
        }
        return "ipobjects/common/reception/eupatent/eupatent_modal :: result";
    }

    @PostMapping("/add")
    public String addEuPatent(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String filingNumber) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        ReceptionForm receptionForm = HttpSessionUtils.getSessionAttribute(sessionIdentifier, request);

        CEbdPatent cEbdPatent = ebdPatentService.selectByFileNumber(Integer.valueOf(filingNumber));
        receptionForm.getEuPatent().setObjectNumber(Integer.valueOf(filingNumber));
        receptionForm.setName(cEbdPatent.getTitle());

        receptionForm.setOwnershipData(new COwnershipData());// Removes previous applicants
        List<CPerson> owners = cEbdPatent.getOwners();
        owners.forEach(cPerson -> PersonUtils.saveApplicant(request, cPerson, sessionIdentifier, personService, tempID, true));

        ReceptionUtils.addReceptionPanelBasicModelAttributes(model, receptionForm, sessionIdentifier, submissionTypeService, receptionTypeService, dailyLogService, fileTypeGroupService);
        return "ipobjects/common/reception/reception_panel :: panel";
    }

    @PostMapping("/delete")
    public String deleteEuPatent(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        ReceptionForm receptionForm = HttpSessionUtils.getSessionAttribute(sessionIdentifier, request);
        receptionForm.getEuPatent().setObjectNumber(null);
        model.addAttribute("receptionForm", receptionForm);
        return "ipobjects/common/reception/eupatent/eupatent :: object";
    }

    @ResponseBody
    @PostMapping("/change-type")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSessionEpoPatentType(HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String euPatentType) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        ReceptionForm receptionForm = HttpSessionUtils.getSessionAttribute(sessionIdentifier, request);
        EuPatentReceptionType euPatentReceptionType = EuPatentReceptionType.selectByCode(euPatentType);
        receptionForm.getEuPatent().setType(euPatentReceptionType.code());
    }

}
