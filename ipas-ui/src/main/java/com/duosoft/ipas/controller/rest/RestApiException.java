package com.duosoft.ipas.controller.rest;

/**
 * User: Georgi
 * Date: 15.9.2020 г.
 * Time: 15:33
 */
public class RestApiException extends RuntimeException {

    public RestApiException() {
    }

    public RestApiException(String message) {
        super(message);
    }

    public RestApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestApiException(Throwable cause) {
        super(cause);
    }

    public RestApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
