package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.persistence.model.nonentity.ExtLiabilityDetailsExtended;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.ExtLiabilityDetailsExtendedRepository;
import bg.duosoft.ipas.util.filter.LastPaymentsFilter;
import bg.duosoft.ipas.util.filter.sorter.LastPaymentsSorterUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 18.08.2021
 * Time: 11:02
 */
@Repository
public class ExtLiabilityDetailsExtendedRepositoryImpl extends BaseRepositoryCustomImpl implements ExtLiabilityDetailsExtendedRepository {
    public List<ExtLiabilityDetailsExtended> getLastPayments(Date dateLastPaymentFrom, Date dateLastPaymentTo, List<Integer> responsibleUsers, Integer processed, Integer start, Integer limit, String sortColumn, String sortOrder) {
        if (CollectionUtils.isEmpty(responsibleUsers)) {
            return new ArrayList<>();
        }
        String sql = _generateGetLastPaymentsQuery(dateLastPaymentFrom, dateLastPaymentTo, responsibleUsers, processed, sortColumn, sortOrder, false);
        Query q = em.createNativeQuery(sql, ExtLiabilityDetailsExtended.class);
        addParams(q, dateLastPaymentFrom, dateLastPaymentTo, responsibleUsers, processed);
        if (start != null) {
            q.setFirstResult(start);
        }
        if (limit != null) {
            q.setMaxResults(limit);
        }


        return q.getResultList();
    }

    private static final String SELECT_OBJECT_PAYMENTS = "SELECT {COLUMNS} \n" +
            "from ext_liability_details ld\n" +
            "join IP_PROC p on p.FILE_SEQ = ld.FILE_SEQ and p.FILE_TYP = ld.FILE_TYP and p.FILE_SER = ld.FILE_SER and p.FILE_NBR = ld.FILE_NBR\n" +
            "join IP_DOC d on d.FILE_SEQ = ld.FILE_SEQ and d.FILE_TYP = ld.FILE_TYP and d.FILE_SER = ld.FILE_SER and d.FILE_NBR = ld.FILE_NBR\n" +
            "WHERE ld.LAST_DATE_PAYMENT is not null ";
    private static final String SELECT_USERDOC_PAYMENTS = "SELECT {COLUMNS} \n" +
            "from ext_liability_details ld\n" +
            "join IP_PROC p on p.DOC_ORI = ld.FILE_SEQ and p.DOC_LOG = ld.FILE_TYP and p.DOC_SER = ld.FILE_SER and p.DOC_NBR = ld.FILE_NBR\n" +
            "join IP_DOC d on d.DOC_ORI = ld.FILE_SEQ and d.DOC_LOG = ld.FILE_TYP and d.DOC_SER = ld.FILE_SER and d.DOC_NBR = ld.FILE_NBR\n" +
            "WHERE ld.LAST_DATE_PAYMENT is not null  ";

    @Override
    public long getLastPaymentsCount(Date dateLastPaymentFrom, Date dateLastPaymentTo, List<Integer> responsibleUsers, Integer processed) {
        String sql = _generateGetLastPaymentsQuery(dateLastPaymentFrom, dateLastPaymentTo, responsibleUsers, processed, null, null, true);
        Query q = em.createNativeQuery(sql);
        addParams(q, dateLastPaymentFrom, dateLastPaymentTo, responsibleUsers, processed);
        return ((Number) q.getSingleResult()).longValue();
    }

    private String _generateGetLastPaymentsQuery(Date dateLastPaymentFrom, Date dateLastPaymentTo, List<Integer> responsibleUsers, Integer processed, String sortColumn, String sortOrder, boolean count) {
        List<String> where = new ArrayList<>();
        if (dateLastPaymentFrom != null) {
            where.add("ld.LAST_DATE_PAYMENT >= :dateLastPaymentFrom");
        }
        if (processed != null) {
            where.add("processed = :processed");
        }
        if (dateLastPaymentTo != null) {
            where .add("ld.LAST_DATE_PAYMENT <= :dateLastPaymentTo");
        }
        if (!CollectionUtils.isEmpty(responsibleUsers)) {
            where.add("p.RESPONSIBLE_USER_ID in :responsibleUsers");
        }
        String columns = count ? "count(*)" : "ld.*, EXTERNAL_SYSTEM_ID";
        String objectsSql = _prepareSql(SELECT_OBJECT_PAYMENTS, columns, where );
        String userdocsSql = _prepareSql(SELECT_USERDOC_PAYMENTS, columns, where );


        if (count) {
            return  "SELECT (" + objectsSql + ") + (" + userdocsSql + ")";
        } else {
            return "SELECT a.* from (" + objectsSql + " UNION " + userdocsSql + ") as a ORDER BY  " + generateOrderByStatement(sortColumn, sortOrder);
        }
    }

    private static String _prepareSql(String baseSql, String columns, List<String> where) {
        baseSql = baseSql.replaceAll("\\{COLUMNS}", columns);
        baseSql = baseSql + (where.size() == 0 ? "" : " AND " +where.stream().collect(Collectors.joining(" AND ")));
        return baseSql;
    }

    private static String generateOrderByStatement(String sortColumn, String sortOrder) {
        String res;
        if (StringUtils.isEmpty(sortColumn)) {
            sortColumn = LastPaymentsSorterUtils.LAST_DATE_PAYMENT;
            sortOrder = LastPaymentsFilter.DESC_ORDER;
        }
        if (StringUtils.isEmpty(sortOrder)) {
            sortOrder = LastPaymentsFilter.ASC_ORDER;
        }
        if (LastPaymentsSorterUtils.FILING_NUMBER.equals(sortColumn)) {
            res = "FILE_SEQ {0}, FILE_TYP {0}, FILE_SER {0}, FILE_NBR {0}, id {0}";
        } else if (LastPaymentsSorterUtils.LAST_PAYER_NAME.equals(sortColumn)) {
            res = "LAST_PAYER_NAME {0}, id {0}";
        } else if (LastPaymentsSorterUtils.LIABILITY_AMOUNT.equals(sortColumn)) {
            res = "amount {0}, id {0}";
        } else if (LastPaymentsSorterUtils.LIABILITY_AMOUNT_OUTSTANDING.equals(sortColumn)) {
            res = "AMOUNT_OUTSTANDING {0}, id {0}";
        } else if (LastPaymentsSorterUtils.LIABILITY_CODE_NAME.equals(sortColumn)) {
            res = "LIABILITY_CODE {0}, id {0}";
        } else {
            res = "LAST_DATE_PAYMENT {0}, id {0}";
        }
        return MessageFormat.format(res, sortOrder);
    }

    private void addParams(Query q, Date dateLastPaymentFrom, Date dateLastPaymentTo, List<Integer> responsibleUsers, Integer processed) {
        Map<String, Object> where = new HashMap<>();
        if (dateLastPaymentFrom != null) {
            where.put("dateLastPaymentFrom", dateLastPaymentFrom);
        }
        if (dateLastPaymentTo != null) {
            where.put("dateLastPaymentTo", dateLastPaymentTo);
        }
        if (!CollectionUtils.isEmpty(responsibleUsers)) {
            where.put("responsibleUsers", responsibleUsers);
        }
        if (processed != null) {
            where.put("processed", processed);
        }
        where.forEach((k, v) -> q.setParameter(k, v));
    }
}
