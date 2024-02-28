package bg.duosoft.ipas.core.model.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class User extends CUser implements Serializable {

    private Integer userId;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String login;

    private Boolean indAdministrator;

    private Boolean indExaminer;

    private Boolean indInactive;

    private Boolean indExternal;

    @NotEmpty
    private String officeDivisionCode;

    private String officeDepartmentCode;

    private String officeSectionCode;

    @NotEmpty
    private String initials;

    private String email;

    private String telephone;

    private String fullName;

//    private String personalId;

//    private Date creationDate;

//    private Integer creationUserId;

//    private Date lastUpdateDate;

//    private Integer lastUpdateUserId;

//    private String signatureTyp;

//    private byte[] signatureData;

    private List<Integer> groupIds;
}
