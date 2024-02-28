package com.duosoft.ipas.controller.rest.process;

import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.action.InsertActionException;
import bg.duosoft.ipas.core.service.action.InsertActionService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.action.RDeleteActionRequest;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.custommodel.process.RGetProcessRequest;
import bg.duosoft.ipas.rest.model.mark.RMark;
import bg.duosoft.ipas.rest.model.process.RProcess;
import bg.duosoft.ipas.rest.model.process.RProcessInsertActionRequest;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.mapper.RActionIdMapper;
import com.duosoft.ipas.controller.rest.mapper.RProcessIdMapper;
import com.duosoft.ipas.controller.rest.mapper.RProcessInsertActionRequestMapper;
import com.duosoft.ipas.controller.rest.mapper.RProcessMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

/**
 * User: Georgi
 * Date: 16.7.2020 г.
 * Time: 23:40
 */
@RestController
@Api(tags = {"PROCESS"})
@RequestMapping(BASE_REST_URL + "/process")
public class ProcessRestController extends BaseRestController {
    @Autowired
    private ProcessService processService;
    @Autowired
    private InsertActionService insertActionService;
    @Autowired
    private ActionService actionService;
    @Autowired
    private UserService userService;



    @Autowired
    private RProcessMapper processMapper;
    @Autowired
    private RProcessIdMapper processIdMapper;
    @Autowired
    private RProcessInsertActionRequestMapper rProcessInsertActionRequestMapper;
    @Autowired
    private RActionIdMapper rActionIdMapper;


    @ApiOperation(value = "Select process")
    @PostMapping(value = "/get", produces = "application/json")
//    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).MarkViewAll.code())")//TODO:Process role
    public RestApiResponse<RProcess> getProcess(@RequestBody RestApiRequest<RGetProcessRequest> rq) {
        CProcess process = processService.selectProcess(processIdMapper.toCore(rq.getData().getProcessId()), rq.getData().isAddProcessEvents());
        return new RestApiResponse<>(process == null ? null : processMapper.toRest(process));
    }
    @ApiOperation(value = "Select process")
    @PostMapping(value = "/action/insert", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ProcessExecuteActionsForForeignObjects.code())")
    public RestApiResponse<EmptyResponse> insertAction(@RequestBody RestApiRequest<RProcessInsertActionRequest> rq) throws InsertActionException {
        insertActionService.insertAction(rProcessInsertActionRequestMapper.toCore(rq.getData()));
        return generateResponse(new EmptyResponse());
    }

    @ApiOperation(value = "Delete action")
    @PostMapping(value = "/action/delete", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ProcessExecuteActionsForForeignObjects.code())")
    public RestApiResponse<Boolean> deleteAction(@RequestBody RestApiRequest<RDeleteActionRequest> rq) {
        Integer userId = userService.getUserByLogin(rq.getUsername()).getUserId();
        boolean res = actionService.deleteAction(rActionIdMapper.toCore(rq.getData().getActionId()), userId, rq.getData().getReason());
        return generateResponse(res);
    }
}
