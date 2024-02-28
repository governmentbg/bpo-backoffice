package com.duosoft.ipas.controller.ipobjects.userdoc;


import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPatentData;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.json.PatentDetailsData;
import com.duosoft.ipas.util.json.UserdocPatentData;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@RequestMapping("/userdoc-patent-data")
public class UserdocPatentDataController {


    @PostMapping("/edit")
    public String edit(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {

        CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        if (!isCancel) {
            UserdocPatentData userdocPatentData = JsonUtil.readJson(data, UserdocPatentData.class);
            if (Objects.isNull(userdoc.getPatentData())) {
                userdoc.setPatentData(new CUserdocPatentData());
            }
            userdoc.getPatentData().setClaimsCount(userdocPatentData.getClaimsCount());
            userdoc.getPatentData().setDescriptionPagesCount(userdocPatentData.getDescriptionPagesCount());
            userdoc.getPatentData().setDrawingsCount(userdocPatentData.getDrawingsCount());
            userdoc.getPatentData().setTitleBg(userdocPatentData.getTitleBg());
        }
        model.addAttribute("patentData", userdoc.getPatentData());
        return "ipobjects/userdoc/patent_data/patent_data :: panel";
    }

}
