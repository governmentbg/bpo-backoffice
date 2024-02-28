package com.duosoft.ipas.controller.rest.ipobjects;

import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.mapper.RDocumentIdMapper;
import com.duosoft.ipas.controller.rest.mapper.RFileIdMapper;
import com.duosoft.ipas.controller.rest.mapper.RReceptionResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class IpObjectsRestController extends BaseRestController {
    @Autowired
    protected RFileIdMapper fileIdMapper;
    @Autowired
    protected RDocumentIdMapper documentIdMapper;
    @Autowired
    protected RReceptionResponseMapper rReceptionResponseMapper;

    protected RAcceptIpObjectResponse createRAcceptIpObjectResponse(CReceptionResponse response) {
        return new RAcceptIpObjectResponse(rReceptionResponseMapper.toRest(response));
    }

}
