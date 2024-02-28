package com.duosoft.ipas.controller.ipobjects.common;

import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.document.CDocument;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.ext.OffidocAbdocsDocumentService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.reception.AbdocsDocumentTypeService;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.enums.IpasObjectType;
import bg.duosoft.ipas.util.file.FileTypeUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.offidoc.OffidocUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.util.ProcessUtils;
import com.duosoft.ipas.util.RedirectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/ipobject/view")
public class IpObjectViewController {

    @Autowired
    private MarkService markService;

    @Autowired
    private PatentService patentService;

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @Autowired
    private DocService docService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private AbdocsDocumentTypeService abdocsDocumentTypeService;

    @Autowired
    private OffidocAbdocsDocumentService offidocAbdocsDocumentService;

    @GetMapping
    public String view(@RequestParam(required = false) Integer abdocsId,
                       @RequestParam(required = false) String filingNumber,
                       @RequestParam(required = false) String msprocess,
                       @RequestParam(required = false) String offidocFilingNumber,
                       @RequestParam(required = false) String userdocFilingNumber) {

        try {
            if (Objects.nonNull(abdocsId)) {
                return openByAbdocsId(abdocsId);
            } else if (!StringUtils.isEmpty(filingNumber)) {
                return openByFilingNumber(filingNumber);
            } else if (!StringUtils.isEmpty(offidocFilingNumber)) {
                return openByOffidocFilingNumber(offidocFilingNumber);
            } else if (!StringUtils.isEmpty(userdocFilingNumber)) {
                return openByUserdocFilingNumber(userdocFilingNumber);
            } else if (!StringUtils.isEmpty(msprocess)) {
                return openByManualSubProcessIdentifierr(msprocess);
            } else {
                throw new RuntimeException("Missing query params !");
            }
        } catch (Exception e) {
            log.warn("Error occurred during view page loading... " + e.getMessage(),e);
            return "error/error";
        }
    }

    private String openByFilingNumber(String filingNumber) {
        if (OffidocUtils.isOffidoc(filingNumber)) {
            return RedirectUtils.redirectToOffidocViewPage(filingNumber);
        } else if (UserdocUtils.isUserdoc(filingNumber)) {
            return openByUserdocFilingNumber(filingNumber);
        } else {
            String type = BasicUtils.selectFileTypeOfFilingNumber(filingNumber);
            if (FileTypeUtils.isMarkFileType(type)) {
                return selectMarkLikeUrl(BasicUtils.createCFileId(filingNumber));
            } else if (FileTypeUtils.isPatentFileType(type)) {
                return selectPatentLikeUrl(BasicUtils.createCFileId(filingNumber));
            } else {
                throw new RuntimeException("Invalid object type: " + type);
            }
        }
    }

    private String selectMarkLikeUrl(CFileId id) {
        CMark mark = markService.findMark(id, false);
        return getMainObjectUrl(id, Objects.isNull(mark));
    }

    private String selectPatentLikeUrl(CFileId id) {
        CPatent patent = patentService.findPatent(id, false);
        return getMainObjectUrl(id, Objects.isNull(patent));
    }

    private String selectUserdocUrlByAbdocsRegistrationNumber(String externalSystemId) {
        CDocumentId id = docService.selectDocumentIdByExternalSystemId(externalSystemId);
        if (Objects.isNull(id))
            return null;

        return RedirectUtils.redirectToObjectViewPage(UserdocUtils.convertDocumentIdToString(id), false);
    }

    private String selectOffidocUrlByAbdocsDocument(Document document) {
        String registrationNumber = document.getRegUri();
        COffidocAbdocsDocument byRegistrationNumber = offidocAbdocsDocumentService.selectByRegistrationNumber(registrationNumber);
        if (Objects.isNull(byRegistrationNumber)) {
            Integer docId = document.getDocId();
            COffidocAbdocsDocument byAbdocsId = offidocAbdocsDocumentService.selectByAbdocsId(docId);
            if (Objects.isNull(byAbdocsId))
                return null;

            return openByOffidocFilingNumber(byAbdocsId.getOffidocId().createFilingNumber());
        }
        return openByOffidocFilingNumber(byRegistrationNumber.getOffidocId().createFilingNumber());
    }

    private String getMainObjectUrl(CFileId id, boolean isIpasObjectNull) {
        boolean isReception = false;
        if (isIpasObjectNull) {
            CReceptionRequest cReceptionRequest = receptionRequestService.selectReceptionByFileId(id.getFileSeq(), id.getFileType(), id.getFileSeries(), id.getFileNbr());
            if (Objects.nonNull(cReceptionRequest)) {
                Integer status = cReceptionRequest.getStatus();
                if (Objects.nonNull(status) && status == 1)
                    throw new RuntimeException("Object " + id.createFilingNumber() + " has status 'Accepted' in EXT_RECEPTION.RECEPTION_REQUEST but missing in IPAS !");

                isReception = true;
            } else
                throw new RuntimeException("Object " + id.createFilingNumber() + " does not exist !");

        }
        return RedirectUtils.redirectToObjectViewPage(id, isReception);
    }

    private String openByAbdocsId(Integer abdocsId) {
        Document document = abdocsServiceAdmin.selectDocumentById(abdocsId);
        if (Objects.isNull(document))
            throw new RuntimeException("Cannot find abdocs document with id = " + abdocsId);

        IpasObjectType ipasObjectType = abdocsDocumentTypeService.selectIpasObjectTypeByAbdocsDocumentId(document.getDocTypeId());
        if (Objects.isNull(ipasObjectType))
            throw new RuntimeException("Cannot find ipas object type in CF_ABODCS_DOCUMENT_TYPE table! Type:  " + document.getDocTypeId() + " Document id: " + abdocsId);

        String registrationNumber = document.getRegUri();
        switch (ipasObjectType) {
            case PATENT:
                return selectPatentLikeUrl(BasicUtils.createCFileId(registrationNumber));
            case MARK:
                return selectMarkLikeUrl(BasicUtils.createCFileId(registrationNumber));
            case USERDOC:
                return selectUserdocUrlByAbdocsRegistrationNumber(registrationNumber);
            case OFFIDOC:
                return selectOffidocUrlByAbdocsDocument(document);
            default:
                throw new RuntimeException("Unknown ipas object type !" + ipasObjectType);
        }
    }

    private String openByOffidocFilingNumber(String offidocFilingNumber) {
        return RedirectUtils.redirectToOffidocViewPage(offidocFilingNumber);
    }

    private String openByUserdocFilingNumber(String userdocFilingNumber) {
        CDocumentId cDocumentId = UserdocUtils.convertStringToDocumentId(userdocFilingNumber);
        CDocument cDocument = docService.selectDocument(cDocumentId);
        if (Objects.isNull(cDocument))
            return selectUserdocUrlByAbdocsRegistrationNumber(userdocFilingNumber);

        return RedirectUtils.redirectToObjectViewPage(userdocFilingNumber, false);
    }

    private String openByManualSubProcessIdentifierr(String processIdString) {
        CProcessId processIdFromString = ProcessUtils.getProcessIdFromString(processIdString);
        return RedirectUtils.redirectToManualSubProcessViewPage(processIdFromString);
    }

}
