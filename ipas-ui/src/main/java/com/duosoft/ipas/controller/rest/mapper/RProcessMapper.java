package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.rest.model.mark.RMark;
import bg.duosoft.ipas.rest.model.process.RProcess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RProcessMapper extends BaseRestObjectMapper<RProcess, CProcess> {
}
