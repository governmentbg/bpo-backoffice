package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.search.CSearchParam;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.rest.model.search.RSearchParam;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class RSearchParamMapper extends BaseRestObjectMapper<RSearchParam, CSearchParam> {

    @AfterMapping
    public void initSearchPage(RSearchParam source, @MappingTarget CSearchParam target) {
        if (target.getPage() == null) {
            target.setPage(Pageable.DEFAULT_PAGE - 1);
        }
        if (target.getPageSize() == null) {
            target.setPageSize(Pageable.DEFAULT_PAGE_SIZE);
        }
    }
}
