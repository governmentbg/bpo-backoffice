package bg.duosoft.ipas.core.service.impl.search;

import bg.duosoft.ipas.core.model.search.CSearchParam;
import bg.duosoft.ipas.core.model.util.IpDocSearchResult;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.util.search.SearchUtil;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class IpDocServiceImpl
        extends SearchServiceImpl<IpDocSearchResult, CSearchParam> {

    @Override
    public Query getQuery(CSearchParam searchParam) {
        BooleanQuery.Builder boolQuery = new BooleanQuery.Builder();

        if (
                   (searchParam.getFromRequestForValidationNbr() != null && !searchParam.getFromRequestForValidationNbr().isEmpty())
                || (searchParam.getToRequestForValidationNbr() != null && !searchParam.getToRequestForValidationNbr().isEmpty())
                || (searchParam.getFromRequestForValidationDate() != null || searchParam.getToRequestForValidationDate() != null)
            ) {

            QueryBuilder ipDocQueryBuilder = hibernateSearchService.getQueryBuilder(IpDoc.class);

            if (searchParam.getFromRequestForValidationDate() != null && searchParam.getToRequestForValidationDate() != null ) {
                Query query = SearchUtil.getQueryRange(
                        ipDocQueryBuilder,
                        SearchUtil.FIELD_FILING_DATE,
                        searchParam.getFromRequestForValidationDate(),
                        searchParam.getToRequestForValidationDate()
                );
                boolQuery.add(query, BooleanClause.Occur.MUST);
            }

            if (searchParam.getFromRequestForValidationDate() != null && searchParam.getToRequestForValidationDate() == null) {
                Query query = SearchUtil.getQueryAbove(
                        ipDocQueryBuilder,
                        SearchUtil.FIELD_FILING_DATE,
                        searchParam.getFromRequestForValidationDate());
                boolQuery.add(query, BooleanClause.Occur.MUST);
            }

            if (searchParam.getFromRequestForValidationDate() == null && searchParam.getToRequestForValidationDate() != null) {
                Query query = SearchUtil.getQueryBelow(
                        ipDocQueryBuilder,
                        SearchUtil.FIELD_FILING_DATE,
                        searchParam.getFromRequestForValidationDate());
                boolQuery.add(query, BooleanClause.Occur.MUST);
            }

            if (searchParam.getFromRequestForValidationNbr() != null && !searchParam.getFromRequestForValidationNbr().isEmpty()
                && searchParam.getToRequestForValidationNbr() != null && !searchParam.getToRequestForValidationNbr().isEmpty()) {
                Query ipDocQuery = SearchUtil.getQueryRange(ipDocQueryBuilder,
                        SearchUtil.FIELD_DOC_SEQ_NBR,
                        Integer.valueOf(searchParam.getFromRequestForValidationNbr()),
                        Integer.valueOf(searchParam.getToRequestForValidationNbr()));
                boolQuery.add(ipDocQuery, BooleanClause.Occur.MUST);
            }

            if (searchParam.getFromRequestForValidationNbr() != null && !searchParam.getFromRequestForValidationNbr().isEmpty()
                    && (searchParam.getToRequestForValidationNbr() == null || searchParam.getToRequestForValidationNbr().isEmpty())) {
                Query ipDocQuery = SearchUtil.getQueryAbove(ipDocQueryBuilder,
                        SearchUtil.FIELD_DOC_SEQ_NBR,
                        Integer.valueOf(searchParam.getFromRequestForValidationNbr()));
                boolQuery.add(ipDocQuery, BooleanClause.Occur.MUST);
            }

            if ((searchParam.getFromRequestForValidationNbr() == null || searchParam.getFromRequestForValidationNbr().isEmpty())
                    && searchParam.getToRequestForValidationNbr() != null && !searchParam.getToRequestForValidationNbr().isEmpty()) {
                Query ipDocQuery = SearchUtil.getQueryBelow(ipDocQueryBuilder,
                        SearchUtil.FIELD_DOC_SEQ_NBR,
                        Integer.valueOf(searchParam.getToRequestForValidationNbr()));
                boolQuery.add(ipDocQuery, BooleanClause.Occur.MUST);
            }

            if (searchParam.getRequestForValidationType() != null) {
                Query ipDocQuery = SearchUtil.getTermQuery(
                        SearchUtil.FIELD_DOC_SEQ_TYP_DOC_SEQ_TYPE_CUSTOM,
                        searchParam.getRequestForValidationType());
                boolQuery.add(ipDocQuery, BooleanClause.Occur.MUST);
            }
        }

        Query query = boolQuery.build();

        return query;
    }

    public Page<IpDocSearchResult> search(Query luceneQuery, SearchPage searchPage) {
        QueryBuilder qb = hibernateSearchService.getQueryBuilder(IpPatent.class);
        FullTextQuery jpaQuery = hibernateSearchService.getFullTextQuery(luceneQuery, IpDoc.class);

        jpaQuery.setProjection(SearchUtil.FIELD_PK,
                SearchUtil.FIELD_DOC_SEQ_NBR);

        List resultList = jpaQuery.getResultList();

        List<IpDocSearchResult> ipos = new ArrayList<>(resultList.size());
        for (Object obj : resultList) {
            Object[] arrObj = (Object[]) obj;

            IpDocSearchResult el = new IpDocSearchResult();
            IpDocPK ipDocPK = (IpDocPK) arrObj[0];

            StringBuilder sb = new StringBuilder();
            sb.append(ipDocPK.getDocOri());
            sb.append("/");
            sb.append(ipDocPK.getDocLog());
            sb.append("/");
            sb.append(ipDocPK.getDocSer());
            sb.append("/");
            sb.append(ipDocPK.getDocNbr());

            el.pk(sb.toString())
                    .docSeqNbr(((Integer) arrObj[1]).toString());

            ipos.add(el);
        }

        org.springframework.data.domain.Pageable springPage = PageRequest.of(0,
                0 == resultList.size()?1:resultList.size());

        return new PageImpl<>(ipos, springPage, jpaQuery.getResultSize());
    }
}
