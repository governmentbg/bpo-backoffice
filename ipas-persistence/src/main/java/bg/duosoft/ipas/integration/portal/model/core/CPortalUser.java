package bg.duosoft.ipas.integration.portal.model.core;

import bg.duosoft.ipas.enums.PortalUserSyncStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class CPortalUser implements Serializable {
    private Date createDate;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date modifiedDate;
    private String screenName;
    private Integer userId;
    private String uuid;
    private PortalUserSyncStatus syncStatus;
}
