package bg.duosoft.ipas.persistence.model.entity.vw;

import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Immutable
@Table(name = "VW_JOURNAL", schema = "EXT_JOURNAL")
@Getter
@Setter
@EqualsAndHashCode
public class VwJournal implements Serializable {

    @EmbeddedId
    private IpActionPK pk;

    private String code;

    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String year;

    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String buletin;

    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String sect;

    @Column(name = "SECT_DEF")
    private String sectDef;
}
