package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.model.nonentity.GenerateJournalData;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.JournalElementRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;

@Repository
@Transactional
public class JournalElementRepositoryCustomImpl extends BaseRepositoryCustomImpl implements JournalElementRepositoryCustom {

    @Override
    public GenerateJournalData selectJournalParamsByAction(String procTyp, Integer procNbr, Integer actionNbr) {
        Query q = em.createNativeQuery("SELECT j.ELEMENT_NBR, j.NAME, p.REGISTRATION_NBR from EXT_JOURNAL.JOURNAL_ELEMENT j\n" +
                "                JOIN IP_PROC pr on j.PROC_TYP=pr.PROC_TYP and j.PROC_NBR = pr.PROC_NBR\n" +
                "                LEFT JOIN IP_PATENT p  on pr.FILE_SEQ = p.FILE_SEQ and pr.FILE_TYP=p.FILE_TYP and pr.FILE_SER=p.FILE_SER and pr.FILE_NBR=p.FILE_NBR\n" +
                "                WHERE j.PROC_TYP = :procTyp AND j.PROC_NBR = :procNbr and j.ACTION_NBR = :actionNbr");
        q.setParameter("procTyp", procTyp);
        q.setParameter("procNbr", procNbr);
        q.setParameter("actionNbr", actionNbr);
        return fillResult(q);
    }

    private GenerateJournalData fillResult(Query query) {
        Object[] res = (Object[]) query.getSingleResult();
        Integer elementNbr = ((BigDecimal) res[0]).intValue();
        String appNbr = (String) res[1];
        Integer registrationNbr = res[2] != null ? ((BigDecimal) res[2]).intValue() : null;
        return new GenerateJournalData(elementNbr, appNbr, registrationNbr);
    }
}
