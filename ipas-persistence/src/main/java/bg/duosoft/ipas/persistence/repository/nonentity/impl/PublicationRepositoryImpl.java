package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.persistence.model.nonentity.PublicationInfoResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.PublicationRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PublicationRepositoryImpl extends BaseRepositoryCustomImpl implements PublicationRepository {

    @Override
    public List<PublicationInfoResult> selectPublications(String filingNumber) {
        Query query = em.createNativeQuery("SELECT j.CODE, j.PUBLICATION_DATE, nd.NAME\n" +
                "FROM EXT_JOURNAL.JOURNAL_ELEMENT e\n" +
                "         JOIN EXT_JOURNAL.JOURNAL j on e.JOURNAL_NBR = j.JOURNAL_NBR\n" +
                "         join IPASPROD.IP_ACTION a on e.ACTION_PROC_NBR = a.PROC_NBR and e.ACTION_PROC_TYPE = a.PROC_TYP and e.ACTION_ACTION_NBR = a.ACTION_NBR or e.ACTIONUDOC_PROC_NBR = a.PROC_NBR and e.ACTIONUDOC_PROC_TYPE = a.PROC_TYP and e.ACTIONUDOC_ACTION_NBR = a.ACTION_NBR\n" +
                "         JOIN EXT_JOURNAL.JOURNAL_STRUCT_NODE n on e.JOURNAL_STRUCT_NODE_NBR = n.NODE_NBR\n" +
                "         JOIN EXT_JOURNAL.NODE_DEFINITION nd on n.NODE_DEF_NBR = nd.NODE_DEF_NBR\n" +
                "         JOIN EXT_JOURNAL.NODE_DEFINITION_SUB nds on nd.NODE_DEF_NBR = nds.NODE_DEF_NBR and nds.ACTION_TYP=a.ACTION_TYP\n" +
                "where e.NAME = :filingNumber\n" +
                "  and nds.FILE_TYP = :fileTyp ORDER BY j.PUBLICATION_DATE");

        int fileTypIndex = filingNumber.indexOf("/");
        String fileTyp = filingNumber.substring(fileTypIndex + 1, fileTypIndex + 2);
        query.setParameter("filingNumber", filingNumber);
        query.setParameter("fileTyp", fileTyp);


        List<Object[]> resultList = query.getResultList();
        return resultList.stream()
                .map(objects -> {
                    PublicationInfoResult publicationInfo = new PublicationInfoResult();
                    publicationInfo.setJournalCode((String) objects[0]);
                    publicationInfo.setPublicationDate((Date) objects[1]);
                    publicationInfo.setDefinition((String) objects[2]);
                    return publicationInfo;
                })
                .collect(Collectors.toList());
    }
}
