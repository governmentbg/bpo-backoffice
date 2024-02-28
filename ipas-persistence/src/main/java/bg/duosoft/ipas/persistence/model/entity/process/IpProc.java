package bg.duosoft.ipas.persistence.model.entity.process;

import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfProcessType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfSubStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidoc;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.util.search.IpProcBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.search.annotations.Index;
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
@Table(name = "IP_PROC", schema = "IPASPROD")
@Indexed
@Cacheable(value = false)
public class IpProc implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @Field(name = "proc.pk", index = Index.YES, analyze = Analyze.NO, store = Store.YES, bridge = @FieldBridge(impl = IpProcBridge.class))
    @SortableField(forField = "proc.pk")
    @FieldBridge(impl = IpProcBridge.class)
    @IndexedEmbedded
    private IpProcPK pk;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "STATUS_CODE", referencedColumnName = "STATUS_CODE")),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "PROC_TYP", referencedColumnName = "PROC_TYP"))
    })
    @IndexedEmbedded
    private CfStatus statusCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATUS_DATE")
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    @DateBridge(resolution = Resolution.MINUTE, encoding = EncodingType.STRING)
    private Date statusDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRATION_DATE")
    private Date expirationDate;

    @Column(name = "IND_FREEZING_JUST_ENDED")
    private String indFreezingJustEnded;

    @Column(name = "MANUAL_PROC_DESCRIPTION")
    private String manualProcDescription;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumns({//setting insertable to true, because the process might be created from ipas-persistence project, but these fields should never be updated from the code, so updatable is false!
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = true, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = true, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = true, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = true, updatable = false),
    })
    private IpFile file;

    @Column(name = "APPL_TYP")
    private String applTyp;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumns({
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = true, updatable = false),
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = true, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = true, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = true, updatable = false)}
    )
    private IpDoc userdocIpDoc;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "USERDOC_TYP", referencedColumnName = "USERDOC_TYP")
    private CfUserdocType userdocTyp;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumns({
            @JoinColumn(name = "USERDOC_FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = true, updatable = false),
            @JoinColumn(name = "USERDOC_FILE_TYP", referencedColumnName = "FILE_TYP", insertable = true, updatable = false),
            @JoinColumn(name = "USERDOC_FILE_SER", referencedColumnName = "FILE_SER", insertable = true, updatable = false),
            @JoinColumn(name = "USERDOC_FILE_NBR", referencedColumnName = "FILE_NBR", insertable = true, updatable = false),
    })
    private IpFile userdocFile;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumns({
            @JoinColumn(name = "OFFIDOC_ORI", referencedColumnName = "OFFIDOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "OFFIDOC_SER", referencedColumnName = "OFFIDOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "OFFIDOC_NBR", referencedColumnName = "OFFIDOC_NBR", insertable = false, updatable = false),
    })
    private IpOffidoc offidoc;

    @Column(name = "FILE_PROC_TYP")
    private String fileProcTyp;

    @Column(name = "FILE_PROC_NBR")
    private Integer fileProcNbr;

    @Column(name = "DROP1")
    private Integer drop1;

    @Column(name = "DROP2")
    private String drop2;

    @Column(name = "MANUAL_PROC_REF")
    private Integer manualProcRef;

    @Column(name = "END_FREEZE_FLAG")
    private String endFreezeFlag;

    @Column(name = "IND_SIGNATURE_PENDING")
    private String indSignaturePending;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumns({
            @JoinColumn(name = "UPPER_PROC_NBR", referencedColumnName = "PROC_NBR", insertable = true, updatable = false),
            @JoinColumn(name = "UPPER_PROC_TYP", referencedColumnName = "PROC_TYP", insertable = true, updatable = false)
    })
    private IpProc upperProc;

    @OneToMany(mappedBy = "ipProc", cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    private List<IpAction> ipActions;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false)
    private CfProcessType processType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FROZEN_PROC_NBR", referencedColumnName = "PROC_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "FROZEN_PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false)
    })
    private List<IpProcFreezes> ipProcFreezingList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FREEZING_PROC_NBR", referencedColumnName = "PROC_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "FREEZING_PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false)
    })
    private List<IpProcFreezes> ipProcFrozenList;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumns({
            @JoinColumn(name = "PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "STATUS_CODE", referencedColumnName = "STATUS_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "SUB_STATUS_CODE", referencedColumnName = "SUB_STATUS_CODE", insertable = false, updatable = false)
    })
    private CfSubStatus subStatusCode;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "RESPONSIBLE_USER_ID", referencedColumnName = "USER_ID")
    @IndexedEmbedded
    private IpUser responsibleUser;

}
