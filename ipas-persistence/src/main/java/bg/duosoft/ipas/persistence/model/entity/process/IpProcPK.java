package bg.duosoft.ipas.persistence.model.entity.process;

import lombok.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class IpProcPK implements Serializable {

    @Column(name = "PROC_TYP")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    private String procTyp;

    @Column(name = "PROC_NBR")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    @FieldBridge(impl = IntegerBridge.class)
    private Integer procNbr;

}