package com.duosoft.ipas.controller.ipobjects.common.reception;

import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.util.general.BasicUtils;
import com.duosoft.ipas.util.RedirectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reception/detail")
public class ReceptionDetailsController {
    public static final String RECEPTION_PARAMETER = "?reception=true";

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @GetMapping(value = "/{fileSeq}/{fileType}/{fileSer}/{fileNbr}")
    public String redirectReceptionToDetailPage(@PathVariable("fileSeq") String fileSeq,
                                                @PathVariable("fileType") String fileType,
                                                @PathVariable("fileSer") Integer fileSer,
                                                @PathVariable("fileNbr") Integer fileNbr) {

        CReceptionRequest cReceptionRequest = receptionRequestService.selectReceptionByFileId(fileSeq, fileType, fileSer, fileNbr);
        String filingNumber = BasicUtils.createFilingNumber(fileSeq, fileType, fileSer, fileNbr);
        return RedirectUtils.redirectToObjectViewPage(filingNumber, cReceptionRequest.getStatus() !=null?false:true);
    }

}
