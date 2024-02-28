package bg.duosoft.ipas.core.service.impl;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.persistence.model.entity.InsertableEntity;
import bg.duosoft.ipas.persistence.model.entity.UpdatableEntity;
import bg.duosoft.ipas.util.function.FourArgsFunction;
import bg.duosoft.ipas.util.security.SecurityUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 25.7.2019 г.
 * Time: 17:32
 */
public class ServiceBaseImpl {
    @PersistenceContext
    protected EntityManager em;

    protected void updateUpdatableEntity(UpdatableEntity entity) {
        entity.setLastUpdateDate(new Date());
        entity.setLastUpdateUserId(SecurityUtils.getLoggedUserId());
        em.merge(entity);
    }

    protected void insertInsertableEntity(InsertableEntity entity) {
        entity.setCreationDate(new Date());
        entity.setCreationUserId(SecurityUtils.getLoggedUserId());
        em.persist(entity);
    }

    protected <T> T callDocumentFunction(CDocumentId docId,  FourArgsFunction<String, String, Integer, Integer, T> function) {
        return function.apply(docId.getDocOrigin(), docId.getDocLog(), docId.getDocSeries(), docId.getDocNbr());
    }
    protected <T> T callFileFunction(CFileId fileId, FourArgsFunction<String, String, Integer, Integer, T> function) {
        return function.apply(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
    }
}
