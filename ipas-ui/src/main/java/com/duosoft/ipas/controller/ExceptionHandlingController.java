package com.duosoft.ipas.controller;

import bg.duosoft.ipas.exception.SessionObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackages = "com.duosoft.ipas.controller")
public class ExceptionHandlingController {

    private final ApplicationContext applicationContext;

    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = SessionObjectNotFoundException.class)
    public HashMap<String, Object> handleRestApiValidationException(SessionObjectNotFoundException e, WebRequest request) {
        log.debug(e.getMessage(), e);
        HashMap<String, Object> result = new HashMap<>();
        result.put("error", e.getMessage());
        result.put("message", applicationContext.getMessage("session.object.not.found.instructions", null, LocaleContextHolder.getLocale()));
        return result;
    }

}
