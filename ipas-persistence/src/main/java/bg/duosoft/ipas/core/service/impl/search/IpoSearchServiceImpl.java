package bg.duosoft.ipas.core.service.impl.search;

import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.model.search.CSearchParam;
import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.core.model.util.IpDocSearchResult;
import bg.duosoft.ipas.core.model.util.PersonAddressResult;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.core.service.search.PersonAddressSearchService;
import bg.duosoft.ipas.enums.SearchResultColumn;
import bg.duosoft.ipas.enums.SearchSortType;
import bg.duosoft.ipas.enums.search.PersonNameSearchType;
import bg.duosoft.ipas.enums.search.SearchOperatorTextType;
import bg.duosoft.ipas.enums.search.SearchOperatorType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogoViennaClasses;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkAttachmentViennaClasses;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClasses;
import bg.duosoft.ipas.persistence.model.entity.patent.*;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.util.search.CustomKeyValueComparatorSource;
import bg.duosoft.ipas.util.search.IpMarkNiceClassesPkBridge;
import bg.duosoft.ipas.util.search.SearchUtil;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.*;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static bg.duosoft.ipas.util.search.SearchUtil.FIELD_RESPONSIBLE_USER;

@Service
@Transactional
@Slf4j
public class IpoSearchServiceImpl
        extends SearchServiceImpl<CSearchResult, CSearchParam>
        implements IpoSearchService {

    @Autowired
    private StatusService statusService;

    @Autowired
    private IpDocServiceImpl ipDocService;
    @Autowired
    private PersonAddressSearchService personAddressSearchService;

    @Override
    public Query getQuery(CSearchParam searchParam) {
        QueryBuilder qb = hibernateSearchService.getQueryBuilder(IpPatent.class);
        BooleanQuery.Builder boolQuery = new BooleanQuery.Builder();

        List<String> fileTypes = searchParam.getFileTypes();

        if (fileTypes != null && !fileTypes.isEmpty()) {
            BooleanQuery.Builder fileTypeBoolQuery = new BooleanQuery.Builder();
            for (String fileType : fileTypes) {
                fileTypeBoolQuery.add(SearchUtil.getTermQuery(SearchUtil.FIELD_PK_FILE_TYP, fileType.toUpperCase()), BooleanClause.Occur.SHOULD);
            }

            boolQuery.add(fileTypeBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (!Objects.isNull(searchParam.getApplTyp()) && !searchParam.getApplTyp().isBlank()) {
            Query termQuery = SearchUtil.getTermQuery("file.applTyp", searchParam.getApplTyp());
            boolQuery.add(termQuery, BooleanClause.Occur.MUST);
        }

        if (!Objects.isNull(searchParam.getSubApplTyp()) && !searchParam.getSubApplTyp().isBlank()) {
            Query termQuery = SearchUtil.getTermQuery("file.applSubtyp", searchParam.getSubApplTyp());
            boolQuery.add(termQuery, BooleanClause.Occur.MUST);
        }

        if (searchParam.getTitle() != null && !searchParam.getTitle().isEmpty()) {
            if (searchParam.getTitleTypeSearch() == SearchOperatorTextType.CONTAINS_STRING) {
                String titleNorm = SearchUtil.normString(searchParam.getTitle());

                Query query = SearchUtil.getQueryWildcard(
                        qb,
                        SearchUtil.FIELD_FILE_TITLE_CUSTOM,
                        "*" + titleNorm + "*");

                boolQuery.add(query, BooleanClause.Occur.MUST);
            } else if (searchParam.getTitleTypeSearch() == SearchOperatorTextType.KEYWORDS) {
                Query query = SearchUtil.getQueryByPhrase(qb, SearchUtil.FIELD_FILE_TITLE, searchParam.getTitle());

                boolQuery.add(query, BooleanClause.Occur.MUST);
            } else {
                Query query = SearchUtil.getTermQuery(SearchUtil.FIELD_FILE_TITLE,  "*");
                boolQuery.add(query, BooleanClause.Occur.MUST);
            }
        }

        if (searchParam.getEnglishTitle() != null && !searchParam.getEnglishTitle().isEmpty()) {
            if (searchParam.getEnglishTitleTypeSearch() == SearchOperatorTextType.CONTAINS_STRING) {
                String titleNorm = SearchUtil.normString(searchParam.getEnglishTitle());

                Query query = SearchUtil.getQueryWildcard(
                        qb,
                        SearchUtil.FIELD_FILE_ENGLISH_TITLE_CUSTOM,
                        "*" + titleNorm + "*");

                boolQuery.add(query, BooleanClause.Occur.MUST);
            } else if (searchParam.getEnglishTitleTypeSearch() == SearchOperatorTextType.KEYWORDS) {
                Query query = SearchUtil.getQueryByPhrase(qb, SearchUtil.FIELD_FILE_ENGLISH_TITLE, searchParam.getEnglishTitle());

                boolQuery.add(query, BooleanClause.Occur.MUST);
            } else {
                Query query = SearchUtil.getTermQuery(SearchUtil.FIELD_FILE_ENGLISH_TITLE,  "*");
                boolQuery.add(query, BooleanClause.Occur.MUST);
            }
        }

        if (searchParam.getFileSeq() != null && !searchParam.getFileSeq().isEmpty()) {
            Query query = SearchUtil.getQueryByPhrase(qb, SearchUtil.FIELD_PK_FILE_SEQ, searchParam.getFileSeq());
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if ((searchParam.getFromFileSer() != null && !searchParam.getFromFileSer().isEmpty())
                || (searchParam.getToFileSer() != null && !searchParam.getToFileSer().isEmpty())) {

            Query query = SearchUtil.getTermQuery(SearchUtil.FIELD_PK_FILE_SER, "0");
            try {
                if ((searchParam.getFromFileSer() != null && !searchParam.getFromFileSer().isEmpty())
                        && (searchParam.getToFileSer() == null || searchParam.getToFileSer().isEmpty())) {
                    Integer fromNbr = Integer.valueOf(searchParam.getFromFileSer().trim());

                    query = SearchUtil.getQueryAbove(qb,
                            SearchUtil.FIELD_PK_FILE_SER,
                            fromNbr);
                }
                if ((searchParam.getFromFileSer() == null || searchParam.getFromFileSer().isEmpty())
                        && (searchParam.getToFileSer() != null && !searchParam.getToFileSer().isEmpty())) {
                    Integer toNbr = Integer.valueOf(searchParam.getToFileSer().trim());

                    query = SearchUtil.getQueryBelow(qb,
                            SearchUtil.FIELD_PK_FILE_SER,
                            toNbr);
                }

                if ((searchParam.getFromFileSer() != null && !searchParam.getFromFileSer().isEmpty())
                        && (searchParam.getToFileSer() != null && !searchParam.getToFileSer().isEmpty())) {
                    Integer fromNbr = Integer.valueOf(searchParam.getFromFileSer().trim());
                    Integer toNbr = Integer.valueOf(searchParam.getToFileSer().trim());

                    query = SearchUtil.getQueryRange(qb,
                            SearchUtil.FIELD_PK_FILE_SER,
                            fromNbr,
                            toNbr);
                }

            } catch (NumberFormatException e) {
                log.warn("Cannot parse value....", e);
            }

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if ((searchParam.getFromFileNbr() != null && !searchParam.getFromFileNbr().isEmpty())
                || (searchParam.getToFileNbr() != null && !searchParam.getToFileNbr().isEmpty())) {
            Query query = SearchUtil.getTermQuery(SearchUtil.FIELD_PK_FILE_SER, "0");
            try {
                if ((searchParam.getFromFileNbr() != null && !searchParam.getFromFileNbr().isEmpty())
                        && (searchParam.getToFileNbr() == null || searchParam.getToFileNbr().isEmpty())) {
                    Integer fromNbr = Integer.valueOf(searchParam.getFromFileNbr().trim());

                    query = SearchUtil.getQueryAbove(qb,
                            SearchUtil.FIELD_PK_FILE_NBR,
                            fromNbr);
                }
                if ((searchParam.getFromFileNbr() == null || searchParam.getFromFileNbr().isEmpty())
                        && (searchParam.getToFileNbr() != null && !searchParam.getToFileNbr().isEmpty())) {
                    Integer toNbr = Integer.valueOf(searchParam.getToFileNbr().trim());

                    query = SearchUtil.getQueryBelow(qb,
                            SearchUtil.FIELD_PK_FILE_NBR,
                            toNbr);
                }

                if ((searchParam.getFromFileNbr() != null && !searchParam.getFromFileNbr().isEmpty())
                        && (searchParam.getToFileNbr() != null && !searchParam.getToFileNbr().isEmpty())) {
                    Integer fromNbr = Integer.valueOf(searchParam.getFromFileNbr().trim());
                    Integer toNbr = Integer.valueOf(searchParam.getToFileNbr().trim());

                    query = SearchUtil.getQueryRange(qb,
                            SearchUtil.FIELD_PK_FILE_NBR,
                            fromNbr,
                            toNbr);
                }

            } catch (NumberFormatException e) {
            }

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getFromFilingDate() != null || searchParam.getToFilingDate() != null) {
            Query query = SearchUtil.getQueryRange(
                    qb,
                    SearchUtil.FIELD_FILE_FILING_DATE,
                    searchParam.getFromFilingDate(),
                    searchParam.getToFilingDate()
            );
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if ((searchParam.getFromRegistrationNbr() != null && !searchParam.getFromRegistrationNbr().isEmpty())
                || (searchParam.getToRegistrationNbr() != null && !searchParam.getToRegistrationNbr().isEmpty())) {

            Query query = SearchUtil.getTermQuery(SearchUtil.FIELD_FILE_REGISTRATION_NBR, "0");
            try {
                if ((searchParam.getFromRegistrationNbr() != null && !searchParam.getFromRegistrationNbr().isEmpty())
                        && (searchParam.getToRegistrationNbr() == null || searchParam.getToRegistrationNbr().isEmpty())) {
                    Integer fromNbr = Integer.valueOf(searchParam.getFromRegistrationNbr().trim());

                    query = SearchUtil.getQueryAbove(qb,
                            SearchUtil.FIELD_FILE_REGISTRATION_NBR,
                            fromNbr);
                }
                if ((searchParam.getFromRegistrationNbr() == null || searchParam.getFromRegistrationNbr().isEmpty())
                        && (searchParam.getToRegistrationNbr() != null && !searchParam.getToRegistrationNbr().isEmpty())) {
                    Integer toNbr = Integer.valueOf(searchParam.getToRegistrationNbr().trim());

                    query = SearchUtil.getQueryBelow(qb,
                            SearchUtil.FIELD_FILE_REGISTRATION_NBR,
                            toNbr);
                }

                if ((searchParam.getFromRegistrationNbr() != null && !searchParam.getFromRegistrationNbr().isEmpty())
                        && (searchParam.getToRegistrationNbr() != null && !searchParam.getToRegistrationNbr().isEmpty())) {
                    Integer fromNbr = Integer.valueOf(searchParam.getFromRegistrationNbr().trim());
                    Integer toNbr = Integer.valueOf(searchParam.getToRegistrationNbr().trim());

                    query = SearchUtil.getQueryRange(qb,
                            SearchUtil.FIELD_FILE_REGISTRATION_NBR,
                            fromNbr,
                            toNbr);
                }

            } catch (NumberFormatException e) {
            }

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getFromRegistrationDate() != null || searchParam.getToRegistrationDate() != null) {
            Query query = SearchUtil.getQueryRange(
                    qb,
                    SearchUtil.FIELD_FILE_REGISTRATION_DATE,
                    searchParam.getFromRegistrationDate(),
                    searchParam.getToRegistrationDate()
            );
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        createFromToCriteria(hibernateSearchService.getQueryBuilder(IpMark.class), SearchUtil.FIELD_MARK_INTERNATIONAL_REGISTRATION_NBR, parseInteger(searchParam.getFromInternationalRegistrationNbr()), parseInteger(searchParam.getToInternationalRegistrationNbr())).ifPresent(q -> boolQuery.add(q, BooleanClause.Occur.MUST));


        if (searchParam.getFromExpirationDate() != null || searchParam.getToExpirationDate() != null) {
            Query query = SearchUtil.getQueryRange(
                    qb,
                    SearchUtil.FIELD_FILE_EXPIRATION_DATE,
                    searchParam.getFromExpirationDate(),
                    searchParam.getToExpirationDate()
            );
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getFromEntitlementDate() != null || searchParam.getToEntitlementDate() != null) {
            Query query = SearchUtil.getQueryRange(
                    qb,
                    SearchUtil.FIELD_FILE_ENTITLEMENT_DATE,
                    searchParam.getFromEntitlementDate(),
                    searchParam.getToEntitlementDate()
            );
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        addPersonNameQuery(searchParam.getOwnerName(), searchParam.getOwnerNameTypeSearch(), SearchUtil.FIELD_OWNER_IP_PERSON_ADDRESSES_IP_PERSON_PERSON_NBR).ifPresent(q -> boolQuery.add(q, BooleanClause.Occur.MUST));
        addPersonNameQuery(searchParam.getServicePersonName(), searchParam.getServicePersonNameTypeSearch(), SearchUtil.FIELD_SERVICE_PERSON_IP_PERSON_PERSON_NBR).ifPresent(q -> boolQuery.add(q, BooleanClause.Occur.MUST));
        addPersonNameQuery(searchParam.getInventorName(), searchParam.getInventorNameTypeSearch(), SearchUtil.FIELD_IP_PATENT_INVENTORS_IP_PERSON_ADDRESSES_IP_PERSON_PERSON_NBR).ifPresent(q -> boolQuery.add(q, BooleanClause.Occur.MUST));
        addPersonNameQuery(searchParam.getRepresentativeName(), searchParam.getRepresentativeNameTypeSearch(), SearchUtil.FIELD_REPRESENTATIVES_IP_PERSON_ADDRESSES_IP_PERSON_PERSON_NBR).ifPresent(q -> boolQuery.add(q, BooleanClause.Occur.MUST));

        if (searchParam.getStatusCodes() != null && searchParam.getStatusCodes().size() > 0) {
            if (searchParam.getFromStatusDate() != null || searchParam.getToStatusDate() != null) {
                Date fromDate = new Date();
                if (searchParam.getFromStatusDate() != null) {
                    fromDate = searchParam.getFromStatusDate();
                }

                Date toDate = new Date();
                if (searchParam.getToStatusDate() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(searchParam.getToStatusDate());
                    c.add(Calendar.DATE, 1);
                    toDate = c.getTime();
                }

                BooleanQuery.Builder statusBoolQuery = new BooleanQuery.Builder();
                // prior status code and prior date
                for (String statusCode : searchParam.getStatusCodes()) {
                    Query queryPrior = getQueryByFieldAndDateRange(
                            SearchUtil.FIELD_PRIOR_STATUS_CODE_PK_STATUS_CODE,
                            statusCode,
                            SearchUtil.FIELD_PRIOR_STATUS_DATE,
                            fromDate,
                            toDate);
                    statusBoolQuery.add(queryPrior, BooleanClause.Occur.SHOULD);
                }

                // new status code and action date
                for (String statusCode : searchParam.getStatusCodes()) {
                    Query queryNew = getQueryByFieldAndDateRange(
                            SearchUtil.FIELD_NEW_STATUS_CODE_PK_STATUS_CODE,
                            statusCode,
                            SearchUtil.FIELD_NEW_STATUS_DATE,
                            fromDate,
                            toDate);
                    statusBoolQuery.add(queryNew, BooleanClause.Occur.SHOULD);
                }

                Query queryStatus = statusBoolQuery.build();

                Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), queryStatus, SearchUtil.FIELD_PROC_PK, SearchUtil.FIELD_FILE_PROC_PK, IpAction.class);

                boolQuery.add(joinQuery, BooleanClause.Occur.MUST);

            } else {

                BooleanQuery.Builder statusBoolQuery = new BooleanQuery.Builder();
                // prior status code and prior date
                for (String statusCode : searchParam.getStatusCodes()) {

                    Query queryStatus = hibernateSearchService.getQueryBuilder(IpProc.class)
                            .keyword()
                            .onField(SearchUtil.FIELD_STATUS_CODE_PK_STATUS_CODE)
                            .matching(statusCode)
                            .createQuery();
                    statusBoolQuery.add(queryStatus, BooleanClause.Occur.SHOULD);
                }
                Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), statusBoolQuery.build(), SearchUtil.FIELD_PROC_PK, SearchUtil.FIELD_FILE_PROC_PK, IpProc.class);

                boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
            }
        }

        if (!Objects.isNull(searchParam.getResponsibleUserId()) ) {
            Query queryStatus = hibernateSearchService.getQueryBuilder(IpProc.class)
                    .range()
                    .onField(FIELD_RESPONSIBLE_USER)
                    .from(searchParam.getResponsibleUserId())
                    .to(searchParam.getResponsibleUserId())
                    .createQuery();
            Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), queryStatus, SearchUtil.FIELD_PROC_PK, SearchUtil.FIELD_FILE_PROC_PK, IpProc.class);

            boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
        }
        creteActionQuery(searchParam).ifPresent(q -> boolQuery.add(SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), q, SearchUtil.FIELD_PROC_PK, SearchUtil.FIELD_FILE_PROC_PK, IpAction.class), BooleanClause.Occur.MUST));


        addNationalityCountry(searchParam.getOwnerNationality(), SearchUtil.FIELD_OWNER_IP_PERSON_ADDRESSES_IP_PERSON_PERSON_NBR).ifPresent(r -> boolQuery.add(r, BooleanClause.Occur.MUST));
        addNationalityCountry(searchParam.getServicePersonNationality(), SearchUtil.FIELD_SERVICE_PERSON_IP_PERSON_PERSON_NBR).ifPresent(r -> boolQuery.add(r, BooleanClause.Occur.MUST));


        if (searchParam.getAgentCode() != null && !searchParam.getAgentCode().isEmpty()) {
            Query queryPersonByAgentCode = SearchUtil.getTermQuery(SearchUtil.FIELD_IP_PERSON_IP_AGENT_AGENT_CODE_EXACT, searchParam.getAgentCode());

            Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), queryPersonByAgentCode, SearchUtil.FIELD_IP_PERSON_PERSON_NBR, SearchUtil.FIELD_REPRESENTATIVES_IP_PERSON_ADDRESSES_IP_PERSON_PERSON_NBR, IpPersonAddresses.class);

            boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
        }

        if (searchParam.getPatentSummary() != null && !searchParam.getPatentSummary().isEmpty()) {


            if (searchParam.getPatentSummaryTypeSearch() == SearchOperatorTextType.CONTAINS_STRING) {
                String summaryNorm = SearchUtil.normString(searchParam.getPatentSummary());

                Query summaryQuery = SearchUtil.getQueryWildcard(
                        hibernateSearchService.getQueryBuilder(IpPatentSummary.class),
                        SearchUtil.FIELD_SUMMARY_CUSTOM,
                        "*" + summaryNorm + "*");

                Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), summaryQuery, SearchUtil.FIELD_FILE_PK, SearchUtil.FIELD_FILE_PK_SORT, IpPatentSummary.class);

                boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
            } else if (searchParam.getPatentSummaryTypeSearch() == SearchOperatorTextType.KEYWORDS) {

                String summary = SearchUtil.normString(searchParam.getPatentSummary());
                String[] summarySplit = summary.split(" ");
                BooleanQuery.Builder summaryBoolQuery = new BooleanQuery.Builder();

                for (String str:summarySplit) {

                    Query query = hibernateSearchService.getQueryBuilder(IpPatentSummary.class)
                            .phrase()
                            .onField(SearchUtil.FIELD_SUMMARY)
                            .sentence(str)
                            .createQuery();

                    if (!query.toString().equals("")) {
                        summaryBoolQuery.add(query, BooleanClause.Occur.MUST);
                    }
                }

                Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), summaryBoolQuery.build(), SearchUtil.FIELD_FILE_PK, SearchUtil.FIELD_FILE_PK_SORT, IpPatentSummary.class);

                boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
            } else {
                Query query = SearchUtil.getTermQuery(SearchUtil.FIELD_FILE_TITLE,  "*");
                boolQuery.add(query, BooleanClause.Occur.MUST);
            }
        }

        if ((searchParam.getPublicationYear() != null && !searchParam.getPublicationYear().isEmpty())
                || (searchParam.getPublicationBulletin() != null && !searchParam.getPublicationBulletin().isEmpty())
                || (searchParam.getPublicationSect() != null && !searchParam.getPublicationSect().isEmpty())) {

            BooleanQuery.Builder publicationBoolQuery = new BooleanQuery.Builder();

            if (searchParam.getPublicationYear() != null && !searchParam.getPublicationYear().isEmpty()) {

                Query actionQueryByPublicationYear = hibernateSearchService.getQueryBuilder(IpAction.class)
                        .keyword()
                        .onField(SearchUtil.FIELD_VW_JOURNAL_YEAR)
                        .matching(searchParam.getPublicationYear())
                        .createQuery();
                publicationBoolQuery.add(actionQueryByPublicationYear, BooleanClause.Occur.MUST);
            }

            if (searchParam.getPublicationBulletin() != null && !searchParam.getPublicationBulletin().isEmpty()) {

                Query actionQueryByPublicationYear = hibernateSearchService.getQueryBuilder(IpAction.class)
                        .keyword()
                        .onField(SearchUtil.FIELD_VW_JOURNAL_BULETIN)
                        .matching(searchParam.getPublicationBulletin())
                        .createQuery();
                publicationBoolQuery.add(actionQueryByPublicationYear, BooleanClause.Occur.MUST);
            }

            if (searchParam.getPublicationSect() != null && !searchParam.getPublicationSect().isEmpty()) {

                Query actionQueryByPublicationYear = hibernateSearchService.getQueryBuilder(IpAction.class)
                        .keyword()
                        .onField(SearchUtil.FIELD_VW_JOURNAL_SECT)
                        .matching(searchParam.getPublicationSect())
                        .createQuery();
                publicationBoolQuery.add(actionQueryByPublicationYear, BooleanClause.Occur.MUST);
            }

            Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), publicationBoolQuery.build(), SearchUtil.FIELD_PROC_PK, SearchUtil.FIELD_FILE_PROC_PK, IpAction.class);

            boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
        }

        if (searchParam.getIpcClasses() != null && searchParam.getIpcClasses().size() > 0) {
            BooleanQuery.Builder ipcClassBoolQuery = new BooleanQuery.Builder();

            for (String ipcClass : searchParam.getIpcClasses()) {
                Query patentIpcClassQuery = hibernateSearchService.getQueryBuilder(IpPatentIpcClasses.class)
                        .keyword()
                        .wildcard()
                        .onField(SearchUtil.FIELD_IPC_CODE)
                        .matching(ipcClass)
                        .createQuery();

                Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), patentIpcClassQuery, SearchUtil.FIELD_FILE_PK, SearchUtil.FIELD_FILE_PK_SORT, IpPatentIpcClasses.class);
                switch (searchParam.getIpcClassType()) {
                    case AND_NOT:
                        boolQuery.add(joinQuery, BooleanClause.Occur.MUST_NOT);
                        break;
                    case OR:
                        ipcClassBoolQuery.add(joinQuery, BooleanClause.Occur.SHOULD);
                        break;
                    default:
                        ipcClassBoolQuery.add(joinQuery, BooleanClause.Occur.MUST);
                        break;
                }
            }

            if (searchParam.getIpcClassType() == SearchOperatorType.AND_NOT) {
                boolQuery.add(SearchUtil.getQueryWildcard(qb, SearchUtil.FIELD_PK_FILE_TYP, "*"), BooleanClause.Occur.MUST);
            } else {
                boolQuery.add(ipcClassBoolQuery.build(), BooleanClause.Occur.MUST);
            }
        }


        if (searchParam.getCpcClasses() != null && searchParam.getCpcClasses().size() > 0) {
            BooleanQuery.Builder cpcClassBoolQuery = new BooleanQuery.Builder();

            for (String cpcClass : searchParam.getCpcClasses()) {
                Query patentCpcClassQuery = hibernateSearchService.getQueryBuilder(IpPatentCpcClasses.class)
                        .keyword()
                        .wildcard()
                        .onField(SearchUtil.FIELD_CPC_CODE)
                        .matching(cpcClass)
                        .createQuery();

                Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), patentCpcClassQuery, SearchUtil.FIELD_FILE_PK, SearchUtil.FIELD_FILE_PK_SORT, IpPatentCpcClasses.class);
                switch (searchParam.getCpcClassType()) {
                    case AND_NOT:
                        boolQuery.add(joinQuery, BooleanClause.Occur.MUST_NOT);
                        break;
                    case OR:
                        cpcClassBoolQuery.add(joinQuery, BooleanClause.Occur.SHOULD);
                        break;
                    default:
                        cpcClassBoolQuery.add(joinQuery, BooleanClause.Occur.MUST);
                        break;
                }
            }

            if (searchParam.getCpcClassType() == SearchOperatorType.AND_NOT) {
                boolQuery.add(SearchUtil.getQueryWildcard(qb, SearchUtil.FIELD_PK_FILE_TYP, "*"), BooleanClause.Occur.MUST);
            } else {
                boolQuery.add(cpcClassBoolQuery.build(), BooleanClause.Occur.MUST);
            }
        }

        if (searchParam.getNiceClasses() != null && searchParam.getNiceClasses().size() > 0) {

            BooleanQuery.Builder niceBoolQuery = new BooleanQuery.Builder();

            for (String niceClass : searchParam.getNiceClasses()) {
                Query niceClassQuery = hibernateSearchService.getQueryBuilder(IpMarkNiceClasses.class)
                        .keyword()
                        .wildcard()
                        .onField(SearchUtil.FIELD_PK_NICE_CLASS_CODE)
                        .matching(niceClass)
                        .createQuery();

                Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), niceClassQuery, SearchUtil.FIELD_FILE_PK, SearchUtil.FIELD_FILE_PK_SORT, IpMarkNiceClasses.class);
                switch (searchParam.getNiceClassesType()) {
                    case AND_NOT:
                        boolQuery.add(joinQuery, BooleanClause.Occur.MUST_NOT);
                        break;
                    case OR:
                        niceBoolQuery.add(joinQuery, BooleanClause.Occur.SHOULD);
                        break;
                    default:
                        niceBoolQuery.add(joinQuery, BooleanClause.Occur.MUST);
                        break;
                }
            }

            if (searchParam.getNiceClassesType() == SearchOperatorType.AND_NOT) {
                boolQuery.add(SearchUtil.getQueryWildcard(qb, SearchUtil.FIELD_PK_FILE_TYP, "*"), BooleanClause.Occur.MUST);
            } else {
                boolQuery.add(niceBoolQuery.build(), BooleanClause.Occur.MUST);
            }
        }

        if (searchParam.getViennaClassCodes() != null && searchParam.getViennaClassCodes().size() > 0) {
            BooleanQuery.Builder viennaClassesBoolQuery = new BooleanQuery.Builder();

            for (String viennaCode : searchParam.getViennaClassCodes()) {
                Query logoJoinQuery = createViennaClassQuery(viennaCode, IpLogoViennaClasses.class);
                Query attachmentJoinQuery = createViennaClassQuery(viennaCode, IpMarkAttachmentViennaClasses.class);
                BooleanQuery.Builder joinQuery = new BooleanQuery.Builder();
                joinQuery.add(logoJoinQuery, BooleanClause.Occur.SHOULD);
                joinQuery.add(attachmentJoinQuery, BooleanClause.Occur.SHOULD);


                switch (searchParam.getViennaClassCodeType()) {
                    case AND_NOT:
                        boolQuery.add(joinQuery.build(), BooleanClause.Occur.MUST_NOT);
                        break;
                    case OR:
                        viennaClassesBoolQuery.add(joinQuery.build(), BooleanClause.Occur.SHOULD);
                        break;
                    default:
                        viennaClassesBoolQuery.add(joinQuery.build(), BooleanClause.Occur.MUST);
                        break;
                }
            }

            if (searchParam.getViennaClassCodeType() == SearchOperatorType.AND_NOT) {
                boolQuery.add(SearchUtil.getQueryWildcard(qb, SearchUtil.FIELD_PK_FILE_TYP, "*"), BooleanClause.Occur.MUST);
            } else {
                boolQuery.add(viennaClassesBoolQuery.build(), BooleanClause.Occur.MUST);
            }

        }

        if (searchParam.getSignCode() != null && !searchParam.getSignCode().isEmpty()) {
            Query query = SearchUtil.getTermQuery(SearchUtil.FIELD_SIGN_WCODE, searchParam.getSignCode());
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getBgPermitNumber() != null && !searchParam.getBgPermitNumber().isEmpty()) {
            Query query = SearchUtil.getQueryByPhrase(hibernateSearchService.getQueryBuilder(IpPatent.class), SearchUtil.FIELD_SPC_EXTENDED_BG_PERMIT_NUMBER, searchParam.getBgPermitNumber());
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getFromBgPermitDate() != null || searchParam.getToBgPermitDate() != null) {
            Query query = SearchUtil.getQueryRange(
                    qb,
                    SearchUtil.FIELD_SPC_EXTENDED_BG_PERMIT_DATE,
                    searchParam.getFromBgPermitDate(),
                    searchParam.getToBgPermitDate()
            );
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getEuPermitNumber() != null && !searchParam.getEuPermitNumber().isEmpty()) {
            Query query = SearchUtil.getQueryByPhrase(hibernateSearchService.getQueryBuilder(IpPatent.class), SearchUtil.FIELD_SPC_EXTENDED_EU_PERMIT_NUMBER, searchParam.getEuPermitNumber());
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getFromEuPermitDate() != null || searchParam.getToEuPermitDate() != null) {
            Query query = SearchUtil.getQueryRange(
                    qb,
                    SearchUtil.FIELD_SPC_EXTENDED_EU_PERMIT_DATE,
                    searchParam.getFromEuPermitDate(),
                    searchParam.getToEuPermitDate()
            );
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getTaxon() != null && !searchParam.getTaxon().isEmpty()) {
            BooleanQuery.Builder taxonBoolQuery = new BooleanQuery.Builder();

            Query queryBulClassify = SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_PLANT_NUMENCLATURE_COMMON_CLASSIFY_BUL,
                    searchParam.getTaxon()
            );

            taxonBoolQuery.add(queryBulClassify, BooleanClause.Occur.SHOULD);

            Query queryEngClassify = SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_PLANT_NUMENCLATURE_COMMON_CLASSIFY_ENG,
                    searchParam.getTaxon()
            );

            taxonBoolQuery.add(queryEngClassify, BooleanClause.Occur.SHOULD);

            Query queryLatinClassify = SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_PLANT_NUMENCLATURE_LATIN_CLASSIFY,
                    searchParam.getTaxon()
            );

            taxonBoolQuery.add(queryLatinClassify, BooleanClause.Occur.SHOULD);

            boolQuery.add(taxonBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (searchParam.getProposedDenomination() != null && !searchParam.getProposedDenomination().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_PROPOSED_DENOMINATION,
                    searchParam.getProposedDenomination()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getProposedDenominationEng() != null && !searchParam.getProposedDenominationEng().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_PROPOSED_DENOMINATION_ENG,
                    searchParam.getProposedDenominationEng()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getPublDenomination() != null && !searchParam.getPublDenomination().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_PUBL_DENOMINATION,
                    searchParam.getPublDenomination()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getPublDenominationEng() != null && !searchParam.getPublDenominationEng().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_PUBL_DENOMINATION_ENG,
                    searchParam.getPublDenominationEng()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getApprDenomination() != null && !searchParam.getApprDenomination().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_APPR_DENOMINATION,
                    searchParam.getApprDenomination()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getApprDenominationEng() != null && !searchParam.getApprDenominationEng().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_APPR_DENOMINATION_ENG,
                    searchParam.getApprDenominationEng()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getRejDenomination() != null && !searchParam.getRejDenomination().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_REJ_DENOMINATION,
                    searchParam.getRejDenomination()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getRejDenominationEng() != null && !searchParam.getRejDenominationEng().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_REJ_DENOMINATION_ENG,
                    searchParam.getRejDenominationEng()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getFeatures() != null && !searchParam.getFeatures().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_FEATURES,
                    searchParam.getFeatures()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getStability() != null && !searchParam.getStability().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_STABILITY,
                    searchParam.getStability()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getTesting() != null && !searchParam.getTesting().isEmpty()) {

            Query query= SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_PLANT_DATA_TESTING,
                    searchParam.getTesting()
            );

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        Query ipDocBooleanQuery = ipDocService.getQuery(searchParam);
        if (ipDocBooleanQuery != null && !ipDocBooleanQuery.toString().equals("")) {
            Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(),
                    ipDocBooleanQuery,
                    SearchUtil.FIELD_PK,
                    SearchUtil.FIELD_FILE_IP_DOC_FILES_COLECTION_IP_DOC_PK,
                    IpDoc.class);
            boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
        }

        if (searchParam.getLocarnoClasses() != null && searchParam.getLocarnoClasses().size() > 0) {
            createLocarnoQuery(
                    searchParam,
                    qb,
                    boolQuery,
                    "file2.pk",
                    "file1.pk");
        }
        Query query = boolQuery.build();

        return query;
    }
    private Query createViennaClassQuery(String viennaCode, Class<?> cls) {
        String[] strings = viennaCode.split("\\.");
        if (strings.length > 3) {
            throw new RuntimeException("Vienna class code is too long!");
        }

        String viennaClassCode = strings.length > 0 ? strings[0].replaceFirst("^0+(?!$)", "") : "*";
        String viennaGroupCode = strings.length > 1 ? strings[1].replaceFirst("^0+(?!$)", "") : "*";
        String viennaElemCode = strings.length > 2 ? strings[2].replaceFirst("^0+(?!$)", "") : "*";

        BooleanQuery.Builder viennaBoolQuery = new BooleanQuery.Builder();

        Query viennaClassCodeQuery = hibernateSearchService.getQueryBuilder(cls)
                .keyword()
                .wildcard()
                .onField(SearchUtil.FIELD_PK_VIENNA_CLASS_CODE)
                .matching(viennaClassCode)
                .createQuery();
        viennaBoolQuery.add(viennaClassCodeQuery, BooleanClause.Occur.MUST);

        Query viennaGroupCodeQuery = hibernateSearchService.getQueryBuilder(cls)
                .keyword()
                .wildcard()
                .onField(SearchUtil.FIELD_PK_VIENNA_GROUP_CODE)
                .matching(viennaGroupCode)
                .createQuery();
        viennaBoolQuery.add(viennaGroupCodeQuery, BooleanClause.Occur.MUST);

        Query viennaElemCodeQuery = hibernateSearchService.getQueryBuilder(cls)
                .keyword()
                .wildcard()
                .onField(SearchUtil.FIELD_PK_VIENNA_ELEM_CODE)
                .matching(viennaElemCode)
                .createQuery();
        viennaBoolQuery.add(viennaElemCodeQuery, BooleanClause.Occur.MUST);

        Query viennaClassQuery = viennaBoolQuery.build();

        Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), viennaClassQuery, SearchUtil.FIELD_FILE_PK, SearchUtil.FIELD_FILE_PK_SORT, cls);
        return joinQuery;
    }
    private Optional<Query> addNationalityCountry(String nationalityCountry, String joinQueryField) {
        if (!ObjectUtils.isEmpty(nationalityCountry)) {
            Query queryPersonByNationalityCode = SearchUtil.getTermQuery(SearchUtil.FIELD_IP_PERSON_NATIONALITY_COUNTRY_CODE, nationalityCountry);

            Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), queryPersonByNationalityCode, SearchUtil.FIELD_IP_PERSON_PERSON_NBR, joinQueryField, IpPersonAddresses.class);
            return Optional.of(joinQuery);
        }
        return Optional.empty();
    }

    private Optional<Query> addPersonNameQuery(String personName, PersonNameSearchType searchType, String joinQueryField) {
        if (!ObjectUtils.isEmpty(personName)) {
            BooleanQuery.Builder personBoolQuery = new BooleanQuery.Builder();
            QueryBuilder qbPerson = hibernateSearchService.getQueryBuilder(IpPersonAddresses.class);

            if (searchType == PersonNameSearchType.CONTAINS_WORDS) {
                personName = SearchUtil.normString(personName);

                String[] personNames = personName.split(" ");
                BooleanQuery.Builder nameBoolQuery = new BooleanQuery.Builder();

                for (String name:personNames) {
                    Query query = SearchUtil.getQueryWildcard(
                            qbPerson,
                            SearchUtil.FIELD_IP_PERSON_PERSON_NAME_CUSTOM,
                            "*" + name + "*");

                    nameBoolQuery.add(query, BooleanClause.Occur.MUST);
                }

                personBoolQuery.add(nameBoolQuery.build(), BooleanClause.Occur.MUST);
            }

            if (searchType == PersonNameSearchType.WHOLE_WORDS) {
                personName = SearchUtil.normString(personName);

                BooleanQuery.Builder nameBoolQuery = new BooleanQuery.Builder();
                Query query = SearchUtil.getQueryByPhrase(
                        qbPerson,
                        SearchUtil.FIELD_IP_PERSON_PERSON_NAME,
                        personName);
                nameBoolQuery.add(query, BooleanClause.Occur.MUST);

                personBoolQuery.add(nameBoolQuery.build(), BooleanClause.Occur.MUST);
            }

            if (searchType == PersonNameSearchType.EXACTLY) {
                personName = SearchUtil.normString(personName);

                Query query = SearchUtil.getQueryWildcard(
                        qbPerson,
                        SearchUtil.FIELD_IP_PERSON_PERSON_NAME_CUSTOM,
                        "*" + personName + "*");

                personBoolQuery.add(query, BooleanClause.Occur.MUST);
            }

            Query joinQuery = SearchUtil.joinTwoIndexQuery(
                    hibernateSearchService.getFullTextSession(),
                    personBoolQuery.build(),
                    SearchUtil.FIELD_IP_PERSON_PERSON_NBR,
                    joinQueryField,
                    IpPersonAddresses.class);

            return Optional.of(joinQuery);
        }
        return Optional.empty();
    }

    @Override
    public Page<CSearchResult> search(Query luceneQuery, SearchPage searchPage) {
        CSearchParam searchParam = (CSearchParam) searchPage;
        Map<String, String> ipDocMap = new HashMap<>();

        if ( searchParam.getRequestForValidationType() != null
                && !searchParam.getRequestForValidationType().isEmpty()) {
            BooleanQuery.Builder boolQuery = new BooleanQuery.Builder();

//            Query ipoQuery = getQuery(searchParam);
//            if (ipoQuery != null && !ipoQuery.toString().equals("")) {
//                Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(),
//                        ipoQuery,
//                        SearchUtil.FIELD_FILE_IP_DOC_FILES_COLECTION_IP_DOC_PK,
//                        SearchUtil.FIELD_PK,
//                        IpMark.class, IpPatent.class);
//                boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
//            }

            Query ipDocQuery = SearchUtil.getTermQuery(
                    SearchUtil.FIELD_DOC_SEQ_TYP_DOC_SEQ_TYPE_CUSTOM,
                    searchParam.getRequestForValidationType());
            boolQuery.add(ipDocQuery, BooleanClause.Occur.MUST);

            Query query = boolQuery.build();

            Page<IpDocSearchResult> ipDocSearchResults = ipDocService.search(query, searchParam);

            ipDocMap = ipDocSearchResults.stream().collect(
                    Collectors.toMap(IpDocSearchResult::getPk,IpDocSearchResult::getDocSeqNbr));
        }

        QueryBuilder qb = hibernateSearchService.getQueryBuilder(IpPatent.class);
        FullTextQuery jpaQuery = hibernateSearchService.getFullTextQuery(luceneQuery, IpMark.class, IpPatent.class);

        if (searchPage.getSortColumn() != null) {
            if ( searchPage.getSortColumn().equals(SearchResultColumn.STATUS_SORT) ) {

                List<CStatus> allByFileTypesOrderByStatusName = statusService.getAllByFileTypesOrder(((CSearchParam) searchPage).getFileTypes());
                Map<String, String> customKeySorter = allByFileTypesOrderByStatusName
                        .stream()
                        .collect(Collectors.toMap(r -> r.getStatusId().getStatusCode(), r -> r.getStatusName(), (r1, r2) -> r1));

                Sort sort = new Sort(
                        new SortField(
                                SearchUtil.FIELD_FILE_STATUS_CODE,
                                new CustomKeyValueComparatorSource(customKeySorter),
                                searchPage.getSortOrder().equals(SearchSortType.DESC.toString())
                        )
                );
                jpaQuery.setSort(sort);
            } else {
                Sort sort = SearchUtil.getSort(qb, searchPage);
                jpaQuery.setSort(sort);
            }
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
                SearchUtil.FIELD_FILE_FILING_DATA_SORT,
                SearchUtil.FIELD_FILE_REGISTRATION_NBR_SORT,
                SearchUtil.FIELD_FILE_REGISTRATION_DUP,
                SearchUtil.FIELD_MAIN_OWNER_IP_PERSON_PERSON_NAME_SORT,
                SearchUtil.FIELD_FILE_TITLE_SORT,
                SearchUtil.FIELD_IMG,
                SearchUtil.FIELD_SIGN_WCODE,
                SearchUtil.FIELD_FILE_REGISTRATION_DATE,
                SearchUtil.FIELD_FILE_STATUS_CODE,
                SearchUtil.FIELD_FILE_PROC_TYP,
                SearchUtil.FIELD_FILE_PROC_NBR,
                SearchUtil.FIELD_FILE_EXPIRATION_DATE,
                FullTextQuery.DOCUMENT);

        List resultList = jpaQuery.getResultList();
        if (pageable == null) {
            pageable = PageRequest.of(0, resultList.size());
        }
        org.apache.commons.lang3.time.StopWatch sw = new org.apache.commons.lang3.time.StopWatch();
        sw.start();
        sw.suspend();
        List<CSearchResult> ipos = new ArrayList<>(resultList.size());
        Map<String, PersonAddressResult> cachedRepresentatives = new HashMap<>();//tyj kato representative-ite sa kraen broj (nqkolko stotin), gi keshiram

        for (Object obj : resultList) {
            Object[] arrObj = (Object[]) obj;

            int arg = 0;
            String fileSeq = (String) arrObj[arg++];
            String fileTyp = (String) arrObj[arg++];
            Integer fileSer = (Integer) arrObj[arg++];
            Integer fileNbr = (Integer) arrObj[arg++];
            Date filingDate = (Date) arrObj[arg++];
            Integer registrationNbr = (Integer) arrObj[arg++];
            String registrationDup = (String) arrObj[arg++];
            String ownerName = (String) arrObj[arg++];
            String title = (String)arrObj[arg++];
            Boolean img = (Boolean) arrObj[arg++];
            String signWcode = (String) arrObj[arg++];
            Date registrationDate = (Date) arrObj[arg++];
            String statusCode = (String) arrObj[arg++];
            Integer procTyp = (Integer) arrObj[arg++];
            Integer procNbr = (Integer) arrObj[arg++];
            Date expirationDate = (Date) arrObj[arg++];
            Document document = (Document) arrObj[arg++];


            List<String> requestForValidationIds = getFieldsValue(document, SearchUtil.FIELD_FILE_IP_DOC_FILES_COLECTION_IP_DOC_PK);
            List<String> niceClasses = getFieldsValue(document, SearchUtil.FIELD_IP_MARK_NICE_CLASSES_PK);
            List<String> representativePersonNbrs = getFieldsValue(document, SearchUtil.FIELD_REPRESENTATIVES_IP_PERSON_ADDRESSES_IP_PERSON_PERSON_NBR);

            sw.resume();
            List<PersonAddressResult> representatives = new ArrayList<>();
            if (representativePersonNbrs != null) {
                for (String r : representativePersonNbrs) {
                    if (!cachedRepresentatives.containsKey(r)) {
                        CCriteriaPerson reprCriteria = new CCriteriaPerson();
                        reprCriteria.setPersonNbr(r);
                        Page<PersonAddressResult> reprSearchResult = personAddressSearchService.search(reprCriteria);
                        if (reprSearchResult.getTotalElements() == 1) {
                            cachedRepresentatives.put(r, reprSearchResult.stream().findFirst().get());
                        }
                    }
                    if (cachedRepresentatives.containsKey(r)) {
                        representatives.add(cachedRepresentatives.get(r));
                    }
                }
            }
            sw.suspend();



            boolean hasImg = (Objects.equals(true, img))
                    && !(Objects.equals(signWcode, "D"));

            CSearchResult el = new CSearchResult();

            CStatus status = null;
            if (procTyp != null && !StringUtils.isEmpty(statusCode)) {
                status = statusService.getStatus(procTyp.toString(), statusCode);
            }
            if (!canViewObjectData(status)) {
                ownerName = "";
                title = "";
                representatives = new ArrayList<>();
            }



            el.pk(fileSeq, fileTyp, fileSer, fileNbr)
                    .filingDate(filingDate)
                    .registrationNbr(registrationNbr)
                    .mainOwner(ownerName)
                    .title(title)
                    .hasImg(hasImg)
                    .registrationDate(registrationDate)
                    .registrationDup(registrationDup)
                    .status(status)
                    .niceClassCodes(getNiceClassCodes(niceClasses))
                    .processId(new CProcessId(procTyp.toString(), procNbr))
                    .expirationDate(expirationDate)
                    .representatives(representatives)
                    .requestForValidationNbr(getRequestForValidationNbrs(ipDocMap, requestForValidationIds));

            ipos.add(el);
        }
        sw.stop();
        log.debug("Search all representatives execution time " + sw.getTime() );


        return new PageImpl<>(ipos, pageable, jpaQuery.getResultSize());
    }

    private Query getQueryByFieldAndDateRange(String actionTypField, String actionTyp, String statusDateField, Date fromDate, Date toDate) {
        QueryBuilder actionQueryBuilder = hibernateSearchService.getQueryBuilder(IpAction.class);

        BooleanQuery.Builder queryPrior = new BooleanQuery.Builder();

        Query queryPriorStatusCode = actionQueryBuilder
                .keyword()
                .onField(actionTypField)
                .matching(actionTyp)
                .createQuery();
        queryPrior.add(queryPriorStatusCode, BooleanClause.Occur.MUST);

        Query queryPriorDateRange = SearchUtil.getQueryRange(actionQueryBuilder, statusDateField, fromDate, toDate);
        queryPrior.add(queryPriorDateRange, BooleanClause.Occur.MUST);

        return queryPrior.build();
    }



    private List<String> getRequestForValidationNbrs(Map<String, String> ipDocMap, List<String> requestForValidationIds) {

        List<String> list = new ArrayList<>();

        requestForValidationIds
                .stream()
                .forEach(
                        r -> {
                            String str = ipDocMap.get(r);

                            if(!Objects.isNull(str)) {
                                list.add(ipDocMap.get(r));
                            }
                        }
                );

        return list;
    }

    private List<Long> getNiceClassCodes(List<String> niceClasses) {
        return niceClasses.stream().map(IpMarkNiceClassesPkBridge::toIpMarkNiceClassesPK).map(r -> r.getNiceClassCode()).collect(Collectors.toList());
    }

    private Query createJoinQueryRelationship(Query childQuery,
                                             String firstToField,
                                             String secondToField) {

        Query joinQueryFilePk = SearchUtil.joinTwoIndexQuery(
                hibernateSearchService.getFullTextSession(),
                childQuery,
                SearchUtil.FIELD_FILE_PK,
                firstToField,
                IpPatentLocarnoClasses.class);

        Query joinQueryRelationship = SearchUtil.joinTwoIndexQuery(
                hibernateSearchService.getFullTextSession(),
                joinQueryFilePk,
                secondToField,
                SearchUtil.FIELD_FILE_PK_SORT,
                IpFileRelationship.class);

        return joinQueryRelationship;
    }

    private void createLocarnoQuery(CSearchParam searchParam,
                                    QueryBuilder qb,
                                    BooleanQuery.Builder boolQuery,
                                    String firstToField,
                                    String secondToField) {
        BooleanQuery.Builder childBoolQuery = new BooleanQuery.Builder();

        for (String locarno : searchParam.getLocarnoClasses()) {

            Query locarnoClassCodeQuery = hibernateSearchService.getQueryBuilder(IpPatentLocarnoClasses.class)
                    .keyword()
                    .wildcard()
                    .onField("locarnoClasses.pk.locarnoClassCode")
                    .matching(locarno)
                    .createQuery();

            Query firstJoinQuery = createJoinQueryRelationship(
                    locarnoClassCodeQuery,
                    firstToField,
                    secondToField
            );

            Query secondJoinQuery = createJoinQueryRelationship(
                    locarnoClassCodeQuery,
                    secondToField,
                    firstToField
            );

            BooleanQuery.Builder joinBoolQuery = new BooleanQuery.Builder();
            joinBoolQuery.add(firstJoinQuery, BooleanClause.Occur.SHOULD);
            joinBoolQuery.add(secondJoinQuery, BooleanClause.Occur.SHOULD);


            switch (searchParam.getLocarnoClassCodeType()) {
                case AND_NOT:
                    boolQuery.add(joinBoolQuery.build(), BooleanClause.Occur.MUST_NOT);
                    break;
                case OR:
                    childBoolQuery.add(joinBoolQuery.build(), BooleanClause.Occur.SHOULD);
                    break;
                default:
                    childBoolQuery.add(joinBoolQuery.build(), BooleanClause.Occur.MUST);
                    break;
            }
        }

        if (searchParam.getLocarnoClassCodeType() == SearchOperatorType.AND_NOT) {
            boolQuery.add(SearchUtil.getQueryWildcard(qb, SearchUtil.FIELD_PK_FILE_TYP, "*"), BooleanClause.Occur.MUST);
        } else {
            boolQuery.add(childBoolQuery.build(), BooleanClause.Occur.MUST);
        }

    }

    private boolean canViewObjectData(CStatus status) {
        return status != null && (!status.isSecretPatentProcessResultType() || (SecurityUtils.hasRights(SecurityRole.PatentSecretData) && status.isSecretPatentProcessResultType()));
    }



    private static Integer parseInteger(String str) {
        try {
            return str == null ? null : Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
