package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import lombok.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class IpPatentCpcClassesPK implements Serializable, FileSeqTypSerNbrPK {

    @Column(name = "FILE_SEQ")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String fileSeq;

    @Column(name = "FILE_TYP")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String fileTyp;

    @Column(name = "FILE_SER")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @FieldBridge(impl = IntegerBridge.class)
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @FieldBridge(impl = IntegerBridge.class)
    private Integer fileNbr;

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
}
