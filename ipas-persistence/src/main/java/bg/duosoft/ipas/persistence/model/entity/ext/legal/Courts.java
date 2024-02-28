package bg.duosoft.ipas.persistence.model.entity.ext.legal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "Courts", schema = "EXT_LEGAL")
public class Courts implements Serializable {

    @Id
    @Column(name = "CourtId")
    private Integer id;

    @Column(name = "Alias")
    private String alias;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;
}
