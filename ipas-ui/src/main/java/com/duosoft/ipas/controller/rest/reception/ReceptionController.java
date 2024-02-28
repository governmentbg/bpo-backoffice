package com.duosoft.ipas.controller.rest.reception;

import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.model.reception.RReception;
import bg.duosoft.ipas.rest.model.reception.RReceptionResponse;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.mapper.RReceptionMapper;
import com.duosoft.ipas.controller.rest.mapper.RReceptionResponseMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

@Slf4j
@RestController
@Api(tags = {"RECEPTION"})
@RequestMapping(BASE_REST_URL + "/reception")
public class ReceptionController extends BaseRestController {

    @Autowired
    private ReceptionService receptionService;
    @Autowired
    private RReceptionMapper rReceptionMapper;
    @Autowired
    private RReceptionResponseMapper rReceptionResponseMapper;

    @ApiOperation(value = "Create reception")
    @PostMapping(value = "/create", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
    public RestApiResponse<RReceptionResponse> createReception(@RequestBody RestApiRequest<RReception> receptionRequest) {
        CReception creception = rReceptionMapper.toCore(receptionRequest.getData());
        CReceptionResponse resp = receptionService.createReception(creception);
        return generateResponse(rReceptionResponseMapper.toRest(resp));
    }
}
