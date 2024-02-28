package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.rest.model.userdoc.RUserdoc;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RUserdocMapper extends BaseRestObjectMapper<RUserdoc, CUserdoc> {

}
