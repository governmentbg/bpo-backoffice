package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.userdoc.grounds.CGroundNiceClasses;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.userdoc.grounds.RGroundNiceClasses;
import org.mapstruct.Mapper;

/**
 * User: Georgi
 * Date: 16.7.2020 г.
 * Time: 9:39
 */
@Mapper(componentModel = "spring")
public abstract class RFileIdMapper extends BaseRestObjectMapper<RFileId, CFileId> {
}
