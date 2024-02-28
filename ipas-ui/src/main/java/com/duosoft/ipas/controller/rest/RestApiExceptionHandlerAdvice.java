package com.duosoft.ipas.controller.rest;

import bg.duosoft.ipas.rest.custommodel.RestApiError;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.rest.custommodel.RestApiValidationError;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
class RestApiExceptionHandlerAdvice {

    @ExceptionHandler(value = IpasValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public RestApiError handleRestApiValidationException(IpasValidationException e, WebRequest request) {
        log.error(e.getMessage(), e);
        List<RestApiValidationError.ValidationErrorDetail> details = new ArrayList<>();
        if (e.getErrors() != null) {
            details = e.getErrors().stream().map(r -> new RestApiValidationError.ValidationErrorDetail(r.getPointer(), r.getMessageCode(), r.getMessage(), r.getInvalidValue())).collect(Collectors.toList());
        }
        return new RestApiValidationError(e.getMessage(), e.getClass().getCanonicalName(), new Date(), HttpStatus.UNPROCESSABLE_ENTITY.value(), details);
    }

    @ExceptionHandler(value = { RestApiException.class, Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestApiError handleRestApiException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        return new RestApiError(e.getMessage(), e.getClass().getCanonicalName(), new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


}