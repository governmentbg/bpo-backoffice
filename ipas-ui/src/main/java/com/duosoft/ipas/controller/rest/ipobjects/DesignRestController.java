package com.duosoft.ipas.controller.rest.ipobjects;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.design.RAcceptDesignRequest;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.model.patent.RPatent;
import com.duosoft.ipas.controller.rest.mapper.RAttachmentMapper;
import com.duosoft.ipas.controller.rest.mapper.RPatentMapper;
import com.duosoft.ipas.controller.rest.mapper.RReceptionMapper;
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
@Api(tags = {"DESIGNS"})
@RequestMapping(BASE_REST_URL + "/design")
public class DesignRestController extends IpObjectsRestController{

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private DesignService designService;

    @Autowired
    private RPatentMapper patentMapper;

    @Autowired
    private RAttachmentMapper attachmentMapper;

    @Autowired
    private RReceptionMapper rReceptionMapper;

    @ApiOperation(value = "Accept design")
    @PostMapping(value = "/accept", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RAcceptIpObjectResponse> acceptDesign(@RequestBody RestApiRequest<RAcceptDesignRequest> designRequest) {
        CPatent mainDesign = patentMapper.toCore(designRequest.getData().getMainDesign());
        List<CPatent> singleDesigns = patentMapper.toCoreList(designRequest.getData().getSingleDesigns());
        List<CAttachment> attachmentList=attachmentMapper.toCoreList(designRequest.getData().getAttachments());
        List<CReception> userdocReceptions = rReceptionMapper.toCoreList(designRequest.getData().getUserdocReceptions());
        CReceptionResponse response = receptionService.acceptDesign(mainDesign, singleDesigns, attachmentList, userdocReceptions, designRequest.getData().getDoNotRegisterInDocflowSystem() == null || designRequest.getData().getDoNotRegisterInDocflowSystem() == false);
        return new RestApiResponse<>(createRAcceptIpObjectResponse(response));
    }

    @ApiOperation(value = "Select Single Design")
    @PostMapping(value = "/getSingleDesign", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).PatentViewAll.code())")
    public RestApiResponse<RPatent> getSingleDesign(@RequestBody RestApiRequest<RGetIpObjectRequest> findIpObjectRequest) {
        CPatent patent = designService.findSingleDesign(findIpObjectRequest.getData().getFileId().getFileSeq(),
                findIpObjectRequest.getData().getFileId().getFileType(),
                findIpObjectRequest.getData().getFileId().getFileSeries(),
                findIpObjectRequest.getData().getFileId().getFileNbr(),
                findIpObjectRequest.getData().isAddAttachments());

        return new RestApiResponse<>(patent == null ? null : patentMapper.toRest(patent));
    }
}
