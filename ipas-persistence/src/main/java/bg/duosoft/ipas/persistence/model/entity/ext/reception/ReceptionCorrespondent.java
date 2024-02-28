package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CORRESPONDENT", schema = "EXT_RECEPTION")
@Cacheable(value = false)
public class ReceptionCorrespondent implements Serializable {

    @EmbeddedId
    private ReceptionCorrespondentPK pk;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "TYPE")
    private CorrespondentType correspondentType;

    @Column(name = "REPRESENTATIVE_TYP")
    private String representativeType;

}
