package com.duosoft.ipas.controller.rest.mapper;

import org.mapstruct.InheritInverseConfiguration;

import java.util.List;

public abstract class BaseRestObjectMapper<R, C> {

    public abstract C toCore(R e);

    public abstract List<C> toCoreList(List<R> eList);

    @InheritInverseConfiguration
    public abstract R toRest(C c);

    @InheritInverseConfiguration
    public abstract List<R> toRestList(List<C> cList);
}
