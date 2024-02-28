package com.duosoft.ipas.controller.rest.person;

import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.person.RGetPersonRequest;
import bg.duosoft.ipas.rest.model.person.RPerson;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.mapper.RPersonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

/**
 * User: Georgi
 * Date: 21.7.2020 г.
 * Time: 15:32
 */
@RestController
@Api(tags = {"PERSON"})
@RequestMapping(BASE_REST_URL + "/person")
public class PersonRestController extends BaseRestController {
    @Autowired
    private PersonService personService;
    @Autowired
    private RPersonMapper rPersonMapper;

    @ApiOperation(value = "Select process")
    @PostMapping(value = "/get", produces = "application/json")
//    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).MarkViewAll.code())")//TODO:Person role
    public RestApiResponse<RPerson> getPerson(@RequestBody RestApiRequest<RGetPersonRequest> rq) {
        CPerson res = personService.selectPersonByPersonNumberAndAddressNumber(rq.getData().getPersonNbr(), rq.getData().getAddressNbr());
        return generateResponse(res == null ? null : rPersonMapper.toRest(res));
    }
}
