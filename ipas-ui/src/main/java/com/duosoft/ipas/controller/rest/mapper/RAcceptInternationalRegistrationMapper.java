package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.userdoc.international_registration.CAcceptInternationalRegistrationRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.international_registration.RAcceptInternationalRegistrationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RAcceptInternationalRegistrationMapper extends BaseRestObjectMapper<RAcceptInternationalRegistrationRequest, CAcceptInternationalRegistrationRequest> {
}
