package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.rest.model.action.RActionId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RActionIdMapper extends BaseRestObjectMapper<RActionId, CActionId> {
}
