package bg.duosoft.ipas.integration.portal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalUserGroup {
    private Boolean addedByLDAPImport;
    private Integer companyId;
    private Date createDate;
    private String description;
    private Date modifiedDate;
    private String name;
    private Integer parentUserGroupId;
    private Integer userGroupId;
    private Integer userId;
    private String userName;
    private String uuid;
}
