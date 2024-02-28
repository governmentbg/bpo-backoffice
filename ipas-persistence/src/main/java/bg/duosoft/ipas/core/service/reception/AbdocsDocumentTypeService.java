package bg.duosoft.ipas.core.service.reception;


import bg.duosoft.ipas.core.model.miscellaneous.CAbdocsDocumentType;
import bg.duosoft.ipas.enums.IpasObjectType;

public interface AbdocsDocumentTypeService {

    Integer selectAbdocsDocTypeIdByType(String type);

    Integer getDocRegistrationType(String type);

    String selectNameByAbdocsDocTypeId(Integer id);

    CAbdocsDocumentType selectByAbdocsDocTypeId(Integer id);

    CAbdocsDocumentType selectByType(String type);

    CAbdocsDocumentType saveAbdocsDocumentType(CAbdocsDocumentType cAbdocsDocumentType);

    IpasObjectType selectIpasObjectTypeByAbdocsDocumentId(Integer docTypeId);

}
