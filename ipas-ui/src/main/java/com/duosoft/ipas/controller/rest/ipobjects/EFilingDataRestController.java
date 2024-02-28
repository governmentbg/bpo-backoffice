package com.duosoft.ipas.controller.rest.ipobjects;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.efiling.IpObjectEfilingDataService;
import bg.duosoft.ipas.core.service.efiling.UserdocEfilingDataService;
import bg.duosoft.ipas.integration.portal.service.PortalService;
import bg.duosoft.ipas.persistence.repository.entity.efiling.UserdocEfilingDataRepository;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RIpObjectEfilingUserRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.efiling.RUserdocEfilingUserRequest;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.mapper.RFileIdMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = {"IPAS-EFILING-DATA-ACTIONS"})
public class EFilingDataRestController  extends BaseRestController {

    @Autowired
    private UserdocEfilingDataService userdocEfilingDataService;

    @Autowired
    private IpObjectEfilingDataService ipObjectEfilingDataService;

    @Autowired
    private RFileIdMapper rFileIdMapper;

    @ApiOperation(value = "Update userdoc efiling logusername")
    @PostMapping(value = "/updateUserdocEfilingLogUserName", produces = "application/json")
    public RestApiResponse<String> updateUserdocEfilingLogUserName(@RequestBody RestApiRequest<RUserdocEfilingUserRequest> request) {
        userdocEfilingDataService.updateLogUserName(request.getData().getExternalSystemId(),request.getData().getUser());
        return new RestApiResponse<String>();

    }

    @ApiOperation(value = "Update ip object efiling logusername")
    @PostMapping(value = "/updateIpObjectEfilingLogUserName", produces = "application/json")
    public RestApiResponse<String> updateIpObjectEfilingLogUserName(@RequestBody RestApiRequest<RIpObjectEfilingUserRequest> request) {
        CFileId cFileId = rFileIdMapper.toCore(request.getData().getId());
        ipObjectEfilingDataService.updateLogUserName(cFileId,request.getData().getUser());
        return new RestApiResponse<String>();
    }

}
