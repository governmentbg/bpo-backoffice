package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.rest.model.offidoc.ROffidocAbdocsDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ROffidocAbdocsDocumentMapper extends BaseRestObjectMapper<ROffidocAbdocsDocument, COffidocAbdocsDocument> {
}
