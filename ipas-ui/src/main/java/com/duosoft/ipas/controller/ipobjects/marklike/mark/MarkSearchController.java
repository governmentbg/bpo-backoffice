package com.duosoft.ipas.controller.ipobjects.marklike.mark;


import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.controller.ipobjects.common.search.SearchBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mark/search")
public class MarkSearchController extends SearchBaseController {

    public MarkSearchController(final YAMLConfig properties) {
        super(properties);
    }

    @Override
    protected YAMLConfig.Search.SearchBase getSearchProperties() {
        return properties.getSearch().getMark();
    }

}