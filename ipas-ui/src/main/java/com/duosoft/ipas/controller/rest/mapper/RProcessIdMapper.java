package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import bg.duosoft.ipas.rest.model.process.RProcess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RProcessIdMapper extends BaseRestObjectMapper<RProcessId, CProcessId> {
}
