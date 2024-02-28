package bg.duosoft.ipas.util;

import java.time.LocalDate;
import java.time.Month;

public interface DefaultValue {
    String IMARK_ABDOCS_DOCUMENT_TYPE_SUFFIX="-IMARK";
    public long USERDOC_GROUND_NEWEST_EARLIER_RIGHT_TYPE=12;
    public long MAX_SINGLE_DESIGN_NBR_LENGTH=1000000;
    public long LENGTH_100=100;
    int ROW_VERSION = 1;
    int INCREMENT_VALUE = 1;
    int FIRST_RESULT = 0;
    int ONE_RESULT_SIZE = 1;
    int EMPTY_COLLECTION_SIZE = 0;
    int FIRST_PERSON_ADDRESS_NUMBER = 1;
    int OFFIDOC_FILING_NUMBER_PARTS = 3;
    String DOT = ".";
    String DASH = "-";
    String PARTNERSHIP_PREFIX = "A";
    String BULGARIA_CODE = "BG";
    String MARK_OBJECT_INDICATION = "MARCA";
    String PATENT_OBJECT_INDICATION = "SOLICITUD";
    String IPAS_YES = "S";
    String IPAS_NO = "N";
    String IPAS_OBJECT_ID_SEPARATOR = "/";
    Integer DEFAULT_ORDER_NUMBER = 0;
    Integer ORDER_NUMBER_INCREMENTAL = 1;
    Integer MIN_POSITIVE_NUMBER = 1;
    Integer MIN_NOT_EMPTY_COLLECTION_SIZE = 1;
    String DEFAULT_DOC_ORI = "BG";
    String DEFAULT_DOC_LOG = "E";
    String EMPTY_OBJECT_NAME = "EMPTY_NAME";
    String FIGURATIVE_MARK_CONST = "FIGURATIVE_MARK";
    String DEFAULT_LOCARNO_EDITION_CODE = "0";
    String EMPTY_STRING = "";
    String COMMA = ",";
    String ABDOCS_DOCUMENTS_IPAS_GROUP = "IPAS";
    String CONFIRMATION_TEXT = "OK";
    String PERSONS_PANEL = "Persons";
    String UNDEFINED_PORTAL_USER = "**UNDEFINED_USER**";
    Integer DEFAULT_USER_ID = 4;
    LocalDate NEW_IPAS_START_DATE = LocalDate.of(2020, Month.OCTOBER, 9);
    Integer INTELLECTUAL_PROPERTY_AGENT_START_NUMBER = 10_000;
}
