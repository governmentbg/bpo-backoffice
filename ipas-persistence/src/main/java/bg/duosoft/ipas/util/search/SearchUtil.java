package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.enums.SearchSortType;
import bg.duosoft.ipas.core.model.search.Sortable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.sort.SortFieldContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchUtil {
    public final static String BG_CODE = "BG";

    public final static String SPLIT_REG_EXP = "\\s-\\\"\\.]+";

    public final static String FIELD_ACTION_TYP = "actionTyp";

    public final static String FIELD_ADDR_STREET = "addrStreet";

    public final static String FIELD_ADDR_STREET_EXACT = "addrStreetExact";

    public final static String FIELD_ADDR_ZONE = "addrZone";

    public final static String FIELD_CITY_NAME = "cityName";

    public final static String FIELD_CITY_NAME_EXACT = "cityNameExact";

    public final static String FIELD_DOC_SEQ_NBR = "docSeqNbr";

    public final static String FIELD_DOC_SEQ_TYP_DOC_SEQ_TYPE_CUSTOM = "docSeqTyp.docSeqTypeCustom";

    public final static String FIELD_FILE_ENGLISH_TITLE = "file.englishTitle";

    public final static String FIELD_FILE_ENGLISH_TITLE_CUSTOM = "file.englishTitleCustom";

    public final static String FIELD_FILE_ENGLISH_TITLE_EXACT = "file.englishTitleExact";

    public static final String FIELD_FILE_ENGLISH_TITLE_SORT = "file.englishTitleSort";

    public final static String FIELD_FILE_ENTITLEMENT_DATE = "file.entitlementDate";

    public final static String FIELD_FILE_EXPIRATION_DATE = "file.expirationDate";

    public static final String FIELD_FILE_FILING_DATA_SORT = "file.filingDateSort";

    public final static String FIELD_FILE_FILING_DATE = "file.filingDate";

    public final static String FIELD_FILE_IP_DOC_FILES_COLECTION_IP_DOC_PK = "file.ipDocFilesCollection.ipDocPK";

    public final static String FIELD_IP_MARK_NICE_CLASSES_PK = "ipMarkNiceClasses.pk";

    public final static String FIELD_FILING_DATE = "filingDate";

    public final static String FIELD_FILE_PK = "file.pk";

    public static final String FIELD_FILE_PK_SORT = "file.pkSort";

    public final static String FIELD_FILE_PROC_NBR = "file.procNbr";

    public final static String FIELD_FILE_PROC_PK = "file.proc.pk";

    public final static String FIELD_FILE_PROC_TYP = "file.procTyp";

    public final static String FIELD_FILE_REGISTRATION_DATE = "file.registrationDate";

    public static final String FIELD_FILE_REGISTRATION_DUP = "file.registrationDup";

    public final static String FIELD_FILE_REGISTRATION_NBR = "file.registrationNbr";

    public static final String FIELD_FILE_REGISTRATION_NBR_SORT = "file.registrationNbrSort";

    public final static String FIELD_FILE_STATUS_CODE = "file.statusCode";

    public final static String FIELD_FILE_TITLE = "file.title";

    public final static String FIELD_FILE_TITLE_CUSTOM = "file.titleCustom";

    public final static String FIELD_FILE_TITLE_EXACT = "file.titleExact";

    public static final String FIELD_FILE_TITLE_SORT = "file.titleSort";

    public final static String FIELD_IMG = "img";

    public final static String FIELD_IP_AGENT_AGENT_CODE_EXACT = "ipAgent.agentCodeExact";

    public final static String FIELD_IP_DOC_FILING_DATE = "ipDoc.filingDate";

    public final static String FIELD_IP_DOC_FILING_DATE_SORT = "ipDoc.filingDateSort";

    public final static String FIELD_IP_DOC_EXTERNAL_SYSTEM_ID = "ipDoc.externalSystemId";

    public final static String FIELD_IP_DOC_PROC_STATUS = "ipDoc.proc.statusCode";

    public final static String FIELD_IP_PATENT_INVENTORS_IP_PERSON_ADDRESSES_IP_PERSON_PERSON_NBR = "ipPatentInventors.ipPersonAddresses.ipPerson.personNbr";

    public final static String FIELD_IP_PERSON_EMAIL = "ipPerson.email";

    public final static String FIELD_IP_PERSON_EMAIL_EXACT = "ipPerson.emailExact";

    public final static String FIELD_IP_PERSON_EXTENDED_PARTNERSHIP_PARTNERSHIP_CODE_EXACT ="ipPerson.extendedPartnership.partnershipCodeExact";

    public final static String FIELD_IP_PERSON_EXTENDED_PARTNERSHIP_PARTNERSHIP_CODE_FIELD ="ipPerson.extendedPartnership.partnershipCodeField";

    public final static String FIELD_IP_PERSON_EXTENDED_PARTNERSHIP_IND_INACTIVE = "ipPerson.extendedPartnership.indInactive";

    public final static String FIELD_IP_PERSON_GRAL_PERSON_ID_TYP = "ipPerson.gralPersonIdTyp";

    public final static String FIELD_IP_PERSON_IP_AGENT_AGENT_CODE_EXACT = "ipPerson.ipAgent.agentCodeExact";

    public final static String FIELD_IP_PERSON_IP_AGENT_AGENT_CODE_FIELD = "ipPerson.ipAgent.agentCodeField";

    public final static String FIELD_IP_PERSON_IP_AGENT_IND_INACTIVE = "ipPerson.ipAgent.indInactive";

    public final static String FIELD_IP_PERSON_NATIONALITY_COUNTRY_CODE = "ipPerson.nationalityCountryCode";

    public final static String FIELD_IP_PERSON_INDIVIDUAL_ID_TYPE = "ipPerson.indiPersonIdTyp";

    public final static String FIELD_IP_PERSON_INDIVIDUAL_ID_TXT = "ipPerson.indiPersonIdTxt";

    public final static String FIELD_IP_PERSON_PERSON_NAME = "ipPerson.personName";

    public final static String FIELD_IP_PERSON_PERSON_NAME_CUSTOM = "ipPerson.personNameCustom";

    public final static String FIELD_IP_PERSON_PERSON_NAME_EXACT = "ipPerson.personNameExact";

    public final static String FIELD_IP_PERSON_PERSON_NAME_SORT = "ipPerson.personNameSort";

    public final static String FIELD_IP_PERSON_PERSON_NBR = "ipPerson.person_nbr";

    public final static String FIELD_IP_PERSON_PERSON_WCODE = "ipPerson.personWcode";

    public final static String FIELD_IP_PERSON_TELEPHONE = "ipPerson.telephone";

    public final static String FIELD_IP_USER_DOC_TYPES_PK_USERDOK_TYP = "ipUserdocTypes.pk.userdocTyp";

    public final static String FIELD_IP_USER_DOC_TYPES_PK_USERDOK_TYP_SORT = "ipUserdocTypes.pk.userdocTypSort";

    public final static String FIELD_IPC_CODE = "ipcCode";

    public final static String FIELD_CPC_CODE = "cpcCode";

    public static final String FIELD_MAIN_OWNER_IP_PERSON_PERSON_NAME_SORT = "mainOwner.ipPerson.personNameSort";

    public final static String FIELD_NATIONALITY_COUNTRY_CODE = "nationalityCountryCode";

    public final static String FIELD_NEW_STATUS_CODE_PK_STATUS_CODE = "newStatusCode.pk.statusCode";

    public final static String FIELD_NEW_STATUS_DATE = "actionDate";

    public final static String FIELD_RESPONSIBLE_USER = "responsibleUser.uid";

    public final static String FIELD_CAPTURE_USER = "captureUser.uid";

    public final static String FIELD_OWNER_IP_PERSON_ADDRESSES_IP_PERSON_PERSON_NBR = "owners.ipPersonAddresses.ipPerson.personNbr";

    public final static String FIELD_SERVICE_PERSON_IP_PERSON_PERSON_NBR = "servicePerson.ipPerson.personNbr";

    public final static String FIELD_PERSON_NAME = "personName";

    public final static String FIELD_PERSON_NAME_SORT = "personNameSort";

    public final static String FIELD_PERSON_NBR = "personNbr";

    public final static String FIELD_PK = "pk";

    public final static String FIELD_PK_ADDR_NBR = "pk.addrNbr";

    public final static String FIELD_PK_PERSON_NBR = "pk.personNbr";

    public final static String FIELD_PK_PERSON__NBR = "pk.person_nbr";

    public final static String FIELD_PK_FILE_NBR = "pk.fileNbr";

    public final static String FIELD_PK_FILE_NBR_STR = "pk.fileNbrStr";

    public final static String FIELD_PK_FILE_SEQ = "pk.fileSeq";

    public final static String FIELD_PK_FILE_SER = "pk.fileSer";

    public final static String FIELD_PK_FILE_TYP = "pk.fileTyp";

    public final static String FIELD_PK_NICE_CLASS_CODE = "pk.niceClassCode";

    public final static String FIELD_PK_VIENNA_CLASS_CODE = "pk.viennaClassCode";

    public final static String FIELD_PK_VIENNA_ELEM_CODE = "pk.viennaElemCode";

    public final static String FIELD_PK_VIENNA_GROUP_CODE = "pk.viennaGroupCode";

    public final static String FIELD_PK_SORT = "pkSort";

    public final static String FIELD_PLANT_DATA_APPR_DENOMINATION = "plantData.apprDenomination";

    public final static String FIELD_PLANT_DATA_APPR_DENOMINATION_ENG = "plantData.apprDenominationEng";

    public final static String FIELD_PLANT_DATA_FEATURES = "plantData.features";

    public final static String FIELD_PLANT_DATA_PLANT_NUMENCLATURE_COMMON_CLASSIFY_BUL = "plantData.plantNumenclature.commonClassifyBul";

    public final static String FIELD_PLANT_DATA_PLANT_NUMENCLATURE_COMMON_CLASSIFY_ENG = "plantData.plantNumenclature.commonClassifyEng";

    public final static String FIELD_PLANT_DATA_PLANT_NUMENCLATURE_LATIN_CLASSIFY = "plantData.plantNumenclature.latinClassify";

    public final static String FIELD_PLANT_DATA_PROPOSED_DENOMINATION = "plantData.proposedDenomination";

    public final static String FIELD_PLANT_DATA_PROPOSED_DENOMINATION_ENG = "plantData.proposedDenominationEng";

    public final static String FIELD_PLANT_DATA_PUBL_DENOMINATION = "plantData.publDenomination";

    public final static String FIELD_PLANT_DATA_PUBL_DENOMINATION_ENG = "plantData.publDenominationEng";

    public final static String FIELD_PLANT_DATA_REJ_DENOMINATION = "plantData.rejDenomination";

    public final static String FIELD_PLANT_DATA_REJ_DENOMINATION_ENG = "plantData.rejDenominationEng";

    public final static String FIELD_PLANT_DATA_STABILITY = "plantData.stability";

    public final static String FIELD_PLANT_DATA_TESTING = "plantData.testing";

    public final static String FIELD_PRIOR_STATUS_CODE_PK_STATUS_CODE = "priorStatusCode.pk.statusCode";

    public final static String FIELD_PRIOR_STATUS_DATE = "priorStatusDate";

    public final static String FIELD_PROC_PK = "proc.pk";

    public final static String FIELD_REPRESENTATIVES_IP_PERSON_ADDRESSES_IP_PERSON_PERSON_NBR = "representatives.ipPersonAddresses.ipPerson.personNbr";

    public final static String FIELD_RESIDENCE_COUNTRY_COUNTRY_CODE = "residenceCountry.country_code";

    public static final String FIELD_SERVICE_PERSON_IP_PERSON_PERSON_NAME_SORT = "servicePerson.ipPerson.personNameSort";

    public final static String FIELD_SIGN_WCODE = "signWcode";

    public final static String FIELD_SPC_EXTENDED_BG_PERMIT_DATE = "spcExtended.bgPermitDate";

    public final static String FIELD_SPC_EXTENDED_BG_PERMIT_NUMBER = "spcExtended.bgPermitNumber";

    public final static String FIELD_SPC_EXTENDED_EU_PERMIT_DATE = "spcExtended.euPermitDate";

    public final static String FIELD_SPC_EXTENDED_EU_PERMIT_NUMBER = "spcExtended.euPermitNumber";

    public final static String FIELD_STATUS_CODE_PK_STATUS_CODE = "statusCode.pk.statusCode";

    public final static String FIELD_SUMMARY = "summary";

    public final static String FIELD_SUMMARY_CUSTOM = "summaryCustom";

    public final static String FIELD_VW_JOURNAL_BULETIN = "vwJournal.buletin";

    public final static String FIELD_VW_JOURNAL_SECT = "vwJournal.sect";

    public final static String FIELD_VW_JOURNAL_YEAR = "vwJournal.year";

    public final static String FIELD_ZIP_CODE = "zipcode";

    public final static String FIELD_ZIP_CODE_EXACT = "zipcodeExact";

    public final static String FIELD_MARK_INTERNATIONAL_REGISTRATION_NBR = "intregnNumber";

    public static Sort getSort(QueryBuilder qb, Sortable sortable) {
        SortFieldContext sortFieldContext = qb
                .sort()
                .byField(SearchSortMapper.toSearchSortField(sortable.getSortColumn()))
                .asc();
        if (sortable.getSortColumn() != null) {
            if (sortable.getSortOrder().equals(SearchSortType.DESC.toString())) {
                sortFieldContext.desc();
            } else {
                sortFieldContext.asc();
            }
        }
        return sortFieldContext.createSort();
    }


    public static Query getQueryRange(QueryBuilder qb, String field, Object from, Object to) {
        if (to instanceof Date) {
            Calendar c = Calendar.getInstance();
            c.setTime((Date) to);
            c.add(Calendar.DATE, 1);
            c.add(Calendar.MINUTE, -1);
            to = c.getTime();
        }

        if (to instanceof java.sql.Date) {
            Calendar c = Calendar.getInstance();
            c.setTime((java.sql.Date) to);
            c.add(Calendar.DATE, 1);
            c.add(Calendar.MINUTE, -1);
            to = c.getTime();
        }

        if (to instanceof Calendar) {
            ((Calendar) to).add(Calendar.DATE, 1);
            ((Calendar) to).add(Calendar.MINUTE, -1);
        }

        return qb
                .range()
                .onField(field)
                .from(from)
                .to(to)
                .createQuery();
    }

    public static Query getQueryBelow(QueryBuilder qb, String field, Object to) {
        if (to instanceof Date) {
            Calendar c = Calendar.getInstance();
            c.setTime((Date) to);
            c.add(Calendar.DATE, 1);
            c.add(Calendar.MINUTE, -1);
            to = c.getTime();
        }

        if (to instanceof java.sql.Date) {
            Calendar c = Calendar.getInstance();
            c.setTime((java.sql.Date) to);
            c.add(Calendar.DATE, 1);
            c.add(Calendar.MINUTE, -1);
            to = c.getTime();
        }

        if (to instanceof Calendar) {
            ((Calendar) to).add(Calendar.DATE, 1);
            ((Calendar) to).add(Calendar.MINUTE, -1);
        }

        return qb
                .range()
                .onField(field)
                .below(to)
                .createQuery();
    }

    public static Query getQueryAbove(QueryBuilder qb, String field, Object from) {
        return qb
                .range()
                .onField(field)
                .above(from)
                .createQuery();
    }

    public static Query getQueryWildcard(QueryBuilder qb, String field, String searchStr) {

        Query query = qb
                .keyword()
                .wildcard()
                .onField(field)
                .matching(searchStr.toLowerCase())
                .createQuery();

        return query;
    }

    public static Query getQueryByPersonName(QueryBuilder qb, String field, String searchStr) {
        BooleanJunction<BooleanJunction> boolQuery = qb.bool();

        List<String> personNames = Arrays.asList(searchStr.toLowerCase().split(" "));
        int size = personNames.size();
        if (1 <= size){
            for (String str:personNames) {
                Query query = getQueryWildcard(qb,
                        field,
                        "*" + str + "*");
                boolQuery.must(query);
            }
        }

        return boolQuery.createQuery();
    }

    public static Query getQueryByKeywords(QueryBuilder qb, String field, String searchStr) {
        String[] strs = searchStr.toLowerCase().split(" ");
        BooleanJunction<BooleanJunction> boolQuery = qb.bool();

        for (String str:strs) {
            Query query = qb
                    .keyword()
                    .onField(field)
                    .matching(str)
                    .createQuery();
            boolQuery.must(query);
        }

        return boolQuery.createQuery();
    }

    public static Query getQueryByPhrase(QueryBuilder qb, String field, String searchStr) {
        Query query = qb
                .phrase()
                .onField(field)
                .sentence(searchStr)
                .createQuery();

        BooleanJunction<BooleanJunction> boolQuery = qb.bool();
        if (query instanceof TermQuery) {
            boolQuery.must(query);
        } else {
            Term[] terms = ((PhraseQuery) query).getTerms();

            for (Term term:terms) {
                Query termQuery = new TermQuery(term);
                boolQuery.must(termQuery);
            }
        }

        return boolQuery.createQuery();
    }

    public static Query getQueryWildcard(QueryBuilder qb, String field, List<String> searchStrs) {
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE - 100);
        for (String string : searchStrs) {
            Query query = getQueryWildcard(qb, field, string.toLowerCase());
            booleanQueryBuilder.add(query, BooleanClause.Occur.SHOULD);
        }
        return booleanQueryBuilder.build();
    }

    public static Query getQueryWildcardContainAllWords(QueryBuilder qb, String field, List<String> searchStrs) {
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE - 100);
        for (String string : searchStrs) {
            Query query = getQueryWildcard(qb, field, string.toLowerCase());
            booleanQueryBuilder.add(query, BooleanClause.Occur.MUST);
        }
        return booleanQueryBuilder.build();
    }

    public static Query getTermQuery(String field, String queryStr) {
        Term term = new Term(field, queryStr);
        return new TermQuery(term);
    }

    public static Query joinTwoIndexQuery(FullTextSession fts, Query fromQuery, String fromField, String toField , Class<?>... entities) {

        Query query = null;

        SearchFactory searchFactory = fts.getSearchFactory();
        IndexReader reader = searchFactory.getIndexReaderAccessor().open(entities);
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        try {
            query = JoinUtil.createJoinQuery(fromField, false, toField, fromQuery, indexSearcher, ScoreMode.None);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            searchFactory.getIndexReaderAccessor().close(reader);
        }
        return query;
    }

    /**
     * This method remove special symbol """, escape "+ - && || ! ( ) { } [ ] ^ ~ * ? : \", change string to lower case and trim him.
     * @param str
     * @return string
     */
    public static String normString(String str) {

        str = str.replace("\"", "")
                .replace("'", "")
                .replace("„", "")
                .replace("*", "\\*")
                .replace("\\s{2,}", " ")
                .toLowerCase().trim()
                .replaceAll("\\.$", "");
        return str;
    }
}
