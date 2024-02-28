package bg.duosoft.ipas.persistence.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class BaseRepositoryCustomImpl {
    @PersistenceContext
    protected EntityManager em;


    protected void setDocumentIdParams(String docOri, String docLog, Integer docSer, Integer docNbr, Query query) {
        query.setParameter("docOri", docOri);
        query.setParameter("docLog", docLog);
        query.setParameter("docSer", docSer);
        query.setParameter("docNbr", docNbr);
    }

    protected void setOffidocIdParams(String offidocOri, Integer offidocSer, Integer offidocNbr, Query query) {
        query.setParameter("offidocOri", offidocOri);
        query.setParameter("offidocSer", offidocSer);
        query.setParameter("offidocNbr", offidocNbr);
    }

    protected void setProcessParams(String processTyp, Integer processNbr, Query query) {
        query.setParameter("processTyp", processTyp);
        query.setParameter("processNbr", processNbr);
    }

    protected void setFileIdParams(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, Query query) {
        query.setParameter("fileSeq", fileSeq);
        query.setParameter("fileTyp", fileTyp);
        query.setParameter("fileSer", fileSer);
        query.setParameter("fileNbr", fileNbr);
    }
}
