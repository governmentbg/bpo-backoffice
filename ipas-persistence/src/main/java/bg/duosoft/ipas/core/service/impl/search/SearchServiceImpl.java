package bg.duosoft.ipas.core.service.impl.search;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.service.search.SearchService;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.core.model.search.SearchActionParam;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.util.search.SearchUtil;
import bg.duosoft.logging.annotation.LogExecutionArguments;
import bg.duosoft.logging.annotation.LogExecutionTime;
import bg.duosoft.logging.annotation.LoggingLevel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bg.duosoft.ipas.util.search.SearchUtil.FIELD_CAPTURE_USER;
import static bg.duosoft.ipas.util.search.SearchUtil.FIELD_RESPONSIBLE_USER;

@Transactional
abstract class SearchServiceImpl<SR, SP extends SearchPage>
        implements SearchService<SR, SP> {

    @Autowired
    protected HibernateSearchService hibernateSearchService;

    public abstract Query getQuery(SP searchParam);

    public abstract Page<SR> search(Query luceneQuery, SearchPage searchPage);
    public Page<SR> search(SP searchParam) {
        Query query = getQuery(searchParam);
        return search(query, searchParam);
    }

    protected Optional<Query> creteActionQuery(SearchActionParam searchActionParam) {
        BooleanQuery.Builder res = new BooleanQuery.Builder();
        List<Query> parts = Stream.of(
                createActionDateQuery(searchActionParam.getFromActionDate(), searchActionParam.getToActionDate()),
                createActionTypeQuery(searchActionParam),
                createActionCaptureUserQuery(searchActionParam.getActionCaptureUserId()),
                createActionResponsibleUserQuery(searchActionParam.getActionResponsibleUserId())
        )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(parts)) {
            return Optional.empty();
        } else {
            parts.forEach(r -> res.add(r, BooleanClause.Occur.MUST));
            return Optional.of(res.build());
        }
    }

    private Optional<Query> createActionTypeQuery(SearchActionParam searchActionParam) {
        if (!CollectionUtils.isEmpty(searchActionParam.getActionTypes())) {
            BooleanQuery.Builder boolQueryActionType = new BooleanQuery.Builder();
            for (String actionTyp : searchActionParam.getActionTypes()) {
                Query queryActionType = hibernateSearchService.getQueryBuilder(IpAction.class)
                        .keyword()
                        .onField(SearchUtil.FIELD_ACTION_TYP)
                        .matching(actionTyp)
                        .createQuery();
                boolQueryActionType.add(queryActionType, BooleanClause.Occur.SHOULD);
            }
            return Optional.of(boolQueryActionType.build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<Query> createActionDateQuery(Date fromDate, Date toDate) {
        if (fromDate != null || toDate != null) {
            if (fromDate == null) {
                fromDate = new Date();
            }
            if (toDate == null) {
                toDate = new Date();
            }
            Query query = SearchUtil.getQueryRange(
                    hibernateSearchService.getQueryBuilder(IpAction.class),
                    SearchUtil.FIELD_NEW_STATUS_DATE,
                    fromDate,
                    toDate);
            return Optional.of(query);
        } else {
            return Optional.empty();
        }
    }
    private Optional<Query> createActionResponsibleUserQuery(Integer actionResponsibleUserId) {
        if (actionResponsibleUserId != null) {
            Query actionResponsibleUser = SearchUtil.getQueryRange(hibernateSearchService.getQueryBuilder(IpAction.class),
                    FIELD_RESPONSIBLE_USER,
                    actionResponsibleUserId,
                    actionResponsibleUserId
            );
            return Optional.of(actionResponsibleUser);
        } else {
            return Optional.empty();
        }
    }
    private Optional<Query> createActionCaptureUserQuery(Integer actionCaptureUserId) {
        if (actionCaptureUserId != null) {
            Query actionCaptureUser = SearchUtil.getQueryRange(hibernateSearchService.getQueryBuilder(IpAction.class),
                    FIELD_CAPTURE_USER,
                    actionCaptureUserId,
                    actionCaptureUserId
            );
            return Optional.of(actionCaptureUser);
        } else {
            return Optional.empty();
        }
    }

    public org.springframework.data.domain.Pageable toSpringPage(Pageable pageable) {
        if (pageable.getPage() != null && pageable.getPageSize() != null && !pageable.getPageSize().equals(0) ) {
            org.springframework.data.domain.Pageable springPage = PageRequest.of(pageable.getPage(), pageable.getPageSize());
            return springPage;
        }

        return null;
    }

    protected Optional<Query> createFromToCriteria(org.hibernate.search.query.dsl.QueryBuilder qb, String fieldName, Integer from, Integer to) {
        Query query = null;
        if (from != null && to == null) {
            query = SearchUtil.getQueryAbove(qb,
                    fieldName,
                    from);
        } else if (from == null && to != null) {
            query = SearchUtil.getQueryBelow(qb,
                    fieldName,
                    to);
        } else if (from != null && to != null) {
            query = SearchUtil.getQueryRange(qb,
                    fieldName,
                    from,
                    to);
        }
        return Optional.ofNullable(query);
    }


    protected List<String> getFieldsValue(Document doc, String fieldName) {
        return Arrays.stream(doc.getValues(fieldName)).collect(Collectors.toList());
//        return Arrays.stream(doc.getFields(fieldName)).map(r -> r.stringValue()).collect(Collectors.toList());
    }
}
