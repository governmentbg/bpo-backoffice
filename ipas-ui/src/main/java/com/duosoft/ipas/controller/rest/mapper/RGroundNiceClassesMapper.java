package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.userdoc.grounds.CGroundNiceClasses;
import bg.duosoft.ipas.rest.model.userdoc.grounds.RGroundNiceClasses;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public abstract class RGroundNiceClassesMapper extends BaseRestObjectMapper<RGroundNiceClasses, CGroundNiceClasses> {

}
