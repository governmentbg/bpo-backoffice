package com.duosoft.ipas.controller.rest.ipobjects;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.model.util.CEuPatentsReceptionIds;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptEuPatentRequest;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptedEuPatentResponse;
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
@Api(tags = {"EU-PATENTS"})
@RequestMapping(BASE_REST_URL + "/epo-patent")
public class EuPatentRestController extends IpObjectsRestController {

    @Autowired
    private RPatentMapper patentMapper;

    @Autowired
    private RAttachmentMapper attachmentMapper;

    @Autowired
    private PatentService patentService;
    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private RReceptionMapper rReceptionMapper;

    @ApiOperation(value = "Accept eu patent")
    @PostMapping(value = "/accept", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RAcceptedEuPatentResponse> acceptEuPatent(@RequestBody RestApiRequest<RAcceptEuPatentRequest> patentRequest) {
        CPatent patent = patentMapper.toCore(patentRequest.getData().getEuPatent());
        List<CAttachment> attachmentList = attachmentMapper.toCoreList(patentRequest.getData().getAttachments());
        List<CReception> userdocReceptions = rReceptionMapper.toCoreList(patentRequest.getData().getUserdocReceptions());

        CEuPatentsReceptionIds euPatentsReceptionIds = receptionService.acceptEuPatent(patent, attachmentList, userdocReceptions, patentRequest.getData().getUserdocType(), patentRequest.getData().getObjectNumber());
        RAcceptedEuPatentResponse response = new RAcceptedEuPatentResponse();
        response.setPatentFileId(fileIdMapper.toRest(euPatentsReceptionIds.getEuPatentFileId()));
        response.setFinalUserdocReceptionResponse(rReceptionResponseMapper.toRest(euPatentsReceptionIds.getFinalUserdocReceptionResponse()));
        return new RestApiResponse<>(response);

    }
}
