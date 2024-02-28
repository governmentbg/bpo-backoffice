package com.duosoft.ipas.controller.ipobjects.patentlike.plant;


import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentAttachment;
import bg.duosoft.ipas.core.model.patent.CPatentDetails;
import bg.duosoft.ipas.core.model.plant.CPlant;
import bg.duosoft.ipas.core.model.plant.CPlantTaxonNomenclature;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.nomenclature.AttachmentTypeService;
import bg.duosoft.ipas.core.service.nomenclature.PlantTaxonNomenclatureService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.json.PlantDetailsData;
import com.duosoft.ipas.util.json.PlantTaxonNomenclatureData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/plants_and_breeds/detail")
public class PlantDetailController {

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private PlantTaxonNomenclatureService plantTaxonNomenclatureService;

    @Autowired
    private AttachmentTypeService attachmentTypeService;
    @Autowired
    private UserService userService;

    @Autowired
    private ActionService actionService;

    @PostMapping("/edit-panel-detail")
    public String editDetailPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {

        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        boolean hasSecretDataRights = SecurityUtils.hasSecretDataRights(patent.getFile().getProcessSimpleData().getResponsibleUser(), patent.getFile().getFileId(), actionService, userService);

        if (!isCancel) {
            PlantDetailsData plantDetailsData = JsonUtil.readJson(data, PlantDetailsData.class);
            List<CPatentAttachment> plantAttachments = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTACHMENTS, sessionIdentifier, request);
            fillPlantDetailData(patent, plantDetailsData,plantAttachments);
        }

        model.addAttribute("panelPrefix", CoreUtils.getUrlPrefixByFileType(patent.getFile().getFileId().getFileType()));
        model.addAttribute("patent", patent);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("hasSecretDataRights", hasSecretDataRights);
        model.addAttribute("attachmentTypes",attachmentTypeService.findAllByTypeAndOrder(patent.getFile().getFileId().getFileType()));
        PatentSessionUtils.removePatentDetailPanelSessionObjects(request, sessionIdentifier);
        return "ipobjects/patentlike/plant/plant_details_panel :: plant-details";
    }


    @GetMapping(value = "/autocomplete", produces = "application/json")
    @ResponseBody
    public List<PlantTaxonNomenclatureData> autocompleteTaxonNomenclatureData(@RequestParam String taxonNomData) {
        List<PlantTaxonNomenclatureData> autocompleteList = new ArrayList<>();

        List<CPlantTaxonNomenclature> cPlantTaxonNomenclatures = plantTaxonNomenclatureService.findCPlantTaxonNomenclatureByCommonClassifyBulAndCommonClassifyEngAndLatinClassify(taxonNomData, yamlConfig.getMaxAutoCompletResults());
        cPlantTaxonNomenclatures.stream().forEach(f -> autocompleteList.add(new PlantTaxonNomenclatureData(f)));

        return autocompleteList;
    }


    private void fillPlantDetailData(CPatent patent, PlantDetailsData plantDetailsData, List<CPatentAttachment> plantAttachments) {
        patent.getTechnicalData().setTitle(plantDetailsData.getTitle());
        patent.getTechnicalData().setEnglishTitle(plantDetailsData.getEnglishTitle());
        if (Objects.isNull(patent.getPlantData())) {
            patent.setPlantData(new CPlant());
        }
        patent.getPlantData().setProposedDenomination(plantDetailsData.getProposedDenomination());
        patent.getPlantData().setProposedDenominationEng(plantDetailsData.getProposedDenominationEng());
        patent.getPlantData().setApprDenomination(plantDetailsData.getApprDenomination());
        patent.getPlantData().setApprDenominationEng(plantDetailsData.getApprDenominationEng());
        patent.getPlantData().setPublDenomination(plantDetailsData.getPublDenomination());
        patent.getPlantData().setPublDenominationEng(plantDetailsData.getPublDenominationEng());
        patent.getPlantData().setRejDenomination(plantDetailsData.getRejDenomination());
        patent.getPlantData().setRejDenominationEng(plantDetailsData.getRejDenominationEng());
        patent.getTechnicalData().setMainAbstract(plantDetailsData.getMainAbstract());
        patent.getPlantData().setStability(plantDetailsData.getStability());
        patent.getPlantData().setFeatures(plantDetailsData.getFeatures());
        patent.getPlantData().setTesting(plantDetailsData.getTesting());
        if (plantDetailsData.getId() != null) {
            patent.getPlantData().setPlantNumenclature(plantTaxonNomenclatureService.findById(plantDetailsData.getId()));
        } else {
            patent.getPlantData().setPlantNumenclature(null);
        }
        if (Objects.isNull(patent.getPatentDetails())){
            patent.setPatentDetails(new CPatentDetails());
        }
        patent.getPatentDetails().setPatentAttachments(plantAttachments);
    }
}



