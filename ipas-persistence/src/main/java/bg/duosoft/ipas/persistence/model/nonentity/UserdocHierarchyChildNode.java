package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 21.01.2021
 * Time: 15:18
 */
/*@SqlResultSetMapping(
        name = "UserdocHierarchy",
        classes = @ConstructorResult(
                targetClass = UserdocHierarchyNode.class,
                columns = {
                        @ColumnResult(name = "PROC_NBR"),
                        @ColumnResult(name = "PROC_TYP"),
                        @ColumnResult(name = "DOC_ORI"),
                        @ColumnResult(name = "DOC_LOG"),
                        @ColumnResult(name = "DOC_SER"),
                        @ColumnResult(name = "DOC_NBR"),
                        @ColumnResult(name = "UPPER_PROC_NBR"),
                        @ColumnResult(name = "UPPER_PROC_TYP"),
                        @ColumnResult(name = "USERDOC_TYP"),
                }))*/
@Data
@Entity
public class UserdocHierarchyChildNode implements Serializable {
    @Id
    @Column(name = "PROC_NBR")
    private Integer procNbr;
    @Id
    @Column(name = "PROC_TYP")
    private String procTyp;
    @Column(name = "DOC_ORI")
    private String docOri;
    @Column(name = "DOC_LOG")
    private String docLog;
    @Column(name = "DOC_SEQ_TYP")
    private String docSeqTyp;
    @Column(name = "DOC_SEQ_NBR")
    private Integer docSeqNbr;
    @Column(name = "DOC_SEQ_SERIES")
    private Integer docSeqSeries;
    @Column(name = "DOC_SER")
    private Integer docSer;
    @Column(name = "DOC_NBR")
    private Integer docNbr;
    @Column(name = "UPPER_PROC_NBR")
    private Integer upperProcNbr;
    @Column(name = "UPPER_PROC_TYP")
    private String upperProcTyp;
    @Column(name = "USERDOC_TYP")
    private String userdocTyp;
    @Column(name = "EXTERNAL_SYSTEM_ID")
    private String externalSystemId;
    @Column(name = "FILING_DATE")
    private Date filingDate;

    @Column(name = "FILE_SEQ")
    private String fileSeq;
    @Column(name = "FILE_TYP")
    private String fileTyp;
    @Column(name = "FILE_SER")
    private Integer fileSer;
    @Column(name = "FILE_NBR")
    private Integer fileNbr;
    @Column(name = "LOG_USER_NAME")
    private String efilingUser;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    @Column(name = "RESPONSIBLE_USER_ID")
    private Integer responsibleUserId;
}
