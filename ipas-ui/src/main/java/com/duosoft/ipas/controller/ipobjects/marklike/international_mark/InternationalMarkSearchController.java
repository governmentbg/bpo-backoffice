package com.duosoft.ipas.controller.ipobjects.marklike.international_mark;


import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.controller.ipobjects.common.search.SearchBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("international_mark/search")
public class InternationalMarkSearchController extends SearchBaseController {

    public InternationalMarkSearchController(final YAMLConfig properties) {
        super(properties);
    }

    @Override
    protected YAMLConfig.Search.SearchBase getSearchProperties() {
        return properties.getSearch().getInternationalMark();
    }

}