package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@EqualsAndHashCode
@Getter
@Setter
@Embeddable
public class CfClassLocarnoPK implements Serializable {

    @Column(name = "LOCARNO_CLASS_CODE")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String locarnoClassCode;

    @Column(name = "LOCARNO_EDITION_CODE")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String locarnoEditionCode;
}
