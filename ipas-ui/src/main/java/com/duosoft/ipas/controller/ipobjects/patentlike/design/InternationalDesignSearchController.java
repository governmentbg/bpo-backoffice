package com.duosoft.ipas.controller.ipobjects.patentlike.design;


import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.controller.ipobjects.common.search.SearchBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/international-design/search")
public class InternationalDesignSearchController extends SearchBaseController {

    public InternationalDesignSearchController(final YAMLConfig properties) {
        super(properties);
    }

    @Override
    protected YAMLConfig.Search.SearchBase getSearchProperties() {
        return properties.getSearch().getInternationalDesign();
    }
}