package com.duosoft.ipas.controller.ipobjects.ebd;

import bg.duosoft.ipas.core.model.ebddownload.CEbdPatentSearchResult;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * User: Georgi
 * Date: 21.2.2020 г.
 * Time: 14:47
 */
@Controller
public class EbdAutocompleteController {
    @Autowired
    private EbdPatentService ebdPatentService;
    @RequestMapping("/ebd-autocomplete")
    @ResponseBody
    public List<CEbdPatentSearchResult> autocompleteEbdPatent( @RequestParam(required = false) String filingNumber,
                                                               @RequestParam(required = false) String registrationNumber) {
        if (StringUtils.isEmpty(filingNumber) && StringUtils.isEmpty(registrationNumber)) {
            return null;
        }
        return ebdPatentService.searchEbdPatents(filingNumber, registrationNumber);
    }
}
