package com.duosoft.ipas.controller.ipobjects.common.process.document;

import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.ext.OffidocAbdocsDocumentService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.util.offidoc.OffidocUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.util.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/process/document")
public class ProcessViewDocumentController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private DocService docService;

    @Autowired
    private OffidocAbdocsDocumentService offidocAbdocsDocumentService;

    @RequestMapping(value = "/open-office-document")
    @ResponseBody
    public String selectAbdocsOffidocUrl(@RequestParam String offidoc, @RequestParam String process) {
        CProcessId cProcessId = ProcessUtils.getProcessIdFromString(process);
        COffidocId offidocId = OffidocUtils.convertStringToOffidocId(offidoc);
        COffidocAbdocsDocument cOffidocAbdocsDocument = offidocAbdocsDocumentService.selectById(offidocId);
        if (Objects.nonNull(cOffidocAbdocsDocument)) {
            Integer abdocsDocId = cOffidocAbdocsDocument.getAbdocsDocumentId();
            if (Objects.isNull(abdocsDocId)) {
                String registrationNumber = cOffidocAbdocsDocument.getRegistrationNumber();
                abdocsDocId = abdocsService.selectDocumentIdByRegistrationNumber(registrationNumber);
            }
            if (Objects.nonNull(abdocsDocId)) {
                return abdocsService.generateViewDocumentUrl(abdocsDocId);
            } else
                return getOriginalFileDocumentUrl(cProcessId);
        } else
            return getOriginalFileDocumentUrl(cProcessId);
    }

    @RequestMapping(value = "/open-user-document")
    @ResponseBody
    public String selectAbdocsUserdocUrl(@RequestParam String process, @RequestParam String document) {
        CProcessId cProcessId = ProcessUtils.getProcessIdFromString(process);
        CDocumentId documentId = UserdocUtils.convertStringToDocumentId(document);
        if (Objects.isNull(document))
            throw new RuntimeException("Document id is empty");

        String externalSystemId = docService.selectExternalSystemId(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
        if (StringUtils.isEmpty(externalSystemId)) {
            return getOriginalFileDocumentUrl(cProcessId);
        } else {
            Integer abdocsDocId = abdocsService.selectDocumentIdByRegistrationNumber(externalSystemId);
            if (Objects.isNull(abdocsDocId))
                return getOriginalFileDocumentUrl(cProcessId);

            return abdocsService.generateViewDocumentUrl(abdocsDocId);
        }

    }

    @RequestMapping(value = "/open-object-document")
    @ResponseBody
    public String selectAbdocsObjectUrl(@RequestParam String filingNumber) {
        Integer fileDocumentId = abdocsService.selectDocumentIdByRegistrationNumber(filingNumber);
        return abdocsService.generateViewDocumentUrl(fileDocumentId);
    }

    private String getOriginalFileDocumentUrl(CProcessId cProcessId) {
        CFileId cFileId = processService.selectTopProcessFileId(cProcessId);
        Integer fileDocumentId = abdocsService.selectDocumentIdByRegistrationNumber(cFileId.createFilingNumber());
        return abdocsService.generateViewDocumentUrl(fileDocumentId);
    }

}
