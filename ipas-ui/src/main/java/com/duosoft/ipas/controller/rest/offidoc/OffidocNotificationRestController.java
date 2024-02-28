package com.duosoft.ipas.controller.rest.offidoc;

import bg.duosoft.abdocs.model.DocFile;
import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.ext.OffidocAbdocsDocumentService;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeService;
import bg.duosoft.ipas.core.service.offidoc.OffidocService;
import bg.duosoft.ipas.enums.OffidocParentObjectType;
import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.abdocs.document.RAbdocsDocFile;
import bg.duosoft.ipas.rest.custommodel.offidoc.ROffidocAbdocsDataResponse;
import bg.duosoft.ipas.rest.custommodel.offidoc.ROffidocUpdateNotificationReadDateRequest;
import bg.duosoft.ipas.util.general.BasicUtils;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.RestApiException;
import com.duosoft.ipas.controller.rest.mapper.RAbdocsDocFileMapper;
import com.duosoft.ipas.controller.rest.mapper.ROffidocAbdocsDocumentMapper;
import com.duosoft.ipas.controller.rest.mapper.ROffidocTypeMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"OFFICE DOCUMENTS"})
@RequestMapping(BASE_REST_URL + "/offidoc")
public class OffidocNotificationRestController extends BaseRestController {

    private final DocService docService;
    private final OffidocService offidocService;
    private final AbdocsService abdocsServiceAdmin;
    private final OffidocTypeService offidocTypeService;
    private final ROffidocTypeMapper rOffidocTypeMapper;
    private final RAbdocsDocFileMapper rAbdocsDocFileMapper;
    private final OffidocAbdocsDocumentService offidocAbdocsDocumentService;
    private final ROffidocAbdocsDocumentMapper rOffidocAbdocsDocumentMapper;

    @ApiOperation(value = "Select office document by hash key")
    @PostMapping(value = "/selectOffidocByHashKey", produces = "application/json")
    public ResponseEntity<RestApiResponse<ROffidocAbdocsDataResponse>> selectOffidocByHashKey(@RequestBody RestApiRequest<String> request) {
        String abdocsIdSha256Hex = request.getData();
        if (StringUtils.isEmpty(abdocsIdSha256Hex)) {
            return ResponseEntity.badRequest().body(null);
        }

        Document document = abdocsServiceAdmin.selectDocumentByHashNumber(abdocsIdSha256Hex);
        if (Objects.isNull(document)) {
            log.debug("Cannot find document with key " + abdocsIdSha256Hex);
            return ResponseEntity.ok(new RestApiResponse<>(null));
        }

        COffidocAbdocsDocument offidocAbdocsDocument = offidocAbdocsDocumentService.selectByAbdocsId(document.getDocId());
        if (Objects.isNull(offidocAbdocsDocument)) {
            return ResponseEntity.ok(new RestApiResponse<>(null));
        }

        String offidocType = offidocService.findOffidocType(offidocAbdocsDocument.getOffidocId());
        COffidocType cOffidocType = offidocTypeService.selectById(offidocType);
        if (Objects.isNull(offidocType)) {
            throw new RestApiException("OffidocType is empty ! IPAS ID  " + offidocAbdocsDocument.getOffidocId());
        }

        return ResponseEntity.ok(new RestApiResponse<>(ROffidocAbdocsDataResponse.builder()
                .offidocAbdocsDocument(rOffidocAbdocsDocumentMapper.toRest(offidocAbdocsDocument))
                .offidocType(rOffidocTypeMapper.toRest(cOffidocType))
                .closestMainParentObjectRegistrationNumber(selectParentRegistrationNumber(offidocAbdocsDocument))
                .files(selectFiles(document))
                .documentSubject(document.getDocSubject())
                .build()));
    }

    @ApiOperation(value = "Update email notification read date")
    @PostMapping(value = "/updateEmailNotificationReadDate", produces = "application/json")
    public RestApiResponse<EmptyResponse> updateEmailNotificationReadDate(@RequestBody RestApiRequest<ROffidocUpdateNotificationReadDateRequest> request) {
        validateNotificationDateRequestData(request);
        offidocAbdocsDocumentService.updateEmailNotificationReadDate(request.getData().getDate(), request.getData().getAbdocsDocumentId());
        return generateResponse(new EmptyResponse());
    }

    @ApiOperation(value = "Update portal notification read date")
    @PostMapping(value = "/updatePortalNotificationReadDate", produces = "application/json")
    public RestApiResponse<EmptyResponse> updatePortalNotificationReadDate(@RequestBody RestApiRequest<ROffidocUpdateNotificationReadDateRequest> request) {
        validateNotificationDateRequestData(request);
        offidocAbdocsDocumentService.updatePortalNotificationReadDate(request.getData().getDate(), request.getData().getAbdocsDocumentId());
        return generateResponse(new EmptyResponse());
    }

    private void validateNotificationDateRequestData(RestApiRequest<ROffidocUpdateNotificationReadDateRequest> request) {
        ROffidocUpdateNotificationReadDateRequest data = request.getData();
        if (Objects.isNull(data)) {
            throw new RestApiException("Empty data !");
        }
        Date date = data.getDate();
        if (Objects.isNull(date)) {
            throw new RestApiException("Empty date !");
        }
        Integer abdocsDocumentId = data.getAbdocsDocumentId();
        if (Objects.isNull(abdocsDocumentId)) {
            throw new RestApiException("Empty abdocs document id !");
        }
    }


    private List<RAbdocsDocFile> selectFiles(Document document) {
        List<RAbdocsDocFile> result = new ArrayList<>();

        List<DocFile> docFiles = document.getDocFiles();
        if (!CollectionUtils.isEmpty(docFiles)) {
            result.addAll(rAbdocsDocFileMapper.toRestList(docFiles));
        }
        List<DocFile> docFileLinks = document.getDocFileLinks();
        if (!CollectionUtils.isEmpty(docFileLinks)) {
            for (DocFile docFileLink : docFileLinks) {
                docFileLink.setDocFileVisibility(DocFileVisibility.PublicAttachedFile);// Linked files are always public, but they don't return that information in JSON
                result.add(rAbdocsDocFileMapper.toRest(docFileLink));
            }
        }

        return CollectionUtils.isEmpty(result) ? null : result;
    }

    private String selectParentRegistrationNumber(COffidocAbdocsDocument offidocAbdocsDocument) {
        OffidocParentObjectType offidocParentObjectType = OffidocParentObjectType.valueOf(offidocAbdocsDocument.getClosestMainParentObjectType());
        switch (offidocParentObjectType) {
            case IPOBJECT:
                return offidocAbdocsDocument.getClosestMainParentObjectId();
            case USERDOC:
                CDocumentId userdocId = BasicUtils.createCDocumentId(offidocAbdocsDocument.getClosestMainParentObjectId());
                if (Objects.nonNull(userdocId)) {
                    return docService.selectExternalSystemId(userdocId);
                }
            default:
                return null;
        }
    }

}
