package com.duosoft.ipas.controller.ipobjects.patentlike.eupatent;


import bg.duosoft.ipas.core.model.patent.CPatent;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.controller.ipobjects.common.search.SearchBaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/eupatent/search")
public class EuPatentSearchController extends SearchBaseController<CPatent> {

    public EuPatentSearchController(final YAMLConfig properties) {
        super(properties);
    }

    @Override
    protected YAMLConfig.Search.SearchBase getSearchProperties() {
        return properties.getSearch().getEupatent();
    }

    @Override
    protected String getRequestForValidationType(){
        return "EV";
    }
}