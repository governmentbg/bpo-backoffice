package com.duosoft.ipas.controller.ipobjects.patentlike.utilitymodel;


import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.controller.ipobjects.common.search.SearchBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/utility_model/search")
public class UtilityModelSearchController extends SearchBaseController {

    public UtilityModelSearchController(final YAMLConfig properties) {
        super(properties);
    }

    @Override
    protected YAMLConfig.Search.SearchBase getSearchProperties() {
        return properties.getSearch().getUtilityModel();
    }
}