package bg.duosoft.ipas.persistence.model.entity.ext.core;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_IMAGE_VIEW_TYPE", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfImageViewType implements Serializable {

    @Id
    @Column(name = "VIEW_TYPE_ID")
    private Integer viewTypeId;

    @Column(name = "VIEW_TYPE_NAME")
    private String viewTypeName;

}
