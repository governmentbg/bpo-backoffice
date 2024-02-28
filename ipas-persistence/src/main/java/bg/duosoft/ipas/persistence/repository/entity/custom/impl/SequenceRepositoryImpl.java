package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

/**
 * User: Georgi
 * Date: 26.5.2020 г.
 * Time: 17:35
 */

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDailyLog;
import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDay;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfDocSequence;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.SequenceRepository;
import bg.duosoft.ipas.persistence.repository.entity.dailylog.DailyLogRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.DocSequenceRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SequenceRepositoryImpl extends BaseRepositoryCustomImpl implements SequenceRepository {
    @Autowired
    private DailyLogRepository dailyLogRepository;
    @Autowired
    private DocSequenceRepository docSequenceRepository;

    @Override
    public Integer getNextSequenceValue(SEQUENCE_NAME sequenceName) {
        Query insertNewSequenceQuery = em.createNativeQuery("INSERT INTO " + sequenceName.getName() + " values('')");
        insertNewSequenceQuery.executeUpdate();

        Query getNewSequenceNumberQuery = em.createNativeQuery("SELECT @@IDENTITY");

        BigDecimal result = (BigDecimal) getNewSequenceNumberQuery.getSingleResult();
        int seqNbr = 1 + result.intValueExact();
        return seqNbr;
    }

    public synchronized Integer getNextDocNumber(String docOri) {
        Optional<IpDailyLog> dailyLog = dailyLogRepository.getOpenedDailyLog(docOri);
        if (dailyLog.isEmpty()) {
            throw new RuntimeException("No opened daily log!");
        }
        IpDailyLog dl = dailyLog.get();
        if (dl.getLastDocNbr() == null) {
            dl.setLastDocNbr(dl.getFirstDocNbr());
        } else {
            dl.setLastDocNbr(dl.getLastDocNbr() + 1);
        }
        em.persist(dl);
        return dl.getLastDocNbr();
    }

    @Override
    public Integer getDocSeries(String docOri) {
        Query query = em.createQuery("select d from IpDay d where d.docSer.indActive = :active and d.dailyDate = :dailyDate");
        query.setParameter("active", "S");
        query.setParameter("dailyDate", dailyLogRepository.getOpenedDailyLog(docOri).get().getPk().getDailyLogDate());
        List<IpDay> res = (List<IpDay>) query.getResultList();
        return CollectionUtils.isEmpty(res) ? null : res.get(0).getDocSer().getDocSer();
    }
    public Integer getDocSeqSeries(String docOri, String docSeqTyp) {
        CfDocSequence cfDocSeq = docSequenceRepository.getOne(docSeqTyp);
        if (!MapperHelper.getTextAsBoolean(cfDocSeq.getIndAnnualSeries())) {
            return cfDocSeq.getFixedSeries() == null ? 1 : cfDocSeq.getFixedSeries();
        } else {
            return getDocSeries(docOri);
        }
    }

    public boolean isGenerateNextDocSequenceFromExternalSystemId(String docSeqTyp) {
        CfDocSequence cfDocSeq = docSequenceRepository.getOne(docSeqTyp);
        return cfDocSeq.getIndContNbrForFile() == null;

    }
    @Override
    public Integer getNextDocSequenceNumber(String docSeqType, Integer docSeqSeries) {
        Integer res = _getNextDocSequenceNumber(docSeqType, docSeqSeries);
        //ako nqma razdaden docSeqNumber za tekushtata godina, i e vdignat flaga IndContNbrForFile, se vzema maksimalniq nomer edna godina nazad. Ako prednata godina e nqmalo razdaden nomer, togava se vzema naj golemiq nomer bez da se gleda godinata
        //ako ne se nameri nomer po gornata shema, se vry6ta 1
        if (res == null) {
            CfDocSequence cfDocSeq = docSequenceRepository.getOne(docSeqType);
            if (!StringUtils.isEmpty(cfDocSeq.getIndContNbrForFile()) && MapperHelper.getTextAsBoolean(cfDocSeq.getIndContNbrForFile())) {
                res = _getNextDocSequenceNumber(docSeqType, docSeqSeries - 1);
                if (res == null) {
                    res = _getNextDocSequenceNumber(docSeqType);
                }
            }
        }
        return res == null ? 1 : res;
    }
    private Integer _getNextDocSequenceNumber(String docSeqType, Integer docSeqSeries) {
        Query query = em.createQuery(" SELECT 1 + MAX(docSeqNbr) FROM IpDoc WHERE docSeqTyp.docSeqTyp = :docSeqType AND docSeqSeries = :docSeqSeries");
        query.setParameter("docSeqType", docSeqType);
        query.setParameter("docSeqSeries", docSeqSeries);
        return (Integer) query.getSingleResult();
    }
    private Integer _getNextDocSequenceNumber(String docSeqType) {
        Query query = em.createQuery(" SELECT 1 + MAX(docSeqNbr) FROM IpDoc WHERE docSeqTyp.docSeqTyp = :docSeqType ");
        query.setParameter("docSeqType", docSeqType);
        return (Integer) query.getSingleResult();
    }
}
