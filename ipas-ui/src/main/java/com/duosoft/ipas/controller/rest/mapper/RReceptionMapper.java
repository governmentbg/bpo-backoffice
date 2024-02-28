package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.rest.model.reception.RReception;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        RAttachmentMapper.class
})
public abstract class RReceptionMapper extends BaseRestObjectMapper<RReception, CReception> {

}
