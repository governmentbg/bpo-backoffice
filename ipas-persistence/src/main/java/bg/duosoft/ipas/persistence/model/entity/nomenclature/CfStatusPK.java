package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.*;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CfStatusPK implements Serializable {

    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "STATUS_CODE")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    private String statusCode;

}
