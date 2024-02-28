package com.duosoft.ipas.controller.ipobjects.patentlike.plant;

import bg.duosoft.ipas.core.model.patent.CPatent;
import com.duosoft.ipas.controller.ipobjects.patentlike.common.PatentLikeRightsController;
import com.duosoft.ipas.util.json.PatentRightsData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/plants_and_breeds")
public class PlantsRightsController extends PatentLikeRightsController<PatentRightsData> {

    protected PlantsRightsController() {
        super(PatentRightsData.class);
    }

    @Override
    protected void processAdditionalTransformation(CPatent patent, PatentRightsData rights) {

    }

    @Override
    protected boolean processSpecificObjectChanges(CPatent patent, PatentRightsData rights) {
        return false;
    }

    @Override
    protected void initAdditionalData(CPatent patent, Model model) {

    }
}
