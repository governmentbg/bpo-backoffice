package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.process.CProcessInsertActionRequest;
import bg.duosoft.ipas.rest.model.process.RProcessInsertActionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RProcessInsertActionRequestMapper extends BaseRestObjectMapper<RProcessInsertActionRequest, CProcessInsertActionRequest> {
}
