package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.rest.model.person.RPerson;
import org.mapstruct.Mapper;

/**
 * User: Georgi
 * Date: 21.7.2020 г.
 * Time: 15:37
 */
@Mapper(componentModel = "spring")
public abstract class RPersonMapper extends BaseRestObjectMapper<RPerson, CPerson> {
}
