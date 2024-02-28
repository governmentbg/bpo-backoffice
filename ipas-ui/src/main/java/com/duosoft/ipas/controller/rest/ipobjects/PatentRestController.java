package com.duosoft.ipas.controller.rest.ipobjects;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptPatentRequest;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.patent.RPatent;
import com.duosoft.ipas.controller.rest.mapper.RAttachmentMapper;
import com.duosoft.ipas.controller.rest.mapper.RPatentMapper;
import com.duosoft.ipas.controller.rest.mapper.RReceptionMapper;
import com.duosoft.ipas.util.CFileRelationshipUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

@Slf4j
@RestController
@Api(tags = {"PATENTS"})
@RequestMapping(BASE_REST_URL + "/patent")
public class PatentRestController extends IpObjectsRestController {

    @Autowired
    private RPatentMapper patentMapper;

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private RAttachmentMapper attachmentMapper;

    @Autowired
    private PatentService patentService;
    @Autowired
    private RReceptionMapper rReceptionMapper;

    @Autowired
    private FileService fileService;


    @ApiOperation(value = "Accept patent")
    @PostMapping(value = "/accept", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RAcceptIpObjectResponse> acceptPatent(@RequestBody RestApiRequest<RAcceptPatentRequest> patentRequest) {
        CPatent patent = patentMapper.toCore(patentRequest.getData().getPatent());
        patentService.saveEuPatentFromRelationshipIfMissing(patent);
        patent.getFile().setRelationshipList(CFileRelationshipUtils.constructRelationshipListWithExistingObjects(fileService,patent.getFile().getRelationshipList()));
        List<CAttachment> attachmentList=attachmentMapper.toCoreList(patentRequest.getData().getAttachments());
        List<CReception> userdocReceptions = rReceptionMapper.toCoreList(patentRequest.getData().getUserdocReceptions());
        CReceptionResponse response = receptionService.acceptPatent(patent, attachmentList, userdocReceptions);
        return new RestApiResponse<>(createRAcceptIpObjectResponse(response));

    }

    @ApiOperation(value = "Select Patent")
    @PostMapping(value = "/get", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).PatentViewAll.code())")
    public RestApiResponse<RPatent> getPatent(@RequestBody RestApiRequest<RGetIpObjectRequest> findIpObjectRequest) {
        CPatent patent = patentService.findPatent(findIpObjectRequest.getData().getFileId().getFileSeq(),
                findIpObjectRequest.getData().getFileId().getFileType(),
                findIpObjectRequest.getData().getFileId().getFileSeries(),
                findIpObjectRequest.getData().getFileId().getFileNbr(),
                findIpObjectRequest.getData().isAddAttachments());

        return new RestApiResponse<>(patent == null ? null : patentMapper.toRest(patent));
    }

    @ApiOperation(value = "Update Patent")
    @PostMapping(value = "/update", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).PatentEditAll.code())")
    public RestApiResponse<Boolean> updatePatent(@RequestBody RestApiRequest<RPatent> rq) {
        patentService.updatePatent(patentMapper.toCore(rq.getData()));
        return new RestApiResponse<>(true);
    }

    @ApiOperation(value = "Delete Patent")
    @PostMapping(value = "/delete", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).PatentEditAll.code())")
    public RestApiResponse<EmptyResponse> deletePatent(@RequestBody RestApiRequest<RFileId> rq) {
        patentService.deletePatent(fileIdMapper.toCore(rq.getData()));
        return new RestApiResponse<>(new EmptyResponse());
    }

    @ApiOperation(value = "Checks if the userdoc is main for the epo patent")
    @PostMapping(value = "/isMainEpoPatentRequestForValidation", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).PatentEditAll.code())")
    public RestApiResponse<Boolean> isMainEpoPatentRequestForValidation(@RequestBody RestApiRequest<RDocumentId> rq) {
        return new RestApiResponse<>(patentService.isMainEpoPatentRequestForValidation(documentIdMapper.toCore(rq.getData())));
    }
}
