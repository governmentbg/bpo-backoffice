package bg.duosoft.ipas.persistence.model.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import lombok.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;
import org.hibernate.search.bridge.builtin.LongBridge;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class IpMarkNiceClassesPK implements Serializable, FileSeqTypSerNbrPK {

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

    @Column(name = "NICE_CLASS_CODE")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @FieldBridge(impl = LongBridge.class)
    private Long niceClassCode;

    @Column(name = "NICE_CLASS_STATUS_WCODE")
    private String niceClassStatusWcode;

}
