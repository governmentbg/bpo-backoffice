package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import org.mapstruct.Mapper;

/**
 * User: Georgi
 * Date: 16.7.2020 г.
 * Time: 9:39
 */
@Mapper(componentModel = "spring")
public abstract class RDocumentIdMapper extends BaseRestObjectMapper<RDocumentId, CDocumentId> {
}
