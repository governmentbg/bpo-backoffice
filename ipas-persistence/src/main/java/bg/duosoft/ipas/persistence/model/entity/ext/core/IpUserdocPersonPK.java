package bg.duosoft.ipas.persistence.model.entity.ext.core;

import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.UserDocumentRelatedPersonPK;
import lombok.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class IpUserdocPersonPK implements Serializable, UserDocumentRelatedPersonPK {
    @Column(name = "DOC_ORI")
    private String docOri;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

    @Column(name = "PERSON_NBR")
    @Field(name = "person_nbr", index = Index.YES, store = Store.YES)
    @SortableField(forField = "person_nbr")
    @NumericField(forField = "person_nbr")
    @FieldBridge(impl = IntegerBridge.class)
    private Integer personNbr;

    @Column(name = "ADDR_NBR")
    private Integer addrNbr;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    @Field(analyze = Analyze.NO, store = Store.YES, bridge=@FieldBridge(impl= EnumBridge.class))
    private UserdocPersonRole role;
}
