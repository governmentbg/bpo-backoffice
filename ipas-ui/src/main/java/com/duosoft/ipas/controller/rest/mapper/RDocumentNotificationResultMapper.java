package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.abdocs.notification.CDocumentNotificationProcessResult;
import bg.duosoft.ipas.rest.custommodel.abdocs.notification.RDocumentNotificationProcessResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RDocumentNotificationResultMapper extends BaseRestObjectMapper<RDocumentNotificationProcessResult, CDocumentNotificationProcessResult> {

}
