package com.duosoft.ipas.controller.ipobjects.public_register;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.file.FileRelationshipsService;
import bg.duosoft.ipas.util.file.FilePublicUrlUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/public-register")
public class PublicRegisterRedirectController {

    @Autowired
    private FileRelationshipsService fileRelationshipsService;

    @Value("${ipas.properties.publicRegisterURL}")
    private String publicRegisterUrl;

    @RequestMapping(path = "/open-public-register")
    @ResponseBody
    public String selectPublicRegisterUrl(@RequestParam String filingNumber) {
        CFileId fileId = BasicUtils.createCFileId(filingNumber);
        return FilePublicUrlUtils.selectPublicUrl(publicRegisterUrl, fileId, fileRelationshipsService);
    }
}
