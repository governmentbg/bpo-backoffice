package bg.duosoft.ipas.persistence.model.entity.person;

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
public class IpPersonAddressesPK implements Serializable {

    @Column(name = "PERSON_NBR")
    @Field(name="person_nbr", index = Index.YES, store = Store.YES)
    @SortableField
    @NumericField(forField = "person_nbr")
    @FieldBridge(impl = IntegerBridge.class)
    private Integer personNbr;

    @Column(name = "ADDR_NBR")
    @Field(analyze = Analyze.NO, index = Index.YES, store = Store.YES)
    @FieldBridge(impl = IntegerBridge.class)
    private Integer addrNbr;

}
