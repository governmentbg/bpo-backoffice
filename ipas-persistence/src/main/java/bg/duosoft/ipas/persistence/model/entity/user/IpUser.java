package bg.duosoft.ipas.persistence.model.entity.user;

import bg.duosoft.ipas.persistence.model.entity.InsertableEntity;
import bg.duosoft.ipas.persistence.model.entity.UpdatableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "IP_USER", schema = "IPASPROD")
@Cacheable(value = false)
public class IpUser implements Serializable, UpdatableEntity, InsertableEntity {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;
    @Id
    @Column(name = "USER_ID")
    @Field(name = "uid", analyze = Analyze.NO, store = Store.YES)
    private Integer userId;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "IND_ADMINISTRATOR")
    private String indAdministrator;
    @Column(name = "IND_EXAMINER")
    private String indExaminer;
    @Column(name = "IND_INACTIVE")
    private String indInactive;
    @Column(name = "IND_EXTERNAL")
    private String indExternal;
    @Column(name = "OFFICE_DIVISION_CODE")
    private String officeDivisionCode;
    @Column(name = "OFFICE_DEPARTMENT_CODE")
    private String officeDepartmentCode;
    @Column(name = "OFFICE_SECTION_CODE")
    private String officeSectionCode;
    @Column(name = "INITIALS")
    private String initials;
//    @Column(name = "FOOTER_DESCRIPTION")
//    private String footerDescription;
//    @Column(name = "LOGIN_PASSWORD")
//    private String loginPassword;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGIN_DATE")
    private Date lastLoginDate;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "TELEPHONE")
    private String telephone;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private List<CfThisUserGroup> userGroups;
//    @Column(name = "QTY_WORK")
//    private Integer qtyWork;
//    @Column(name = "IND_TEST_USER")
//    private String indTestUser;
//    @Column(name = "FORCE_DOC_ORI")
//    private String forceDocOri;
    @Column(name = "FULL_NAME")
    private String fullName;
//    @Column(name = "PERSONAL_ID")
//    private String personalId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    private Date creationDate;
    @Column(name = "CREATION_USER_ID")
    private Integer creationUserId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;
    @Column(name = "LAST_UPDATE_USER_ID")
    private Integer lastUpdateUserId;
//    @Column(name = "SIGNATURE_TYP")
//    private String signatureTyp;
//    @Column(name = "SIGNATURE_DATA")
//    private byte[] signatureData;

    public IpUser(Integer userId) {
        this.userId = userId;
    }

}
