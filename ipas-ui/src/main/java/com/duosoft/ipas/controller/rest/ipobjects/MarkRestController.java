package com.duosoft.ipas.controller.rest.ipobjects;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.action.InternationalMarkActionService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.custommodel.mark.RAcceptTradeMarkRequest;
import bg.duosoft.ipas.rest.custommodel.mark.international_registration.RAcceptIntlTradeMarkRequest;
import bg.duosoft.ipas.rest.model.mark.RMark;
import com.duosoft.ipas.controller.rest.mapper.RAttachmentMapper;
import com.duosoft.ipas.controller.rest.mapper.RMarkMapper;
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
import java.util.Objects;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

@Slf4j
@RestController
@Api(tags = {"MARKS"})
@RequestMapping(BASE_REST_URL + "/mark")//okazva se che tova override-va /rest-a v BaseRestController, zatova trqbva da e /rest/mark
public class MarkRestController extends IpObjectsRestController {

    @Autowired
    private MarkService markService;

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private FileService fileService;

    @Autowired
    private RMarkMapper markMapper;

    @Autowired
    private RAttachmentMapper attachmentMapper;

    @Autowired
    private InternationalMarkActionService internationalMarkActionService;

    @ApiOperation(value = "Select Trademark")
    @PostMapping(value = "/get", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).MarkViewAll.code())")
    public RestApiResponse<RMark> getMark(@RequestBody RestApiRequest<RGetIpObjectRequest> findIpObjectRequest) {
        CMark mark = markService.findMark(findIpObjectRequest.getData().getFileId().getFileSeq(),
                findIpObjectRequest.getData().getFileId().getFileType(),
                findIpObjectRequest.getData().getFileId().getFileSeries(),
                findIpObjectRequest.getData().getFileId().getFileNbr(),
                findIpObjectRequest.getData().isAddAttachments());

        return new RestApiResponse<>(mark == null ? null : markMapper.toRest(mark));
    }

    @ApiOperation(value = "Accept trademark")
    @PostMapping(value = "/accept", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RAcceptIpObjectResponse> acceptTrademark(@RequestBody RestApiRequest<RAcceptTradeMarkRequest> markRequest) {
        CMark mark = markMapper.toCore(markRequest.getData().getMark());
        mark.getFile().setRelationshipList(CFileRelationshipUtils.constructRelationshipListWithExistingObjects(fileService,mark.getFile().getRelationshipList()));
        List<CAttachment> attachmentList=attachmentMapper.toCoreList(markRequest.getData().getAttachments());
        CReceptionResponse response = receptionService.acceptTrademark(mark, attachmentList);
        return new RestApiResponse<>(createRAcceptIpObjectResponse(response));
    }

    @ApiOperation(value = "Accept international trademark")
    @PostMapping(value = "/acceptInternationalMark", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RAcceptIpObjectResponse> acceptInternationalTrademark(@RequestBody RestApiRequest<RAcceptIntlTradeMarkRequest> markRequest) {
        CMark mark = markMapper.toCore(markRequest.getData().getMark());
        CReceptionResponse response = receptionService.acceptInternationalTrademark(mark, null);
        CMark insertedMark = markService.findMark(response.getFileId(), false);
        internationalMarkActionService.insertMarkFirstNormalAction(insertedMark, markRequest.getData().isFullyDivided());
        return new RestApiResponse<>(createRAcceptIpObjectResponse(response));
    }

}
