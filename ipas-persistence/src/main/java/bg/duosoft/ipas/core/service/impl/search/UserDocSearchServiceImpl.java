package bg.duosoft.ipas.core.service.impl.search;

import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.process.ProcessIdMapper;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.search.CSearchParam;
import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.core.model.search.CUserdocSearchParam;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.core.model.util.UserDocSearchResult;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeGroupService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.core.service.search.UserDocSearchService;
import bg.duosoft.ipas.enums.SearchResultColumn;
import bg.duosoft.ipas.enums.SearchSortType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.enums.search.PersonNameSearchType;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPerson;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatusPK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfUserdocTypeRepository;
import bg.duosoft.ipas.util.search.CustomKeyValueComparatorSource;
import bg.duosoft.ipas.util.search.IpDocPKBridge;
import bg.duosoft.ipas.util.search.SearchUtil;
import org.apache.lucene.search.*;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bg.duosoft.ipas.util.search.SearchUtil.FIELD_RESPONSIBLE_USER;

@Service
@Transactional
public class UserDocSearchServiceImpl
        extends SearchServiceImpl<UserDocSearchResult, CUserdocSearchParam>
        implements UserDocSearchService {

    @Autowired
    private CfUserdocTypeRepository cfUserdocTypeRepository;
    @Autowired
    private ProcessIdMapper processIdMapper;
    @Autowired
    private DocumentIdMapper documentIdMapper;
    @Autowired
    private StatusService statusService;
    @Autowired
    private IpoSearchService ipoSearchService;
    @Autowired
    private FileTypeGroupService fileTypeGroupService;

    @Override
    public Query getQuery(CUserdocSearchParam searchParam) {
        BooleanQuery.Builder boolQuery = new BooleanQuery.Builder();

        setupUserDocQuery(boolQuery, searchParam);

        if (searchParam.getPersonName() != null && !searchParam.getPersonName().isEmpty()) {
            QueryBuilder personQueryBuilder = hibernateSearchService.getQueryBuilder(IpPersonAddresses.class);

            BooleanQuery.Builder personBoolQuery = new BooleanQuery.Builder();
            String personName = SearchUtil.normString(searchParam.getPersonName());

            if (searchParam.getPersonNameSearchType() == PersonNameSearchType.CONTAINS_WORDS) {
                String[] personNames = personName.split(" ");
                BooleanQuery.Builder nameBoolQuery = new BooleanQuery.Builder();

                for (String name:personNames) {
                    Query query = SearchUtil.getQueryWildcard(
                            personQueryBuilder,
                            SearchUtil.FIELD_IP_PERSON_PERSON_NAME_CUSTOM,
                            "*" + name + "*");

                    nameBoolQuery.add(query, BooleanClause.Occur.MUST);
                }

                personBoolQuery.add(nameBoolQuery.build(), BooleanClause.Occur.MUST);
            }

            if (searchParam.getPersonNameSearchType() == PersonNameSearchType.WHOLE_WORDS) {
                String[] personNames = personName.split(" ");
                BooleanQuery.Builder nameBoolQuery = new BooleanQuery.Builder();

                for (String name:personNames) {
                    Query query = SearchUtil.getQueryByPhrase(
                            personQueryBuilder,
                            SearchUtil.FIELD_IP_PERSON_PERSON_NAME,
                            name);

                    nameBoolQuery.add(query, BooleanClause.Occur.MUST);
                }

                personBoolQuery.add(nameBoolQuery.build(), BooleanClause.Occur.MUST);
            }

            if (searchParam.getPersonNameSearchType() == PersonNameSearchType.EXACTLY) {
                Query query = SearchUtil.getQueryWildcard(
                        personQueryBuilder,
                        SearchUtil.FIELD_IP_PERSON_PERSON_NAME_CUSTOM,
                        "*" + personName + "*");

                personBoolQuery.add(query, BooleanClause.Occur.MUST);
            }

            BooleanJunction<BooleanJunction> boolUserdocperson = personQueryBuilder.bool();
            Query joinPersonQuery = SearchUtil.joinTwoIndexQuery(
                    hibernateSearchService.getFullTextSession(),
                    personBoolQuery.build(),
                    "ipPerson.person_nbr",
                    "pk.person_nbr",
                    IpPersonAddresses.class);

            boolUserdocperson.must(joinPersonQuery);
            if (searchParam.getRole() != null && !searchParam.getRole().isEmpty()) {
                Query queryRole = SearchUtil.getTermQuery("pk.role", searchParam.getRole());

                boolUserdocperson.must(queryRole);
            }

            Query joinQuery = SearchUtil.joinTwoIndexQuery(
                    hibernateSearchService.getFullTextSession(),
                    boolUserdocperson.createQuery(),
                    "doc.pk",
                    "pk",
                    IpUserdocPerson.class);

            boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
        }

        if (searchParam.getRole() != null && !searchParam.getRole().isEmpty()) {
            Query queryRole = SearchUtil.getTermQuery("pk.role", searchParam.getRole());

            Query joinQuery = SearchUtil.joinTwoIndexQuery(
                    hibernateSearchService.getFullTextSession(),
                    queryRole,
                    "doc.pk",
                    "pk",
                    IpUserdocPerson.class);

            boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
        }

        Query query = boolQuery.build();

        return query;
    }

    private static final int MAX_FILE_DETAILS_COUNT = 100;
    @Override
    public Page<UserDocSearchResult> search(Query luceneQuery, SearchPage searchPage) {
        if (!(searchPage instanceof CUserdocSearchParam)) {
            throw new RuntimeException("Object isn't instance of UserDocSearchParam!");
        }

        Query personAddressQuery = getPersonAddressQuery((CUserdocSearchParam) searchPage);
        Map<Integer, String> personMap = searchPersonAddress(personAddressQuery);

        Query userDocPersonQuery = getUserDocPersonQuery((CUserdocSearchParam) searchPage);
        Map<String, List<Integer>> userDocPersonMap = searchUserDocPerson(userDocPersonQuery);

        QueryBuilder qb = hibernateSearchService.getQueryBuilder(IpUserdoc.class);
        FullTextQuery jpaQuery = hibernateSearchService.getFullTextQuery(luceneQuery, IpUserdoc.class);

        if (searchPage.getSortColumn() != null) {
            if ( searchPage.getSortColumn().equals(SearchResultColumn.STATUS_SORT) ) {

                Map<String, String> statusMap = statusService.getStatusMap();
                Map<String, String> sortOrd = statusMap
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(r -> r.getKey().split("-")[1], r -> r.getValue(), (r1, r2) -> r1));
                Sort sort = new Sort(
                        new SortField(
                                SearchUtil.FIELD_IP_DOC_PROC_STATUS,
                                new CustomKeyValueComparatorSource(sortOrd),
                                searchPage.getSortOrder().equals(SearchSortType.DESC.toString())
                        )
                );
                jpaQuery.setSort(sort);
            } else if ( searchPage.getSortColumn().equals("docType") ) {

                List<CfUserdocType> allByFileTypesOrderByStatusName =
                        cfUserdocTypeRepository.findAllByUserdocNameContainingOrderByUserdocName("");
                Map<String, String> sortKeyValues = allByFileTypesOrderByStatusName.stream().collect(Collectors.toMap(r -> r.getUserdocTyp(), r -> r.getUserdocName()));
                Sort sort = new Sort(
                        new SortField(
                                "ipDoc.proc.userdocTyp",
                                new CustomKeyValueComparatorSource(sortKeyValues),
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

        jpaQuery.setProjection(
                "ipDoc.proc.subProc.fileSeq",
                "ipDoc.proc.subProc.fileTyp",
                "ipDoc.proc.subProc.fileSer",
                "ipDoc.proc.subProc.fileNbr",
                "ipDoc.filingDate",
                "ipDoc.proc.userdocTyp",
                "ipDoc.externalSystemId",
                "pk",
                "ipDoc.proc.pk",
                "ipDoc.proc.statusCode");

        List resultList = jpaQuery.getResultList();

        List<UserDocSearchResult> list = new ArrayList<>(resultList.size());
        int cnt = 0;
        for (Object obj : resultList) {
            Object[] arrObj = (Object[]) obj;

            IpDocPK ipDoc = (IpDocPK) arrObj[7];
            List<String> applicants = new ArrayList<>();
            if (Objects.nonNull(ipDoc)) {
                String userDocPK = IpDocPKBridge.convertToString(ipDoc);
                List<Integer> personNbrs = userDocPersonMap.get(userDocPK);

                if (Objects.nonNull(personNbrs)){
                    for (Integer personNbr:personNbrs) {
                        applicants.add(personMap.get(personNbr));
                    }
                }
            }

            String userdoctypeStr = "";
            if (Objects.nonNull(arrObj[5])) {
                CfUserdocType userDocType = cfUserdocTypeRepository.findById((String) arrObj[5]).orElse(null);
                if (Objects.nonNull(userDocType)) {
                    userdoctypeStr = userDocType.getUserdocName();
                }
            }
            IpProcPK procPk = (IpProcPK) arrObj[8];
            String statusCode = (String) arrObj[9];
            String status = "";
            if (procPk != null && !StringUtils.isEmpty(statusCode)) {

                CfStatusPK pk = new CfStatusPK();
                pk.setProcTyp(procPk.getProcTyp());
                pk.setStatusCode(statusCode);

                CStatus cStatus = statusService.getStatus(procPk.getProcTyp(), statusCode);
                if (!Objects.isNull(cStatus)) {
                    status = cStatus.getStatusName();
                }
            }

            String title = null;
            String registrationNbr = null;
            CFileId fileId = arrObj[0] == null ? null : new CFileId((String) arrObj[0], (String) arrObj[1], (Integer) arrObj[2], (Integer) arrObj[3]);
            if (++cnt <= MAX_FILE_DETAILS_COUNT) {
                if (fileId != null) {
                    CSearchParam fileSearchParam = new CSearchParam(SearchPage.create(0, 1));
                    fileSearchParam
                            .addFileType(fileId.getFileType())
                            .fileSeq(fileId.getFileSeq())
                            .fileNbr(fileId.getFileNbr().toString())
                            .fileSer(fileId.getFileSeries().toString());

                    Page<CSearchResult> res = ipoSearchService.search(fileSearchParam);
                    if (res.getTotalElements() == 1) {
                        CSearchResult file = res.getContent().get(0);
                        registrationNbr = Stream.of(file.getRegistrationNbr(), file.getRegistrationDup()).filter(Objects::nonNull).map(r -> r.toString()).collect(Collectors.joining());
                        title = file.getTitle();
                    }
                }
            } else {
                title = "Not loaded. Loading only the first " + MAX_FILE_DETAILS_COUNT + " file titles";
            }

            UserDocSearchResult el = new UserDocSearchResult();
            el.documentId(documentIdMapper.toCore(ipDoc))
                    .fileId(fileId)
                    .filingDate((Date) arrObj[4])
                    .docType(userdoctypeStr)
                    .applicants(applicants)
                    .docNumber((String) arrObj[6])
                    .processId(processIdMapper.toCore(procPk))
                    .status(status)
                    .fileRegistrationNumber(registrationNbr)
                    .fileTitle(title);

            list.add(el);
        }

        return new PageImpl<>(list, toSpringPage(searchPage), jpaQuery.getResultSize());
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

    public void setupUserDocQuery(BooleanQuery.Builder boolQuery, CUserdocSearchParam searchParam) {
        QueryBuilder qbIpUserdoc = hibernateSearchService.getQueryBuilder(IpUserdoc.class);

        Query allQuery = qbIpUserdoc.all().createQuery();
        boolQuery.add(allQuery, BooleanClause.Occur.MUST);

        if (searchParam.getUserDocType() != null && !searchParam.getUserDocType().isEmpty()) {
            Query query = SearchUtil.getTermQuery("ipDoc.proc.userdocTyp", searchParam.getUserDocType());
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getFileType() != null && !searchParam.getFileType().isEmpty()) {
            Query query = SearchUtil.getTermQuery("ipDoc.proc.subProc.fileTyp", searchParam.getFileType());
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (!ObjectUtils.isEmpty(searchParam.getFileTypeGroup())) {
            BooleanQuery.Builder fileTypeBoolQuery = new BooleanQuery.Builder();
            List<String> fileTypes = fileTypeGroupService.getFileTypesByGroup(searchParam.getFileTypeGroup());
            for (String fileType : fileTypes) {
                fileTypeBoolQuery.add(SearchUtil.getTermQuery("ipDoc.proc.subProc.fileTyp", fileType), BooleanClause.Occur.SHOULD);
            }

            boolQuery.add(fileTypeBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (searchParam.getDocNumber() != null && !searchParam.getDocNumber().isEmpty()) {
            Query query = SearchUtil.getQueryWildcard(qbIpUserdoc, "ipDoc.externalSystemId", "*" + searchParam.getDocNumber() + "*");
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getFromFilingDate() != null || searchParam.getToFilingDate() != null) {
            Query query = SearchUtil.getQueryRange(
                    qbIpUserdoc,
                    "ipDoc.filingDate",
                    searchParam.getFromFilingDate(),
                    searchParam.getToFilingDate()
            );
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

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

                Query joinQuery = SearchUtil.joinTwoIndexQuery(
                        hibernateSearchService.getFullTextSession(),
                        queryStatus,
                        SearchUtil.FIELD_PROC_PK,
                        "ipDoc.proc.pk",
                        IpAction.class);

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
                Query joinQuery = SearchUtil.joinTwoIndexQuery(
                        hibernateSearchService.getFullTextSession(),
                        statusBoolQuery.build(),
                        SearchUtil.FIELD_PROC_PK,
                        "ipDoc.proc.pk",
                        IpProc.class);

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
            Query joinQuery = SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), queryStatus, SearchUtil.FIELD_PROC_PK, "ipDoc.proc.pk", IpProc.class);

            boolQuery.add(joinQuery, BooleanClause.Occur.MUST);
        }

        creteActionQuery(searchParam).ifPresent(q -> boolQuery.add(SearchUtil.joinTwoIndexQuery(hibernateSearchService.getFullTextSession(), q, SearchUtil.FIELD_PROC_PK, "ipDoc.proc.pk", IpAction.class), BooleanClause.Occur.MUST));


    }

    public Query getPersonAddressQuery( CUserdocSearchParam searchParam) {
        BooleanQuery.Builder userDocBoolQuery = new BooleanQuery.Builder();
        setupUserDocQuery(userDocBoolQuery, searchParam);

        BooleanQuery userDocQuery = userDocBoolQuery.build();

        Query joinUserDocQuery = SearchUtil.joinTwoIndexQuery(
                hibernateSearchService.getFullTextSession(),
                userDocQuery,
                "pk",
                "doc.pk",
                IpUserdoc.class);


        BooleanQuery.Builder userDocPersonBoolQuery = new BooleanQuery.Builder();
        userDocPersonBoolQuery.add(joinUserDocQuery, BooleanClause.Occur.MUST);
        
        Query queryRole = SearchUtil.getTermQuery("pk.role", UserdocPersonRole.APPLICANT.toString());
        userDocPersonBoolQuery.add(queryRole, BooleanClause.Occur.MUST);


        Query joinPersonQuery = SearchUtil.joinTwoIndexQuery(
                hibernateSearchService.getFullTextSession(),
                userDocPersonBoolQuery.build(),
                "pk.person_nbr",
                "ipPerson.person_nbr",
                IpUserdocPerson.class);

        return joinPersonQuery;
    }
    
    public Map<Integer, String> searchPersonAddress(Query luceneQuery) {

        FullTextQuery jpaQuery = hibernateSearchService.getFullTextQuery(luceneQuery, IpPersonAddresses.class);

        jpaQuery.setProjection(
                "ipPerson.person_nbr",
                "ipPerson.personName");

        List resultList = jpaQuery.getResultList();

        Map<Integer, String> map = new HashMap<>();
        for (Object obj : resultList) {
            Object[] arrObj = (Object[]) obj;

            map.put((Integer) arrObj[0], (String) arrObj[1]);
        }

        return map;
    }
    
    public Query getUserDocPersonQuery( CUserdocSearchParam searchParam) {
        BooleanQuery.Builder userDocBoolQuery = new BooleanQuery.Builder();
        setupUserDocQuery(userDocBoolQuery, searchParam);

        BooleanQuery userDocQuery = userDocBoolQuery.build();

        Query joinUserDocQuery = SearchUtil.joinTwoIndexQuery(
                hibernateSearchService.getFullTextSession(),
                userDocQuery,
                "pk",
                "doc.pk",
                IpUserdoc.class);


        BooleanQuery.Builder userDocPersonBoolQuery = new BooleanQuery.Builder();
        userDocPersonBoolQuery.add(joinUserDocQuery, BooleanClause.Occur.MUST);

        Query queryRole = SearchUtil.getTermQuery("pk.role", UserdocPersonRole.APPLICANT.toString());
        userDocPersonBoolQuery.add(queryRole, BooleanClause.Occur.MUST);

        return userDocPersonBoolQuery.build();
    }

    public Map<String, List<Integer>> searchUserDocPerson(Query luceneQuery) {

        FullTextQuery jpaQuery = hibernateSearchService.getFullTextQuery(luceneQuery, IpUserdocPerson.class);

        jpaQuery.setProjection(
                "doc.pk",
                "pk.person_nbr");

        List resultList = jpaQuery.getResultList();

        Map<String, List<Integer>> map = new HashMap<>();
        for (Object obj : resultList) {
            Object[] arrObj = (Object[]) obj;
            String key = (String) arrObj[0];
            Integer personNbr = (Integer) arrObj[1];

            List<Integer> value = map.get(key);

            if (Objects.isNull(value)) {
                value = new ArrayList<>();
            }

            value.add(personNbr);
            map.put(key, value);
        }

        return map;
    }

}
