package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocInvalidationRelation;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocTypeToPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocTypeToUiPanel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_USERDOC_TYPE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfUserdocType implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "USERDOC_TYP")
    private String userdocTyp;

    @Column(name = "USERDOC_NAME")
    private String userdocName;

    @Column(name = "IND_RESPONSE")
    private String indResponse;

    @Column(name = "IND_AFFECTS_FILE")
    private String indAffectsFile;

    @Column(name = "IND_MULTI_FILES")
    private String indMultiFiles;

    @Column(name = "IND_CHANGES_OWNER")
    private String indChangesOwner;

    @Column(name = "IND_CHANGES_REPRES")
    private String indChangesRepres;

    @Column(name = "IND_THIRD_PARTY")
    private String indThirdParty;

    @Column(name = "IND_COURT")
    private String indCourt;

    @Column(name = "IND_RENEWAL")
    private String indRenewal;

    @Column(name = "AUXILIARY_REGISTER_TYP")
    private String auxiliaryRegisterTyp;

    @Column(name = "AUXI_REGISTER_ACTION_WCODE")
    private String auxiRegisterActionWcode;

    @Column(name = "GENERATE_PROC_TYP")
    private String generateProcTyp;

    @Column(name = "IND_TRIGGER_FREEZE_FILE_PROC")
    private String indTriggerFreezeFileProc;

    @Column(name = "IND_FREEZE_NO_OFFIDOC")
    private String indFreezeNoOffidoc;

    @Column(name = "RESPONSIBLE_USER_ID")
    private Integer responsibleUserId;

    @Column(name = "IND_INACTIVE")
    private String indInactive;

    @Column(name = "IND_SHOW_TO_PUBLIC")
    private String indShowToPublic;

    @Column(name = "IND_PUBLIC_IF_NOT_PENDING")
    private String indPublicIfNotPending;

    @Column(name = "IND_AUTOCREATE")
    private String indAutocreate;

    @Column(name = "GENERATE_ACTION_TYP")
    private String generateActionTyp;

    @Column(name = "IND_FREEZE_CONTINUE_WHEN_END")
    private String indFreezeContinueWhenEnd;

    @Column(name = "USERDOC_GROUP_NAME")
    private String userdocGroupName;

    @Column(name = "NOTES_PROMPT")
    private String notesPrompt;

    @Column(name = "DOC_SEQ_TYP")
    private String docSeqTyp;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @Column(name = "IND_USED")
    private String indUsed;

    @Column(name = "CONTROL_PROC_TYP")
    private String controlProcTyp;

    @Column(name = "CONTROL_STATUS_CODE")
    private String controlStatusCode;

    @Column(name = "IND_AFFECTS_USERDOC")
    private String indAffectsUserdoc;

    @Column(name = "EDOC_TYP")
    private String edocTyp;

    @Column(name = "GENERIC_EDOC_TYP")
    private String genericEdocTyp;

    @Column(name = "EFOLDER_SEQ")
    private Integer efolderSeq;

    @Column(name = "EDOC_SEQ")
    private Integer edocSeq;

    @Column(name = "FILE_ACCEPTANCE_ACTION_TYP")
    private String fileAcceptanceActionTyp;

    @Column(name = "CONTROL_STATUS_CODE2")
    private String controlStatusCode2;

    @Column(name = "IND_GENERATE_EFOLDER")
    private String indGenerateEfolder;

    @Column(name = "IND_POA")
    private String indPoa;

    @Column(name = "CONTROL_STATUS_LIST_CODE3")
    private String controlStatusListCode3;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USERDOC_TYP", referencedColumnName = "USERDOC_TYP", insertable = false, updatable = false)
    private List<CfUserdocTypeToPersonRole> personRoles;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USERDOC_TYP", referencedColumnName = "USERDOC_TYP", insertable = false, updatable = false)
    private List<CfUserdocTypeToUiPanel> panels;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USERDOC_TYPE", referencedColumnName = "USERDOC_TYP", insertable = false, updatable = false)
    private List<CfUserdocInvalidationRelation> invalidationRelations;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "USERDOC_TYP", referencedColumnName = "USERDOC_TYP", updatable = false)}
    )
    private CfUserdocTypeConfig userdocTypeConfig;

}
