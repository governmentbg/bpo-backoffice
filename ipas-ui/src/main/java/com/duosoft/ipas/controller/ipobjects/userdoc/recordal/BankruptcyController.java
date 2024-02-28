package com.duosoft.ipas.controller.ipobjects.userdoc.recordal;

import bg.duosoft.ipas.core.model.miscellaneous.CCourt;
import bg.duosoft.ipas.core.service.nomenclature.CourtsService;
import com.duosoft.ipas.controller.ipobjects.userdoc.ExtraDataBaseController;
import com.duosoft.ipas.util.json.RelationshipData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/userdoc/bankruptcy")
public class BankruptcyController extends ExtraDataBaseController {

    @Autowired
    private CourtsService courtsService;

    @Override
    public String getPanelPage() {
        return "ipobjects/userdoc/recordal/bankruptcy/bankruptcy_panel :: bankruptcy";
    }

    @GetMapping(value = "/courts-autocomplete", produces = "application/json")
    @ResponseBody
    public List<CCourt> courtsAutocomplete(@RequestParam String name) {
        List<CCourt> result = courtsService.selectByNameLike(name);
        if (CollectionUtils.isEmpty(result))
            return null;
        result.sort(Comparator.comparing(CCourt::getName));
        return result;
    }


}
