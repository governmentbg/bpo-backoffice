package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.abdocs.model.DocFile;
import bg.duosoft.ipas.rest.custommodel.abdocs.document.RAbdocsDocFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RAbdocsDocFileMapper extends BaseRestObjectMapper<RAbdocsDocFile, DocFile> {
}
