package bg.duosoft.ipas.integration.portal.utils;


import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.integration.portal.model.core.CPortalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
public class PortalUserUtils {

    private static final String DEFAULT_OFFICE_DIVISION_CODE = "01";

    public static boolean isUserChanged(CPortalUser portalUser, User ipasUser) {
        if (!(createFullName(portalUser).equals(ipasUser.getUserName()))) {
            return true;
        }
        if (!(portalUser.getEmailAddress().equals(ipasUser.getEmail()))) {
            return true;
        }
        return false;
    }

    public static String createFullName(CPortalUser portalUser) {
        String firstName = StringUtils.isEmpty(portalUser.getFirstName()) ? null : portalUser.getFirstName();
        String middleName = StringUtils.isEmpty(portalUser.getMiddleName()) ? null : portalUser.getMiddleName();
        String lastName = StringUtils.isEmpty(portalUser.getLastName()) ? null : portalUser.getLastName();

        String prefix = " ";
        StringBuilder builder = new StringBuilder();
        if (Objects.nonNull(firstName)) {
            builder.append(firstName);
        }
        if (Objects.nonNull(middleName)) {
            builder.append(builder.length() > 0 ? prefix + middleName : middleName);
        }
        if (Objects.nonNull(lastName)) {
            builder.append(builder.length() > 0 ? prefix + lastName : lastName);
        }
        return builder.toString();
    }

    public static void fillIpUserFields(CPortalUser portalUser, User ipasUser) {
        if (Objects.nonNull(portalUser)) {
            ipasUser.setUserName(createFullName(portalUser));
            ipasUser.setLogin(portalUser.getScreenName());
            ipasUser.setEmail(portalUser.getEmailAddress());
            ipasUser.setFullName(createFullName(portalUser));

            String initials = ipasUser.getInitials();
            if (StringUtils.isEmpty(initials)) {
                ipasUser.setInitials(portalUser.getScreenName().toUpperCase());
            }

            String officeDivisionCode = ipasUser.getOfficeDivisionCode();
            if (StringUtils.isEmpty(officeDivisionCode)) {
                ipasUser.setOfficeDivisionCode(DEFAULT_OFFICE_DIVISION_CODE);
            }

            Boolean indInactive = ipasUser.getIndInactive();
            if (Objects.isNull(indInactive)) {
                ipasUser.setIndInactive(false);
            }

            Boolean indAdministrator = ipasUser.getIndAdministrator();
            if (Objects.isNull(indAdministrator)) {
                ipasUser.setIndAdministrator(false);
            }

            Boolean indExaminer = ipasUser.getIndExaminer();
            if (Objects.isNull(indExaminer)) {
                ipasUser.setIndExaminer(false);
            }

        }
    }

    public static PortalUserSyncResult convertToSynchronizedUserResult(User ipasUser, CPortalUser portalUser) {
        PortalUserSyncResult result = new PortalUserSyncResult();
        if (Objects.isNull(ipasUser)) {
            result.setSync(false);
        } else {
            result.setSync(true);
        }
        result.setEmail(portalUser.getEmailAddress());
        result.setLogin(portalUser.getScreenName());
        result.setUsername(createFullName(portalUser));
        return result;
    }

}
