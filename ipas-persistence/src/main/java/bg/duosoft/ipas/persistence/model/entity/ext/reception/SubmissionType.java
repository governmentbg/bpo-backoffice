package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CF_SUBMISSION_TYPE", schema = "EXT_RECEPTION")
@Cacheable(value = false)
public class SubmissionType implements Serializable {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_EN")
    private String nameEn;

}
