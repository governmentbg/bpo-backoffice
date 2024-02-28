package bg.duosoft.ipas.core.service.impl.search;

import bg.duosoft.ipas.core.model.util.AutocompleteIpoSearchResult;
import bg.duosoft.ipas.core.service.search.AutocompleteIpoSearchService;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.core.model.search.AutocompleteIpoSearchParam;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.util.search.SearchUtil;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AutocompleteIpoSearchServiceImpl
        extends SearchServiceImpl<AutocompleteIpoSearchResult, AutocompleteIpoSearchParam>
        implements AutocompleteIpoSearchService {
    @Override
    public Query getQuery(AutocompleteIpoSearchParam searchParam) {
        QueryBuilder qb = hibernateSearchService.getQueryBuilder(IpPatent.class);
        BooleanQuery.Builder boolQuery = new BooleanQuery.Builder();

        List<String> fileTypes = searchParam.getFileTypes();


        if (searchParam.getFileSeq() != null && !searchParam.getFileSeq().isEmpty()) {
            Query query = SearchUtil.getQueryByPhrase(qb, SearchUtil.FIELD_PK_FILE_SEQ, searchParam.getFileSeq());
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (fileTypes != null && !fileTypes.isEmpty()) {
            BooleanQuery.Builder fileTypeBoolQuery = new BooleanQuery.Builder();
            for (String fileType : fileTypes) {
                fileTypeBoolQuery.add(SearchUtil.getTermQuery(SearchUtil.FIELD_PK_FILE_TYP, fileType.toUpperCase()), BooleanClause.Occur.SHOULD);
            }

            boolQuery.add(fileTypeBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (searchParam.getFileSer() != null && !searchParam.getFileSer().isEmpty()) {
            Query query = SearchUtil.getQueryRange(qb,
                    SearchUtil.FIELD_PK_FILE_SER,
                    Integer.valueOf(searchParam.getFileSer()),
                    Integer.valueOf(searchParam.getFileSer()));

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getFileNbr() != null && !searchParam.getFileNbr().isEmpty()) {
            Query query = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_PK_FILE_NBR_STR,
                    "*" + searchParam.getFileNbr());

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getRegistrationNbr() != null && !searchParam.getRegistrationNbr().isEmpty()) {
            Query query = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_FILE_REGISTRATION_NBR_SORT,
                    "*" + searchParam.getRegistrationNbr()
            );
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        Query query = boolQuery.build();

        return query;
    }

    @Override
    public Page<AutocompleteIpoSearchResult> search(Query luceneQuery, SearchPage searchPage) {
        QueryBuilder qb = hibernateSearchService.getQueryBuilder(IpPatent.class);
        FullTextQuery jpaQuery = hibernateSearchService.getFullTextQuery(luceneQuery, IpMark.class, IpPatent.class);

        if (searchPage.getSortColumn() != null) {
            Sort sort = SearchUtil.getSort(qb, searchPage);
            jpaQuery.setSort(sort);
        }

        Pageable pageable = toSpringPage(searchPage);
        if (pageable != null) {
            jpaQuery.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
            jpaQuery.setMaxResults(searchPage.getPageSize());
        }

        jpaQuery.setProjection(SearchUtil.FIELD_PK_FILE_SEQ,
                SearchUtil.FIELD_PK_FILE_TYP,
                SearchUtil.FIELD_PK_FILE_SER,
                SearchUtil.FIELD_PK_FILE_NBR,
                SearchUtil.FIELD_FILE_TITLE_SORT,
                SearchUtil.FIELD_FILE_FILING_DATA_SORT,
                SearchUtil.FIELD_FILE_REGISTRATION_DATE,
                SearchUtil.FIELD_FILE_REGISTRATION_NBR_SORT,
                SearchUtil.FIELD_FILE_REGISTRATION_DUP,
                SearchUtil.FIELD_FILE_ENTITLEMENT_DATE,
                SearchUtil.FIELD_FILE_EXPIRATION_DATE,
                SearchUtil.FIELD_FILE_STATUS_CODE,
                SearchUtil.FIELD_FILE_PROC_TYP,
                SearchUtil.FIELD_FILE_PROC_NBR,
                SearchUtil.FIELD_SIGN_WCODE);

        List resultList = jpaQuery.getResultList();

        List<AutocompleteIpoSearchResult> ipos = new ArrayList<>(resultList.size());
        for (Object obj : resultList) {
            Object[] arrObj = (Object[]) obj;

            AutocompleteIpoSearchResult el = new AutocompleteIpoSearchResult();

            el.pk((String) arrObj[0], (String) arrObj[1], (Integer) arrObj[2], (Integer) arrObj[3])
                    .title((String) arrObj[4])
                    .filingDate((Date) arrObj[5])
                    .registrationDate((Date) arrObj[6])
                    .registrationNbr((Integer) arrObj[7])
                    .registrationDup((String) arrObj[8])
                    .entitlementDate((Date) arrObj[9])
                    .expirationDate((Date) arrObj[10])
                    .statusCode((String) arrObj[11])
                    .procTyp((Integer) arrObj[12])
                    .procNbr((Integer) arrObj[13])
                    .signWcode((String)arrObj[14]);

            ipos.add(el);
        }

        return new PageImpl<>(ipos, toSpringPage(searchPage), jpaQuery.getResultSize());
    }
}
