package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.rest.model.patent.RPatent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RPatentMapper extends BaseRestObjectMapper<RPatent, CPatent> {

}
