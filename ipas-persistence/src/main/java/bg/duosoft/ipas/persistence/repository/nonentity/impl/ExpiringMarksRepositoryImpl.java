package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.persistence.model.nonentity.ExpiringMarkResult;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.ExpiringMarksRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExpiringMarksRepositoryImpl extends BaseRepositoryCustomImpl implements ExpiringMarksRepository {

    @Override
    public List<ExpiringMarkResult> selectExpiringMarks(Date expirationDateFrom, Date expirationDateTo) {
        Query query = em.createNativeQuery("select m.FILE_SEQ,\n" +
                "       m.FILE_TYP,\n" +
                "       m.FILE_SER,\n" +
                "       m.FILE_NBR,\n" +
                "       f.TITLE,\n" +
                "       p.RESPONSIBLE_USER_ID,\n" +
                "       u.USER_NAME,\n" +
                "       s.STATUS_NAME,\n" +
                "       f.FILING_DATE,\n" +
                "       f.EXPIRATION_DATE,\n" +
                "       pa.ADDR_STREET,\n" +
                "       pa.CITY_NAME,\n" +
                "       pa.ZIPCODE,\n" +
                "       pa.RESIDENCE_COUNTRY_CODE,\n" +
                "       m.REGISTRATION_NBR,\n" +
                "       pr.PERSON_NAME,\n" +
                "       pr2.PERSON_NAME            as SERV_PERSON_NAME,\n" +
                "       m.SERVICE_PERSON_NBR,\n" +
                "       ag.IND_INACTIVE,\n" +
                "       mpa.ADDR_STREET            as MAIN_OWNER_ADDR_STREET,\n" +
                "       mpa.CITY_NAME              as MAIN_OWNER_CITY_NAME,\n" +
                "       mpa.ZIPCODE                as MAIN_OWNER_ZIPCODE,\n" +
                "       mpa.RESIDENCE_COUNTRY_CODE as MAIN_OWNER_RESIDENCE_COUNTRY_CODE\n" +
                "FROM IPASPROD.IP_MARK m\n" +
                "         INNER JOIN IPASPROD.IP_FILE f\n" +
                "                    ON m.FILE_NBR = f.FILE_NBR AND m.FILE_SEQ = f.FILE_SEQ AND m.FILE_TYP = f.FILE_TYP AND\n" +
                "                       m.FILE_SER = f.FILE_SER\n" +
                "         INNER JOIN IPASPROD.IP_PROC p ON f.PROC_NBR = p.PROC_NBR AND f.PROC_TYP = p.PROC_TYP\n" +
                "         INNER JOIN IPASPROD.CF_STATUS s ON p.PROC_TYP = s.PROC_TYP AND p.STATUS_CODE = s.STATUS_CODE\n" +
                "         INNER JOIN IPASPROD.IP_USER u on p.RESPONSIBLE_USER_ID = u.USER_ID\n" +
                "         LEFT OUTER JOIN IPASPROD.IP_PERSON pr2 ON pr2.PERSON_NBR = m.SERVICE_PERSON_NBR\n" +
                "         LEFT OUTER JOIN IPASPROD.IP_PERSON_ADDRESSES pa\n" +
                "                         ON pa.PERSON_NBR = m.SERVICE_PERSON_NBR and pa.ADDR_NBR = m.SERVICE_ADDR_NBR\n" +
                "         LEFT OUTER JOIN IPASPROD.IP_AGENT ag ON pr2.AGENT_CODE = ag.AGENT_CODE\n" +
                "         INNER JOIN IPASPROD.IP_PERSON pr ON pr.PERSON_NBR = m.MAIN_OWNER_PERSON_NBR\n" +
                "         INNER JOIN IPASPROD.IP_PERSON_ADDRESSES mpa\n" +
                "                    ON mpa.PERSON_NBR = m.MAIN_OWNER_PERSON_NBR and mpa.ADDR_NBR = m.MAIN_OWNER_ADDR_NBR\n" +
                "WHERE m.FILE_TYP IN ('N', 'D')\n" +
                "  AND ((p.STATUS_CODE in ('034', '987', '835') AND m.EXPIRATION_DATE >= :expirationDateFrom AND\n" +
                "        m.EXPIRATION_DATE <= :expirationDateTo))\n" +
                "order by f.EXPIRATION_DATE");

        query.setParameter("expirationDateFrom", expirationDateFrom);
        query.setParameter("expirationDateTo", expirationDateTo);

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        return convertResult(resultList);
    }

    private List<ExpiringMarkResult> convertResult(List<Object[]> resultList) {
        List<ExpiringMarkResult> result = new ArrayList<>();
        for (Object[] object : resultList) {
            result.add(
                    ExpiringMarkResult.builder()
                            .fileId(new CFileId((String) object[0], (String) object[1], ((Number) object[2]).intValue(), ((Number) object[3]).intValue()))
                            .title((String) object[4])
                            .statusName((String) object[7])
                            .expirationDate((Date) object[9])
                            .build()
            );

        }
        return result;
    }
}
