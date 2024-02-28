package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraData;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraDataType;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraDataValue;

public interface UserdocExtraDataService {

    CUserdocExtraData save(CUserdocExtraData userdocExtraData);

    CUserdocExtraData save(CDocumentId documentId, String type, CUserdocExtraDataValue value);

    CUserdocExtraDataType selectExtraDataType(String code);

    CUserdocExtraData selectById(CDocumentId documentId, String type);

}