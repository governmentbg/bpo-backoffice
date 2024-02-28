package com.duosoft.ipas.controller.ipobjects.patentlike.spc;

import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.controller.ipobjects.common.search.SearchBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/spc/search")
public class SpcSearchController extends SearchBaseController {

    public SpcSearchController(final YAMLConfig properties) {
        super(properties);
    }

    @Override
    protected YAMLConfig.Search.SearchBase getSearchProperties() {
        return properties.getSearch().getSpc();
    }
}