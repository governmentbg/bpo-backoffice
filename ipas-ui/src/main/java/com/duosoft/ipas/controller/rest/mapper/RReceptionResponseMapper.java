package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.rest.model.reception.RReceptionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RReceptionResponseMapper extends BaseRestObjectMapper<RReceptionResponse, CReceptionResponse> {

}
