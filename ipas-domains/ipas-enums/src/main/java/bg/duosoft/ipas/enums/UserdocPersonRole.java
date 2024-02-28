package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.List;

public enum UserdocPersonRole {
    APPLICANT,
    GRANTEE,
    GRANTOR,
    PAYEE,
    PAYER,
    PLEDGER,
    CREDITOR,
    OLD_CORRESPONDENCE_ADDRESS,
    NEW_CORRESPONDENCE_ADDRESS,
    NEW_OWNER,
    OLD_OWNER,
    REPRESENTATIVE,
    REPRESENTATIVE_OF_THE_OWNER,
    //    TRUE_AUTHOR,
    AFFECTED_INVENTOR,
    OLD_REPRESENTATIVE,
    NEW_REPRESENTATIVE;

    public static List<UserdocPersonRole> selectAll() {
        return Arrays.asList(UserdocPersonRole.class.getEnumConstants());
    }

    public static boolean hasAttorneyData(UserdocPersonRole role) {
        return UserdocPersonRole.REPRESENTATIVE == role || UserdocPersonRole.REPRESENTATIVE_OF_THE_OWNER == role || UserdocPersonRole.OLD_REPRESENTATIVE == role || UserdocPersonRole.NEW_REPRESENTATIVE == role;
    }
}
