package com.duosoft.ipas.controller.rest.ipobjects;

import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import com.duosoft.ipas.controller.rest.BaseRestController;
import bg.duosoft.ipas.rest.custommodel.EmptyRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@Api(tags = {"IPAS-COMMON-ACTIONS"})
public class IpasCommonRestController extends BaseRestController {

    @Autowired
    private DailyLogService dailyLogService;

    @ApiOperation(value = "Get working date")
    @PostMapping(value = "/getWorkingDate", produces = "application/json")
    public RestApiResponse<Date> getWorkingDate(@RequestBody RestApiRequest<EmptyRequest> request) {
        return new RestApiResponse<>(dailyLogService.getWorkingDate());
    }

}
