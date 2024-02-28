package bg.duosoft.ipas.core.model.miscellaneous;

/**
 * Created by Raya
 * 17.04.2020
 */
public interface CommonTerm {

    String EDITABLE = "editable";
    String VALID = "valid";
    String NOTFOUND = "notfound";
    String INVALID = "invalid";
    String UNKNOWN = "unknown";
    String DUPLICATE = "duplicate";

    String getMatchedTermsString();
}
