package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.rest.model.offidoc.ROffidocType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ROffidocTypeMapper extends BaseRestObjectMapper<ROffidocType, COffidocType> {
}
