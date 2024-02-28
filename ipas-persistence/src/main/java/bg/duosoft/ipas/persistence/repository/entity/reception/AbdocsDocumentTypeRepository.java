package bg.duosoft.ipas.persistence.repository.entity.reception;

import bg.duosoft.ipas.persistence.model.entity.ext.reception.CfAbdocsDocumentType;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbdocsDocumentTypeRepository extends BaseRepository<CfAbdocsDocumentType, String> {

    @Query(value = "SELECT * FROM EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE t where t.TYPE = ?1 ", nativeQuery = true)
    CfAbdocsDocumentType selectByType(String type);

    @Query(value = "SELECT * FROM EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE t where t.ABDOCS_DOC_TYPE_ID = ?1 ", nativeQuery = true)
    CfAbdocsDocumentType selectByAbdocsDocTypeId(Integer docTypeId);

    @Query(value = "SELECT DISTINCT t.IPAS_OBJECT FROM EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE t where t.ABDOCS_DOC_TYPE_ID = ?1 ", nativeQuery = true)
    String selectIpasObjectTypeByAbdocsDocumentId(Integer docTypeId);

}
