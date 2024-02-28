package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.rest.model.mark.RMark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RMarkMapper extends BaseRestObjectMapper<RMark, CMark> {
}
