package bg.duosoft.ipas.persistence.model.entity.ext;

import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPersonPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipPK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogoViennaClassesPK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkAttachmentViennaClassesPK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClassesPK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentIpcClassesPK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentLocarnoClassesPK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentSummaryPK;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@Table(name = "INDEX_QUEUE", schema = "EXT_CORE")
public class IndexQueue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_SER")
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;

    @Column(name = "ADDR_NBR")
    private Integer addrNbr;

    @Column(name = "PERSON_NBR")
    private Integer personNbr;

    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "PROC_NBR")
    private Integer procNbr;

    @Column(name = "ACTION_NBR")
    private Integer actionNbr;

    @Column(name = "VIENNA_CLASS_CODE")
    private Long viennaClassCode;

    @Column(name = "VIENNA_GROUP_CODE")
    private Long viennaGroupCode;

    @Column(name = "VIENNA_ELEM_CODE")
    private Long viennaElemCode;

    @Column(name = "NICE_CLASS_CODE")
    private Long niceClassCode;

    @Column(name = "NICE_CLASS_STATUS_WCODE")
    private String niceClassStatusWcode;

    @Column(name = "IPC_EDITION_CODE")
    private String ipcEditionCode;

    @Column(name = "IPC_SECTION_CODE")
    private String ipcSectionCode;

    @Column(name = "IPC_CLASS_CODE")
    private String ipcClassCode;

    @Column(name = "IPC_SUBCLASS_CODE")
    private String ipcSubclassCode;

    @Column(name = "IPC_GROUP_CODE")
    private String ipcGroupCode;

    @Column(name = "IPC_SUBGROUP_CODE")
    private String ipcSubgroupCode;

    @Column(name = "IPC_QUALIFICATION_CODE")
    private String ipcQualificationCode;

    @Column(name = "CPC_EDITION_CODE")
    private String cpcEditionCode;

    @Column(name = "CPC_SECTION_CODE")
    private String cpcSectionCode;

    @Column(name = "CPC_CLASS_CODE")
    private String cpcClassCode;

    @Column(name = "CPC_SUBCLASS_CODE")
    private String cpcSubclassCode;

    @Column(name = "CPC_GROUP_CODE")
    private String cpcGroupCode;

    @Column(name = "CPC_SUBGROUP_CODE")
    private String cpcSubgroupCode;

    @Column(name = "CPC_QUALIFICATION_CODE")
    private String cpcQualificationCode;

    @Column(name = "LANGUAGE_CODE")
    private String languageCode;

    @Column(name = "INSERTED_AT")
    private Date insertedAt;

    @Column(name = "OPERATION")
    private String operation;

    @Column(name = "INDEXED_AT")
    private Date indexedAt;

    @Column(name = "CHECKED")
    private Boolean checked;

    @Column(name = "DOC_ORI")
    private String docOri;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private UserdocPersonRole role;

    @Column(name = "FILE_SEQ2")
    private String fileSeq2;

    @Column(name = "FILE_TYP2")
    private String fileTyp2;

    @Column(name = "FILE_SER2")
    private Integer fileSer2;

    @Column(name = "FILE_NBR2")
    private Integer fileNbr2;

    @Column(name = "RELATIONSHIP_TYP")
    private String relationshipTyp;

    @Column(name = "LOCARNO_CLASS_CODE")
    private String locarnoClassCode;

    @Column(name = "ATTACHMENT_ID")
    private Integer attachmentId;

    public IndexQueue(IpDocPK ipDocPK, String type, String operation, Date insertedAt) {
        this.docLog = ipDocPK.getDocLog();
        this.docNbr = ipDocPK.getDocNbr();
        this.docOri = ipDocPK.getDocOri();
        this.docSer = ipDocPK.getDocSer();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpPatentSummaryPK ipPatentSummaryPK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.fileSeq = ipPatentSummaryPK.getFileSeq();
        this.fileTyp = ipPatentSummaryPK.getFileTyp();
        this.fileSer = ipPatentSummaryPK.getFileSer();
        this.fileNbr = ipPatentSummaryPK.getFileNbr();
        this.languageCode = ipPatentSummaryPK.getLanguageCode();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpPatentIpcClassesPK ipPatentIpcClassesPK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.fileSeq = ipPatentIpcClassesPK.getFileSeq();
        this.fileTyp = ipPatentIpcClassesPK.getFileTyp();
        this.fileSer = ipPatentIpcClassesPK.getFileSer();
        this.fileNbr = ipPatentIpcClassesPK.getFileNbr();
        this.ipcEditionCode = ipPatentIpcClassesPK.getIpcEditionCode();
        this.ipcSectionCode = ipPatentIpcClassesPK.getIpcSectionCode();
        this.ipcClassCode = ipPatentIpcClassesPK.getIpcClassCode();
        this.ipcSubclassCode = ipPatentIpcClassesPK.getIpcSubclassCode();
        this.ipcGroupCode = ipPatentIpcClassesPK.getIpcGroupCode();
        this.ipcSubgroupCode = ipPatentIpcClassesPK.getIpcSubgroupCode();
        this.ipcQualificationCode = ipPatentIpcClassesPK.getIpcQualificationCode();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpPatentLocarnoClassesPK ipPatentLocarnoClassesPK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.fileSeq = ipPatentLocarnoClassesPK.getFileSeq();
        this.fileTyp = ipPatentLocarnoClassesPK.getFileTyp();
        this.fileSer = ipPatentLocarnoClassesPK.getFileSer();
        this.fileNbr = ipPatentLocarnoClassesPK.getFileNbr();
        this.locarnoClassCode = ipPatentLocarnoClassesPK.getLocarnoClassCode();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpFileRelationshipPK ipFileRelationshipPK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.fileSeq = ipFileRelationshipPK.getFileSeq1();
        this.fileTyp = ipFileRelationshipPK.getFileTyp1();
        this.fileSer = ipFileRelationshipPK.getFileSer1();
        this.fileNbr = ipFileRelationshipPK.getFileNbr1();
        this.fileSeq2 = ipFileRelationshipPK.getFileSeq2();
        this.fileTyp2 = ipFileRelationshipPK.getFileTyp2();
        this.fileSer2 = ipFileRelationshipPK.getFileSer2();
        this.fileNbr2 = ipFileRelationshipPK.getFileNbr2();
        this.relationshipTyp = ipFileRelationshipPK.getRelationshipTyp();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpMarkNiceClassesPK ipMarkNiceClassesPK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.fileSeq = ipMarkNiceClassesPK.getFileSeq();
        this.fileTyp = ipMarkNiceClassesPK.getFileTyp();
        this.fileSer = ipMarkNiceClassesPK.getFileSer();
        this.fileNbr = ipMarkNiceClassesPK.getFileNbr();
        this.niceClassCode = ipMarkNiceClassesPK.getNiceClassCode();
        this.niceClassStatusWcode = ipMarkNiceClassesPK.getNiceClassStatusWcode();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpLogoViennaClassesPK ipLogoViennaClassesPK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.fileSeq = ipLogoViennaClassesPK.getFileSeq();
        this.fileTyp = ipLogoViennaClassesPK.getFileTyp();
        this.fileSer = ipLogoViennaClassesPK.getFileSer();
        this.fileNbr = ipLogoViennaClassesPK.getFileNbr();
        this.viennaClassCode = ipLogoViennaClassesPK.getViennaClassCode();
        this.viennaGroupCode = ipLogoViennaClassesPK.getViennaGroupCode();
        this.viennaElemCode = ipLogoViennaClassesPK.getViennaElemCode();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpPersonAddressesPK ipPersonAddressesPK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.personNbr = ipPersonAddressesPK.getPersonNbr();
        this.addrNbr = ipPersonAddressesPK.getAddrNbr();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpFilePK ipFilePK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.fileSeq = ipFilePK.getFileSeq();
        this.fileTyp = ipFilePK.getFileTyp();
        this.fileSer = ipFilePK.getFileSer();
        this.fileNbr = ipFilePK.getFileNbr();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpUserdocPersonPK ipUserdocPersonPK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.docLog = ipUserdocPersonPK.getDocLog();
        this.docNbr = ipUserdocPersonPK.getDocNbr();
        this.docOri = ipUserdocPersonPK.getDocOri();
        this.docSer = ipUserdocPersonPK.getDocSer();
        this.personNbr = ipUserdocPersonPK.getPersonNbr();
        this.addrNbr = ipUserdocPersonPK.getAddrNbr();
        this.role = ipUserdocPersonPK.getRole();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpProcPK ipProcPK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.procTyp = ipProcPK.getProcTyp();
        this.procNbr = ipProcPK.getProcNbr();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }

    public IndexQueue(IpActionPK ipActionPK,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.procTyp = ipActionPK.getProcTyp();
        this.procNbr = ipActionPK.getProcNbr();
        this.actionNbr = ipActionPK.getActionNbr();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }
    public IndexQueue(IpMarkAttachmentViennaClassesPK pk,
                      String type,
                      String operation,
                      Date insertedAt) {
        this.fileSeq = pk.getFileSeq();
        this.fileTyp = pk.getFileTyp();
        this.fileSer = pk.getFileSer();
        this.fileNbr = pk.getFileNbr();
        this.viennaClassCode = pk.getViennaClassCode();
        this.viennaGroupCode = pk.getViennaGroupCode();
        this.viennaElemCode = pk.getViennaElemCode();
        this.attachmentId = pk.getAttachmentId();

        this.type = type;
        this.operation = operation;
        this.insertedAt = insertedAt;
        this.checked = false;
    }
}
