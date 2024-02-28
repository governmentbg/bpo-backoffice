package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PATENT_NOTES", schema = "IPASPROD")
@Cacheable(value = false)
public class IpPatentNotes implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "NOTES")
    private String notes;



}
