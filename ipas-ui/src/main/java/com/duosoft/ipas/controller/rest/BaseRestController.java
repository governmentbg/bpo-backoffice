package com.duosoft.ipas.controller.rest;

import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.logging.annotation.LogExecutionTime;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(BaseRestController.BASE_REST_URL)
@LogExecutionTime
public class BaseRestController {
    public static final String BASE_REST_URL = "/rest";

    protected <T> RestApiResponse<T> generateResponse(T data) {
        return new RestApiResponse<>(data);
    }
}
