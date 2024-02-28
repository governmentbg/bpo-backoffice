package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.core.model.person.CReprsPowerOfAttorney;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.ReprsPowerOfAttorneyUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import com.duosoft.ipas.webmodel.structure.AttorneyDataWebModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/representative/power-of-attorney-data")
public class ReprsPowerOfAttorneyController extends BasePersonActionController {

    @PostMapping("/open-edit-modal")
    public String openEditModal(HttpServletRequest request, Model model,
                                @RequestParam String sessionIdentifier,
                                @RequestParam Integer personNbr,
                                @RequestParam Integer addressNbr,
                                @RequestParam Integer personKind) {

        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        CReprsPowerOfAttorney powerOfAttorneyData = null;

        switch (sessionObjectType) {
            case MARK:
            case PATENT:
                powerOfAttorneyData = ReprsPowerOfAttorneyUtils.constructObjectAttorneyData(request, sessionIdentifier, personNbr, addressNbr);
                break;
            case USERDOC:
                powerOfAttorneyData = ReprsPowerOfAttorneyUtils.constructUserdocAttorneyData(request, sessionIdentifier, personNbr, addressNbr, personKind);
                break;
        }

        model.addAttribute("attorneyInitialData", ReprsPowerOfAttorneyUtils.convertCoreToWebModel(powerOfAttorneyData));
        return "ipobjects/common/person/modal/agent_power_of_attorney_modal :: agent-power-of-attorney-form-modal";
    }


    @PostMapping("/open-attorney-info-modal")
    public String openInfoModal(HttpServletRequest request, Model model,
                                @RequestParam String powerOfAttorneyData) {
        CReprsPowerOfAttorney attorneyPowerData = JsonUtil.readJson(powerOfAttorneyData, CReprsPowerOfAttorney.class);
        model.addAttribute("reprPowerOfAttorneyData", attorneyPowerData);
        return "ipobjects/common/person/modal/agent_power_of_attorney_info_modal :: agent-power-of-attorney-info-modal";
    }


    @PostMapping("/initial-representative-data/edit")
    public String initialRepresentativeDataEdit(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,
                                                @RequestParam String powerOfAttorneyInitialData) {

        AttorneyDataWebModel attorneyInitialData = JsonUtil.readJson(powerOfAttorneyInitialData, AttorneyDataWebModel.class);
        List<ValidationError> errors = validateAttorneyInitData(attorneyInitialData);
        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("validationErrors", errors);
            model.addAttribute("attorneyInitialData", attorneyInitialData);
        } else {
            PersonSessionUtils.setSessionAttorneyData(request, ReprsPowerOfAttorneyUtils.convertWebModelToCore(attorneyInitialData), sessionIdentifier);
            model.addAttribute("representativeType", RepresentativeType.NATURAL_PERSON.name());//Default representativeType
        }
        model.addAttribute("personKind", attorneyInitialData.getPersonKind());
        model.addAttribute("onlyActive", attorneyInitialData.getOnlyActive());
        return "ipobjects/common/person/modal/agent_modal :: agent-form-modal";
    }

    @PostMapping("/edit")
    public String edit(HttpServletRequest request, Model model, @RequestParam(required = false) String sessionIdentifier,
                       @RequestParam String powerOfAttorneyInitialData) {
        AttorneyDataWebModel attorneyInitialData = JsonUtil.readJson(powerOfAttorneyInitialData, AttorneyDataWebModel.class);
        List<ValidationError> errors = validateAttorneyInitData(attorneyInitialData);
        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("validationErrors", errors);
            model.addAttribute("attorneyInitialData", attorneyInitialData);
            return "ipobjects/common/person/modal/agent_power_of_attorney_modal :: agent-power-of-attorney-form-modal";
        }

        CReprsPowerOfAttorney attorneyPowerData = ReprsPowerOfAttorneyUtils.convertWebModelToCore(attorneyInitialData);
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
            case PATENT:
                ReprsPowerOfAttorneyUtils.editObjectAttorneyData(request, sessionIdentifier, attorneyPowerData);
                break;
            case USERDOC:
                ReprsPowerOfAttorneyUtils.editUserdocAttorneyData(request, sessionIdentifier, attorneyPowerData);
                break;
        }
        return personListPage(request, model, sessionIdentifier, attorneyPowerData.getPersonKind());
    }

    private List<ValidationError> validateAttorneyInitData(AttorneyDataWebModel initialData) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.isNull(initialData.getAttorneyPowerTerm()) && !initialData.getAttorneyPowerTermIndefinite()) {
            errors.add(ValidationError.builder().pointer("attorney.data.errors").messageCode("attorney.power.term.select.option").build());
        }
        if (Objects.nonNull(initialData.getAttorneyPowerTerm()) && initialData.getAttorneyPowerTermIndefinite()) {
            errors.add(ValidationError.builder().pointer("attorney.data.errors").messageCode("attorney.power.term.select.option").build());
        }
        if (Objects.isNull(initialData.getReauthorizationRight())) {
            errors.add(ValidationError.builder().pointer("attorney.data.errors").messageCode("attorney.reauthorization.right.select.option").build());
        }
        return errors;
    }
}



