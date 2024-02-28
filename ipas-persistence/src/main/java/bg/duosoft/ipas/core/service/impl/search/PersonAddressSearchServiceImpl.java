package bg.duosoft.ipas.core.service.impl.search;


import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.model.util.PersonAddressResult;
import bg.duosoft.ipas.core.service.search.PersonAddressSearchService;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public
class PersonAddressSearchServiceImpl
        extends SearchServiceImpl<PersonAddressResult, CCriteriaPerson>
        implements PersonAddressSearchService {

    public Query getQuery(CCriteriaPerson searchParam) {
        QueryBuilder qb = hibernateSearchService.getQueryBuilder(IpPersonAddresses.class);

        BooleanQuery.Builder boolQuery = new BooleanQuery.Builder();
        if (!ObjectUtils.isEmpty(searchParam.getPersonNbr())) {
            Query query = SearchUtil.getTermQuery(SearchUtil.FIELD_IP_PERSON_PERSON_NBR, searchParam.getPersonNbr());
            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getPersonName() != null) {
            Query query = qb
                    .keyword()
                    .onField(SearchUtil.FIELD_IP_PERSON_PERSON_NAME_EXACT)
                    .matching(searchParam.getPersonName())
                    .createQuery();

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getPersonNameContainsWords() != null) {
            String personName = SearchUtil.normString(searchParam.getPersonNameContainsWords());

            String[] personNames = personName.split(" ");
            BooleanQuery.Builder nameBoolQuery = new BooleanQuery.Builder();

            for (String name:personNames) {
                Query query = SearchUtil.getQueryWildcard(
                        qb,
                        SearchUtil.FIELD_IP_PERSON_PERSON_NAME_CUSTOM,
                        "*" + name + "*");

                nameBoolQuery.add(query, BooleanClause.Occur.MUST);
            }

            boolQuery.add(nameBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (searchParam.getPersonNameWholeWords() != null) {
            String personName = SearchUtil.normString(searchParam.getPersonNameWholeWords());

            String[] personNames = personName.split(" ");
            BooleanQuery.Builder nameBoolQuery = new BooleanQuery.Builder();

            for (String name:personNames) {
                Query query = SearchUtil.getQueryByPhrase(
                        qb,
                        SearchUtil.FIELD_IP_PERSON_PERSON_NAME,
                        name);

                nameBoolQuery.add(query, BooleanClause.Occur.MUST);
            }

            boolQuery.add(nameBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (searchParam.getPersonNameExactly() != null) {
            String personName = SearchUtil.normString(searchParam.getPersonNameExactly());

            Query query = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_PERSON_NAME_CUSTOM,
                    "*" + personName + "*");

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (!StringUtils.isEmpty(searchParam.getIndividualIdText())) {
            String individualIdTxt = SearchUtil.normString(searchParam.getIndividualIdText());

            Query query = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_INDIVIDUAL_ID_TXT,
                    "*" + individualIdTxt + "*");

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (!StringUtils.isEmpty(searchParam.getIndividualIdType())) {
            Query query = SearchUtil.getTermQuery(SearchUtil.FIELD_IP_PERSON_INDIVIDUAL_ID_TYPE, searchParam.getIndividualIdType());

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getNationalityCountryCode() != null) {
            Query query = SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_NATIONALITY_COUNTRY_CODE,
                    searchParam.getNationalityCountryCode());

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getResidenceCountryCode() != null) {
            Query query = SearchUtil.getTermQuery(
                    SearchUtil.FIELD_RESIDENCE_COUNTRY_COUNTRY_CODE,
                    searchParam.getResidenceCountryCode());

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getEmail() != null) {
            Query query = SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_EMAIL_EXACT,
                    searchParam.getEmail());

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getEmailContainsWords() != null) {
            Query query = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_EMAIL,
                    "*" + searchParam.getEmailContainsWords() + "*");

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getTelephoneContainsWords() != null) {
            Query query = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_TELEPHONE,
                    "*" + searchParam.getTelephoneContainsWords() + "*");

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getCity() != null) {
            Query query = SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_CITY_NAME_EXACT,
                    searchParam.getCity());

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getCityContainsWords() != null) {
            String cityName = searchParam.getCityContainsWords().toLowerCase().trim() + "*";

            List<String> cityNames = Arrays.asList(cityName.split(" "));
            Query query = SearchUtil.getQueryWildcardContainAllWords(
                    qb,
                    SearchUtil.FIELD_CITY_NAME,
                    cityNames);

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getStreet() != null) {
            Query query = SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_ADDR_STREET_EXACT,
                    searchParam.getStreet());

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getStreetContainsWords() != null) {

            String streetWord = SearchUtil.normString(searchParam.getStreetContainsWords()) + "*";

            List<String> streetWords = Arrays.asList(streetWord.split(" "));
            Query query = SearchUtil.getQueryWildcardContainAllWords(
                    qb,
                    SearchUtil.FIELD_ADDR_STREET,
                    streetWords);

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getZipCode() != null) {
            Query query = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_ZIP_CODE_EXACT,
                    "*" + searchParam.getZipCode() + "*");

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        if (searchParam.getAgentCode() != null) {
            BooleanQuery.Builder agentBoolQuery = new BooleanQuery.Builder();

            Query queryAgent = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_IP_AGENT_AGENT_CODE_FIELD,
                    searchParam.getAgentCode().trim() + "*");
            agentBoolQuery.add(queryAgent, BooleanClause.Occur.SHOULD);

            Query queryPartnership = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_EXTENDED_PARTNERSHIP_PARTNERSHIP_CODE_FIELD,
                    searchParam.getAgentCode().trim() + "*");
            agentBoolQuery.add(queryPartnership, BooleanClause.Occur.SHOULD);

            boolQuery.add(agentBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (searchParam.isOnlyActiveAgent()) {
            BooleanQuery.Builder activeAgentBoolQuery = new BooleanQuery.Builder();

            Query queryAgent = SearchUtil.getTermQuery(
                    SearchUtil.FIELD_IP_PERSON_IP_AGENT_IND_INACTIVE,
                    "" + searchParam.isOnlyActiveAgent());

            activeAgentBoolQuery.add(queryAgent, BooleanClause.Occur.SHOULD);

            Query queryPartnership = SearchUtil.getTermQuery(
                    SearchUtil.FIELD_IP_PERSON_EXTENDED_PARTNERSHIP_IND_INACTIVE,
                    "" + searchParam.isOnlyActiveAgent());

            activeAgentBoolQuery.add(queryPartnership, BooleanClause.Occur.SHOULD);

            boolQuery.add(activeAgentBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (searchParam.getAgentCodeOrNameContainsWords() != null) {
            BooleanQuery.Builder agentOrNameBoolQuery = new BooleanQuery.Builder();

            //Search agent by agentCode
            BooleanQuery.Builder agentBoolQuery = new BooleanQuery.Builder();
            Query queryAgent = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_IP_AGENT_AGENT_CODE_FIELD,
                    searchParam.getAgentCodeOrNameContainsWords().trim() + "*");
            agentBoolQuery.add(queryAgent, BooleanClause.Occur.SHOULD);

            Query queryPartnership = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_EXTENDED_PARTNERSHIP_PARTNERSHIP_CODE_FIELD,
                    searchParam.getAgentCodeOrNameContainsWords().trim() + "*");
            agentBoolQuery.add(queryPartnership, BooleanClause.Occur.SHOULD);

            agentOrNameBoolQuery.add(agentBoolQuery.build(), BooleanClause.Occur.SHOULD);

            //Search agent by name
            BooleanQuery.Builder agentByNameBoolQuery = new BooleanQuery.Builder();

            String agentName = SearchUtil.normString(searchParam.getAgentCodeOrNameContainsWords()) + "*";
            Query agentNameQuery = SearchUtil.getQueryByPersonName(qb,
                    SearchUtil.FIELD_IP_PERSON_PERSON_NAME_CUSTOM,
                    agentName );
            agentByNameBoolQuery.add(agentNameQuery, BooleanClause.Occur.MUST);

            BooleanQuery.Builder agentByNameQuery = new BooleanQuery.Builder();
            Query queryAgentCodeByName = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_IP_AGENT_AGENT_CODE_FIELD,
                    "*");
            agentByNameQuery.add(queryAgentCodeByName, BooleanClause.Occur.SHOULD);

            Query queryPartnershipCodeByName = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_EXTENDED_PARTNERSHIP_PARTNERSHIP_CODE_FIELD,
                    "*");
            agentByNameQuery.add(queryPartnershipCodeByName, BooleanClause.Occur.SHOULD);

            agentByNameBoolQuery.add(agentByNameQuery.build(), BooleanClause.Occur.MUST);

            agentOrNameBoolQuery.add(agentByNameBoolQuery.build(), BooleanClause.Occur.SHOULD);

            // add query about agent to main query
            boolQuery.add(agentOrNameBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (searchParam.isOnlyAgent()) {
            BooleanQuery.Builder agentOrPartnershipBoolQuery = new BooleanQuery.Builder();

            Query queryAgernt = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_IP_AGENT_AGENT_CODE_FIELD,
                    "*");
            agentOrPartnershipBoolQuery.add(queryAgernt, BooleanClause.Occur.SHOULD);

            Query queryPartnership = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_IP_PERSON_EXTENDED_PARTNERSHIP_PARTNERSHIP_CODE_FIELD,
                    "*");
            agentOrPartnershipBoolQuery.add(queryPartnership, BooleanClause.Occur.SHOULD);

            boolQuery.add(agentOrPartnershipBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (searchParam.isOnlyForeignCitizens()) {


            BooleanQuery.Builder onlyForeignCitizensBoolQuery = new BooleanQuery.Builder();
            Query query = SearchUtil.getQueryByPhrase(
                    qb,
                    SearchUtil.FIELD_RESIDENCE_COUNTRY_COUNTRY_CODE,
                    SearchUtil.BG_CODE);

            onlyForeignCitizensBoolQuery.add(query, BooleanClause.Occur.MUST_NOT);

            Query baseQuery = SearchUtil.getQueryWildcard(
                    qb,
                    SearchUtil.FIELD_PK_ADDR_NBR,
                    "*");

            onlyForeignCitizensBoolQuery.add(baseQuery, BooleanClause.Occur.MUST);


            boolQuery.add(onlyForeignCitizensBoolQuery.build(), BooleanClause.Occur.MUST);
        }

        if (searchParam.isExcludeOldVersions()) {
            Query termQuery = SearchUtil.getTermQuery(SearchUtil.FIELD_IP_PERSON_GRAL_PERSON_ID_TYP, "OLD");

            boolQuery.add(termQuery, BooleanClause.Occur.MUST_NOT);
        }
        if (searchParam.getIndCompany() != null) {
            Query query = SearchUtil.getTermQuery(SearchUtil.FIELD_IP_PERSON_PERSON_WCODE, searchParam.getIndCompany() ? "M" : "F");

            boolQuery.add(query, BooleanClause.Occur.MUST);
        }

        return boolQuery.build();
    }

    public Page<PersonAddressResult> search(Query luceneQuery, SearchPage searchPage) {
        QueryBuilder qb = hibernateSearchService.getQueryBuilder(IpPersonAddresses.class);
        FullTextQuery jpaQuery = hibernateSearchService.getFullTextQuery(luceneQuery, IpPersonAddresses.class);

        if (searchPage.getSortColumn() != null) {
            Sort sort = SearchUtil.getSort(qb, searchPage);
            jpaQuery.setSort(sort);
        }

        Pageable pageable = toSpringPage(searchPage);
        if (pageable != null) {
            jpaQuery.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
            jpaQuery.setMaxResults(searchPage.getPageSize());
        }

        jpaQuery.setProjection(
                SearchUtil.FIELD_PK_PERSON__NBR,
                SearchUtil.FIELD_PK_ADDR_NBR,
                SearchUtil.FIELD_IP_PERSON_PERSON_NAME_EXACT,
                SearchUtil.FIELD_IP_PERSON_NATIONALITY_COUNTRY_CODE,
                SearchUtil.FIELD_IP_PERSON_PERSON_WCODE,
                SearchUtil.FIELD_IP_PERSON_IP_AGENT_AGENT_CODE_EXACT,
                SearchUtil.FIELD_IP_PERSON_EXTENDED_PARTNERSHIP_PARTNERSHIP_CODE_EXACT,
                SearchUtil.FIELD_CITY_NAME,
                SearchUtil.FIELD_ADDR_ZONE,
                SearchUtil.FIELD_ADDR_STREET,
                SearchUtil.FIELD_ZIP_CODE,
                SearchUtil.FIELD_IP_PERSON_EMAIL,
                SearchUtil.FIELD_IP_PERSON_TELEPHONE,
                SearchUtil.FIELD_RESIDENCE_COUNTRY_COUNTRY_CODE,
                SearchUtil.FIELD_IP_PERSON_GRAL_PERSON_ID_TYP,
                SearchUtil.FIELD_IP_PERSON_IP_AGENT_IND_INACTIVE,
                SearchUtil.FIELD_IP_PERSON_EXTENDED_PARTNERSHIP_IND_INACTIVE
                );

        List resultList = jpaQuery.getResultList();

        List<PersonAddressResult> ipos = new ArrayList<>(resultList.size());
        for (Object obj : resultList) {
            Object[] arrObj = (Object[]) obj;

            PersonAddressResult el = new PersonAddressResult();

            el.personNbr((Integer) arrObj[0])
                    .addressNbr((Integer) arrObj[1])
                    .personName((String) arrObj[2])
                    .nationalityCountryCode((String) arrObj[3])
                    .personWcode((String) arrObj[4])
                    .agentCode((Integer) arrObj[5], (String) arrObj[6])
                    .cityName((String) arrObj[7])
                    .addressZone((String) arrObj[8])
                    .addressStreet((String) arrObj[9])
                    .zipCode((String) arrObj[10])
                    .email((String) arrObj[11])
                    .telephone((String) arrObj[12])
                    .residenceCountryCode((String) arrObj[13])
                    .gralPersonIdTyp((String)arrObj[14])
                    .agentIndInactive((Integer) arrObj[5], (String) arrObj[6], (String)arrObj[15], (String)arrObj[16]);

            ipos.add(el);
        }

        return new PageImpl<>(ipos, toSpringPage(searchPage), jpaQuery.getResultSize());
    }


}
