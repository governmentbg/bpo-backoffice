package bg.duosoft.ipas.integration.portal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalUser implements Serializable {
    private Boolean agreedToTermsOfUse;
    private String comments;
    private Integer companyId;
    private Integer contactId;
    private Date createDate;
    private Boolean defaultUser;
    private String emailAddress;
    private Boolean emailAddressVerified;
    private Integer facebookId;
    private Integer failedLoginAttempts;
    private String firstName;
    private Integer graceLoginCount;
    private String greeting;
    private String jobTitle;
    private String languageId;
    private Date lastFailedLoginDate;
    private Date lastLoginDate;
    private String lastLoginIP;
    private String lastName;
    private Integer ldapServerId;
    private Boolean lockout;
    private Date lockoutDate;
    private Date loginDate;
    private String loginIP;
    private String middleName;
    private Date modifiedDate;
    private String openId;
    private Integer portraitId;
    private String reminderQueryAnswer;
    private String reminderQueryQuestion;
    private String screenName;
    private Integer status;
    private String timeZoneId;
    private Integer userId;
    private String uuid;
}
