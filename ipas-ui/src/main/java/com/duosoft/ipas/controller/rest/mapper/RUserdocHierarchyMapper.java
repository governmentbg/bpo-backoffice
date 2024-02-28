package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.userdoc.CUserdocHierarchyNode;
import bg.duosoft.ipas.rest.model.userdoc.RUserdocHierarchyNode;
import org.mapstruct.Mapper;

/**
 * User: ggeorgiev
 * Date: 28.01.2021
 * Time: 15:44
 */
@Mapper(componentModel = "spring")
public abstract class RUserdocHierarchyMapper extends BaseRestObjectMapper<RUserdocHierarchyNode, CUserdocHierarchyNode> {
}
