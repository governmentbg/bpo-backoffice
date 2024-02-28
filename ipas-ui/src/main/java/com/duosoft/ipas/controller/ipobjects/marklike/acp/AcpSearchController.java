package com.duosoft.ipas.controller.ipobjects.marklike.acp;

import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.controller.ipobjects.common.search.SearchBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/acp/search")
public class AcpSearchController extends SearchBaseController {
    public AcpSearchController(final YAMLConfig properties) {
        super(properties);
    }

    @Override
    protected YAMLConfig.Search.SearchBase getSearchProperties() {
        return properties.getSearch().getAcp();
    }
}
