package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.rest.model.util.RAttachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RAttachmentMapper extends BaseRestObjectMapper<RAttachment, CAttachment> {
}
